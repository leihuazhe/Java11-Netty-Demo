package com.maple.netty;

import com.maple.hello.common.Constants;
import com.maple.hello.common.netty.RpcFrameDecoder;
import com.maple.netty.handler.RpcLogHandler;
import com.maple.netty.handler.RpcMsgDecoder;
import com.maple.netty.handler.RpcMsgEncoder;
import com.maple.netty.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author maple 2018.09.27 下午2:19
 */
public class NettySimpleServer {

    private static Logger logger = LoggerFactory.getLogger(NettySimpleServer.class);

    private int port;

    public NettySimpleServer(int port) {
        this.port = port;
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("netty-server-boss-group", Boolean.TRUE));
        EventLoopGroup workerGroup = new NioEventLoopGroup(Constants.DEFAULT_IO_THREADS, new DefaultThreadFactory("netty-server-work-group", Boolean.TRUE));
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("logHandler", new RpcLogHandler());
                            ch.pipeline().addLast("frameDecoder", new RpcFrameDecoder());
                            ch.pipeline().addLast("encoder", new RpcMsgEncoder());
                            ch.pipeline().addLast("decoder", new RpcMsgDecoder());
                            ch.pipeline().addLast(new ServerHandler());
                        }
                    })

                    /**
                     * BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，Java将使用默认值50。
                     */
                    .option(ChannelOption.SO_BACKLOG, 1024)

                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
//                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
                    .childOption(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT);


            // 绑定端口，开始接收进来的连接
            ChannelFuture future = bootstrap.bind(port).sync();

            logger.info("Server start listen at " + port);
            future.channel().closeFuture().sync();

        } catch (Exception e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
