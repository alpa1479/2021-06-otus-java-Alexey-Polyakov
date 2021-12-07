package ru.otus.hw17.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw17.converter.Converter;
import ru.otus.hw17.converter.GeneratedNumberToGeneratedNumberMessageConverter;
import ru.otus.hw17.converter.RangeMessageToRangeConverter;
import ru.otus.hw17.model.GeneratedNumber;
import ru.otus.hw17.model.Range;
import ru.otus.hw17.protobuf.generated.GeneratedNumberMessage;
import ru.otus.hw17.protobuf.generated.RangeMessage;
import ru.otus.hw17.server.service.RealNumberGenerator;
import ru.otus.hw17.server.service.RealNumberGeneratorImpl;
import ru.otus.hw17.server.service.RemoteNumberGeneratorImpl;

public class GrpcServerMain {

    private static final Logger log = LoggerFactory.getLogger(GrpcServerMain.class);

    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws Exception {
        RealNumberGenerator realNumberGenerator = new RealNumberGeneratorImpl();
        Converter<RangeMessage, Range> rangeMessageToRangeConverter = new RangeMessageToRangeConverter();
        Converter<GeneratedNumber, GeneratedNumberMessage> generatedNumberToGeneratedNumberMessageConverter =
                new GeneratedNumberToGeneratedNumberMessageConverter();

        RemoteNumberGeneratorImpl remoteNumberGenerator = new RemoteNumberGeneratorImpl(
                realNumberGenerator,
                rangeMessageToRangeConverter,
                generatedNumberToGeneratedNumberMessageConverter
        );

        Server server = ServerBuilder.forPort(SERVER_PORT)
                .addService(remoteNumberGenerator)
                .build();
        server.start();
        log.info(">>>> server waiting for client connections...");
        server.awaitTermination();
    }
}
