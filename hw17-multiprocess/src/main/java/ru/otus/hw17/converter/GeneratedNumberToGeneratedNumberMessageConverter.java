package ru.otus.hw17.converter;

import ru.otus.hw17.model.GeneratedNumber;
import ru.otus.hw17.protobuf.generated.GeneratedNumberMessage;

public class GeneratedNumberToGeneratedNumberMessageConverter implements Converter<GeneratedNumber, GeneratedNumberMessage> {

    @Override
    public GeneratedNumberMessage convert(GeneratedNumber generatedNumber) {
        return GeneratedNumberMessage.newBuilder()
                .setValue(generatedNumber.value())
                .build();
    }
}
