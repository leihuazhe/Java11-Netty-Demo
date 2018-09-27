package com.maple.hello.client.service;

import com.maple.hello.HelloRequest;
import com.maple.hello.HelloResponse;

/**
 * @author maple 2018.09.27 上午11:39
 */
public interface HelloClient {

    HelloResponse sayHello(HelloRequest hello);
}
