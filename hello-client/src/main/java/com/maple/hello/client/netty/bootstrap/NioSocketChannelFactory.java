package com.maple.hello.client.netty.bootstrap;

import io.netty.channel.ChannelFactory;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author Denim.leihz 2019-11-10 5:13 PM
 */
public enum NioSocketChannelFactory implements ChannelFactory<NioSocketChannel> {

    INSTANCE;
    /**
     * Creates a new channel.
     */
    @Override
    public NioSocketChannel newChannel() {
        return new NioSocketChannel();
    }
}
