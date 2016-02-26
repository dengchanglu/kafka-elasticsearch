package kafka;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.EsForward;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import utils.ResourcesUtil;

/**
 * Created by dengchanglu on 15-11-24.
 */
public class Consumer {
    private final ConsumerConnector consumer;
    private final String topic;
    private ExecutorService executor;

    public Consumer(String topic, int consumerThreadNumber, List<EsForward> esForwards) {
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
                createConsumerConfig());
        this.topic = topic;
        setRun(consumerThreadNumber, esForwards);
    }

    private static ConsumerConfig createConsumerConfig() {
        Properties props = new Properties();
        props.put("zookeeper.connect", ResourcesUtil.getZookeeper("zookeeper.connect"));
        props.put("group.id", ResourcesUtil.getConsumer("group.id"));
        props.put("zookeeper.session.timeout.ms", ResourcesUtil.getZookeeper("zookeeper.session.timeout.ms"));
        props.put("zookeeper.sync.time.ms", ResourcesUtil.getZookeeper("zookeeper.sync.time.ms"));
        props.put("auto.commit.interval.ms", ResourcesUtil.getZookeeper("auto.commit.interval.ms"));
        props.put("zookeeper.connection.timeout.ms", ResourcesUtil.getZookeeper("zookeeper.connection.timeout.ms"));

        return new ConsumerConfig(props);

    }

    /**
     * 从生产者订阅的消息
     *
     * @param a_threadNumber 处理线程数量
     * @param forwards       es的数据处理逻辑类
     */
    private void setRun(int a_threadNumber, List<EsForward> forwards) {
        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(topic, a_threadNumber);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

        executor = Executors.newFixedThreadPool(a_threadNumber);

        for (final KafkaStream stream : streams) {
            executor.submit(new KafkaConsumer(stream, forwards));
        }
    }

    public void shutdown() {
        if (consumer != null)
            consumer.shutdown();

        if (executor != null)
            executor.shutdown();
    }
}
