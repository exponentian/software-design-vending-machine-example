package vendingmachine;

public class VendingMachineFactory {

    public static VendingMachine getVendingMachine() {
        return new VendingMachineImpl();
    }
}