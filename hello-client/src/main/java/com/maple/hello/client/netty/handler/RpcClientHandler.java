package com.maple.hello.client.netty.handler;

import com.maple.hello.HelloResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * @author maple 2018.08.26 23:03
 */
public class RpcClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(RpcClientHandler.class);

    private CallBack callBack;

    public interface CallBack {
        void onSuccess(HelloResponse msg) throws ExecutionException, InterruptedException;
    }

    public RpcClientHandler(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        HelloResponse response = (HelloResponse) msg;
        if (callBack != null) {
            try {
                callBack.onSuccess(response);
            } catch (ExecutionException | InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error(getClass().getSimpleName() + "::exceptionCaught: " + cause.getMessage(), cause);

        logger.error(getClass().getSimpleName() + "::exceptionCaught, close channel:" + ctx.channel());
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.warn("channel掉线了,channel: {}", ctx.channel().id());
        //使用过程中断线重连
        super.channelInactive(ctx);
    }


}
