package com.orangeandbronze.enlistment;

import org.apache.commons.lang3.*;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.isAlphanumeric;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;

public class Room {
    private final String roomName;
    private final int capacity;

    Room(String roomName, int capacity) {
        notBlank(roomName);
        isTrue(isAlphanumeric(roomName), "roomName must be alphanumeric, was: " + roomName);
        Validate.notNaN(capacity);
        //Validate.notBlank(capacity);
        this.roomName = roomName;
        this.capacity = capacity;
    }

    boolean isFull(int no_of_students){
        return no_of_students >= capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;

        Room room = (Room)o;
        return capacity == room.capacity && roomName.equals(room.roomName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomName, capacity);
    }

    @Override
    public String toString() {
        return roomName;
    }
}
