import org.medusa.node.interfaces.MedusaRpcFactory;
import org.medusa.rpc.MedusaServer;

module org.medusa.rpc {
    provides MedusaRpcFactory
            with MedusaServer;
    requires org.medusa.node;
    requires grpc.api;
    requires java.logging;
    requires com.google.protobuf;
    requires grpc.stub;
    requires java.annotation;
    requires com.google.common;
    requires grpc.protobuf;
}