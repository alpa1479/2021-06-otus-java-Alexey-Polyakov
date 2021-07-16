package ru.otus.hw02;

import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

    private final Map<Customer, String> customerToStringMap = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        return customerToStringMap.entrySet().iterator().next();
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return customerToStringMap.entrySet().stream()
                .filter(customerToStringEntry -> customerToStringEntry.getKey().hasHigherScoresThan(customer))
                .findFirst()
                .orElse(null);
    }

    public void add(Customer customer, String data) {
        customerToStringMap.put(customer, data);
    }
}
