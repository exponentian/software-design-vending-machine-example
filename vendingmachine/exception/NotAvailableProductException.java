package vendingmachine.exception;

public class NotAvailableProductException extends RuntimeException {
    private String message;
    
    public NotAvailableProductException() {
        this.message = "Not available at the moment";
    }
    
    @Override
    public String getMessage() {
        return this.message;
    }
}