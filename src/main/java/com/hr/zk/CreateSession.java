package com.hr.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author 胡冉
 * @ClassName CreateSession
 * @Description: TODO
 * @Date 2019/8/2 18:30
 * @Version 2.0
 */
public class CreateSession {
    private static final String CONNECTION_STRING = "192.168.10.179:2181";
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper(CONNECTION_STRING, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();
        System.out.println(zooKeeper.getState());
        //创建一个节点
        try {
//            String s = zooKeeper.create("/liudehua", "aaa".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode
//                    .PERSISTENT);
//            System.out.println(s);
            //获取数据
            Stat stat = new Stat();
            byte[] data = zooKeeper.getData("/liudehua", true, stat);
            String s1 = new String(data);
            System.out.println(s1);
            System.out.println(stat);
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
}
