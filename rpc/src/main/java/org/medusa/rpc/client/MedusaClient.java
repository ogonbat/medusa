package org.medusa.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.medusa.rpc.MedusaRequest;
import org.medusa.rpc.proto.ProtocolRequest;

public class MedusaClient {
    private String host = "127.0.0.1";
    private int port = 5060;

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void send(String namespace, String action, String body) throws Exception {
        MedusaRequest request = new MedusaRequest();
        request.setAction(action);
        request.setNamespace(namespace);
        request.setBody(body);
        send(request);
    }
    public void send(String namespace, String action, String body, String hash, String signature) throws Exception {
        MedusaRequest request = new MedusaRequest();
        request.setAction(action);
        request.setNamespace(namespace);
        request.setBody(body);
        request.setHeader(hash, signature);
        send(request);
    }
    private void send(MedusaRequest request) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                public void initChannel(SocketChannel ch)
                        throws Exception {
                    MedusaClientHandler handler = new MedusaClientHandler();
                    handler.setRequest(request);
                    ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                    ch.pipeline().addLast(new ProtobufDecoder(ProtocolRequest.getDefaultInstance()));
                    ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                    ch.pipeline().addLast(new ProtobufEncoder());
                    ch.pipeline().addLast(handler);
                }
            });
            ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
