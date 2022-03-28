package com.app;

import java.util.ArrayList;
import java.util.List;

public class Notification {
    private final List<String> errors = new ArrayList<>();

    public void addError(String caption) {
        errors.add(caption);
    }

}