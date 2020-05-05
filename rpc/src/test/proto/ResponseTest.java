import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.medusa.rpc.proto.MedusaResponse;

@RunWith(JUnit4.class)
public class ResponseTest {
    @Test
    public void response_test(){
        MedusaResponse response = MedusaResponse
                .newBuilder()
                .setBody("Response Body")
                .setCode(200)
                .build();
        Assert.assertEquals(200, response.getCode());
        Assert.assertEquals("Response Body", response.getBody());
    }
}
