package ru.otus.hw17.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw17.converter.RangeToRangeMessageConverter;
import ru.otus.hw17.model.Range;
import ru.otus.hw17.protobuf.generated.GeneratedNumberMessage;
import ru.otus.hw17.protobuf.generated.RangeMessage;
import ru.otus.hw17.protobuf.generated.RemoteNumberGeneratorGrpc;
import ru.otus.hw17.util.Threads;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class BlockingNumberGeneratorClient {

    private static final Logger log = LoggerFactory.getLogger(BlockingNumberGeneratorClient.class);

    private final String host;
    private final int port;
    private final RangeToRangeMessageConverter rangeToRangeMessageConverter;

    private Range clientRange;
    private Range serverRange;
    private int clientDelayTime;
    private int serverDelayTime;
    private AtomicInteger lastValueFromServer;

    private CountDownLatch startSignal;
    private CountDownLatch doneSignal;

    private RemoteNumberGeneratorGrpc.RemoteNumberGeneratorBlockingStub blockingStub;

    public static BlockingNumberGeneratorClient configureTo(String host, int port) {
        return new BlockingNumberGeneratorClient(host, port, new RangeToRangeMessageConverter());
    }

    public BlockingNumberGeneratorClient withClientRange(int firstValue, int lastValue) {
        clientRange = new Range(firstValue, lastValue);
        return this;
    }

    public BlockingNumberGeneratorClient andClientDelayTimeInSeconds(int delayTime) {
        this.clientDelayTime = delayTime;
        return this;
    }

    public BlockingNumberGeneratorClient withServerRange(int firstValue, int lastValue) {
        serverRange = new Range(firstValue, lastValue);
        return this;
    }

    public BlockingNumberGeneratorClient andServerDelayTimeInSeconds(int delayTime) {
        this.serverDelayTime = delayTime;
        return this;
    }

    public BlockingNumberGeneratorClient generate() throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = RemoteNumberGeneratorGrpc.newBlockingStub(channel);

        startSignal = new CountDownLatch(1);
        doneSignal = new CountDownLatch(2);

        lastValueFromServer = new AtomicInteger(0);
        ExecutorService generateTasksExecutor = Executors.newFixedThreadPool(2);
        generateTasksExecutor.submit(this::generateNumbersLocally);
        generateTasksExecutor.submit(this::getLastGeneratedNumberFromServer);

        startSignal.countDown();
        doneSignal.await();

        generateTasksExecutor.shutdown();
        channel.shutdown();
        log.info(">>>> finished main thread");
        return this;
    }

    private void generateNumbersLocally() {
        Threads.await(startSignal);
        log.info(">>>> started generateNumbersLocally thread");
        int currentValue = 0;
        int previousValue = 0;
        for (int i = clientRange.firstValue(); i < clientRange.lastValue(); i++) {
            Threads.delay(clientDelayTime);
            int lastValue = lastValueFromServer.get();
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
        doneSignal.countDown();
    }

    private void getLastGeneratedNumberFromServer() {
        Threads.await(startSignal);
        log.info(">>>> started getLastGeneratedNumberFromServer thread");
        RangeMessage rangeMessage = rangeToRangeMessageConverter.convert(serverRange, serverDelayTime);
        Iterator<GeneratedNumberMessage> generatedNumberMessages = blockingStub.generateNumberSequence(rangeMessage);
        while (generatedNumberMessages.hasNext()) {
            GeneratedNumberMessage message = generatedNumberMessages.next();
            int value = message.getValue();
            lastValueFromServer.set(value);
            log.info(">>>> [server] last value = {}", value);
        }
        doneSignal.countDown();
    }
}
