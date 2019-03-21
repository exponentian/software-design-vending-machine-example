package vendingmachine;

public enum Product {
    GUM("Chewing Bubble Gum", 0.75), CANDY("Lemon Candy", 1.00), WATER("Spring Water", 1.25), CHOCO("Best Chocolate", 2.00), COKE("Coca Cola", 2.50);
    
    private String name;
    private double price;
    
    private Product(String name, double price) {
        this.name = name;
        this.price = price;
    }
    
    public String getName() {
        return this.name;
    }
    
    public double getPrice() {
        return this.price;
    }
}