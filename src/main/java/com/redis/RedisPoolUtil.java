package com.redis;

import redis.clients.jedis.Jedis;

/**
 * 对Jedis的api进行封装，封装一些实现分布式锁需要用到的操作。
 *
 *
 * 几个要用到的redis命令：
 * setnx(key, value)：“set if not exits”，若该key-value不存在，则成功加入缓存并且返回1，否则返回0。
 * get(key)：获得key对应的value值，若不存在则返回nil。
 * getset(key, value)：先获取key对应的value值，返回旧值，若不存在则返回nil，然后将旧的value更新为新的value。
 * expire(key, seconds)：设置key-value的有效期为seconds秒。
 */
public class RedisPoolUtil {

    private RedisPoolUtil() {
    }

    private static RedisPool redisPool;


    public static String get(String key) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
            return result;
        }
    }


    //setnx(key, value)：“set if not exits”，若该key-value不存在，则成功加入缓存并且返回1，否则返回0。
    public static Long setnx(String key, String value) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.setnx(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
            return result;
        }
    }

    //getset(key, value)：先获取key对应的value值，返回旧值，若不存在则返回nil，然后将旧的value更新为新的value。
    public static String getSet(String key, String value) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.getSet(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
            return result;
        }
    }


    public static Long expire(String key, int senconds) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.expire(key, senconds);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
            return result;
        }
    }


    public static Long del(String key) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
            return result;
        }
    }


}
