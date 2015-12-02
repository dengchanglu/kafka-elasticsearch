package es;

import kafka.consumer.KafkaStream;
import kafka.message.Message;
import org.elasticsearch.action.bulk.BulkRequestBuilder;

import java.nio.ByteBuffer;

/**
 * Created by perfection on 15-11-25.
 */
public abstract class MessageHandler {
    public static byte[] getMessageData(Message message) {
        ByteBuffer buf = message.payload();
        byte[] data = new byte[buf.remaining()];
        buf.get(data);
        return data;
    }

    public abstract void handle(BulkRequestBuilder bulkRequestBuilder, KafkaStream message) throws Exception;
}
