package es;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import utils.ResourcesUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 测试用的esClient
 * Created by dengchanglu on 15-11-25.
 */
public class EsClient {
    public TransportClient esForward() {
        Settings settings = Settings.settingsBuilder()
//                .put("client.transport.sniff", true) //把集群中其它机器的ip地址加到客户端中
                .put("cluster.name", ResourcesUtil.getES("es.cluster")).build();
        TransportClient client = null;
        try {
            System.out.println(ResourcesUtil.getES("es.host"));
            System.out.println(ResourcesUtil.getES("es.port"));
            client = TransportClient.builder().settings(settings).build()
                    .addTransportAddresses(new InetSocketTransportAddress(
                            InetAddress.getByName(ResourcesUtil.getES("es.host")),
                            Integer.parseInt(ResourcesUtil.getES("es.port"))));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }
}
