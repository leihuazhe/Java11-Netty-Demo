package com.maple.hello.client;

import com.maple.hello.HelloRequest;
import com.maple.hello.HelloResponse;
import com.maple.hello.client.netty.NettyClient;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * @author maple 2018.09.27 下午7:49
 */
public class AppClient {
    private static final Logger logger = LoggerFactory.getLogger(AppClient.class);
    private Channel channel;
    private NettyClient nettyClient;
    private final String host;
    private final int port;

    public AppClient(String host, int port) {
        this.host = host;
        this.port = port;
        nettyClient = new NettyClient();
        try {
            channel = nettyClient.connect(host, port);
        } catch (InterruptedException e) {
            logger.error("connect to {}:{} failed", host, port);
        }
    }

    public CompletableFuture<HelloResponse> sendMessage(HelloRequest request) {
        try {
            checkChannel();
            return nettyClient.sendAsync(channel, request, 5000L);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 重连接机制
     *
     * @throws Exception
     */
    private void checkChannel() throws Exception {
        if (channel == null) {
            nettyClient.connect(host, port);
        } else if (!channel.isActive()) {
            logger.info("channel 掉线");
            try {
                channel.close();
            } finally {
                channel = null;
                channel = nettyClient.connect(host, port);
            }
        }
    }
}
