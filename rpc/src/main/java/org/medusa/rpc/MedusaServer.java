package org.medusa.rpc;

import io.grpc.ServerBuilder;
import org.medusa.node.interfaces.MedusaRpcFactory;
import org.medusa.rpc.annotations.MedusaRoute;
import org.medusa.rpc.exceptions.MedusaControllerException;
import org.medusa.rpc.interfaces.IMedusaController;

import java.util.ServiceLoader;
import java.util.logging.Logger;

public class MedusaServer implements MedusaRpcFactory {
    private ServerBuilder builder;
    private static final Logger logger = Logger.getLogger("Medusa Server");

    public void start(int port) {
        try{
            MedusaHandler handler = new MedusaHandler();
            ServiceLoader<IMedusaController> clSrvs = ServiceLoader.load(IMedusaController.class);
            MedusaRouter router = new MedusaRouter();
            for(IMedusaController srv: clSrvs){
                router.addRoute(srv);
            }
            handler.addRouter(router);
            builder = ServerBuilder.forPort(port);
            builder.addService(handler);
        } catch (MedusaControllerException e) {
            e.printStackTrace();
        }


    }

}
