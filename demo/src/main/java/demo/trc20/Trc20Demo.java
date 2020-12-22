package demo.trc20;

import org.tron.tronj.client.Trc20Client;
import org.tron.tronj.client.TronClient;
import org.tron.tronj.client.contract.Contract;
import org.tron.tronj.client.contract.Trc20Contract;

public class Trc20Demo {

    TronClient c = TronClient.ofNile("Private key");

    public void getName() {
        Contract cntr = c.getContract("Contract address");
        Trc20Contract token = new Trc20Contract(cntr, "Caller address", c);
        try {
            System.out.println("Name: " + token.name());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getSymbol() {
        Contract cntr = c.getContract("Contract address");
        Trc20Contract token = new Trc20Contract(cntr, "Caller address", c);
        try {
            System.out.println("Symbol: " + token.symbol());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getDecimals() {
        Contract cntr = c.getContract("Contract address");
        Trc20Contract token = new Trc20Contract(cntr, "Caller address", c);
        try {
            System.out.println("Decimals: " + token.decimals());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getTotalSupply() {
        Contract cntr = c.getContract("Contract address");
        Trc20Contract token = new Trc20Contract(cntr, "Caller address", c);
        try {
            System.out.println("Total Supply: " + token.totalSupply());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getBalanceOf() {
        Contract cntr = c.getContract("Contract address");
        Trc20Contract token = new Trc20Contract(cntr, "Caller address", c);
        try {
            System.out.println("Balance: " + token.balanceOf("Owner address"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void transfer() {
        Contract cntr = c.getContract("Contract address");
        Trc20Contract token = new Trc20Contract(cntr, "Caller address", c);
        try {
            // destnation address, amount, memo, feelimit
            System.out.println(token.transfer("To address", 10L, "go!", 100000000L));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void transferFrom() {
        Contract cntr = c.getContract("Contract address");
        Trc20Contract token = new Trc20Contract(cntr, "Caller address", c);
        try {
            // destnation address, amount, memo, feelimit
            System.out.println(token.transferFrom("From Address", "To address", 10L, "go!", 100000000L));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void approve() {
        Contract cntr = c.getContract("Contract address");
        Trc20Contract token = new Trc20Contract(cntr, "Caller address", c);
        try {
            // destnation address, amount, memo, feelimit
            System.out.println(token.approve("Spender", 10L, "go!", 100000000L));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getAllowance() {
        Contract cntr = c.getContract("Contract address");
        Trc20Contract token = new Trc20Contract(cntr, "Caller address", c);
        try {
            System.out.println("Allowance: " + token.allowance("From address", "Spender"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}