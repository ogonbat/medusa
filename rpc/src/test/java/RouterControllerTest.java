import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.medusa.rpc.MedusaAction;
import org.medusa.rpc.MedusaResponse;
import org.medusa.rpc.MedusaRouter;
import org.medusa.rpc.annotations.MedusaNamespace;
import org.medusa.rpc.annotations.MedusaRoute;
import org.medusa.rpc.exceptions.MedusaControllerException;
import org.medusa.rpc.interfaces.IMedusaController;

@RunWith(JUnit4.class)
public class RouterControllerTest {
    @MedusaNamespace(namespace = "controller_test")
    class TestController implements IMedusaController {
        @MedusaRoute(action = "test")
        public MedusaResponse echo(){
            MedusaResponse response = new MedusaResponse();
            response.setBody("Test Controller");
            response.setCode(200);
            return response;
        }
    }
    @Test
    public void controller_router_test() throws MedusaControllerException {
        MedusaRouter routerTest = new MedusaRouter();
        routerTest.addRoute(new TestController());
        MedusaAction actionReturned = routerTest.getRoute("controller_test", "test");
        MedusaResponse response = actionReturned.execute();
        Assert.assertEquals("Test Controller", response.getBody());
    }
}
