package org.medusa.rpc.client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.medusa.rpc.MedusaRequest;
import org.medusa.rpc.MedusaResponse;
import org.medusa.rpc.proto.ProtocolRequest;

import java.util.logging.Logger;

public class MedusaClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = Logger.getLogger("Medusa Handler");
    private MedusaRequest request;

    public void setRequest(MedusaRequest request) {
        this.request = request;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ChannelFuture future = ctx.writeAndFlush(request.encode());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MedusaResponse response = new MedusaResponse();
        response.load((ProtocolRequest)msg);
        logger.info(String.format("Code: %d", response.getCode()));
    }
}
