package org.medusa.rpc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.medusa.rpc.exceptions.MedusaControllerException;
import org.medusa.rpc.interfaces.IMedusaController;
import org.medusa.rpc.proto.ProtocolRequest;

import java.util.ServiceLoader;
import java.util.logging.Logger;

public class MedusaServer extends ChannelInboundHandlerAdapter {

    private static final Logger logger = Logger.getLogger("Medusa Server");

    public void start(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            logger.info("Starting Server...");
            ServerBootstrap b = new ServerBootstrap();
            MedusaHandler handler = new MedusaHandler();
            MedusaRouter router = new MedusaRouter();
            Iterable<IMedusaController> servicesR = ServiceLoader.load(IMedusaController.class);
            for(IMedusaController serviceR: servicesR) {
                try {
                    logger.info("Passa");
                    router.addRoute(serviceR);
                } catch (MedusaControllerException e) {
                    e.printStackTrace();
                }
            }
            handler.addRouter(router);
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                            ch.pipeline().addLast(new ProtobufDecoder(ProtocolRequest.getDefaultInstance()));
                            ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                            ch.pipeline().addLast(new ProtobufEncoder());
                            ch.pipeline().addLast(handler);
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
            logger.info("Server Started");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
