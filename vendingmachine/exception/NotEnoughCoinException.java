package vendingmachine.exception;

public class NotEnoughCoinException extends RuntimeException {
    private String message;
    
    public NotEnoughCoinException() {
        this.message = "Not enough coins";
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}