package mz.hospital.server;

import Hospital.HelloService;
import Hospital.HelloServiceHelper;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

public class Main {

    public static void main(String[] args) {

        try {
            ORB orb = ORB.init(args, null);

            POA rootPOA = POAHelper.narrow(
                    orb.resolve_initial_references("RootPOA")
            );

            rootPOA.the_POAManager().activate();

            HelloServiceImpl helloService = new HelloServiceImpl();

            org.omg.CORBA.Object reference =
                    rootPOA.servant_to_reference(helloService);

            HelloService helloServiceRef =
                    HelloServiceHelper.narrow(reference);

            System.out.println("Servidor CORBA iniciado.");
            System.out.println(
                    "Object reference: " +
                            orb.object_to_string(helloServiceRef)
            );

            orb.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}