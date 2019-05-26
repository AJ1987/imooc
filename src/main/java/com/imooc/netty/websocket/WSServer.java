package com.imooc.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.graalvm.compiler.hotspot.replacements.SHASubstitutions;

/**
 * @Auther: AJ
 * @Date: 2019/5/26 4:34 PM
 * @Function:
 */
public class WSServer {
    public static void main(String[] args) throws  Exception {
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        EventLoopGroup subGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(mainGroup,subGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(null);

            ChannelFuture feature = server.bind(8088).sync();

            feature.channel().closeFuture().sync();
        }
        finally {
            mainGroup.shutdownGracefully();
            subGroup.shutdownGracefully();
        }

    }
}
