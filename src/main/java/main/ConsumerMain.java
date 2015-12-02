package main;

import es.EsForward;
import es.EsPools;
import kafka.Consumer;
import org.elasticsearch.client.transport.TransportClient;
import utils.KafkaProperties;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by dengchanglu on 15-11-25.
 */
public class ConsumerMain {
    public static void main(String args[]){
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai")));
        int consumerThreadNumber = 1;
        List<TransportClient> esClients = new ArrayList<>();
        List<EsForward> esForwards = new ArrayList<>();
        EsPools.getEsClient().forEach(client -> {
            esClients.add(client);
            esForwards.add(new EsForward(client));
        });

        Consumer consumerGroup = new Consumer(KafkaProperties.topic, consumerThreadNumber, esForwards);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            consumerGroup.shutdown();
            esClients.forEach(TransportClient::close);
        }));

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
