package org.medusa.rpc;

import org.medusa.rpc.annotations.MedusaNamespace;
import org.medusa.rpc.annotations.MedusaRoute;
import org.medusa.rpc.exceptions.MedusaControllerException;
import org.medusa.rpc.interfaces.IMedusaController;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.logging.Logger;

public class MedusaRouter {
    private static final Logger logger = Logger.getLogger("Handler");
    private HashMap<String, IMedusaController> routes = new HashMap<String, IMedusaController>();

    public void addRoute(IMedusaController controller) throws MedusaControllerException {
        //get the annotations to charge the router
        Class<?> classController = controller.getClass();
        if(!classController.isAnnotationPresent(MedusaNamespace.class)) {
            throw new MedusaControllerException("The namespace is required for controller");
        }
        String namespace = classController.getAnnotation(MedusaNamespace.class).namespace();
        if (namespace.isEmpty()) {
            throw new MedusaControllerException("Namespace value is required");
        }
        routes.put(namespace, controller);
    }

    public MedusaAction getRoute(String namespace, String action) throws MedusaControllerException {
        if(routes.containsKey(namespace)) {
            IMedusaController controller = routes.get(namespace);
            Class<?> classController = controller.getClass();
            for(Method method: classController.getDeclaredMethods()) {
                // get the list of the methods and check if they have annotation
                if(method.isAnnotationPresent(MedusaRoute.class)) {
                    //the method is a route
                    String actionRoute = method.getAnnotation(MedusaRoute.class).action();
                    boolean authRoute = method.getAnnotation(MedusaRoute.class).auth();
                    if (actionRoute.equals(action)) {
                        return new MedusaAction(method, controller, actionRoute, authRoute);
                    }
                }
            }
            throw new MedusaControllerException("No action in the Controller");
        } else {
            throw new MedusaControllerException("Namespace does not exist");
        }
    }
}
