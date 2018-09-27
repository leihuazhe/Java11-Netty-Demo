package com.maple.hello.client.netty.handler;

import com.google.gson.Gson;
import com.maple.hello.HelloResponse;
import com.maple.hello.common.RpcException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;

import static com.maple.hello.common.Constants.*;


/**
 * @author maple 2018.09.07 上午9:40
 */
public class RpcClientMsgDecoder extends MessageToMessageDecoder<ByteBuf> {
    private static final Logger logger = LoggerFactory.getLogger(RpcClientMsgDecoder.class);

    private Gson gson = new Gson();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        try {
            int readable = msg.readableBytes();

            byte stx = msg.readByte();
            // 通讯协议不正确
            if (stx != STX) {
                ctx.close();
                logger.error(getClass().getSimpleName() + "::decode:通讯包起始符异常, 连接关闭");
                return;
            }

            byte etx = msg.getByte(readable - 1);
            // 通讯协议不正确
            if (etx != ETX) {
                ctx.close();
                logger.error(getClass().getSimpleName() + "::decode:通讯包结束符异常, 连接关闭");
                return;
            }

            // 为什么这里是 -2
            String result = msg.readBytes(readable - 2).toString(Charset.forName("UTF-8"));
            out.add(gson.fromJson(result, HelloResponse.class));
        } catch (RpcException e) {
            logger.error(e.getMessage(), e);
//            RpcObject rpcObject = new RpcObject(9999, e.getMessage());

//            ctx.writeAndFlush(rpcObject).addListener(FIRE_EXCEPTION_ON_FAILURE);
        }
    }

}
