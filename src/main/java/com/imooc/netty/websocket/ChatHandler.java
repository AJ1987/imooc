package com.imooc.netty.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

/**
 * @Auther: AJ
 * @Date: 2019/5/26 4:52 PM
 * @Function:
 */

/*
* @Description 处理消息的handler
* TextWebsockerFrame 在netty中，是用于为websocket处理专门文本的对象，frame是消息的载体
* */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //hannelgroup是用来管理和记录所有的客户端的channel，把所有的channel都保存到相应的组里
    private  static ChannelGroup clients =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //当客户端打开连接之后，获取客户端的channel，并且放到channel group中去进行管理
        clients.add(ctx.channel());
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当触发handlerRemove时，channelgroup会自动移除对应的客户端的channel
        //clients.remove(ctx.channel());
        System.out.println("客户端断开，对应的长Id：" + ctx.channel().id().asLongText());
        System.out.println("客户端断开，对应的短Id：" + ctx.channel().id().asShortText());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        //获取到客户端传输过来的消息
        String content = msg.text();
        System.out.println("接受到的数据: " + content);

        for(Channel channel:clients){
            channel.writeAndFlush(
                    new TextWebSocketFrame(
                            "服务器在:" + LocalDateTime.now()
                                    + "，接收到消息， 消息为：" + content));
        }
    }
}
