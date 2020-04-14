package com.redis;

import redis.clients.jedis.Jedis;

import java.util.UUID;

public class Test {


    public static void main(String[] args) throws InterruptedException {

        final String lockKey = "testKey";

        for (int i = 0; i < 100; i++) {
            new Thread() {
                @Override
                public void run() {
                    Jedis jedis = null;
                    try {
                        Thread.sleep((int)(Math.random()*100));
                        String uuid = UUID.randomUUID().toString();
                        jedis = RedisPool.getJedis();
                        boolean ok = RedisTool.tryGetDistributedLock(jedis, lockKey, uuid, 5000);
                        if (ok) {
                            System.out.println("lock success");
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (RedisTool.releaseDistributedLock(jedis, lockKey, uuid)) {
                                System.out.println("unlock success");
                            } else {
                                System.out.println("unlock fail");
                            }
                        } else {
                            System.out.println("lock fail");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        if (jedis != null) {
                            jedis.close();
                        }
                    }
                }
            }.start();
        }

        Thread.sleep(10000000);
    }


}
