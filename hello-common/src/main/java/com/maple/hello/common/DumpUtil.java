package com.maple.hello.common;

import io.netty.buffer.ByteBuf;

/**
 * @author maple 2018.09.10 上午1:02
 */
public class DumpUtil {


    public static String dumpToStr(ByteBuf buffer) {
        int readerIndex = buffer.readerIndex();
        int availabe = buffer.readableBytes();

        StringBuilder sb = new StringBuilder();

        // XX XX XX XX XX XX XX XX  XX XX XX XX XX XX XX XX  ASCII....
        sb.append("=======[" + availabe + "]\n");
        int i = 0;
        for (; i < availabe; i++) {
            byte b = buffer.getByte(readerIndex + i);

            String it = String.format("%02x ", b & 0xFF);
            sb.append(it);

            if (i % 16 == 15) {
                //int from = i - 15;
                sb.append(' ');
                for (int j = i - 15; j <= i; j++) {
                    char ch = (char) buffer.getByte(readerIndex + j);
                    if (ch >= 0x20 && ch < 0x7F) sb.append(ch);
                    else sb.append('.');
                }
                sb.append("\n");
            }
        }
        i -= 1;
        int from = i / 16 * 16;
        if (i % 16 != 15) {
            for (int j = i; j % 16 != 15; j++) sb.append("   ");
            sb.append(' ');
            for (int j = from; j <= i; j++) {
                char ch = (char) buffer.getByte(readerIndex + j);
                if (ch >= 0x20 && ch < 0x7F) sb.append(ch);
                else sb.append('.');
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
