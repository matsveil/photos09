package com.matsvei.photosapp.photo;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a tag with a type-value pair for a photo.
 * For example: type="person", value="Alice" or type="location", value="New York"
 * 
 * @author matsvei
 */
public class Tag implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String type;
    private final String value;

    /**
     * Creates a new Tag with the specified type and value.
     * 
     * @param type  the tag type (e.g., "person", "location")
     * @param value the tag value (e.g., "Alice", "New York")
     */
    public Tag(String type, String value) {
        if (type == null || type.trim().isEmpty() || value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag type and value cannot be empty");
        }

        this.type = type.trim().toLowerCase();
        this.value = value.trim().toLowerCase();
    }

    /**
     * Gets the tag type.
     * 
     * @return the tag type
     */
    public String getType() { 
        return type; 
    }

    /**
     * Gets the tag value.
     * 
     * @return the tag value
     */
    public String getValue() { 
        return value; 
    }

    /**
     * Returns a string representation of this tag in the format "type:value".
     * 
     * @return string representation of the tag
     */
    @Override
    public String toString() {
        return type + ":" + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag t)) return false;
        return type.equals(t.type) && value.equals(t.value);
    }

    @Override
    public int hashCode() {
        return 31 * type.hashCode() + value.hashCode();
    }
}
