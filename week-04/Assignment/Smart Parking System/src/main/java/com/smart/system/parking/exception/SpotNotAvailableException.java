package com.smart.system.parking.exception;

public class SpotNotAvailableException extends RuntimeException{

    public SpotNotAvailableException(){
        super();
    }

    public SpotNotAvailableException(String message){
        super(message);
    }
}
