package org.medusa.rpc;

import org.medusa.rpc.interfaces.IMedusaController;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MedusaAction {
    private Method method;
    private String action;
    private IMedusaController classObj;
    private boolean auth = false;

    public MedusaAction(Method method, IMedusaController objectClass, String action, boolean auth) {
        this.method = method;
        this.action = action;
        this.auth = auth;
        this.classObj = objectClass;
    }

    public boolean auth() {
        return auth;
    }

    public MedusaResponse execute() {
        try{
            method.setAccessible(true);
            return (MedusaResponse) method.invoke(classObj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public MedusaResponse execute(MedusaRequest request) {
        try{
            method.setAccessible(true);
            return (MedusaResponse) method.invoke(classObj, request);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
