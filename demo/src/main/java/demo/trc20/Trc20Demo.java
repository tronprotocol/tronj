package demo.trc20;

import org.tron.tronj.client.Trc20Client;

public class Trc20Demo {

    public void getName() {
        Trc20Client client = Trc20Client.ofNile("7c3a547f37cf82c7bd7cead99e53fd9a7d3bf6a3c8bd3c8541ad572322d16e42");
        try {
            System.out.println("Name: " + client.getName("TFRgpvvNTe8bwC666D6orYhEkCcYsbax8U", "TF17BgPaZYbz8oxbjhriubPDsA7ArKoLX3"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getSymbol() {
        Trc20Client client = Trc20Client.ofNile("7c3a547f37cf82c7bd7cead99e53fd9a7d3bf6a3c8bd3c8541ad572322d16e42");
        try {
            System.out.println("Symbol: " + client.getSymbol("TFRgpvvNTe8bwC666D6orYhEkCcYsbax8U", "TF17BgPaZYbz8oxbjhriubPDsA7ArKoLX3"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getDecimals() {
        Trc20Client client = Trc20Client.ofNile("7c3a547f37cf82c7bd7cead99e53fd9a7d3bf6a3c8bd3c8541ad572322d16e42");
        try {
            System.out.println("Decimals: "+ client.getDecimals("TFRgpvvNTe8bwC666D6orYhEkCcYsbax8U", "TF17BgPaZYbz8oxbjhriubPDsA7ArKoLX3"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getTotalSupply() {
        Trc20Client client = Trc20Client.ofNile("7c3a547f37cf82c7bd7cead99e53fd9a7d3bf6a3c8bd3c8541ad572322d16e42");
        try {
            System.out.println("TotalSupply: " + client.getTotalSupply("TFRgpvvNTe8bwC666D6orYhEkCcYsbax8U", "TF17BgPaZYbz8oxbjhriubPDsA7ArKoLX3"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getBalanceOf() {
        Trc20Client client = Trc20Client.ofNile("7c3a547f37cf82c7bd7cead99e53fd9a7d3bf6a3c8bd3c8541ad572322d16e42");
        try {
            System.out.println("Balance: " + client.getBalanceOf("TFRgpvvNTe8bwC666D6orYhEkCcYsbax8U", "TFRgpvvNTe8bwC666D6orYhEkCcYsbax8U", "TF17BgPaZYbz8oxbjhriubPDsA7ArKoLX3"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void transfer() {
        Trc20Client client = Trc20Client.ofNile("7c3a547f37cf82c7bd7cead99e53fd9a7d3bf6a3c8bd3c8541ad572322d16e42");
        try {
            // destnation address, amount, caller/from address, contract address, memo, feelimit
            System.out.println(client.transfer("TVw7mwc6vg9BxouG98Z2cS1tiwEvhx45TH", 10L, "TFRgpvvNTe8bwC666D6orYhEkCcYsbax8U", 
                "TF17BgPaZYbz8oxbjhriubPDsA7ArKoLX3", "go!", 100000000L));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void transferFrom() {
        Trc20Client client = Trc20Client.ofNile("7c3a547f37cf82c7bd7cead99e53fd9a7d3bf6a3c8bd3c8541ad572322d16e42");
        try {
            // destnation address, amount, caller/from address, contract address, memo, feelimit
            System.out.println(client.transferFrom("TFRgpvvNTe8bwC666D6orYhEkCcYsbax8U" ,"TVw7mwc6vg9BxouG98Z2cS1tiwEvhx45TH", 10L, "TFRgpvvNTe8bwC666D6orYhEkCcYsbax8U", 
                "TF17BgPaZYbz8oxbjhriubPDsA7ArKoLX3", "go!", 100000000L));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void approve() {
        Trc20Client client = Trc20Client.ofNile("7c3a547f37cf82c7bd7cead99e53fd9a7d3bf6a3c8bd3c8541ad572322d16e42");
        try {
            // destnation address, amount, caller/from address, contract address, memo, feelimit
            System.out.println(client.approve("TVw7mwc6vg9BxouG98Z2cS1tiwEvhx45TH", 10L, "TFRgpvvNTe8bwC666D6orYhEkCcYsbax8U", 
                "TF17BgPaZYbz8oxbjhriubPDsA7ArKoLX3", "go!", 100000000L));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getAllowance() {
        Trc20Client client = Trc20Client.ofNile("7c3a547f37cf82c7bd7cead99e53fd9a7d3bf6a3c8bd3c8541ad572322d16e42");
        try {
            System.out.println("Allowance: " + client.getAllowance("TFRgpvvNTe8bwC666D6orYhEkCcYsbax8U", "TVw7mwc6vg9BxouG98Z2cS1tiwEvhx45TH", "TFRgpvvNTe8bwC666D6orYhEkCcYsbax8U", "TF17BgPaZYbz8oxbjhriubPDsA7ArKoLX3"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}