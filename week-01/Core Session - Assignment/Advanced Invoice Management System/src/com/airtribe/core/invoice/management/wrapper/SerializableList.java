package com.airtribe.core.invoice.management.wrapper;

import java.io.Serializable;
import java.util.List;

public class SerializableList<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<T> data;

    public SerializableList(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }
}
