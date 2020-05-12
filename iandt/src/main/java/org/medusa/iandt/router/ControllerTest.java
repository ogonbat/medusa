package org.medusa.iandt.router;

import org.medusa.rpc.MedusaResponse;
import org.medusa.rpc.annotations.MedusaNamespace;
import org.medusa.rpc.annotations.MedusaRoute;
import org.medusa.rpc.interfaces.IMedusaController;

@MedusaNamespace(namespace = "controller_test")
public class ControllerTest implements IMedusaController {
    @MedusaRoute(action = "test")
    public MedusaResponse echo(){
        MedusaResponse response = new MedusaResponse();
        response.setBody("Test Controller");
        response.setCode(200);
        return response;
    }
}
