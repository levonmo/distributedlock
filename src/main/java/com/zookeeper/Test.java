package com.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.CountDownLatch;

public class Test {

    public static int Stock = 60;

    public static void main(String[] args) throws InterruptedException {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        final CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);
        client.start();

        final CountDownLatch latch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            new Thread() {
                public void run() {
                    InterProcessMutex mutex = null;
                    try {
                        mutex = new InterProcessMutex(client, "/product");
                        mutex.acquire();
                        if (Stock > 0) {
                            //模拟处理业务逻辑
                            Thread.sleep(8);
                            Stock--;
                            System.out.println("购买成功");
                        } else {
                            System.out.println("购买失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (mutex != null) {
                            try {
                                mutex.release();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    latch.countDown();
                }
            }.start();
        }
        latch.await();
        System.out.println("库存还有：" + Stock);
    }

}
