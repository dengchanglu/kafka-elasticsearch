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
import utils.KafkaProperties;

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
        props.put("zookeeper.connect", KafkaProperties.zkConnect);
        props.put("group.id", KafkaProperties.groupId);
        props.put("zookeeper.session.timeout.ms", KafkaProperties.zookeeper_session_timeout_ms);
        props.put("zookeeper.sync.time.ms", KafkaProperties.zookeeper_sync_time_ms);
        props.put("auto.commit.interval.ms", KafkaProperties.auto_commit_interval_ms);

        return new ConsumerConfig(props);

    }

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
