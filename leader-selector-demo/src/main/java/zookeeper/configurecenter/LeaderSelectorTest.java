package zookeeper.configurecenter;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class LeaderSelectorTest {
    private static RetryPolicy retryPolicy = new ExponentialBackoffRetry(5*1000, 10);

    private static final String CONNECT_STR = "192.168.199.101:2181";
    private static CuratorFramework curatorFramework;

    private static final String leaderPath = "/leaderPath";

    private static AtomicInteger counter = new AtomicInteger(0);

    private static CountDownLatch countDownLatch = new CountDownLatch(1);


    public static void main(String[] args) {
        curatorFramework =  CuratorFrameworkFactory.newClient(CONNECT_STR, retryPolicy);
        curatorFramework.getConnectionStateListenable().addListener((client, state) ->{
            if (state == ConnectionState.CONNECTED){
                System.out.println("已连接");
            }
        });
        curatorFramework.start();

        LeaderSelectorListener listener = new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                String appName = System.getProperty("appName");

                System.out.println("I'm leader now. I'm "+appName );
                byte[] bytes = curatorFramework.getData().forPath(leaderPath);
                String currentCounter = new String(bytes);
                System.out.println("currentCounter =" + currentCounter);
                if (currentCounter != null && !currentCounter.equals("")) {
                    Integer i = Integer.parseInt(currentCounter);
                    counter.set(i);
                }
                while(true) {
                    System.out.println("开始同步数据:"+counter.getAndIncrement());

                    curatorFramework.setData().forPath(leaderPath, counter.toString().getBytes());
                    TimeUnit.SECONDS.sleep(1);

                }
            }
        };
        LeaderSelector selector = new LeaderSelector(curatorFramework, leaderPath, listener );
        selector.autoRequeue();
        selector.start();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
