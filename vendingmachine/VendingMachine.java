package vendingmachine;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface VendingMachine {
    // for customers
    public double getProductPrice(Product product);
    public boolean checkProductAvailable(Product product);
    public void insertCoin(Coin coin);
    public double getBalance();
    public Product chooseProduct(Product product);
    public Map<Coin, Integer> refund();

    // Miscellaneous: for owners
    public Map<Product, Integer> getProductInventory();
    public void fillProducts(Product product, int quantity);
    public Map<Coin, Integer> getCoinInventory();
    public void fillCoins(Coin coin, int quantity);
    public double getTotalSales();
    public Map<Product, Integer> getAllSoldProducts();
    public List<Product> getMostSellingProduct();
    public void reset();
}