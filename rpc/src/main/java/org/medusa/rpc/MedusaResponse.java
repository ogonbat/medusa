package org.medusa.rpc;

import org.medusa.rpc.exceptions.MedusaResponseException;
import org.medusa.rpc.proto.ProtocolRequest;

public class MedusaResponse {
    private int code = 0;
    private String body = "";
    private ProtocolRequest response;
    private ProtocolRequest.Builder builder;

    public void setBody(String body) {
        this.body = body;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public int getCode() {
        return code;
    }
    public void load(ProtocolRequest data){
        response = data;
        code = response.getCode();
        body = response.getBody();
    }

    public ProtocolRequest encode() throws MedusaResponseException {
        builder = ProtocolRequest.newBuilder();
        if(code != 0) {
            builder.setCode(code);
        }else {
            throw new MedusaResponseException("Code is mandatory");
        }
        builder.setType(ProtocolRequest.Type.RESPONSE);
        builder.setBody(body);
        return builder.build();
    }



}
