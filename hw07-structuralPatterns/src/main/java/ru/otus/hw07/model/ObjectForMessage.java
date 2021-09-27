package ru.otus.hw07.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage implements Copyable<ObjectForMessage> {

    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public ObjectForMessage copy() {
        ObjectForMessage copy = new ObjectForMessage();
        copy.data = new ArrayList<>(data);
        return copy;
    }

    @Override
    public String toString() {
        return "ObjectForMessage{" +
                "data=" + data +
                '}';
    }
}
