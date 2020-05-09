package org.medusa.node;
import org.medusa.rpc.MedusaServer;
import java.util.logging.Logger;

public class Application {
    public static final Logger logger = Logger.getLogger("Main");
    public static void main(String[] args) {
        logger.info("Main is called");
        MedusaServer server = new MedusaServer();
        server.start(5060);
    }
}
