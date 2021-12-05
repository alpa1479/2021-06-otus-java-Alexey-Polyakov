package ru.otus.hw17.client;

public class GrpcClientMain {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws InterruptedException {
        BlockingNumberGeneratorClient.configureTo(SERVER_HOST, SERVER_PORT)
                .withClientRange(0, 50).andClientDelayTimeInSeconds(1)
                .withServerRange(0, 30).andServerDelayTimeInSeconds(2)
                .generate();
    }
}
