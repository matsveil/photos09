package com.matsvei.photosapp.photo;

import java.io.Serial;
import java.io.Serializable;

public class Tag implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String name;

    public Tag(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Tag name and value cannot be empty");
        }

        this.name = name.trim().toLowerCase();
    }

    public String getName() { return name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag t)) return false;
        return name.equals(t.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
