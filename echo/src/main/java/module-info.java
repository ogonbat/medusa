import org.medusa.controllers.echo.EchoController;
import org.medusa.rpc.interfaces.IMedusaController;


module medusa.controllers.echo {
    requires medusa.rpc;
    provides IMedusaController
            with EchoController;
}