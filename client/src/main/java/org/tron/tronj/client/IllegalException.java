package org.tron.tronj.client;

public class IllegalException extends  Exception {
    public IllegalException(){
        super("The query failed, please check if the parameters are correct.");
    }

    public IllegalException(String message){
        super(message);
    }
}
