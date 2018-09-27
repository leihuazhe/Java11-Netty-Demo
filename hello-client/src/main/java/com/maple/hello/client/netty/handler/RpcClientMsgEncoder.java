package com.maple.hello.client.netty.handler;

import com.google.gson.Gson;
import com.maple.hello.HelloRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

import static com.maple.hello.common.Constants.*;


/**
 * @author maple 2018.09.07 上午9:40
 */
public class RpcClientMsgEncoder extends MessageToByteEncoder<HelloRequest> {

    private Gson gson = new Gson();

    @Override
    protected void encode(ChannelHandlerContext ctx, HelloRequest msg, ByteBuf out) throws Exception {
        int beginIndex = out.readerIndex();
        String response = gson.toJson(msg);

        //先写,占位
        out.writeInt(0);
        //头
        out.writeByte(STX);
        // byte
        out.writeBytes(response.getBytes(CharsetUtil.UTF_8));

        out.writeByte(ETX);

        int endIndex = out.writerIndex();
        int length = endIndex - beginIndex - Integer.BYTES;
        out.writerIndex(beginIndex).writeInt(length);
        out.writerIndex(endIndex);

    }
}
