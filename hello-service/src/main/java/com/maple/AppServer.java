package com.maple;

import com.maple.netty.NettySimpleServer;

/**
 * @author maple 2018.09.27 下午4:35
 */
public class AppServer {
    public static void main(String[] args) {
        NettySimpleServer simpleServer = new NettySimpleServer(8000);
        simpleServer.start();
    }
}
