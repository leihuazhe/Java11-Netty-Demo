package com.maple.netty.handler;

import com.google.gson.Gson;
import com.maple.hello.HelloResponse;
import com.maple.hello.common.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;


/**
 * @author maple 2018.09.07 上午9:40
 */
public class RpcMsgEncoder extends MessageToByteEncoder<HelloResponse> {
    private Gson gson = new Gson();

    @Override
    protected void encode(ChannelHandlerContext ctx, HelloResponse msg, ByteBuf out) throws Exception {
        int beginIndex = out.readerIndex();
        String content = gson.toJson(msg);

        //先写,占位
        out.writeInt(0);
        out.writeByte(Constants.STX);
        out.writeBytes(content.getBytes(CharsetUtil.UTF_8));
        out.writeByte(Constants.ETX);

        int endIndex = out.writerIndex();
        int length = endIndex - beginIndex - Integer.BYTES;
        out.writerIndex(beginIndex).writeInt(length);
        out.writerIndex(endIndex);
    }
}
