//package com.lingzhenren.test;
//
//import com.lingzhenren.demo.DemoApplication;
//import com.lingzhenren.demo.config.CompletableFutureUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.FutureTask;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
///**
// * @description: 对该类作用进行描述
// * @author: yanghaitao
// * @date: 2025/6/12
// */
//@SpringBootTest(classes = DemoApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@RunWith(SpringRunner.class)
//@Slf4j
//public class DemoTest {
//
//    @Resource(name = "testThreadPoll")
//    private ThreadPoolExecutor testThreadPoll;
//
//    @Test
//    public void testThreadPoll(){
//
//        for (int i = 0; i < 10; i++) {
//            testThreadPoll.submit(new Runnable() {
//                @Override
//                public void run() {
//                    log.info("当前时间：" + System.currentTimeMillis());
//                }
//            });
//        }
//    }
//
//    @Test
//    public void testFuture(){
//        List<FutureTask<String>> futureTasks = new ArrayList<>();
//        FutureTask<String> f1 = new FutureTask<String>(
//                () -> {
//                    Thread.sleep(2000);
//                    return "经典";
//                }
//        );
//        FutureTask<String> f2 = new FutureTask<String>(
//                () -> {
//                    return "鸡翅";
//                }
//        );
//        futureTasks.add(f1);
//        futureTasks.add(f2);
//        testThreadPoll.submit(f1);
//        testThreadPoll.submit(f2);
//        for (FutureTask<String> futureTask : futureTasks) {
//            String result = CompletableFutureUtils.getResult(futureTask, 1, TimeUnit.SECONDS, "经典鸡翅", log);
//            log.info("result:{}", result);
//        }
//
//    }
//
//    @Test
//    public void test(){
//        System.out.println("test");
//    }
//}
