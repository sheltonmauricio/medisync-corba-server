package mz.hospital.client;

import Hospital.QueueService;
import Hospital.QueueServiceHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

public class QueueTestClient {

    public static void main(String[] args) {

        try {
            ORB orb = ORB.init(args, null);

            org.omg.CORBA.Object namingReference =
                    orb.resolve_initial_references("NameService");

            NamingContextExt namingContext =
                    NamingContextExtHelper.narrow(namingReference);

            org.omg.CORBA.Object object =
                    namingContext.resolve_str("QueueService");

            QueueService queueService =
                    QueueServiceHelper.narrow(object);

            queueService.addToQueue(1);
            queueService.addToQueue(2);

            System.out.println(
                    "Tamanho da fila: " + queueService.getQueueSize()
            );

            int patientId = queueService.getNextPatient();

            System.out.println(
                    "Próximo paciente: " + patientId
            );

            System.out.println(
                    "Tamanho da fila após atendimento: "
                            + queueService.getQueueSize()
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}