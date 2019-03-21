package vendingmachine;


import java.util.*;

import vendingmachine.exception.*;


public class VendingMachineImpl implements VendingMachine {

    private Inventory<Product> products;
    private Inventory<Coin> coins;
    private Map<Coin, Integer> insertedCoins;
    private Map<Product, Integer> soldProducts;

    private double balance;
    private double totalSales;

    private static final int NUM_PRODUCTS = 10;
    private static final int NUM_COINS = 5;

    // constructor
    public VendingMachineImpl() {
        this.products = new Inventory<>();
        this.coins = new Inventory<>();
        this.insertedCoins = new TreeMap<>((Coin c1, Coin c2) -> c2.getValue().compareTo(c1.getValue()));
        this.soldProducts = new HashMap<>();

        this.balance = 0.00;
        this.totalSales = 0.00;

        this.setUpInventories();
    }

    // for customers

    @Override
    public double getProductPrice(Product product) {
        return Double.parseDouble( String.format("%.2f", product.getPrice()) );
    }
    
    @Override
    public boolean checkProductAvailable(Product product) {
        return this.products.hasItem(product);
    }
    
    @Override
    public void insertCoin(Coin coin) {
        this.insertedCoins.put( coin, this.insertedCoins.getOrDefault(coin, 0) + 1 );
        this.balance += coin.getValue();
    }
    
    @Override
    public double getBalance() {
        return setPrecision(this.balance);
    }
    
    @Override
    public Product chooseProduct(Product product) {
        if ( !products.hasItem(product) ) {
            throw new NotAvailableProductException();
        } else if (this.getBalance() < product.getPrice()) {
            throw new NotEnoughCoinException();
        }
        
        // withdraw the product chosen
        products.withdrawItem(product);
        
        // update coins
        this.updateCoins(product);
        
        return product;
    }
    
    @Override
    public Map<Coin, Integer> refund() {
        if ( this.getInsertedCoins().isEmpty() ) return null;

        Map<Coin, Integer> refund = new LinkedHashMap<>(this.getInsertedCoins());

        this.insertedCoinsClear();
        this.setBalance(0.00);
        
        return refund;
    }

    // miscellaneous: for owners

    @Override
    public Map<Product, Integer> getProductInventory() {
        return this.products.getInventory();
    }

    @Override
    public void fillProducts(Product product, int quantity) {
        this.products.addItem(product, quantity);
    }

    @Override
    public Map<Coin, Integer> getCoinInventory() {
        return this.coins.getInventory();
    }

    @Override
    public void fillCoins(Coin coin, int quantity) {
        this.coins.addItem(coin, quantity);
    }

    @Override
    public double getTotalSales() {
        return this.totalSales;
    }

    @Override
    public Map<Product, Integer> getAllSoldProducts() {
        return this.soldProducts;
    }

    /**
     * Get most selling products so far. It is sorted by using a bucket sorting technique.
     * @returns List<Product>
     */
    @Override
    public List<Product> getMostSellingProduct() {
        if (this.products.isEmpty()) return null;

        int max = 0;
        int min = Integer.MAX_VALUE;
        Map<Product, Integer> productsInventory = this.products.getInventory();
        for (Product product : productsInventory.keySet()) {
            int quantity = productsInventory.get(product);
            if (quantity > max) max = quantity;
            if (quantity < min) min = quantity;
        }

        // add each product with a quantity into the array of arraylist
        List<Product>[] bucket = new ArrayList[max + 1];
        for (Product product : productsInventory.keySet()) {
            int quantity = productsInventory.get(product);
            if (bucket[quantity] == null) bucket[quantity] = new ArrayList<>();
            bucket[quantity].add(product);
        }

        return bucket[min];
    }

    @Override
    public void reset() {
        this.products.clear();
        this.coins.clear();
        this.insertedCoinsClear();
        this.setBalance(0.00);
        this.totalSales = 0.00;
        this.setUpInventories();
    }

    // set up inventories
    private void setUpInventories() {
        for (Product product : Product.values()) products.addItem(product, NUM_PRODUCTS);
        for (Coin coin: Coin.values()) coins.addItem(coin, NUM_COINS);
    }
    
    private void setBalance(double value) {
        this.balance = value;
    }
    
    private Map<Coin, Integer> getInsertedCoins() {
        return this.insertedCoins;
    }
    
    private void insertedCoinsClear() {
        this.insertedCoins.clear();
    }
    
    /**
     * Update coins
     * @params Product product
     */
    private void updateCoins(Product product) {
        double change = this.getBalance() - product.getPrice();
        
        // add inserted coins into a coin inventory with each quantity
        Map<Coin, Integer> insertedCoins = this.getInsertedCoins();
        for (Coin coin : insertedCoins.keySet()) {
            Integer quantity = insertedCoins.get(coin);
            coins.updateQuantity(coin, quantity);
            //this.setBalance( this.getBalance() - (quantity * coin.getValue()) );
        }

        // set a balance to be zero
        this.setBalance(0.00);

        // make inserted coins clear
        this.insertedCoinsClear();
        
        while (change > 0.00) {
            if ( coins.hasItem(Coin.TOONIE) && change >= 2.00 ) {
                change = calculateChange(change, 2.00);
            
            } else if ( coins.hasItem(Coin.LOONIE) && change >= 1.00 ) {
                change = calculateChange(change, 1.00);
            
            } else if ( coins.hasItem(Coin.QUARTER) && change >= 0.25 ) {
                change = calculateChange(change, 0.25);
            
            } else if ( coins.hasItem(Coin.DIME) && change >= 0.10 ) {
                change = calculateChange(change, 0.10);
            
            } else if ( coins.hasItem(Coin.NICKEL) && change >= 0.05 ) {
                change = calculateChange(change, 0.05);
            
            } else if ( coins.hasItem(Coin.PENNY) && change >= 0.01 ) {
                change = calculateChange(change, 0.01);

            } else {
                throw new NotAvailableChangeException(change);
            }
        }
    }
    
    private double calculateChange(double change, double value) {
        Coin coin = convertValueToCoin(value);
        
        int quotient = (int) (change / value);
        int stock = coins.getInventory().get(coin);
        
        if (quotient > stock) quotient = stock;
        
        for (int i = 0; i < quotient; i++) {
            this.insertCoin(coin);
            coins.withdrawItem(coin);
        }
        
        change -= value * quotient;
        return setPrecision(change);     
    }
    
    private Coin convertValueToCoin(double value) {
        Coin coin = null;
        
        if (value == 0.01) coin = Coin.PENNY;
        else if (value == 0.05) coin = Coin.NICKEL;
        else if (value == 0.10) coin = Coin.DIME;
        else if (value == 0.25) coin = Coin.QUARTER;
        else if (value == 1.00) coin = Coin.LOONIE;
        else if (value == 2.00) coin = Coin.TOONIE;
        
        return coin;
    }
    
    private double setPrecision(double num) {
        return Double.parseDouble( String.format("%.2f", num) );
    }
    
}