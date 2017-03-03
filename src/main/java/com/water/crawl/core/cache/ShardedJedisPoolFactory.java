package com.water.crawl.core.cache;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangmiaojie on 2017/2/4.
 */
public class ShardedJedisPoolFactory implements InitializingBean,DisposableBean,FactoryBean<ShardedJedisPool> {

    private JedisPoolConfig poolConfig = null;
    private int timeout;
    private String password;
    private String hostAndPorts;
    private int dbIndex;
    private ShardedJedisPool pool;
    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public ShardedJedisPoolFactory(){
        this.poolConfig = new JedisPoolConfig();
        this.hostAndPorts = "localhost:6379";
        this.dbIndex = 0;
        this.poolConfig.setMaxTotal(200);
        this.poolConfig.setMaxIdle(10);
    }

    @Override
    public ShardedJedisPool getObject() throws Exception {
        if ("".equals(this.hostAndPorts)) return new ShardedJedisPool(new JedisPoolConfig(), new ArrayList<JedisShardInfo>());
        List<JedisShardInfo> jedisShardInfos = new ArrayList<JedisShardInfo>();
        for (String hp : this.hostAndPorts.split(",")) {
            if ("".equals(hp)) continue;
            String[] host_port_s = hp.split(":");
            JedisShardInfo info = new JedisShardInfo(host_port_s[0], Integer.parseInt(host_port_s[1]));
            jedisShardInfos.add(info);
        }
        pool = new ShardedJedisPool(this.poolConfig, jedisShardInfos);
        return pool;
    }

    @Override
    public Class<?> getObjectType() {
        return ShardedJedisPool.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void destroy() throws Exception {
        try{
            pool.destroy();
        }catch (Exception e){

        }
    }

    public JedisPoolConfig getPoolConfig() {
        return poolConfig;
    }

    public void setPoolConfig(JedisPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHostAndPorts() {
        return hostAndPorts;
    }

    public void setHostAndPorts(String hostAndPorts) {
        this.hostAndPorts = hostAndPorts;
    }

    public int getDbIndex() {
        return dbIndex;
    }

    public void setDbIndex(int dbIndex) {
        this.dbIndex = dbIndex;
    }
}
