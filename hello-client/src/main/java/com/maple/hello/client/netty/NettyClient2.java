package com.maple.hello.client.netty;

import com.maple.hello.HelloResponse;
import com.maple.hello.client.netty.bootstrap.NioSocketChannelFactory;
import com.maple.hello.client.netty.handler.RpcClientHandler;
import com.maple.hello.client.netty.handler.RpcClientMsgDecoder;
import com.maple.hello.client.netty.handler.RpcClientMsgEncoder;
import com.maple.hello.common.Constants;
import com.maple.hello.common.netty.RpcFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.AbstractByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @author Denim.leihz 2019-11-10 5:11 PM
 */
public class NettyClient2 {
    private static final Logger logger = LoggerFactory.getLogger(NettyClient2.class);

    private Bootstrap bootstrap = null;
    private final EventLoopGroup workerGroup =
            new NioEventLoopGroup(Constants.DEFAULT_IO_THREADS,
                    new DefaultThreadFactory("netty-client-work-group", Boolean.TRUE)
            );

    /**
     * init
     */
    public NettyClient2() {
        initBootstrap();
    }

    /**
     * init netty client
     *
     * @return
     */
    private Bootstrap initBootstrap() {
        AbstractByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;

        ChannelFactory<? extends Channel> channelFactory = NioSocketChannelFactory.INSTANCE;

        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
//        bootstrap.channel(NioSocketChannel.class);

        bootstrap.channelFactory(channelFactory);

        //保持连接（可以不写，默认为true）
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.ALLOCATOR, allocator)
                //禁用nagle算法,有消息立即发送
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_REUSEADDR, true);


        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast("frameDecoder", new RpcFrameDecoder());
                p.addLast("encoder", new RpcClientMsgEncoder());
                p.addLast("decoder", new RpcClientMsgDecoder());
                p.addLast(new RpcClientHandler(callBack));
            }
        });
        return bootstrap;
    }

    private RpcClientHandler.CallBack callBack = msg -> {
        CompletableFuture<HelloResponse> future /*= NettyClient.RequestQueue.remove(msg.getSeq())*/ = null;
        if (future != null) {
            future.complete(msg);
        } else {
            logger.error("返回结果超时，siqid为：" + msg.getSeq());
        }
    };

    /**
     * 定时任务，使得超时的异步任务返回异常给调用者
     */
    private static long DEFAULT_SLEEP_TIME = 100L;

    static {

        final Thread asyncCheckTimeThread = new Thread("ConnectionPool-ReqTimeout-Thread") {
            @Override
            public void run() {
                while (true) {
                    try {
//                        NettyClient.RequestQueue.checkTimeout();
                        Thread.sleep(DEFAULT_SLEEP_TIME);
                    } catch (Exception e) {
                        logger.error("Check Async Timeout Thread Error", e);
                    }
                }
            }
        };
        asyncCheckTimeThread.start();
    }


    /**
     * 同步连接并返回channel
     *
     * @param host
     * @param port
     * @return
     * @throws InterruptedException
     */
    public Channel connect(String host, int port) throws InterruptedException {
        Channel channel = bootstrap.connect(host, port).sync().channel();
        logger.info("----------->   bind to server host {}, port:{} successful", host, port);
        return channel;
    }

    public void shutdown() {
        logger.warn("NettyClient shutdown gracefully");
        workerGroup.shutdownGracefully();
    }
}
