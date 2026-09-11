package mz.hospital.client;

import Hospital.HelloService;
import Hospital.HelloServiceHelper;
import org.omg.CORBA.ORB;

public class TestClient {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Uso: TestClient <IOR>");
            return;
        }

        try {
            ORB orb = ORB.init(args, null);

            org.omg.CORBA.Object object =
                    orb.string_to_object(args[0]);

            HelloService helloService =
                    HelloServiceHelper.narrow(object);

            String response = helloService.sayHello();

            System.out.println("Resposta do servidor: " + response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}