package vendingmachine;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class SampleExample {
    
    public static void main(String[] args) {
        VendingMachineImpl machine = new VendingMachineImpl();

        machine.insertCoin(Coin.TOONIE); // balance: $2.00
        machine.chooseProduct(Product.GUM); // balance: $1.25
        machine.chooseProduct(Product.WATER);     // balance: $0.00

        machine.insertCoin(Coin.LOONIE); // balance: $1.00

        // buy a gum
        machine.chooseProduct(Product.GUM); // balance: $0.25
        Map<Coin, Integer> refund = machine.refund(); // refund: $0.25 x 1
        System.out.println(refund);


        System.out.println( machine.getBalance() ); // balance: $0.00

        machine.insertCoin(Coin.TOONIE); // balance: $2.00

        // insert more 3 dolloars
        for (int i = 0; i < 13; i++) machine.insertCoin(Coin.TOONIE); // balance: $28.00

        // buy 10 cokes
        for (int i = 0; i < 10; i++) machine.chooseProduct(Product.COKE); // balance: $3.00

        System.out.println( machine.getBalance() );

        for (int i = 0; i < 3; i++) machine.chooseProduct(Product.CANDY); // balance: $0.00

        machine.insertCoin(Coin.TOONIE); // balance: $2.00

        // buy a bottle of water
        machine.chooseProduct(Product.WATER); // balance: $0.75

        refund = machine.refund();
        System.out.println( refund );

        System.out.println(machine.getBalance()); // balance: $0.00

        machine.insertCoin(Coin.TOONIE); // balance: $2.00

        machine.chooseProduct(Product.WATER); // balance: $0.75
        refund = machine.refund();
        System.out.println( refund );

        System.out.println(machine.getBalance()); // balance: $0.00

        machine.insertCoin(Coin.TOONIE);
        //machine.chooseProduct(Product.WATER); // vendingmachine.exception.NotAvailableChangeException: Not available changes: 0.45

        System.out.println(machine.getTotalSales()); // total sales: $33.25
        System.out.println(machine.getMostSellingProduct()); // most selling product: Coke
    }
}