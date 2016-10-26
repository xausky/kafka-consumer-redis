package io.github.xausky.kcr.config;

import java.util.Properties;

/**
 * Created by xausky on 10/26/16.
 */
public class TopicConfig {
    private String topic;
    private int threadCount;
    public TopicConfig(Properties props,String topic){
        this.topic = topic;
        this.threadCount = Integer.parseInt(props.getProperty("io.github.xausky.kcr.topic."+topic+".thread.count","1"));
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }
}
