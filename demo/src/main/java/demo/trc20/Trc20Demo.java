package demo.trc20;

import org.tron.tronj.client.Trc20Client;

public class Trc20Demo {

    public void getName() {
        Trc20Client client = Trc20Client.ofNile("7c3a547f37cf82c7bd7cead99e53fd9a7d3bf6a3c8bd3c8541ad572322d16e42");
        try {
            System.out.println(client.getName("TFRgpvvNTe8bwC666D6orYhEkCcYsbax8U", "TF17BgPaZYbz8oxbjhriubPDsA7ArKoLX3"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getSymbol() {
        Trc20Client client = Trc20Client.ofNile("7c3a547f37cf82c7bd7cead99e53fd9a7d3bf6a3c8bd3c8541ad572322d16e42");
        try {
            System.out.println(client.getSymbol("TFRgpvvNTe8bwC666D6orYhEkCcYsbax8U", "TF17BgPaZYbz8oxbjhriubPDsA7ArKoLX3"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getDecimals() {
        Trc20Client client = Trc20Client.ofNile("7c3a547f37cf82c7bd7cead99e53fd9a7d3bf6a3c8bd3c8541ad572322d16e42");
        try {
            System.out.println(client.getDecimals("TFRgpvvNTe8bwC666D6orYhEkCcYsbax8U", "TF17BgPaZYbz8oxbjhriubPDsA7ArKoLX3"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getTotalSupply() {
        Trc20Client client = Trc20Client.ofNile("7c3a547f37cf82c7bd7cead99e53fd9a7d3bf6a3c8bd3c8541ad572322d16e42");
        try {
            System.out.println(client.getTotalSupply("TFRgpvvNTe8bwC666D6orYhEkCcYsbax8U", "TF17BgPaZYbz8oxbjhriubPDsA7ArKoLX3"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getBalanceOf() {
        Trc20Client client = Trc20Client.ofNile("7c3a547f37cf82c7bd7cead99e53fd9a7d3bf6a3c8bd3c8541ad572322d16e42");
        try {
            System.out.println(client.getBalanceOf("TFRgpvvNTe8bwC666D6orYhEkCcYsbax8U", "TFRgpvvNTe8bwC666D6orYhEkCcYsbax8U", "TF17BgPaZYbz8oxbjhriubPDsA7ArKoLX3"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}