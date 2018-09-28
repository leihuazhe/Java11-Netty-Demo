package com.maple.hello.client;

import com.maple.hello.HelloRequest;
import com.maple.hello.HelloResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author maple 2018.09.27 下午7:54
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private final static AtomicInteger SEQ_ID_ATOMIC = new AtomicInteger(0);

    private final static String SERVER_URL = "127.0.0.1";
    private final static int SERVER_PORT = 8000;


    public static void main(String[] args) throws IOException {
        AppClient client = new AppClient(SERVER_URL, SERVER_PORT);
        logger.info("------ 欢迎进入JDK11的世界: 请输入你的昵称 --------- \n");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String name = in.readLine();
        while (true) {
            try {
                logger.info("------ 请输入任何你想输入的内容: --------- \n");
                Scanner scanner = new Scanner(System.in);
                if (scanner.hasNext()) {
                    String msg = scanner.next();
                    int seq = SEQ_ID_ATOMIC.incrementAndGet();

                    CompletableFuture<HelloResponse> response = client.sendMessage(new HelloRequest(seq, name, msg));
                    response.whenComplete((result, ex) -> {
                        if (ex != null) {
                            logger.info(ex.getMessage(), ex);
                        }
                        logger.info("seq为 {} 的请求,服务端返回结果为:\n{}", seq, result.toString());
                    });
                } else {
                    int seq = SEQ_ID_ATOMIC.incrementAndGet();
                    CompletableFuture<HelloResponse> response = client.sendMessage(new HelloRequest(seq, name, "异常准备关闭"));
                    response.whenComplete((result, ex) -> {
                        if (ex != null) {
                            logger.info(ex.getMessage(), ex);
                        }
                        logger.info("seq为 {} 的请求,服务端返回结果为:{}", seq, result.toString());
                    });
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
