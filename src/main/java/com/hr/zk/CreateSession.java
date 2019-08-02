package com.hr.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author 胡冉
 * @ClassName CreateSession
 * @Date 2019/8/2 18:30
 * @Version 2.0
 */
public class CreateSession {
    private static final String CONNECTION_STRING = "192.168.10.179:2181";
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static ZooKeeper connect() {
        ZooKeeper zooKeeper = null;
        try {
            zooKeeper = new ZooKeeper(CONNECTION_STRING, 5000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                        countDownLatch.countDown();
                    }
                    if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
                        System.out.println("变化的路径:" + watchedEvent.getPath());
                    }

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return zooKeeper;
    }

    @Test
    public void list() throws IOException, InterruptedException, KeeperException {

        List<String> children = connect().getChildren("/", true);
        System.out.println(children.toString());
    }

    @Test
    public void update() {
        try {
            ZooKeeper zooKeeper = connect();
            //watch的监听是一次性的
            zooKeeper.getData("/huran2", true, null);
            Stat stat = zooKeeper.setData("/huran2", "ccc".getBytes(), -1);
            zooKeeper.getData("/huran2", true, null);
            Stat stat2 = zooKeeper.setData("/huran2", "cccc".getBytes(), -1);
            System.out.println(stat2);
            System.out.println(stat);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

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
            //修改数据
            Stat stat1 = zooKeeper.setData("/liudehua", "bbbb".getBytes(), -1);
            System.out.println(stat1);
            //删除节点
            zooKeeper.delete("/liudehua", -1);

        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
}
