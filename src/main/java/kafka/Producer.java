package kafka;

/**
 * Created by dengchanglu on 15-11-24.
 */

import java.util.Properties;

import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import utils.ResourcesUtil;

public class Producer extends Thread {
    private final kafka.javaapi.producer.Producer<Integer, String> producer;
    private final String topic;
    private final Properties props = new Properties();

    public Producer(String topic) {
        props.put("serializer.class", ResourcesUtil.getKafka("serializer.class"));
        props.put("metadata.broker.list", ResourcesUtil.getKafka("metadata.broker.list"));
        producer = new kafka.javaapi.producer.Producer<Integer, String>(new ProducerConfig(props));
        this.topic = topic;
    }

    /**
     * 处理netty接收的消息（这里可以加对数据字符的前期清洗逻辑代码）
     *
     * @param msg 数据字符串
     */
    public void handlerMsg(String msg) {
        String newMsg = msg.substring(2, msg.length());
        newMsg = newMsg.split("&callback")[0];
        producer.send(new KeyedMessage<Integer, String>(topic, newMsg));
    }

    public void close() {
        producer.close();
    }

}
