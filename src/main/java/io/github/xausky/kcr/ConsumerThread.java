package io.github.xausky.kcr;

import io.github.xausky.kcr.config.Config;
import io.github.xausky.kcr.config.TopicConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.Collections;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by xausky on 10/26/16.
 */
public class ConsumerThread extends Thread {
    private static final Logger logger = Logger.getLogger(ConsumerThread.class.getName());

    private Config config;
    private TopicConfig topicConfig;
    private KafkaConsumer<String,String> consumer;
    private Boolean running = false;
    private Jedis jedis;


    public ConsumerThread(Config config, TopicConfig topicConfig) throws Exception {
        try {
            this.config = config;
            this.topicConfig = topicConfig;
            Properties props = new Properties();
            props.put(ConsumerConfig.GROUP_ID_CONFIG,config.getGroupId());
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,config.getBootstrapServer());
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,org.apache.kafka.common.serialization.StringDeserializer.class);
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,org.apache.kafka.common.serialization.StringDeserializer.class);
            consumer = new KafkaConsumer<String, String>(props);
            consumer.subscribe(Collections.singletonList(topicConfig.getTopic()));
            running = true;
            jedis = new Jedis(config.getHost(),config.getPort());
        }catch (Exception e){
            if (consumer!=null) {
                consumer.close();
            }
            if (jedis!=null){
                jedis.close();
            }
            throw e;
        }
    }
    @Override
    public void run() {
        Thread.currentThread().setName("ConsumerThread-"+topicConfig.getTopic());
        try {
            while (running) {

                ConsumerRecords<String, String> records = consumer.poll(1000);
                logger.info(String.format("poll count:"+records.count()));
                Pipeline jedisPipe = jedis.pipelined();
                for (ConsumerRecord<String, String> record : records) {
                    String key = record.key();
                    String value = record.value();
                    if(key!=null && value!=null) {
                        jedisPipe.set(key,value);
                    }
                }
                jedisPipe.sync();
            }
        }finally {
            if (consumer!=null) {
                consumer.close();
            }
        }
    }

    public Boolean getRunning() {
        return running;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }
}
