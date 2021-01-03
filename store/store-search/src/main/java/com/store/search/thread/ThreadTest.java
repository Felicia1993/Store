package com.store.search.thread;

import java.util.concurrent.*;

public class ThreadTest {
    public static ExecutorService execute = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main...start...");
/*        CompletableFuture<Void> future = CompletableFuture.runAsync(()->{
            System.out.println("当前线程" + Thread.currentThread().getId());
            int i = 10/2;
            System.out.println("运行结果" + i);
        },execute);*/
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(()->{
            System.out.println("当前线程" + Thread.currentThread().getId());
            int i = 10/2;
            System.out.println("运行结果" + i);
            return i;
        },execute).whenComplete((res,exception)->{
            //虽然能得到异常信息，但是没法修改返回数据
            System.out.println("异步任务完成了结果是" + res + "，异常是：" + exception);
        }).exceptionally(throwable -> {
            //可以感知异常，同时返回默认值
            return 10;
        });
        //计算完成时回调方法

        System.out.println("main...end..." + future.get());
    }


    public  void thread(String[] args) throws ExecutionException, InterruptedException {
        /**
         * 1.继承Thread
         *  Thread01 thread01 = new Thread01();
         *  thread01.start();
         * 2.实现Runnable接口
         *  Runnable01 runnable01 = new Runnable01();
         *  new Thread(runnable01).start();
         * 3.Callable接口
         *  FutureTask<Integer> futureTask = new FutureTask<>(new Callable01());
         *  new Thread(futureTask).start();
         *  //等待整个线程执行完成，获得返回结果(阻塞等待)
         *  Integer integer = futureTask.get();
         * 4.线程池ExecutorService
         *  给线程池提交代码
         *      1.Exectors工具类
         *      2.原生
         *
         *  区别
         *  1.2不能得到返回值，3可以获取返回值
         *  1.2.3不能控制资源，4可以控制资源
         *  可以控制资源，性能稳定
         */
        //当前系统中池只有一两个，每个异步任务直接提交给线程池，由池去执行
        System.out.println("main...start...");
        /**
         * 线程池的7大参数
         *  int corePoolSize,核心线程数(线程池不销毁一直存在)，线程池创建好以后就准备就绪的线程数量，等待来接受异步任务去执行
         *      5个， Thread thread = new Thread();
         *  int maximumPoolSize, 最大线程数量，控制资源并发
         *  long keepAliveTime,存活时间，当前线程数量大于核心数量，释放空闲的线程（最大的大小-核心的大小），只要线程空闲大于指定的存活时间
         *  TimeUnit unit, 存活时间单位
         *  BlockingQueue<Runnable> workQueue, 阻塞队列，如果任务有很多，多的任务放在队列里，只要有空闲的线程，就会从队列里取新的任务继续执行
         *  ThreadFactory threadFactory, 线程的创建工厂，默认
         *  RejectedExecutionHandler handler 如果队列满了，按照指定的拒绝策略，拒绝执行任务
         *
         *工作顺序
         * 1.线程池创建，准备好核心数量的狠心线程，准备接受任务
         * 1.1 core满了，就将再进来的任务进入阻塞队列中，空闲的core就会自己去阻塞队列获取任务执行
         * 1.2 阻塞队列满了，就直接开新线程执行，最大只能开到max指定的参数
         * 1.3 max满了，就用RejectedExecutionHandler拒绝任务
         * 1.4 max都执行完了，有很多空闲，在指定时间keepAliveTime以后，释放max-core这些线程
         *  new LinkedBlockingDeque,默认是Integer的最大值，会内存不够，指定业务的峰值
         *  拒绝策略
         *  1.丢弃最老的任务
         *  2.不启动线程，调用Run方法
         *  3.新任务直接丢弃 abort丢弃抛异常
         *  4.discard丢弃不抛异常
         *
         *  一个线程池 core：7，max 20， queue：50， 100并发进来怎么分配
         *  7个会立即执行，50个会进入队列，再开13个线程进行执行，剩下的30个使用拒绝策略，一般都是丢弃
         *  如果不想抛弃，使用CallerRunsPolicy同步执行
         *
         *  4中线程池
         *  Executors.newCachedThreadPool();//核心是0，所有都可回收
         *  Executors.newFixedThreadPool(4);//固定大小，核心=最大，都不可回收
         *  Executors.newScheduledThreadPool(100);//定时任务线程池
         *  Executors.newSingleThreadExecutor();//单线程线程池，后台从队列里获取任务，挨个执行
         */
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,
                200,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        //service.execute(new Runnable01());
        Executors.newSingleThreadExecutor();
        System.out.println("main....end...." );
    }

    public static class Thread01 extends Thread{
        @Override
        public void run() {
            System.out.println("当前线程" + Thread.currentThread().getId());
            int i = 10/2;
            System.out.println("运行结果" + i);
        }
    }

    public static class Runnable01 implements Runnable {
        @Override
        public void run() {
            System.out.println("当前线程" + Thread.currentThread().getId());
            int i = 10/2;
            System.out.println("运行结果" + i);
        }
    }

    public static class Callable01 implements Callable<Integer> {
        @Override
        public Integer call() {
            System.out.println("当前线程" + Thread.currentThread().getId());
            int i = 10/2;
            System.out.println("运行结果" + i);
            return i;
        }
    }
}
