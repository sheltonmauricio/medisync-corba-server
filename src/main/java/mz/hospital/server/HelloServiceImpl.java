package mz.hospital.server;

import Hospital.HelloServicePOA;

public class HelloServiceImpl extends HelloServicePOA {

    @Override
    public String sayHello() {
        return "Olá, cliente Python!";
    }
}