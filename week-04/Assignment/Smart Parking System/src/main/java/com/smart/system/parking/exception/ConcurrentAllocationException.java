package com.smart.system.parking.exception;

public class ConcurrentAllocationException extends RuntimeException{

    public ConcurrentAllocationException(){
        super();
    }

    public ConcurrentAllocationException(String message){
        super(message);
    }
}
