package io.github.xausky.kcr.config;

import java.util.Properties;

/**
 * Created by xausky on 10/26/16.
 */
public class Config {
    private int port;
    private String host;
    private String[] topics;
    private String groupId;
    private String bootstrapServer;

    public Config(Properties props){

        topics = props.getProperty("io.github.xausky.kcr.topics","test").split(",");
        groupId = props.getProperty("io.github.xausky.kcr.group.id","kcr");
        port = Integer.parseInt(props.getProperty("io.github.xausky.kcr.redis.port","6379"));
        bootstrapServer = props.getProperty("io.github.xausky.kcr.bootstrap.server","localhost:9092");
        host = props.getProperty("io.github.xausky.kcr.redis.host","localhost");
    }

    public String[] getTopics() {
        return topics;
    }

    public void setTopics(String[] topic) {
        this.topics = topic;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getBootstrapServer() {
        return bootstrapServer;
    }

    public void setBootstrapServer(String bootstrapServer) {
        this.bootstrapServer = bootstrapServer;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
