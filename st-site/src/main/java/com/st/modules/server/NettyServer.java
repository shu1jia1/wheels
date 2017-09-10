package com.st.modules.server;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.st.modules.network.init.NettyChannelInitializer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

@Service("nettyServer")
public class NettyServer implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private ServerBootstrap serverBootstrap;

    //@Resource(name = "tcpSocketAddress")
    private InetSocketAddress tcpSocketAddress;

    @Resource(name = "nettyChannelInitializer")
    private NettyChannelInitializer nettyChannelInitializer;

    private Channel channel;

    @Value("${tcp.port}")
    private int tcpPort;
    @Value("${boss.thread.count}")
    private int bossCount;
    @Value("${worker.thread.count}")
    private int workerCount;
    @Value("${so.keepalive}")
    private boolean keepAlive;
    @Value("${so.backlog}")
    private int backlog;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.serverBootstrap = bootstrap();
        this.tcpSocketAddress = tcpPort();
        start();
        logger.info("tcp server at {} started ", tcpSocketAddress);
    }

    public void start() throws Exception {
        channel = serverBootstrap.bind(new InetSocketAddress(tcpPort)).sync().channel();
        // channel =
        // serverBootstrap.bind(port).sync().channel().closeFuture().sync().channel();
    }

    @PreDestroy
    public void stop() throws Exception {
        channel.close();
        channel.parent().close();
    }

    @Bean(name = "tcpChannelOptions")
    public Map<ChannelOption<?>, Object> tcpChannelOptions() {
        Map<ChannelOption<?>, Object> options = new HashMap<ChannelOption<?>, Object>();
        options.put(ChannelOption.SO_KEEPALIVE, keepAlive);
        options.put(ChannelOption.SO_BACKLOG, backlog);
        return options;
    }

    private ServerBootstrap bootstrap() {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup(), workerGroup()).channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG)).childHandler(nettyChannelInitializer);
        Map<ChannelOption<?>, Object> tcpChannelOptions = tcpChannelOptions();
        Set<ChannelOption<?>> keySet = tcpChannelOptions.keySet();
        for (@SuppressWarnings("rawtypes")
        ChannelOption option : keySet) {
            b.option(option, tcpChannelOptions.get(option));
        }
        return b;
    }

    @Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(bossCount);
    }

    @Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(workerCount);
    }

    private InetSocketAddress tcpPort() {
        return new InetSocketAddress(tcpPort);
    }

}