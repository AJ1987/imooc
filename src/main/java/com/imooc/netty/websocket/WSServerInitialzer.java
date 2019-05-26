package com.imooc.netty.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.util.PriorityQueue;

/**
 * @Auther: AJ
 * @Date: 2019/5/26 4:38 PM
 * @Function:
 */
public class WSServerInitialzer  extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        //websockt 基于http协议，所以需要有http编解码器httpServerCodec
        pipeline.addLast(new HttpServerCodec());

        //对大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());

        //对httpmessage进行聚合，聚合成fullhttprequest或者fullhttpresponse
        //几乎在netty中的编程中斗会使用到此handler
        pipeline.addLast(new HttpObjectAggregator(1024*64));

        //======================以上是用于支持http协议======================


        //======================以以是用于支持httpwebsocket======================
        //websocket 服务器处理的协议，用于指定给客户端访问的路由ws
        //本handler会帮你处理一些繁重复杂的事
        //会帮你处理握手动作： handshaking(close,ping, pong) ping+png = 心跳
        //对于websocket来说，都是以frames进行传输的，不同的数据类型对应的frames也不同
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        //添加自定义的handler
        pipeline.addLast(new ChatHandler());
    }
}
