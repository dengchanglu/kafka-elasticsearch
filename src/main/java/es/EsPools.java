package es;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import utils.ResourcesUtil;

import java.net.InetAddress;
import java.util.*;

/**
 * Created by perfection on 15-11-25.
 */
public class EsPools {

    private static int bulkRequestNumber;

    private static List<TransportClient> clients = new ArrayList<>();
    private static Map<String, String> esMap = new HashMap<>();


    private EsPools() {
    }

    public static List<TransportClient> getEsClient() {
        if (clients.isEmpty()) {
            synchronized (EsPools.class) {
                if (clients.isEmpty()) {
                    clients.addAll(initEsClient(ResourcesUtil.getES("es.host"), ResourcesUtil.getES("es.cluster")));
                }
            }
        }

        return clients;
    }

    private static List<TransportClient> initEsClient(String hosts, String cluster) {
        List<TransportClient> clients = new ArrayList<>();
        try {
            String[] hostArr = hosts.split(";");
//            String[] clusters = cluster.split(";");

            for (int i = 0, l = hostArr.length; i < l; i++) {
                List<InetSocketTransportAddress> addressList = new ArrayList<>();
                for (String host : hostArr[i].split(",")) {
                    String[] arr = host.split(":");
                    if (arr.length == 1)
                        addressList.add(new InetSocketTransportAddress(InetAddress.getByName(arr[0]),
                                Integer.parseInt(ResourcesUtil.getES("es.port"))));
                    else if (arr.length == 2)
                        addressList.add(new InetSocketTransportAddress(InetAddress.getByName(arr[0]),
                                Integer.valueOf(arr[1])));
                }
                String clusterName = cluster;

                Settings settings = Settings.settingsBuilder()
                        .put("cluster.name", clusterName)
                        .put("client.transport.sniff", true)
                        .put("client.transport.ignore_cluster_name", false)
                        .put("client.transport.ping_timeout", "100s")
                        .put("client.transport.nodes_sampler_interval", "150s").build();

                TransportClient client = TransportClient.builder().settings(settings).build()
                        .addTransportAddresses(addressList.toArray(new InetSocketTransportAddress[addressList.size()]));
                clients.add(client);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return clients;
    }


    public static int getBulkRequestNumber() {
        return bulkRequestNumber;
    }

    public static void setBulkRequestNumber(int bulkRequestNumber) {
        EsPools.bulkRequestNumber = bulkRequestNumber;
    }
}
