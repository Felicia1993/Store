package org.concurrent.demo.threaddemo;

import java.util.concurrent.TimeUnit;

/**
 * 常用的构造方法用来创建线程组
 * public ThreadGroup(String name);
 * public ThreadGroup (ThreadGroup parent, String name);
 */
public class ThreadGroupDemo2 {
    public static class R1 implements Runnable {

        @Override
        public void run() {
            Thread thread = Thread.currentThread();
            System.out.println("所属线程组：" + thread.getThreadGroup().getName() + ",线程名称：" + thread.getName());
            try {
                TimeUnit.SECONDS.sleep(1);
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
        System.out.println("threadGroup活动线程数：" + threadGroup.activeCount());
        System.out.println("threadGroup活动线程组：" + threadGroup.activeGroupCount());
        System.out.println("threadGroup线程组名称：" + threadGroup.getName());
        System.out.println("threadGroup父线程组名称：" + threadGroup.getParent().getName());
        System.out.println("---------------------");
        ThreadGroup threadGroup2 = new ThreadGroup(threadGroup,"thread-group-2");
        Thread thread3 = new Thread(threadGroup2, new R1(), "t3");
        Thread thread4 = new Thread(threadGroup2, new R1(), "t4");
        thread3.start();
        thread4.start();
        System.out.println("threadGroup2活动线程数：" + threadGroup.activeCount());
        System.out.println("threadGroup2活动线程组：" + threadGroup.activeGroupCount());
        System.out.println("threadGroup2线程组名称：" + threadGroup.getName());
        System.out.println("threadGroup2父线程组名称：" + threadGroup.getParent().getName());
        System.out.println("---------------------");
        System.out.println("threadGroup线程组名称：" + threadGroup.getName());
        System.out.println("threadGroup活动线程数：" + threadGroup.activeCount());
        System.out.println("threadGroup父线程组名称：" + threadGroup.getParent().getName());
        System.out.println("---------------------");
        threadGroup.list();
    }
}
