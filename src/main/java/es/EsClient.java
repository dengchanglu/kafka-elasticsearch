package es;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import sun.net.util.IPAddressUtil;
import utils.KafkaProperties;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by dengchanglu on 15-11-25.
 */
public class EsClient {
    public TransportClient esForward() {
        Settings settings = Settings.settingsBuilder().put("client.transport.sniff", true)
        .put("cluster.name", KafkaProperties.es_cluster).build();
        TransportClient client = null;
        try {
            client = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(KafkaProperties.es_host), 9305));
            System.out.println(client);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }
}
