package main;

import es.EsForward;
import es.EsPools;
import kafka.Consumer;
import org.elasticsearch.client.transport.TransportClient;
import utils.ResourcesUtil;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by dengchanglu on 15-11-25.
 */
public class ConsumerMain {
    public static void main(String args[]) {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai")));
        int consumerThreadNumber = Integer.parseInt(ResourcesUtil.getConsumer("consumerThreadNumber"));
        List<TransportClient> esClients = new ArrayList<>();
        List<EsForward> esForwards = new ArrayList<>();
        EsPools.setBulkRequestNumber(Integer.parseInt(ResourcesUtil.getConsumer("bulkRequestNumber")));
        EsPools.getEsClient().forEach(client -> {
            esClients.add(client);
            esForwards.add(new EsForward(client));
        });

        Consumer consumerGroup = new Consumer(ResourcesUtil.getConsumer("topic"), consumerThreadNumber, esForwards);

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
