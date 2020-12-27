package org.concurrent.demo.jucdemo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Demo6 {
    private static ReentrantLock lock1 = new ReentrantLock(false);

    public static class T extends Thread {
        public T(String name) {
            super(name);
        }
        @Override
        public void run() {
            try {
                System.out.println(System.currentTimeMillis() + ": " + this.getName() + "开始获取锁");
                if (lock1.tryLock(3, TimeUnit.SECONDS)) {
                    System.out.println(System.currentTimeMillis() + ":" + this.getName() + "获取到了锁");
                    TimeUnit.SECONDS.sleep(5);
                } else {
                    System.out.println(System.currentTimeMillis() + ": "+this.getName() + "未能获取到锁");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (lock1.isHeldByCurrentThread()) {
                    lock1.unlock();
                }
            }
        }

        public static void main(String[] args) {
            Demo5.T t1 = new Demo5.T("t1");
            Demo5.T t2 = new Demo5.T("t2");

            t1.start();
            t2.start();
        }
    }
}
