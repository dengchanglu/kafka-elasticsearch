package kafka;

/**
 * Created by dengchanglu on 15-11-24.
 */
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.alibaba.fastjson.JSONObject;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import utils.KafkaProperties;

public class Producer extends Thread
{
  private final kafka.javaapi.producer.Producer<Integer, String> producer;
  private final String topic;
  private final Properties props = new Properties();

  public Producer(String topic)
  {
    props.put("serializer.class", KafkaProperties.serializer_class);
    props.put("metadata.broker.list", KafkaProperties.metadata_broker_list);
    producer = new kafka.javaapi.producer.Producer<Integer, String>(new ProducerConfig(props));
    this.topic = topic;
  }

  public void handlerMsg(String msg){

    String newMsg = msg.substring(2,msg.length());
    newMsg = newMsg.split("&callback")[0];
    producer.send(new KeyedMessage<Integer, String>(topic, newMsg));
  }
  public void close(){
    producer.close();
  }

}
