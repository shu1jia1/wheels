package com.github.shu1jia1.site.sample.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.github.shu1jia1.site.sample.entity.SampleUser;

/**
 *  作为例子使用
 */
public interface SampleDao {
    void insertTestData(@Param("tableName") String tableName, @Param("name") String name, @Param("age") Integer age);
    
    int insert(@Param("datas") List<SampleUser> entitis);

    /**
     * 删除单个
     * @param id
     * @return
     */
    int delete(String id);

    /**
     * 删除表中某id
     * @param table
     * @param id
     * @return
     */
    int deleteByTable(@Param("tableName") String table, @Param("id") String id);

    /**
     * 删除多个
     * @param ids
     * @return
     */
    int deleteMuti(@Param("tableName") String table, @Param("ids") List<String> ids);

}
