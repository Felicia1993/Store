package com.store.storeproduct.web;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class IndexController {
    @Autowired
    RedissonClient redissonClient;

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
        lock.lock();//阻塞式等待
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
}
