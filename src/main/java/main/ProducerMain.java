package main;

import http.EsChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import kafka.Producer;
import utils.KafkaProperties;

import java.time.ZoneId;
import java.util.TimeZone;

/**
 * Created by dengchanglu on 15-11-25.
 */
public class ProducerMain {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai")));
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup nioEventLoopGroup2 = new NioEventLoopGroup(2);
        NioEventLoopGroup nioEventLoopGroup1 = new NioEventLoopGroup();
        final Producer producer = new Producer(KafkaProperties.topic);
        serverBootstrap.group(nioEventLoopGroup2, nioEventLoopGroup1)
                .channel(NioServerSocketChannel.class)
                .childHandler(new EsChannelInitializer(producer))
                .option(ChannelOption.SO_BACKLOG, 1024);

        try {
            Channel channel = serverBootstrap.bind(Integer.parseInt(KafkaProperties.port)).sync().channel();
            System.out.println("Producer start finished...");

            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            nioEventLoopGroup2.shutdownGracefully();
            nioEventLoopGroup1.shutdownGracefully();
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                producer.close();
            }
        });
    }
}
