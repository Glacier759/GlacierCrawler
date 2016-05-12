package com.glacier.crawler.scheduler;

import com.glacier.crawler.entity.Task;
import com.glacier.crawler.utils.SerializeUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by Glacier on 16/5/5.
 */
public class RedisScheduler extends Scheduler {

    private static final String QUEUE_PREFIX = "queue_";
    private static final String SET_PREFIX = "set_";
    private static JedisPool pool;
    static {
        JedisPoolConfig config = new JedisPoolConfig();
        //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        config.setMaxTotal(500);
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        config.setMaxIdle(5);
        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        config.setMaxWaitMillis(1000 * 10);
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        config.setTestOnBorrow(true);
        pool = new JedisPool(config, "localhost", 6379);
    }

    public synchronized boolean isDuplicate(Task task) {
        Jedis jedis = pool.getResource();
        try {
            String a = SET_PREFIX + key;
            boolean isDuplicate = jedis.sismember(a, task.getUrl());
            return isDuplicate;
        }finally {
            jedis.close();
        }
    }

    public synchronized void push(Task task) {
        Jedis jedis = pool.getResource();
        try {
            if (task.getUrl().indexOf("#") > 0) {
                task.setUrl(task.getUrl().substring(0, task.getUrl().lastIndexOf("#")));
            }
            Boolean isDuplicate = isDuplicate(task);
            if (!isDuplicate) {
                jedis.rpush((QUEUE_PREFIX + key).getBytes(), SerializeUtil.serialize(task));
                jedis.sadd((SET_PREFIX+key), task.getUrl());
            }
        } finally {
            jedis.close();
        }
    }

    public synchronized Task poll() {
        Jedis jedis = pool.getResource();
        try {
            Task task = (Task)SerializeUtil.unserialize(jedis.lpop((QUEUE_PREFIX+key).getBytes()));
            return task;
        }finally {
            jedis.close();
        }
    }

    public int count() {
        Jedis jedis = pool.getResource();
        try {
            Long size = jedis.llen((QUEUE_PREFIX+key).getBytes());
            return size.intValue();
        }finally {
            jedis.close();
        }
    }

    public void clear() {
        Jedis jedis = pool.getResource();
        try {
            jedis.del((QUEUE_PREFIX+key).getBytes());
            jedis.del(SET_PREFIX+key);
        }finally {
            jedis.close();
        }
    }

    public String toString() {
        return "redis";
    }
}
