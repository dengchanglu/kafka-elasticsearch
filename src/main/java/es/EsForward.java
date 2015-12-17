package es;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import utils.KafkaProperties;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by perfection on 15-11-25.
 */
public class EsForward extends Thread{
    private static final int ONE_DAY_SECONDS = 86_400;

    private final int HANDLER_WORKERS = Runtime.getRuntime().availableProcessors() * 2;

    private final BlockingQueue<Map<String, Object>> queue = new LinkedBlockingQueue<>();

    private final ExecutorService preHandlerExecutor = Executors.newFixedThreadPool(HANDLER_WORKERS, new DataPreHandleThreadFactory());

    private final ExecutorService requestHandlerExecutor = Executors.newFixedThreadPool(HANDLER_WORKERS, new EsRequestThreadFactory());

    public EsForward(TransportClient client) {
        BlockingQueue<IndexRequest> requestQueue = new LinkedBlockingQueue<>();
        preHandle(client, requestQueue);
        handleRequest(client, requestQueue);
    }

    public void add(Map<String, Object> obj) {
        queue.add(obj);
    }

    private void preHandle(TransportClient client, BlockingQueue<IndexRequest> requestQueue) {
        for (int i = 0; i < HANDLER_WORKERS; i++)
            preHandlerExecutor.execute(new PreHandleWorker(client, requestQueue));
    }

    private void handleRequest(TransportClient client, BlockingQueue<IndexRequest> requestQueue) {
        for (int i = 0; i < HANDLER_WORKERS; i++)
            requestHandlerExecutor.execute(new RequestHandleWorker(client, requestQueue));
    }

    private void submitRequest(BulkRequestBuilder bulkRequestBuilder) {
        BulkResponse responses = bulkRequestBuilder.get();
        if (responses.hasFailures()) {
            System.out.println("Failure: " + responses.buildFailureMessage());
        }
    }

    private void addRequest(TransportClient client, BlockingQueue<IndexRequest> requestQueue, Map<String, Object> source) {
        Date date=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String time=format.format(date);
        IndexRequestBuilder builder = client.prepareIndex();
        builder.setIndex(KafkaProperties.index+time);
        String type = KafkaProperties.appLog;
        builder.setType(type);
        builder.setSource(JSON.toJSONString(source));
        requestQueue.add(builder.request());
    }


    class DataPreHandleThreadFactory implements ThreadFactory {

        private final AtomicInteger counter = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            PreHandlerThread thread = new PreHandlerThread(r);
            thread.setName("thread-relog-preHandler-" + counter.incrementAndGet());
            return thread;
        }
    }

    class EsRequestThreadFactory implements ThreadFactory {

        private final AtomicInteger counter = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            RequestHandlerThread thread = new RequestHandlerThread(r);
            thread.setName("thread-relog-requestHandler-" + counter.incrementAndGet());
            return thread;
        }
    }

    /**
     * 数据预处理线程
     */
    class PreHandlerThread extends Thread {
        public PreHandlerThread(Runnable target) {
            super(target);
        }
    }

    /**
     * es请求处理线程
     */
    class RequestHandlerThread extends Thread {
        public RequestHandlerThread(Runnable target) {
            super(target);
        }
    }

    class PreHandleWorker implements Runnable {
        private final TransportClient client;
        private final BlockingQueue<IndexRequest> requestQueue;

        PreHandleWorker(TransportClient client, BlockingQueue<IndexRequest> requestQueue) {
            this.client = client;
            this.requestQueue = requestQueue;
        }

        @Override
        public void run() {
            while (true) {
                Map<String, Object> mapSource = null;
                try {
                    mapSource = queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                Map
                addRequest(client, requestQueue, mapSource);
            }
        }

    }

    class RequestHandleWorker implements Runnable {
        private final TransportClient client;
        private final BlockingQueue<IndexRequest> requestQueue;

        public RequestHandleWorker(TransportClient client, BlockingQueue<IndexRequest> requestQueue) {
            this.client = client;
            this.requestQueue = requestQueue;
        }

        @Override
        public void run() {
            BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
            while (true) {
                IndexRequest request = null;
                try {
                    request = requestQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (request == null)
                    continue;

                bulkRequestBuilder.add(request);

                if (requestQueue.isEmpty() && bulkRequestBuilder.numberOfActions() > 0) {
                    submitRequest(bulkRequestBuilder);
                    bulkRequestBuilder = client.prepareBulk();
                    continue;
                }

                if (bulkRequestBuilder.numberOfActions() == EsPools.getBulkRequestNumber()) {
                    submitRequest(bulkRequestBuilder);
                    bulkRequestBuilder = client.prepareBulk();
                }

            }
        }

    }
}
