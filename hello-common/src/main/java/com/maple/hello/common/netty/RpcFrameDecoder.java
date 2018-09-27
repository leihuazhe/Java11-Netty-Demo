package com.maple.hello.common.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;


/**
 * {@link LengthFieldBasedFrameDecoder#lengthAdjustment} 长度调节值，在总长被定义为包含包头长度时，修正信息长度
 * <p>
 * {@link LengthFieldBasedFrameDecoder#initialBytesToStrip} 解析的时候需要跳过的字节数
 *
 * @author maple 2018.09.07 上午9:40
 */
public class RpcFrameDecoder extends LengthFieldBasedFrameDecoder {
    /**
     * 最大包大小, 5M 解码时，处理每个帧数据的最大长度
     */
    private static final int MAX_FRAME_LENGTH = 1024 * 1024 * 5;
    /**
     * 长度属性的起始位（偏移位），包中存放有整个大数据包长度的字节，这段字节的起始位置
     */
    private static final int LENGTH_FIELD_OFFSET = 0;
    /**
     * 长度属性的长度，即存放整个大数据包长度的字节所占的长度
     */
    private static final int LENGTH_FIELD_LENGTH = 4;
    /**
     * 解析的时候需要跳过的字节数,这里解析后跳过长度的字节 int 4字节
     */
    private static final int INITIAL_BYTES_TO_STRIP = 4;


    /**
     * 为true，当frame长度超过maxFrameLength时立即报TooLongFrameException异常，为false，读取完整个帧再报异常
     */
    private static final boolean FAIL_FAST = true;


    public RpcFrameDecoder() {
        super(MAX_FRAME_LENGTH, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH, 0, INITIAL_BYTES_TO_STRIP, FAIL_FAST);
    }


    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        return super.decode(ctx, in);
    }
}
