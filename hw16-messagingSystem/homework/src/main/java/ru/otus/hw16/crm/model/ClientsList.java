package ru.otus.hw16.crm.model;

import lombok.Data;
import ru.otus.hw16.model.resultdatatype.ResultDataType;

import java.util.List;

@Data
public class ClientsList implements ResultDataType {

    private final List<Client> clients;
}
