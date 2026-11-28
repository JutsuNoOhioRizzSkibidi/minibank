package bank.domain;

public class BlockedAccountException extends IllegalStateException{
    public BlockedAccountException(String message){
        super(message);
    }
}