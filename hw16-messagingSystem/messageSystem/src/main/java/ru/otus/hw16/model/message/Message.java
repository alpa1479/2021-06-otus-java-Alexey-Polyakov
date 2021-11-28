package ru.otus.hw16.model.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import ru.otus.hw16.model.resultdatatype.ResultDataType;

@Data
@EqualsAndHashCode(of = "id")
public class Message<T extends ResultDataType> {

    @NonNull
    private final MessageId id;
    private final MessageId sourceMessageId;
    private final String from;
    private final String to;
    private final MessageType type;
    private final T data;
    private final MessageCallback<T> callback;
}
