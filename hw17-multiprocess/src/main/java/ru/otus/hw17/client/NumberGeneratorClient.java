package ru.otus.hw17.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw17.converter.RangeToRangeMessageConverter;
import ru.otus.hw17.model.Range;
import ru.otus.hw17.protobuf.generated.GeneratedNumberMessage;
import ru.otus.hw17.protobuf.generated.RangeMessage;
import ru.otus.hw17.protobuf.generated.RemoteNumberGeneratorGrpc;
import ru.otus.hw17.util.Threads;

@RequiredArgsConstructor
public class NumberGeneratorClient {

    private static final Logger log = LoggerFactory.getLogger(NumberGeneratorClient.class);

    private final String host;
    private final int port;
    private final RangeToRangeMessageConverter rangeToRangeMessageConverter;
    private final Object monitor;

    private Range clientRange;
    private Range serverRange;
    private int clientDelayTime;
    private int serverDelayTime;
    private int lastValueFromServer;
    private RemoteNumberGeneratorGrpc.RemoteNumberGeneratorStub stub;

    public static NumberGeneratorClient configureTo(String host, int port) {
        return new NumberGeneratorClient(host, port, new RangeToRangeMessageConverter(), new Object());
    }

    public NumberGeneratorClient withClientRange(int firstValue, int lastValue) {
        clientRange = new Range(firstValue, lastValue);
        return this;
    }

    public NumberGeneratorClient andClientDelayTimeInSeconds(int delayTime) {
        this.clientDelayTime = delayTime;
        return this;
    }

    public NumberGeneratorClient withServerRange(int firstValue, int lastValue) {
        serverRange = new Range(firstValue, lastValue);
        return this;
    }

    public NumberGeneratorClient andServerDelayTimeInSeconds(int delayTime) {
        this.serverDelayTime = delayTime;
        return this;
    }

    public NumberGeneratorClient generate() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        stub = RemoteNumberGeneratorGrpc.newStub(channel);

        lastValueFromServer = 0;
        startGettingLastGeneratedNumberFromServer();
        generateNumbersLocally();

        channel.shutdown();
        log.info(">>>> finished main thread");
        return this;
    }

    private void generateNumbersLocally() {
        int currentValue = 0;
        int previousValue = 0;
        for (int i = clientRange.firstValue(); i < clientRange.lastValue(); i++) {
            Threads.delay(clientDelayTime);
            int lastValue = getLastValueFromServer();
            log.trace(">>>> [client] last value = {}", lastValue);
            log.trace(">>>> [client] previous value = {}", previousValue);
            if (previousValue != lastValue) {
                currentValue = currentValue + lastValue + 1;
                previousValue = lastValue;
            } else {
                currentValue = currentValue + 1;
            }
            log.info(">>>> [client] current value = {}", currentValue);
        }
    }

    private void startGettingLastGeneratedNumberFromServer() {
        RangeMessage rangeMessage = rangeToRangeMessageConverter.convert(serverRange, serverDelayTime);
        stub.generateNumberSequence(rangeMessage, new StreamObserver<>() {
            @Override
            public void onNext(GeneratedNumberMessage message) {
                synchronized (monitor) {
                    int value = message.getValue();
                    lastValueFromServer = value;
                    log.info(">>>> [server] last value = {}", value);
                }
            }

            @Override
            public void onError(Throwable t) {
                log.error(t.getMessage(), t);
            }

            @Override
            public void onCompleted() {
                log.info(">>>> generateNumberSequence onCompleted");
            }
        });
    }

    private int getLastValueFromServer() {
        synchronized (monitor) {
            return lastValueFromServer;
        }
    }
}
