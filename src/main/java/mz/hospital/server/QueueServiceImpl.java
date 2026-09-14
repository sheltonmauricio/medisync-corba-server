package mz.hospital.server;

import Hospital.QueueServicePOA;

import java.util.LinkedList;
import java.util.Queue;

public class QueueServiceImpl extends QueueServicePOA {

    private final Queue<Integer> queue = new LinkedList<>();

    @Override
    public void addToQueue(int patientId) {
        queue.add(patientId);
    }

    @Override
    public int getNextPatient() {
        if (queue.isEmpty()) {
            return 0;
        }

        return queue.poll();
    }

    @Override
    public int getQueueSize() {
        return queue.size();
    }
}