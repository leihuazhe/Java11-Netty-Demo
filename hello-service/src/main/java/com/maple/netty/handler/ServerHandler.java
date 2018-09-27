package com.maple.netty.handler;

import com.maple.hello.HelloRequest;
import com.maple.hello.HelloResponse;
import com.maple.hello.service.HelloService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述: 核心逻辑处理器
 *
 * @author hz.lei
 * @date 2018年04月18日 上午12:17
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("remote server {}, channelRead, msg:{}", ctx.channel().remoteAddress(), msg);
        HelloRequest request = (HelloRequest) msg;

        HelloService service = HelloService.getService();

        HelloResponse response = service.processRequest(request);

        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("[ServerHandler] exceptionCaught :" + cause.getMessage(), cause);
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        logger.info("与客户端的channel断开,channel: {} ip: {}", ctx.channel().id(), ctx.channel().remoteAddress());
        super.channelInactive(ctx);
    }
}
