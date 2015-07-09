package me.bliss.kafka.core.service.test;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 *
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.core.service.test, v 0.1 7/9/15
 *          Exp $
 */
public class Test {

    public static void main(String[] args) throws IOException {
        final ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 10000, null);
        System.out.println(zooKeeper);
    }
}
