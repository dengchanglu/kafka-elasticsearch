package http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import kafka.Producer;

import java.net.URLDecoder;

/**
 * Created by dengchanglu on 15-11-25.
 */
public class EsHttpHandler extends SimpleChannelInboundHandler<HttpObject> {
    private final Producer producer;

    public EsHttpHandler(Producer producer) {
        this.producer = producer;
    }
    private HttpRequest request;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        if (httpObject instanceof HttpRequest) {

            HttpRequest request = (HttpRequest)httpObject;
            String msg = URLDecoder.decode(request.getUri(),"UTF-8");
            if(msg.equals("/favicon.ico")){
            }else{
                producer.handlerMsg(msg);
            }

        }if (httpObject instanceof HttpContent) {
            HttpContent content = (HttpContent) httpObject;
            ByteBuf buf = content.content();
            System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
            buf.release();

            String res = "OK";
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,Unpooled.wrappedBuffer(res.getBytes("UTF-8")));
            response.headers().set("CONTENT_TYPE", "text/plain");
            response.headers().set("CONTENT_LENGTH",
                    response.content().readableBytes());
            response.headers().set("ACCESS_CONTROL_ALLOW_ORIGIN", "*");
            response.headers().set("CONNECTION", HttpHeaders.Values.CLOSE);
            response.setStatus(HttpResponseStatus.OK);
            channelHandlerContext.write(response);
            channelHandlerContext.flush();
        }
    }
}
