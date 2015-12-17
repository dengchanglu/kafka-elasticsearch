package es;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import utils.KafkaProperties;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by dengchanglu on 15-11-25.
 */
public class EsClient {
    public TransportClient esForward() {
        Settings settings = Settings.settingsBuilder()
//                .put("client.transport.sniff", true) 把集群中其它机器的ip地址加到客户端中
                .put("cluster.name", KafkaProperties.es_cluster).build();
        TransportClient client = null;
        try {
            client = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(KafkaProperties.es_host), 9305));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }
}
