package com.maple.hello.service;

import com.maple.hello.HelloRequest;
import com.maple.hello.HelloResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author maple 2018.09.27 下午2:18
 */
public class HelloService {
    private Logger logger = LoggerFactory.getLogger(HelloService.class);
    private static HelloService instance = new HelloService();

    private HelloService() {
    }

    public static HelloService getService() {
        return instance;
    }

    public HelloResponse processRequest(HelloRequest request) {
        logger.info(HelloService.class.getName() + ": 收到消息 seqId:" + request.getSeq() + ", request: " + request.toString());

        return new HelloResponse(request.getSeq(), request.getName(), request.getName() + " 你好, Server 收到了你的消息并进行了处理 ...");
    }

}
