package com.shujutang.highway.common.util;

import com.google.common.collect.Lists;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;

/**
 * zookeper帮助类
 * 根据curator封装
 * <p/>
 * Created by nancr on 2015/9/23.
 */
public class CuratorUtil {
    public static CuratorFramework client;

    public CuratorUtil(String host, int port) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder().
                connectString(host + ":" + port).
                sessionTimeoutMs(6000).
                retryPolicy(retryPolicy).
                build();
        client.start();
    }

    public CuratorUtil(String connectString) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder().
                connectString(connectString).
                sessionTimeoutMs(6000).
                retryPolicy(retryPolicy).
                build();
        client.start();
    }

    /**
     * 检查节点是否存在
     * @param node 节点名称
     * @return
     */
    public boolean checkExists(String node) {
        boolean isExists = false;
        try {
            Stat stat = client.checkExists().forPath(node);
            if (stat != null){
                isExists = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExists;
    }

    /**
     * 创建永久节点
     *
     * @param node  节点全路径
     * @param value 几点值
     * @return 节点路径全程
     */
    public String createNode(String node, String value) {
        String nodePath = null;
        try {
            Stat stat = client.checkExists().forPath(node);
            if (stat == null) {
                nodePath = client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(node, value.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nodePath;
    }

    /**
     * 创建节点
     * 如果存在删除然后创建
     *
     * @param node  节点路径
     * @param value 节点值
     * @return
     */
    public String createNodeIfExitDel(String node, String value) {
        String nodePath = null;
        try {
            Stat stat = client.checkExists().forPath(node);
            if (stat != null) {
                client.delete().forPath(node);
            }
            nodePath = client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(node, value.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nodePath;
    }

    /**
     * 创建 值递增 的永久节点
     * 节点的值会随着节点调用次数递增
     *
     * @param node 节点路径
     * @return
     */
    public String createNodeValIncr(String node) {
        String nodePath = null;
        try {
            Stat stat = client.checkExists().forPath(node);
            if (stat == null) {
                nodePath = client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(node, "1".getBytes());
            } else {
                int value = Integer.parseInt(new String(client.getData().forPath(node)));
                setNode(node, ++value + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nodePath;
    }

    /**
     * 创建临时节点
     *
     * @param node
     * @param value
     * @return
     */
    public String createNodeEphemeral(String node, String value) {
        String nodePath = null;
        try {
            Stat stat = client.checkExists().forPath(node);
            if (stat == null) {
                nodePath = client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(node, value.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nodePath;
    }

    /**
     * 删除节点
     *
     * @param node
     */
    public void deleteNode(String node) {
        try {
            Stat stat = client.checkExists().forPath(node);
            if (stat != null)
                client.delete().deletingChildrenIfNeeded().forPath(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除节点（值递减，如果value<=0，则删除节点）
     *
     * @param node
     */
    public void deleteNodeValDecr(String node) {
        try {
            int value = Integer.parseInt(new String(client.getData().forPath(node)));
            --value;
            if (value <= 0) {
                client.delete().forPath(node);
            } else {
                setNode(node, value + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询节点值
     *
     * @param node 节点路径
     * @return
     */
    public String getData(String node) {
        String data = null;
        try {
            data = new String(client.getData().forPath(node));
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return data;
    }

    /**
     * 得到所有的子节点路径
     *
     * @param parentPath 父节点路径
     * @return
     */
    public List<String> getChildrenPath(String parentPath){
        List<String> allChildrenPath = Lists.newArrayList();
        Iterator<String> it = null;
        try {
            it = client.getChildren().forPath(parentPath).iterator();
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (it.hasNext()){
            String childNode = it.next();
            allChildrenPath.add(parentPath + "/" + childNode);
        }
        return allChildrenPath;
    }

    /**
     * 更新节点
     *
     * @param node  节点路径
     * @param value 节点值
     */
    public boolean setNode(String node, String value) {
        boolean res = true;
        try {
            if (!checkExists(node)){
                createNode(node, value);
            }else {
                client.setData().forPath(node, value.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = false;
        }
        return res;
    }


    /**
     * 添加子节点监听
     *
     * @param pnodePath 父节点路径
     * @param listener  监听处理程序
     */
    public PathChildrenCache addChildrenNodeListener(String pnodePath, PathChildrenCacheListener listener) {
        PathChildrenCache cache = null;
        try {
            cache = new PathChildrenCache(client, pnodePath, true);
            cache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
            cache.getListenable().addListener(listener);
            System.out.println("节点：" + pnodePath + "  添加子节点监听成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cache;
    }

    /**
     * 添加节点监听
     *
     * @param nodePath               节点路径
     * @param nodeCacheListenerClass
     * @return
     */
    public NodeCache addNodeCacheListener(String nodePath, Class nodeCacheListenerClass) {
        NodeCache cache = null;
        try {
            cache = new NodeCache(client, nodePath, false);
            cache.start(true);

            Constructor<Object> con = nodeCacheListenerClass.getConstructor(NodeCache.class);
            NodeCacheListener listener = (NodeCacheListener) con.newInstance(cache);
            cache.getListenable().addListener(listener);
//            System.out.println("当前节点：" + cache.getCurrentData());
            System.out.println("节点：" + nodePath + "  添加监听成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cache;
    }

    public void closeClient(){
        client.close();
    }
}
