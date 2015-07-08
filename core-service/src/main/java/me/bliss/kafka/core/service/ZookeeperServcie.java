package me.bliss.kafka.core.service;

import me.bliss.kafka.core.service.exception.ZookeeperException;
import me.bliss.kafka.core.service.model.ZookeeperWatcher;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.core.service, v 0.1 7/1/15
 *          Exp $
 */
public class ZookeeperServcie {

    private ZooKeeper zooKeeper;

    public ZookeeperServcie(String host, int port, int timeout) {
        try {
            zooKeeper = new ZooKeeper(host + ":" + port, timeout, null);
        } catch (IOException e) {
            throw new RuntimeException("create zookeeper connection fail!");
        }
    }

    public void destory() {
        try {
            zooKeeper.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getData(String path) throws ZookeeperException {
        try {
            final byte[] data = zooKeeper.getData(path, false, null);
            final String s = new String(data);
            System.out.println(s);
        } catch (Exception e) {
            throw new ZookeeperException(e);
        }
    }

    public List<String> getChildren(String path) throws ZookeeperException {
        try {
            return zooKeeper.getChildren(path, false);
        } catch (Exception e) {
            throw new ZookeeperException(e);
        }
    }

    public List<String> getChildren(String path, ZookeeperWatcher zookeeperWatcher)
            throws ZookeeperException {
        try {
            return zooKeeper.getChildren(path, zookeeperWatcher);
        } catch (Exception e) {
            throw new ZookeeperException(e);
        }
    }

    public List<String> getChildrenByRecursive(String path) throws KeeperException, InterruptedException {
        final List<String> children = zooKeeper.getChildren(path, false);
        final ArrayList<String> childs = new ArrayList<>();
        for (String child : children) {
            String childPath = StringUtils.equals(path,"/") ? path + child : path + "/" + child;
            childs.add(childPath);
            getChildrenByRecursive(childPath);
        }
        return childs;
    }

}
