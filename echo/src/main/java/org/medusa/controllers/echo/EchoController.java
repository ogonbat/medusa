package org.medusa.controllers.echo;
import org.medusa.rpc.MedusaRequest;
import org.medusa.rpc.MedusaResponse;
import org.medusa.rpc.annotations.MedusaNamespace;
import org.medusa.rpc.annotations.MedusaRoute;
import org.medusa.rpc.interfaces.IMedusaController;

@MedusaNamespace(namespace = "echo")
public class EchoController implements IMedusaController {
    @MedusaRoute(action = "echo")
    public MedusaResponse echoAction(MedusaRequest request) {
        String body_request = request.getBody();
        MedusaResponse response = new MedusaResponse();
        response.setCode(200);
        response.setBody(body_request);
        return response;
    }
}
