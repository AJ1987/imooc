/**
 * @Auther: AJ
 * @Date: 2019/5/26 2:54 AM
 * @Function:
 */

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Description: 实现客户端发送一个请求，服务器返回hello netty
 */
public class HelloServer {
    public static void main(String[] args) throws Exception{
        //定义一对线程组
        //定义主线程组，用于接受客户端的链接，但是不做任何处理，像老板一样，不做事
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //定义从线程组，主线程组会把任务丢给从线程组，让从线程组执行任务
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //netty server的创建 ServerBootstrap是一个启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)  //设置主从线程组
                    .channel(NioServerSocketChannel.class)//设置nio双向通道
                    .childHandler(new HelloServerInitializer());//子处理器，用户处理workgroup

            //启动server，并且设置8088位启动的端口号，同时启动方式为同步
            ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();

            //监听关闭的channel，设置为同步方式
            channelFuture.channel().closeFuture().sync();
        }
        finally {
            //关闭主线程group
            bossGroup.shutdownGracefully();

            //关闭从线程
            workerGroup.shutdownGracefully();
        }

    }
}
