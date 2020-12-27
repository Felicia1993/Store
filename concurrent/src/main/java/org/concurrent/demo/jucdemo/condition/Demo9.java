package org.concurrent.demo.jucdemo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Demo9 {
    static ReentrantLock reentrantLock = new ReentrantLock();
    static Condition condition = reentrantLock.newCondition();
    public static class T1 extends Thread {
        @Override
        public void run() {
            reentrantLock.lock();
            try {
                System.out.println(System.currentTimeMillis() + "," + this.getName() + ",start");
                boolean r =  condition.await(5, TimeUnit.SECONDS);
                System.out.println(r);
                System.out.println(System.currentTimeMillis() + "," + this.getName() + "，end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        T1 t1 = new T1();
        t1.setName("t1");
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        reentrantLock.lock();
        try {
            condition.signal();
        }finally {
            reentrantLock.unlock();
        }
    }
}
