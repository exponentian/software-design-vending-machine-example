package vendingmachine;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import vendingmachine.exception.*;

import java.util.Map;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VendingMachineTest {

    private static VendingMachine machine;
    
    @BeforeClass
    public static void setUpClass() {
        machine = VendingMachineFactory.getVendingMachine();
    }
    
    @AfterClass
    public static void tearDownClass() {
        machine = null;
    }
    
    @Test
    public void test01GetProductPrice() {
        assertEquals(0.75, machine.getProductPrice(Product.GUM), 0.00);     // Gum
        assertEquals(1.25, machine.getProductPrice(Product.WATER), 0.00);   // Water
        assertEquals(2.50, machine.getProductPrice(Product.COKE), 0.00);    // Coke
    }
    
    @Test
    public void test02CheckProductAvailable() {
        assertTrue(machine.checkProductAvailable(Product.GUM));     // GUM
        assertTrue(machine.checkProductAvailable(Product.WATER));   // Water
        assertTrue(machine.checkProductAvailable(Product.COKE));    // Coke
    }
    
    @Test
    public void test03InsertCoinAndGetBalance() {
        
        // insert 2 dollars
        machine.insertCoin(Coin.TOONIE); // balance: $2.00
        assertEquals(2.00, machine.getBalance(), 0.00);
    }
    
    @Test
    public void test04ChooseProduct() {
        
        // buy a gum and a bottle of water
        assertEquals("Chewing Bubble Gum", machine.chooseProduct(Product.GUM).getName()); // balance: $1.25
        assertEquals("Spring Water", machine.chooseProduct(Product.WATER).getName());     // balance: $0.00
    }
    
    @Test
    public void test05ChooseProductAndRefund() {
        
        // insert 1 dollar
        machine.insertCoin(Coin.LOONIE); // balance: $1.00
        
        // buy a gum
        assertEquals("Chewing Bubble Gum", machine.chooseProduct(Product.GUM).getName()); // balance: $0.25
        
        Map<Coin, Integer> refund = machine.refund(); // refund: $0.25 x 1
        assertEquals(1, refund.size());
        
        Coin[] coins = refund.keySet().toArray(new Coin[refund.size()]);
        assertEquals(Coin.QUARTER, coins[0]);
        assertEquals(0.25, coins[0].getValue(), 0.00);
        assertEquals(1, (int) refund.get(coins[0]));
        
        assertEquals(0.00, machine.getBalance(), 0.00); // balance: $0.00
    }
    
    @Test(expected = NotEnoughCoinException.class)
    public void test06ChooseProductNotEnoughCoin() {
        
        // insert 2 dollars
        machine.insertCoin(Coin.TOONIE); // balance: $2.00
        
        // want to buy a coke, but...
        machine.chooseProduct(Product.COKE);
    }
    
    @Test(expected = NotAvailableProductException.class)
    //@Test
    public void test07ChooseProductNotAvailableProduct() {
        
        // insert more 3 dolloars
        for (int i = 0; i < 13; i++) machine.insertCoin(Coin.TOONIE); // balance: $28.00
        
        // buy 10 cokes
        for (int i = 0; i < 10; i++) machine.chooseProduct(Product.COKE); // balance: $3.00
        
        assertEquals(3.00, machine.getBalance(), 0.00);
        
        // and choose one more coke, but...
        machine.chooseProduct(Product.COKE);
    }
    
    @Test
    public void test08RefundNull() {
        for (int i = 0; i < 3; i++) machine.chooseProduct(Product.CANDY); // balance: $0.00
        assertEquals(0.00, machine.getBalance(), 0.00);
        assertEquals(null, machine.refund());
    }
    
    @Test
    public void test09RefundWithChanges() {
        machine.insertCoin(Coin.TOONIE); // balance: $2.00
        
        // buy a bottle of water
        machine.chooseProduct(Product.WATER); // balance: $0.75
        
        Map<Coin, Integer> refund = machine.refund();
        assertEquals(1, refund.size());
        
        Coin[] coins = refund.keySet().toArray(new Coin[refund.size()]);
        assertEquals(Coin.QUARTER, coins[0]);
        assertEquals(0.25, coins[0].getValue(), 0.00);
        assertEquals(3, (int) refund.get(coins[0]));
        
        assertEquals(0.00, machine.getBalance(), 0.00); // balance: $0.00
        
        machine.insertCoin(Coin.TOONIE); // balance: $2.00
        
        machine.chooseProduct(Product.WATER); // balance: $0.75
        refund = machine.refund();
        assertEquals(2, refund.size());

        coins = refund.keySet().toArray(new Coin[refund.size()]);
        assertEquals(Coin.QUARTER, coins[0]);
        assertEquals(0.25, coins[0].getValue(), 0.00);
        assertEquals(1, (int) refund.get(coins[0]));

        assertEquals(Coin.DIME, coins[1]);
        assertEquals(0.10, coins[1].getValue(), 0.00);
        assertEquals(5, (int) refund.get(coins[1]));

        assertEquals(0.00, machine.getBalance(), 0.00); // balance: $0.00
    }

    @Test(expected = NotAvailableChangeException.class)
    public void test10RefundNotAvailableChange() {
        machine.insertCoin(Coin.TOONIE);
        machine.chooseProduct(Product.WATER);
        machine.refund();
    }
}