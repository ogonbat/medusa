package org.medusa.iandt.client;

import org.medusa.rpc.client.MedusaClient;

public class ClientTest {
    public void test_echo_server() throws Exception {
        MedusaClient client = new MedusaClient();
        client.setPort(5060);
        client.send("echo", "echo", "Test Echo");
    }
}
