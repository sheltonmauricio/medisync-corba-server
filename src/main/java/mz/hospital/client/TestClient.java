package mz.hospital.client;

import Hospital.HelloService;
import Hospital.HelloServiceHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

public class TestClient {

    public static void main(String[] args) {

        try {
            ORB orb = ORB.init(args, null);

            org.omg.CORBA.Object namingReference =
                    orb.resolve_initial_references("NameService");

            NamingContextExt namingContext =
                    NamingContextExtHelper.narrow(namingReference);

            org.omg.CORBA.Object object =
                    namingContext.resolve_str("HelloService");

            HelloService helloService =
                    HelloServiceHelper.narrow(object);

            String response = helloService.sayHello();

            System.out.println("Resposta do servidor: " + response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}