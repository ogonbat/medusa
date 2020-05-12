package org.medusa.iandt.router;

import org.junit.Assert;
import org.medusa.rpc.MedusaAction;
import org.medusa.rpc.MedusaResponse;
import org.medusa.rpc.MedusaRouter;
import org.medusa.rpc.exceptions.MedusaControllerException;

public class RouterTest {
    public void router_test() throws MedusaControllerException {
        MedusaRouter routerTest = new MedusaRouter();
        routerTest.addRoute(new ControllerTest());
        MedusaAction actionReturned = routerTest.getRoute("controller_test", "test");
        MedusaResponse response = actionReturned.execute();
        Assert.assertEquals("Test Controller", response.getBody());
    }
}
