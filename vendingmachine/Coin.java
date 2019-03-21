package vendingmachine;

public enum Coin {
    PENNY(0.01), NICKEL(0.05), DIME(0.10), QUARTER(0.25), LOONIE(1.00), TOONIE(2.00);
    
    private Double value;
    
    private Coin(Double value) {
        this.value = value;
    }
    
    public Double getValue() {
        return this.value;
    }
}