package com.maple.netty.handler;

import com.google.gson.Gson;
import com.maple.hello.HelloRequest;
import com.maple.hello.HelloResponse;
import com.maple.hello.common.Constants;
import com.maple.hello.common.RpcException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;

import static io.netty.channel.ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE;


/**
 * @author maple 2018.09.07 上午9:40
 */
public class RpcMsgDecoder extends MessageToMessageDecoder<ByteBuf> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcMsgDecoder.class);

    private Gson gson = new Gson();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        try {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(getClass().getSimpleName() + "::decode");
            }
            int readable = msg.readableBytes();

            byte stx = msg.readByte();
            // 通讯协议不正确
            if (stx != Constants.STX) {
                throw new RpcException("Err-Rpc-001", "通讯协议不正确(起始符)");
            }

            byte etx = msg.getByte(readable - 1);
            // 通讯协议不正确
            if (etx != Constants.ETX) {
                throw new RpcException("Err-Rpc-002", "通讯协议不正确(结束符)");
            }

            //不会内存泄漏
            String result = msg.toString(msg.readerIndex(), msg.readableBytes() - 1, Charset.forName("UTF-8"));

            out.add(gson.fromJson(result, HelloRequest.class));
        } catch (RpcException e) {
            LOGGER.error(e.getMessage(), e);
            HelloResponse response = new HelloResponse(9999, "FAILED", e.getMessage());
            ctx.writeAndFlush(response).addListener(FIRE_EXCEPTION_ON_FAILURE);
        }
    }

}
