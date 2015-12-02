package http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import kafka.Producer;

/**
 * Created by dengchanglu on 15-11-25.
 */
public class EsChannelInitializer extends ChannelInitializer {
    private final Producer producer;

    public EsChannelInitializer(Producer producer) {
        this.producer = producer;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline p = channel.pipeline();
        p.addLast("decoder",new HttpRequestDecoder());
        // Uncomment the following line if you don't want to handle HttpChunks.
        //p.addLast(new HttpObjectAggregator(1048576));
        p.addLast(new HttpResponseEncoder());
        // Remove the following line if you don't want automatic content compression.
        p.addLast(new HttpContentCompressor());
        p.addLast(new EsHttpHandler(producer));
    }
}
