import org.medusa.rpc.interfaces.IMedusaController;

module medusa.rpc {
    uses IMedusaController;
    requires java.logging;
    requires com.google.protobuf;
    requires java.annotation;
    requires io.netty.all;
    exports org.medusa.rpc;
    exports org.medusa.rpc.interfaces;
    exports org.medusa.rpc.annotations;
}