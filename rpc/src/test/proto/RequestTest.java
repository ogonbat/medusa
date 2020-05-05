import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.medusa.rpc.proto.MedusaHeader;
import org.medusa.rpc.proto.MedusaRequest;

@RunWith(JUnit4.class)
public class RequestTest {
    @Test
    public void request_test(){
        MedusaRequest request = MedusaRequest
                .newBuilder()
                .setAction("testing")
                .setBody("Test Body")
                .build();
        Assert.assertEquals("testing", request.getAction());
        Assert.assertEquals("Test Body", request.getBody());
    }

    @Test
    public void request_with_header_test(){
        MedusaHeader header = MedusaHeader.newBuilder()
                .setHash("Hash Test")
                .setSignature("Signature")
                .build();
        MedusaRequest request = MedusaRequest
                .newBuilder()
                .setAction("testing")
                .setBody("Test Body")
                .setHeaders(header)
                .build();
        MedusaHeader returnedHeader = request.getHeaders();
        Assert.assertEquals("testing", request.getAction());
        Assert.assertEquals("Test Body", request.getBody());
        Assert.assertEquals("Hash Test", returnedHeader.getHash());
    }
}