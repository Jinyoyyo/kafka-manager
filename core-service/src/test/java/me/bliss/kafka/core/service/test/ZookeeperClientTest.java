package me.bliss.kafka.core.service.test;

import me.bliss.kafka.core.service.ZookeeperClient;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 *
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.core.service.test, v 0.1 7/8/15
 *          Exp $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/spring/*.xml")
public class ZookeeperClientTest {

    @Autowired
    private ZookeeperClient zookeeperServcie;

    @Test
    public void testGetChildren() throws Exception {
        zookeeperServcie.getChildren("/");
    }

    @Test
    public void testGetChildrenByRecursive() throws Exception {
        final List<String> children = zookeeperServcie.getChildrenByRecursive("/");
        System.out.println(ArrayUtils.toString(children));
    }

    public void setZookeeperServcie(ZookeeperClient zookeeperServcie) {
        this.zookeeperServcie = zookeeperServcie;
    }
}
