package com.hr.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author 胡冉
 * @ClassName AuthControllDemo
 * @Description: TODO
 * @Date 2019/8/2 19:07
 * @Version 2.0
 */
public class AuthControllDemo implements Watcher {
    private static final String CONNECTION_STRING = "192.168.10.179:2181";
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static CountDownLatch countDownLatch2 = new CountDownLatch(1);
    private static ZooKeeper zooKeeper;
    private static Stat stat = new Stat();

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, KeeperException,
            InterruptedException {
        zooKeeper = new ZooKeeper(CONNECTION_STRING, 5000, new AuthControllDemo());
        ACL acl = new ACL(ZooDefs.Perms.ALL, new Id("digest", DigestAuthenticationProvider.generateDigest
                ("root:root")));
        ACL acl2 = new ACL(ZooDefs.Perms.CREATE, new Id("ip", "192.168.10.30"));
        List<ACL> aclList = new ArrayList<>();
        aclList.add(acl);
        aclList.add(acl2);
        zooKeeper.create("/auth1", "123".getBytes(), aclList, CreateMode.PERSISTENT);
        zooKeeper.addAuthInfo("digest", "root:root".getBytes());
        zooKeeper.create("/auth1/auth1-1", "123".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);
        
    }

    @Override
    public void process(WatchedEvent event) {

    }
}
