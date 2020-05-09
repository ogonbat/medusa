import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.medusa.rpc.proto.ProtocolHeader;
import org.medusa.rpc.proto.ProtocolRequest;

@RunWith(JUnit4.class)
public class RequestTest {
    @Test
    public void request_test(){
        ProtocolRequest request = ProtocolRequest
                .newBuilder()
                .setAction("testing")
                .setBody("Test Body")
                .build();
        Assert.assertEquals("testing", request.getAction());
        Assert.assertEquals("Test Body", request.getBody());
    }

    @Test
    public void request_with_header_test(){
        ProtocolHeader header = ProtocolHeader.newBuilder()
                .setHash("Hash Test")
                .setSignature("Signature")
                .build();
        ProtocolRequest request = ProtocolRequest
                .newBuilder()
                .setAction("testing")
                .setBody("Test Body")
                .setHeaders(header)
                .build();
        ProtocolHeader returnedHeader = request.getHeaders();
        Assert.assertEquals("testing", request.getAction());
        Assert.assertEquals("Test Body", request.getBody());
        Assert.assertEquals("Hash Test", returnedHeader.getHash());
    }
}