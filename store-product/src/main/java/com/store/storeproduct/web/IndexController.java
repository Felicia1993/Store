package com.store.storeproduct.web;

import org.redisson.Redisson;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
public class IndexController {
    @Autowired
    RedissonClient redissonClient;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        System.out.println("in hello function");
        //1.获取一把锁，只要锁的名字是一样的，就是同一把锁
        RLock lock = redissonClient.getLock("my-lock");
        //2.加锁
        /**
         * redisson会自动续机,如果业务回家爱你长，运行期间自动给续上新的30s，不用担心业务时间长，锁自动过期被释放掉
         * 加锁的业务只要运行完成就不会给当前锁续机，锁默认30s以后自动删除
         */
      //  lock.lock();//阻塞式等待
        lock.lock(10, TimeUnit.SECONDS);//10s自动解锁
        /**
         * 问题：锁超时后，不会自动续机
         * 1.如果传递了锁的超时时间，就发送给redis执行脚本，进行站所，默认超时就是我们指定的时间
         * 2.未指定锁的超时时间,就是用30*1000 默认看门狗的超时时间
         * 3.只要占锁成功就会启动一个定时任务，重新给锁设置过期时间，新的过期时间就是看门狗的过期时间，1/3的时间续机
         *
         * 最佳实战
         * lock.lock(10, TimeUnit.SECONDS);//10s自动解锁
         */
        try {
            System.out.println("加锁成功，执行业务。。。" + Thread.currentThread().getId());
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("释放锁。。。。"+ Thread.currentThread().getId());
            //解锁代码没运行，也不会出现死锁，
            lock.unlock();
        }
        return "hello";
    }

    /**
     * 保证一定能读到最新数据，修改期间，写锁是一个排他锁（互斥锁），读锁是一个共享锁
     * 写锁没释放，读锁就必须等待
     * 读+读：相当于无锁，并发读，只会在redis中记录好，所有当前的读锁，都会同时加锁成功
     * 读+写：
     * 写+读：等待写锁释放
     * 写+写:阻塞方式
     * @return
     */
    @GetMapping("/write")
    public String writeValue() {
        //改数据加写锁
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("rw-lock");
        RLock rLock = readWriteLock.writeLock();
        try {
            rLock.lock();
            System.out.println("写锁加锁成功。。。"+ Thread.currentThread().getId());
            String s = UUID.randomUUID().toString();
            stringRedisTemplate.opsForValue().set("writeValue",s);
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        return "";
    }

    @GetMapping("/read")
    @ResponseBody
    public String readValue() {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("rw-lock");
        //加读锁
        RLock rLock = readWriteLock.readLock();
        String writeValue ="";
        rLock.lock();
        try {
            System.out.println("读锁加锁成功"+Thread.currentThread().getId());
            stringRedisTemplate.opsForValue().get("writeValue");
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        return writeValue;
    }

    /**
     * 信号量：分布式限流
     * 车库停车
     * 3车位
     */
    @GetMapping("/park")
    public String park() throws InterruptedException {
        RSemaphore park = redissonClient.getSemaphore("park");
      //  park.acquire();//获取一个信号，获取一个值
        boolean b = park.tryAcquire();
        if (b) {

        } else {
            return "";
        }
        return "ok";
    }

    @GetMapping("/go")
    public String go() throws InterruptedException {
        RSemaphore park = redissonClient.getSemaphore("park");
        park.release();//释放一个车位
        return "ok";
    }

    /**
     * 闭锁CountDownLatch
     * 放假锁门
     * 5个班，全部走完，可锁大门
     */
    @GetMapping("/lockDoor")
    public String lockDoor() throws InterruptedException {
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.trySetCount(5);
        door.await();//等待闭锁完成
        return "放假了。。。";
    }
    @GetMapping("/gogogo/{id}")
    public String gogogo(@PathVariable("id") long id) {
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.countDown();//计数减一
        return id+"班的人都走了。。。";
    }
}
