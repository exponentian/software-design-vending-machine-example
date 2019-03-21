package vendingmachine;

import java.util.*;


public class Inventory<T> {
    private Map<T, Integer> inventory = new HashMap<>();
    
    public Inventory() {}
    
    public Map<T, Integer> getInventory() {
        return this.inventory;
    }

    public void addItem(T item, int quantity) {
        this.inventory.put(item, quantity);
    }

    public boolean hasItem(T item) {
        return this.inventory.containsKey(item) && this.inventory.get(item) > 0;
    }
    
    public void withdrawItem(T item) {
        this.inventory.put(item, this.inventory.get(item) - 1);
    }
    
    public void updateQuantity(T item, int quantity) {
        this.inventory.put(item, this.inventory.get(item) + quantity);
    }

    public boolean isEmpty() {
        return this.inventory.isEmpty();
    }

    public void clear() {
        this.inventory.clear();
    }
    
}