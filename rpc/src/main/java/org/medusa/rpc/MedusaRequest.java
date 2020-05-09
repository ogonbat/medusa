package org.medusa.rpc;

import org.medusa.rpc.exceptions.MedusaRequestException;
import org.medusa.rpc.proto.ProtocolHeader;
import org.medusa.rpc.proto.ProtocolRequest;

public class MedusaRequest {
    private String version = "0.1.0";
    private String namespace;
    private String action;
    private String body = "";
    private ProtocolRequest request;
    private ProtocolRequest.Builder builder;
    private String hash;
    private String signature;

    public String getVersion() {
        return version;
    }

    public void setHeader(String hash, String signature) {
        this.hash = hash;
        this.signature = signature;
    }
    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return namespace;
    }

    public void load(ProtocolRequest data){
        request = data;
        action = request.getAction();
        namespace = request.getNamespace();
        ProtocolHeader header = request.getHeaders();
        hash = header.getHash();
        signature = header.getSignature();
        body = request.getBody();
    }

    public ProtocolRequest encode() throws MedusaRequestException {
        builder = ProtocolRequest.newBuilder();
        builder.setVersion(version);
        builder.setType(ProtocolRequest.Type.REQUEST);
        if(namespace != null) {
            builder.setNamespace(namespace);
        }else {
            throw new MedusaRequestException("Namespace is required");
        }
        if(action != null) {
            builder.setAction(action);
        }else {
            throw new MedusaRequestException("Action is required");
        }
        if(hash != null && signature != null) {
            ProtocolHeader header = ProtocolHeader.newBuilder().setHash(hash).setSignature(signature).build();
            builder.setHeaders(header);
        }
        builder.setBody(body);
        return builder.build();

    }

}
