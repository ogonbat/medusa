package org.medusa.rpc;

import io.grpc.stub.StreamObserver;
import org.medusa.rpc.exceptions.MedusaControllerException;
import org.medusa.rpc.proto.MedusaRequest;
import org.medusa.rpc.proto.MedusaResponse;
import org.medusa.rpc.proto.MedusaServerGrpc;

import java.util.logging.Logger;

/**
 * Handler for the GRPC Server
 */
public class MedusaHandler extends MedusaServerGrpc.MedusaServerImplBase {
    private static final Logger logger = Logger.getLogger("Medusa Server Handler");
    private MedusaRouter router;

    @Override
    public void endpoint(MedusaRequest request, StreamObserver<MedusaResponse> responseObserver) {
        responseObserver.onNext(invokeAction(request));
        responseObserver.onCompleted();
    }

    @Override
    public void status(MedusaRequest request, StreamObserver<MedusaResponse> responseObserver) {
        responseObserver.onNext(
                MedusaResponse.newBuilder().setCode(200).setBody("Status Ok").build()
        );
        responseObserver.onCompleted();
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
            if(actionMethod.auth()) {
                // authentication is required
                //check if the hash of the node is a peer and get the certificate

            }
        } catch (MedusaControllerException e) {
            e.printStackTrace();
        }

        return MedusaResponse.newBuilder().setCode(200).build();
    }
}
