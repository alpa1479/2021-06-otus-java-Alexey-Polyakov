package ru.otus.hw17.converter;

import ru.otus.hw17.model.Range;
import ru.otus.hw17.protobuf.generated.RangeMessage;

public class RangeMessageToRangeConverter implements Converter<RangeMessage, Range> {

    @Override
    public Range convert(RangeMessage rangeMessage) {
        return new Range(rangeMessage.getFirstValue(), rangeMessage.getLastValue());
    }
}
