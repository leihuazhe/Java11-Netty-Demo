package com.maple.hello;

/**
 * @author maple 2018.09.27 上午9:44
 */
public class HelloRequest {
    private int seq;
    private String name;
    private String message;

    public HelloRequest(int seq, String name, String message) {
        this.seq = seq;
        this.name = name;
        this.message = message;
    }

    public int getSeq() {
        return seq;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}
