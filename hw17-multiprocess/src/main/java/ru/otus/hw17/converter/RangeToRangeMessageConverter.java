package ru.otus.hw17.converter;

import ru.otus.hw17.model.Range;
import ru.otus.hw17.protobuf.generated.RangeMessage;

public class RangeToRangeMessageConverter {

    public RangeMessage convert(Range range, int delayTime) {
        return RangeMessage.newBuilder()
                .setFirstValue(range.firstValue())
                .setLastValue(range.lastValue())
                .setDelayInSeconds(delayTime)
                .build();
    }
}
