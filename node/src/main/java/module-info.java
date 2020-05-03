import org.medusa.node.interfaces.MedusaCertFactory;
import org.medusa.node.interfaces.MedusaKeyPairFactory;

module org.medusa.node {
    uses MedusaCertFactory;
    uses MedusaKeyPairFactory;
    exports org.medusa.node.interfaces;

}