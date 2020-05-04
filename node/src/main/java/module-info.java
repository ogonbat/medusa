import org.medusa.node.interfaces.MedusaCAFactory;
import org.medusa.node.interfaces.MedusaCSRFactory;
import org.medusa.node.interfaces.MedusaCertFactory;
import org.medusa.node.interfaces.MedusaKeyPairFactory;

module org.medusa.node {
    uses MedusaCertFactory;
    uses MedusaCSRFactory;
    uses MedusaCAFactory;
    uses MedusaKeyPairFactory;
    exports org.medusa.node.interfaces;

}