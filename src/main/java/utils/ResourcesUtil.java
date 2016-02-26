package utils;

import sun.security.tools.keytool.Resources;

import java.util.ResourceBundle;

/**
 * Created by dengchanglu on 15-11-23.
 */
public interface ResourcesUtil {
//    final static String zkConnect = GetResources.getProperty("src/main/resources/", "consumer.properties", "zookeeper.connect");
//    //final static String zkConnect = "127.0.0.1:2181";
//    final static String groupId = GetResources.getProperty("src/main/resources/", "consumer.properties", "group.id");
//    //final static  String groupId = "1";
//    final static String topic = GetResources.getProperty("src/main/resources/", "consumer.properties", "topic");
//    //final static String topic = "xyebank_relog";
////    final static String auto_offset_reset = GetResources.getProperty("src/main/resources/", "consumer.properties", "auto.offset.reset");
////    final static String zookeeper_connection_timeout_ms = GetResources.getProperty("src/main/resources/", "consumer.properties", "zookeeper.connection.timeout.ms");
//    final static String auto_commit_interval_ms = GetResources.getProperty("src/main/resources/", "consumer.properties", "auto.commit.interval.ms");
//    final static String zookeeper_sync_time_ms = GetResources.getProperty("src/main/resources/", "consumer.properties", "zookeeper.sync.time.ms");
//    final static String zookeeper_session_timeout_ms = GetResources.getProperty("src/main/resources/", "consumer.properties", "zookeeper.session.timeout.ms");
//    final static String metadata_broker_list = GetResources.getProperty("src/main/resources/", "kafka.properties", "metadata.broker.list");
//    final static String serializer_class = GetResources.getProperty("src/main/resources/", "kafka.properties", "serializer.class");
//    //    final static String partitioner_class = GetResources.getProperty("src/main/resources/", "kafka.properties", "partitioner.class");
//    final static String es_host = GetResources.getProperty("src/main/resources/", "elasticsearch.properties", "es.host");
//    final static String es_cluster = GetResources.getProperty("src/main/resources/", "elasticsearch.properties", "es.cluster");
//    //    final static String request_required_acks = GetResources.getProperty("src/main/resources/", "consumer.properties", "request.required.acks");
//    final static String port = GetResources.getProperty("src/main/resources/", "netty.properties", "port");
//    final static String index = GetResources.getProperty("src/main/resources/", "elasticsearch.properties", "index");
//    final static String appLog = GetResources.getProperty("src/main/resources/", "elasticsearch.properties", "type");
//    final static String es_port = GetResources.getProperty("src/main/resources/", "elasticsearch.properties", "es.port");
//    final static String consumerThreadNumber = GetResources.getProperty("src/main/resources/", "consumer.properties", "consumerThreadNumber");
//    final static String bulkRequestNumber = GetResources.getProperty("src/main/resources/", "consumer.properties", "bulkRequestNumber");

    public static ResourceBundle elasticsearch = ResourceBundle.getBundle("elasticsearch");
    public static ResourceBundle consumer = ResourceBundle.getBundle("consumer");
    public static ResourceBundle kafka = ResourceBundle.getBundle("kafka");
    public static ResourceBundle netty = ResourceBundle.getBundle("netty");
    public static ResourceBundle zookeeper = ResourceBundle.getBundle("zookeeper");

    /**
     * 获取es的properties
     *
     * @param propertiesKey 取值字段
     * @return 对应的响应参数
     */
    public static String getES(String propertiesKey) {
        return elasticsearch.getString(propertiesKey);
    }

    /**
     * 获取consumer的properties
     *
     * @param propertiesKey 取值字段
     * @return 对应的响应参数
     */
    public static String getConsumer(String propertiesKey) {
        return consumer.getString(propertiesKey);
    }

    /**
     * 获取kafka的properties
     *
     * @param propertiesKey 取值字段
     * @return 对应的响应参数
     */
    public static String getKafka(String propertiesKey) {
        return kafka.getString(propertiesKey);
    }

    /**
     * 获取netty的properties
     *
     * @param propertiesKey 取值字段
     * @return 对应的响应参数
     */
    public static String getNetty(String propertiesKey) {
        return netty.getString(propertiesKey);
    }

    /**
     * 获取zookeeper的properties
     *
     * @param propertiesKey 取值字段
     * @return 对应的响应参数
     */
    public static String getZookeeper(String propertiesKey) {
        return zookeeper.getString(propertiesKey);
    }
}
