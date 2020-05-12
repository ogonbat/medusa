package org.medusa.iandt;

import org.medusa.iandt.client.ClientTest;

public class AppTest {
    public static void main(String[] args) {
        try{
            ClientTest test_client = new ClientTest();
            test_client.test_echo_server();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
