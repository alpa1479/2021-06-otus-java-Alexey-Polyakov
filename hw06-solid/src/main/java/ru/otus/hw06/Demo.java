package ru.otus.hw06;

import ru.otus.hw06.atm.ATM;
import ru.otus.hw06.atm.ATMImpl;
import ru.otus.hw06.atm.vault.Vault;
import ru.otus.hw06.atm.vault.VaultImpl;
import ru.otus.hw06.banknote.UnknownBanknote;
import ru.otus.hw06.currency.Currency;
import ru.otus.hw06.currency.CurrencyFactory;
import ru.otus.hw06.currency.CurrencyFactoryImpl;

public class Demo {

    public static void main(String[] args) {
        CurrencyFactory currencyFactory = new CurrencyFactoryImpl();
        Vault vault = new VaultImpl();
        ATM atm = new ATMImpl(vault, currencyFactory);

        Currency currency = currencyFactory.from(
                UnknownBanknote.ONE_HUNDRED,
                UnknownBanknote.FIFTY,
                UnknownBanknote.FIVE
        );
        System.out.printf(">>>> deposit %d currency %s%n", currency.getTotalAmount(), currency);
        atm.deposit(currency);

        int balance = atm.getBalance();
        System.out.printf(">>>> current balance %d%n", balance);

        final int amount = 5;
        System.out.printf(">>>> request to withdraw %d amount %n", amount);
        currency = atm.withdraw(amount);
        System.out.printf(">>>> withdrawn %d amount, currency %s%n", currency.getTotalAmount(), currency);

        balance = atm.getBalance();
        System.out.printf(">>>> current balance %d%n", balance);
    }
}
