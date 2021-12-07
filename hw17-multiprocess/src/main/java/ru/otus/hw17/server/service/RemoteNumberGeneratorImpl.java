package ru.otus.hw17.server.service;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw17.converter.Converter;
import ru.otus.hw17.model.GeneratedNumber;
import ru.otus.hw17.model.Range;
import ru.otus.hw17.protobuf.generated.GeneratedNumberMessage;
import ru.otus.hw17.protobuf.generated.RangeMessage;
import ru.otus.hw17.protobuf.generated.RemoteNumberGeneratorGrpc;
import ru.otus.hw17.util.Threads;

import java.util.List;

@RequiredArgsConstructor
public class RemoteNumberGeneratorImpl extends RemoteNumberGeneratorGrpc.RemoteNumberGeneratorImplBase {

    private static final Logger log = LoggerFactory.getLogger(RemoteNumberGeneratorImpl.class);

    private final RealNumberGenerator realNumberGenerator;
    private final Converter<RangeMessage, Range> rangeMessageToRangeConverter;
    private final Converter<GeneratedNumber, GeneratedNumberMessage> generatedNumberToGeneratedNumberMessageConverter;

    @Override
    public void generateNumberSequence(RangeMessage request, StreamObserver<GeneratedNumberMessage> responseObserver) {
        log.info(">>>> generateNumberSequence started");
        int delayInSeconds = request.getDelayInSeconds();
        Range range = rangeMessageToRangeConverter.convert(request);
        List<GeneratedNumber> generatedNumbers = realNumberGenerator.generateNumberSequence(range);
        List<GeneratedNumberMessage> generatedNumberMessages = generatedNumberToGeneratedNumberMessageConverter.convert(generatedNumbers);
        generatedNumberMessages.forEach(message -> {
            Threads.delay(delayInSeconds);
            log.info(">>>> sending message with value {}", message.getValue());
            responseObserver.onNext(message);
        });
        responseObserver.onCompleted();
        log.info(">>>> generateNumberSequence ended");
    }
}
