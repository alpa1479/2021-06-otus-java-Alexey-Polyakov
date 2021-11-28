package ru.otus.hw16.messagesystem;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw16.client.MsClient;
import ru.otus.hw16.model.message.Message;
import ru.otus.hw16.model.message.MessageBuilder;
import ru.otus.hw16.model.resultdatatype.ResultDataType;
import ru.otus.hw16.model.resultdatatype.VoidResultDataType;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageSystemImpl implements MessageSystem {

    private static final Logger log = LoggerFactory.getLogger(MessageSystemImpl.class);

    private final AtomicBoolean runFlag = new AtomicBoolean(true);

    private static final int MESSAGE_QUEUE_SIZE = 100_000;
    private static final int MSG_HANDLER_THREAD_LIMIT = 2;

    private final Map<String, MsClient> clientMap = new ConcurrentHashMap<>();
    private final BlockingQueue<Message<? extends ResultDataType>> messageQueue = new ArrayBlockingQueue<>(MESSAGE_QUEUE_SIZE);

    private Runnable disposeCallback;

    private final ExecutorService msgProcessor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setName("msg-processor-thread");
        return thread;
    });

    private final ExecutorService msgHandler = Executors.newFixedThreadPool(MSG_HANDLER_THREAD_LIMIT,
            new ThreadFactory() {

                private final AtomicInteger threadNameSeq = new AtomicInteger(0);

                @Override
                public Thread newThread(@NonNull Runnable runnable) {
                    Thread thread = new Thread(runnable);
                    thread.setName("msg-handler-thread-" + threadNameSeq.incrementAndGet());
                    return thread;
                }
            });

    @Override
    public void start() {
        msgProcessor.submit(this::processMessages);
    }

    @Override
    public void dispose() throws InterruptedException {
        log.info("now in the messageQueue {} messages", currentQueueSize());
        runFlag.set(false);
        insertStopMessage();
        msgProcessor.shutdown();
        var result = msgHandler.awaitTermination(60, TimeUnit.SECONDS);
        if (!result) {
            log.warn("the timeout elapsed before termination");
        }
    }

    @Override
    public void dispose(Runnable callback) throws InterruptedException {
        disposeCallback = callback;
        dispose();
    }

    @Override
    public void addClient(MsClient client) {
        String name = client.getName();
        log.info("new client: {}", name);
        if (clientMap.containsKey(name)) {
            throw new IllegalArgumentException("Error. client: " + name + " already exists");
        }
        clientMap.put(name, client);
    }

    @Override
    public void removeClient(String clientId) {
        var removedClient = clientMap.remove(clientId);
        if (removedClient == null) {
            log.warn("client not found: {}", clientId);
        } else {
            log.info("removed client: {}", removedClient);
        }
    }

    @Override
    public <T extends ResultDataType> boolean newMessage(Message<T> msg) {
        if (runFlag.get()) {
            return messageQueue.offer(msg);
        } else {
            log.warn("MS is being shutting down... rejected: {}", msg);
            return false;
        }
    }

    @Override
    public int currentQueueSize() {
        return messageQueue.size();
    }

    private void processMessages() {
        log.info("msgProcessor started, {}", currentQueueSize());
        while (runFlag.get() || !messageQueue.isEmpty()) {
            try {
                var msg = messageQueue.take();
                if (msg == MessageBuilder.getVoidMessage()) {
                    log.info("received the stop message");
                } else {
                    var clientTo = clientMap.get(msg.getTo());
                    if (clientTo == null) {
                        log.warn("client not found");
                    } else {
                        msgHandler.submit(() -> handleMessage(clientTo, msg));
                    }
                }
            } catch (InterruptedException ex) {
                log.error(ex.getMessage(), ex);
                Thread.currentThread().interrupt();
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }

        if (disposeCallback != null) {
            msgHandler.submit(disposeCallback);
        }
        msgHandler.submit(this::messageHandlerShutdown);
        log.info("msgProcessor finished");
    }

    private void messageHandlerShutdown() {
        msgHandler.shutdown();
        log.info("msgHandler has been shut down");
    }

    private void handleMessage(MsClient client, Message<? extends ResultDataType> msg) {
        try {
            client.handle(msg);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            log.error("message: {}", msg);
        }
    }

    private void insertStopMessage() throws InterruptedException {
        Message<VoidResultDataType> voidMessage = MessageBuilder.getVoidMessage();
        boolean result = messageQueue.offer(voidMessage);
        while (!result) {
            Thread.sleep(100);
            result = messageQueue.offer(voidMessage);
        }
    }
}
