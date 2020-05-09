package org.medusa.rpc;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.medusa.rpc.exceptions.MedusaControllerException;
import org.medusa.rpc.proto.ProtocolRequest;

import java.util.logging.Logger;

/**
 * Handler for the GRPC Server
 */
public class MedusaHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = Logger.getLogger("Medusa Server Handler");
    private MedusaRouter router;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ProtocolRequest requestData = (ProtocolRequest) msg;
        MedusaRequest request = new MedusaRequest();
        request.load(requestData);
        MedusaResponse response = invokeAction(request);
        ChannelFuture future = ctx.writeAndFlush(response.encode());
        future.addListener(ChannelFutureListener.CLOSE);
    }

    public void addRouter(MedusaRouter routerObj) {
        router = routerObj;
    }
    /**
     * Get the {@code MedusaRequest} action parameter and check if exist and
     * invoke the action correspondant
     * @param request {@code MedusaRequest}
     * @return response {@code MedusaResponse}
     */
    private MedusaResponse invokeAction(MedusaRequest request){
        try{
            String namespace = request.getNamespace();
            String action = request.getAction();
            MedusaAction actionMethod = router.getRoute(namespace, action);
            // check if the action is auth or not
//            if(actionMethod.auth()) {
//                // authentication is required
//                //check if the hash of the node is a peer and get the certificate
//            }
            return actionMethod.execute();
        } catch (MedusaControllerException e) {
            MedusaResponse response = new MedusaResponse();
            response.setCode(200);
            response.setBody(e.getMessage());
            return response;
        }
    }
}
