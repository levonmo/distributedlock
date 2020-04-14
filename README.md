# distributedlock
分布式锁的几种解决方案


## redis实现分布式锁
### 加锁：
> jedis.set(String key, String value, String nxxx, String expx, int time)

#### 参数解析：
+ 第一个为key，我们使用key来当锁，因为key是唯一的。
+ 第二个为value，我们传的是requestId，很多童鞋可能不明白，有key作为锁不就够了吗，为什么还要用到value？原因就是我们在上面讲到可靠性时，分布式锁要满足第四个条件解铃还须系铃人，通过给value赋值为requestId，我们就知道这把锁是哪个请求加的了，在解锁的时候就可以有依据。requestId可以使用UUID.randomUUID().toString()方法生成。
+ 第三个为nxxx，这个参数我们填的是NX，意思是SET IF NOT EXIST，即当key不存在时，我们进行set操作；若key已经存在，则不做任何操作；
+ 第四个为expx，这个参数我们传的是PX，意思是我们要给这个key加一个过期的设置，具体时间由第五个参数决定。
+ 第五个为time，与第四个参数相呼应，代表key的过期时间，防止客户端奔溃没有释放锁资源。

### 释放锁：
> String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
> Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));

#### 解析：
+ 参数KEYS[1]赋值为lockKey，ARGV[1]赋值为requestId。eval()方法是将Lua代码交给Redis服务端执行，首先获取锁对应的value值，检查是否与requestId相等，如果相等则删除锁（解锁）。
那么为什么要使用Lua语言来实现呢？因为要确保上述操作是原子性的，eval()方法可以确保原子性，eval命令执行Lua代码的时候，Lua代码将被当成一个命令去执行，并且直到eval命令执行完成，Redis才会执行其他命令。
