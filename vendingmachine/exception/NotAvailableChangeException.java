package vendingmachine.exception;

public class NotAvailableChangeException extends RuntimeException {
    private String message;

    public NotAvailableChangeException(double change) {
        this.message = "Not available changes: " + String.format("%.2f", change);
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
