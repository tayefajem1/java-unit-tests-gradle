package com.orangeandbronze.enlistment.exceptions;

public class CapacityReachedException extends RuntimeException{
    public CapacityReachedException(String msg){
        super(msg);
    }
}
