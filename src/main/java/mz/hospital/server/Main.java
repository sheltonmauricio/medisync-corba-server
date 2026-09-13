package mz.hospital.server;

import Hospital.HelloService;
import Hospital.HelloServiceHelper;
import Hospital.PatientService;
import Hospital.PatientServiceHelper;
import mz.hospital.server.db.DatabaseManager;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;


public class Main {

    public static void main(String[] args) {

        try {

            DatabaseManager.initialize();

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

            org.omg.CORBA.Object namingReference =
                    orb.resolve_initial_references("NameService");

            NamingContextExt namingContext =
                    NamingContextExtHelper.narrow(namingReference);

            NameComponent[] name = namingContext.to_name("HelloService");

            namingContext.rebind(name, helloServiceRef);

            PatientServiceImpl patientService = new PatientServiceImpl();

            org.omg.CORBA.Object patientReference =
                    rootPOA.servant_to_reference(patientService);

            PatientService patientServiceRef =
                    PatientServiceHelper.narrow(patientReference);

            NameComponent[] patientName =
                    namingContext.to_name("PatientService");

            namingContext.rebind(patientName, patientServiceRef);

            System.out.println("Servidor CORBA iniciado.");
            System.out.println("HelloService registado no Naming Service.");
            System.out.println("PatientService registado no Naming Service.");

            orb.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}