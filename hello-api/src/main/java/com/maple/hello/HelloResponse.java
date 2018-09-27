package com.maple.hello;

/**
 * @author maple 2018.09.27 上午11:40
 */
public class HelloResponse {
    private int seq;
    private String name;
    private String response;

    public HelloResponse(int seq, String name, String response) {
        this.seq = seq;
        this.name = name;
        this.response = response;
    }

    public int getSeq() {
        return seq;
    }

    public String getName() {
        return name;
    }

    public String getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "HelloResponse{" +
                "seq=" + seq +
                ", name='" + name + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
