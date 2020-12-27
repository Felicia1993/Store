package org.concurrent.demo.jucdemo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * await超时自动返回
 */
public class Demo8 {
    static ReentrantLock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();
    public static class T1 extends Thread{
        public void run() {
            lock.lock();
            try {
                System.out.println(System.currentTimeMillis() + "," + this.getName() + ",start");
                boolean r = condition.await(2, TimeUnit.SECONDS);
                System.out.println(r);
                System.out.println(System.currentTimeMillis() + ", " + this.getName() + ",end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        T1 t1 = new T1();
        t1.setName("t1");
        t1.start();
    }
}
