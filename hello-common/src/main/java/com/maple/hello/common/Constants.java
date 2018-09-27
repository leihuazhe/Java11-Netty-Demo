package com.maple.hello.common;

/**
 * @author maple 2018.09.27 下午2:24
 */
public class Constants {

    public static final int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);

    /**
     * Frame begin flag
     */
    public static final byte STX = 0x02;
    /**
     * Frame end flag
     */
    public static final byte ETX = 0x03;
    /**
     * Soa version
     */
    public static final byte VERSION = 1;
}
