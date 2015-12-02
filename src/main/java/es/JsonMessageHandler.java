package es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import kafka.consumer.KafkaStream;
import kafka.message.Message;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;

import java.util.Map;

/**
 * Created by perfection on 15-11-25.
 */
public class JsonMessageHandler extends MessageHandler {
    private Client client;
    private Map<String, Object> messageMap;

    public JsonMessageHandler(Client client) {
        this.client = client;
    }

    protected void readMessage(String msg) throws Exception {
        JSONObject jsonObject = JSON.parseObject(msg);
        messageMap = (Map) jsonObject;
    }

    protected String getIndex() {
        return (String) messageMap.get("index");
    }

    protected String getType() {
        return (String) messageMap.get("type");
    }

    protected String getId() {
        return (String) messageMap.get("id");
    }

    protected Map<String, Object> getSource() {
        return (Map<String, Object>) messageMap.get("source");
    }

    protected IndexRequestBuilder createIndexRequestBuilder() {
        // Note: prepareIndex() will automatically create the index if it
        // doesn't exist
        return client.prepareIndex(getIndex(), getType(), getId()).setSource(getSource());
    }

    @Override
    public void handle(BulkRequestBuilder bulkRequestBuilder, KafkaStream message) throws Exception {
//        this.readMessage(message);
        bulkRequestBuilder.add(this.createIndexRequestBuilder());
    }

}
