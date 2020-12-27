package org.concurrent.demo.threaddemo;

import java.util.concurrent.TimeUnit;

public class ThreadGroupDemo1 {
    public static class R1 implements Runnable {

        @Override
        public void run() {
            System.out.println("threadName" + Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadGroup threadGroup = new ThreadGroup("thread-group-1");
        Thread t1 = new Thread(threadGroup, new R1(), "t1");
        Thread t2 = new Thread(threadGroup, new R1(), "t2");
        t1.start();
        t2.start();
        TimeUnit.SECONDS.sleep(1);
        //activeCount方法可以返回线程组中的所有活动线程数，包含下卖弄的所有子孙节点的线程，由于线程组中的线程是动态变化的，这个值只能是一个估算值
        System.out.println("活动线程数：" + threadGroup.activeCount());
        System.out.println("活动线程组：" + threadGroup.activeGroupCount());
        System.out.println("线程组名称：" + threadGroup.getName());
    }
}
