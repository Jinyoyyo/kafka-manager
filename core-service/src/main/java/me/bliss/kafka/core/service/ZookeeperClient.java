package me.bliss.kafka.core.service;

import me.bliss.kafka.core.model.zookeeper.ZookeeperMeta;
import me.bliss.kafka.core.service.exception.ZookeeperException;
import me.bliss.kafka.core.service.model.ZookeeperWatcher;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * zookeeper client
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.core.service, v 0.1 7/1/15
 *          Exp $
 */
public class ZookeeperClient implements InitializingBean{

    private ZooKeeper zooKeeper;

    private String host;

    private int port;

    private int timeout;

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            zooKeeper = new ZooKeeper(host + ":" + port, timeout, null);
        } catch (IOException e) {
            throw new RuntimeException("CREATE ZOOKEEPER CONNECTION FAIL!");
        }
    }

    public ZookeeperMeta getZookeeperMeta(){
        //TODO GET MOLTI ZOOKEEPER NODES
        final ZookeeperMeta zookeeperMeta = new ZookeeperMeta();
        zookeeperMeta.setHost(host);
        zookeeperMeta.setPort(port);
        zookeeperMeta.setTimeout(timeout);
        return zookeeperMeta;
    }

    public void destory() {
        try {
            zooKeeper.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getData(String path) throws ZookeeperException {
        try {
            final byte[] data = zooKeeper.getData(path, false, null);
            return new String(data);
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

    public List<String> getChildrenByRecursive(String path) throws ZookeeperException {
        final ArrayList<String> children = new ArrayList<String>();
        try {
            //TODO construct directory tree
            recursiveChildren(path, children);
            return children;
        } catch (Exception e) {
            throw new ZookeeperException(e);
        }
    }

    public boolean isExistNode(String path) throws ZookeeperException {
        try {
            final Stat exists = zooKeeper.exists(path, false);
            return exists != null;
        } catch (Exception e) {
            throw new ZookeeperException(e);
        }
    }

    public void createNode(String path, String data) throws ZookeeperException {
        if (isExistNode(path)) {
            throw new ZookeeperException("NODE ALREADY EXIST");
        }
        final Transaction transaction = zooKeeper.transaction();
        transaction
                .create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        try {
            transaction.commit();
        } catch (Exception e) {
            throw new ZookeeperException(e);
        }
    }

    private List<String> recursiveChildren(String path, List<String> children)
            throws KeeperException, InterruptedException {
        final List<String> subChildren = zooKeeper.getChildren(path, false);
        for (String child : subChildren) {
            String childPath = StringUtils.equals(path, "/") ? path + child : path + "/" + child;
            children.add(childPath);
            recursiveChildren(childPath, children);
        }
        return children;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

}
