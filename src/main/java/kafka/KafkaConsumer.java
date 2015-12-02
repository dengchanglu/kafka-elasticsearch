package kafka;

import com.alibaba.fastjson.JSONObject;
import es.EsForward;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import java.util.List;
import java.util.Map;

/**
 * Created by perfection on 15-11-25.
 */
public class KafkaConsumer implements Runnable {
    private List<EsForward> forwards;
    private KafkaStream m_stream;

    public KafkaConsumer(KafkaStream m_stream, List<EsForward> forwards) {
        this.m_stream = m_stream;
        this.forwards = forwards;
    }


    @Override
    public void run() {
        for (ConsumerIterator<byte[], byte[]> it = m_stream.iterator(); it.hasNext(); ) {
            String msg = (new String(it.next().message())).replaceAll("=",":");
            Map map = (Map) JSONObject.parseObject(msg);
            for (EsForward esForward : forwards) {
                esForward.add(map);
            }
        }
    }
}
