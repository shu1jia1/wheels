package com.github.shu1jia1.site.base.page.interceptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.github.shu1jia1.site.base.entity.PageInfo;
import com.github.shu1jia1.site.base.page.PageHelper;

/**
 * <p>文件名称: MybatisPageInterceptor.java/p>
 * <p>文件描述: from github.mybatis </p>
 * @version 1.0
 * @author  lov
 */
@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class }) })
public class MybatisPageInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(MybatisPageInterceptor.class);
    private static final String SQL_LIMIT = " limit ";
    //属性参数信息
    private Properties properties;

    public Properties getProperties() {
        return properties;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //当前环境 MappedStatement，BoundSql，及sql取得  
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];

        boolean isPageRequest = false;
        if (mappedStatement.getId().contains("listByPage")) {
            isPageRequest = true;
        }

        logger.debug("mappedStatement.getId {} is page {} ." + mappedStatement.getId(), isPageRequest);

        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        StringBuilder sbOriginalSql = new StringBuilder();
        String originalSql = boundSql.getSql().trim();
        Object parameterObject = boundSql.getParameterObject();
        PageInfo page = PageHelper.getCurrentPage();
        sbOriginalSql.append(originalSql);

        if (page != null && isPageRequest) {
            if (page.getMaxCount() > 0) {
                sbOriginalSql.append(SQL_LIMIT).append(page.getMaxCount() + 1);
            }
            //Page对象存在的场合，开始分页处理  
            //TODO version2:有sql解析工具，这样可以支持多种数据库.
            int totpage = getTotole(mappedStatement, boundSql, sbOriginalSql.toString(), parameterObject);
            page.setTotal(-1);
            StringBuilder sb = new StringBuilder();
            if (!(page.getMaxCount() < totpage) || page.getMaxCount() == 0) {
                logger.info("Get totle {} for sql:{} .", totpage, originalSql);
                //分页计算  
                page.setTotal(totpage);
                //对原始Sql追加limit  
                int offset = (page.getPage() - 1) * page.getPer_page();
                if (!StringUtils.isEmpty(page.getSortby())) {
                    sb.append(originalSql).append(" order by ").append(page.getSortby())
                            .append(" " + page.getOrder()).append(SQL_LIMIT).append(offset).append(",")
                            .append(page.getPer_page());
                } else {
                    sb.append(originalSql).append(SQL_LIMIT).append(offset).append(",")
                            .append(page.getPer_page());
                }
            }
            if (sb.length() == 0) {
                sb.append("SELECT * FROM (" + originalSql + ") aliasOrigiPage LIMIT 0");
            }
            logger.info("listPage query SQL:" + sb.toString());
            BoundSql newBoundSql = copyFromBoundSql(mappedStatement, boundSql, sb.toString());
            MappedStatement newMs = copyFromMappedStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));
            invocation.getArgs()[0] = newMs;
        }
        return invocation.proceed();

    }

    private int getTotole(MappedStatement mappedStatement, BoundSql boundSql, String originalSql,
            Object parameterObject) throws SQLException {
        Connection connection = null;
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        int totle = 0;
        String countSql = getCountSql(originalSql);

        try {
            connection = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();

            countStmt = connection.prepareStatement(countSql);
            BoundSql countBS = copyFromBoundSql(mappedStatement, boundSql, countSql);
            DefaultParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject,
                    countBS);
            parameterHandler.setParameters(countStmt);
            rs = countStmt.executeQuery();
            if (rs.next()) {
                totle = rs.getInt(1);
            }
        } catch (Exception e) {
            logger.warn("calc totle failed. sql :" + countSql);
            logger.warn("Exception message :" + e);
        } finally {
            closeResult(rs);
            closeStatement(countStmt);
            closeConnection(connection);
        }
        return totle;
    }

    /** 
     * 复制MappedStatement对象 
     */
    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        Builder builder = new Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        // builder.keyProperty(ms.getKeyProperty());
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    /** 
     * 复制BoundSql对象 
     */
    private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(),
                boundSql.getParameterObject());
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        return newBoundSql;
    }

    /** 
     * 根据原Sql语句获取对应的查询总记录数的Sql语句 
     */
    private String getCountSql(String sql) {
        return "SELECT COUNT(*) FROM (" + sql + ") aliasForPage";
    }

    public class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    @Override
    public Object plugin(Object arg0) {
        return Plugin.wrap(arg0, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // no comment
    }

    public void closeResult(ResultSet rs) {
        if (null != rs) {
            try {
                rs.close();
            } catch (Exception e) {
                logger.error("page error, ResultSet Exception.", e);
            }
        }
    }

    public void closeStatement(PreparedStatement countStmt) {
        if (null != countStmt) {
            try {
                countStmt.close();
            } catch (Exception e) {
                logger.error("page error, PreparedStatement Exception.", e);
            }
        }
    }

    public void closeConnection(Connection connection) {
        if (null != connection) {
            try {
                connection.close();
            } catch (Exception e) {
                logger.error("page error, connection Exception.", e);
            }
        }
    }

}