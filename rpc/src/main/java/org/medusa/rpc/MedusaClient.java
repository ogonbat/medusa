//package org.medusa.rpc;
//
//public class MedusaClient {
//    private final MedusaServerGrpc.MedusaServerBlockingStub serverStub;
//
//    public MedusaClient(String host, int port) {
//        String target = String.format("%1$s:%2$d", host, port);
//        serverStub = MedusaServerGrpc.newBlockingStub(ManagedChannelBuilder
//                .forTarget(target)
//                .usePlaintext()
//                .build());
//    }
//    public MedusaClient(int port) {
//        String target = String.format("%1$s:%2$d", "127.0.0.1", port);
//        serverStub = MedusaServerGrpc.newBlockingStub(ManagedChannelBuilder
//                .forTarget(target)
//                .usePlaintext()
//                .build());
//    }
//
//}
