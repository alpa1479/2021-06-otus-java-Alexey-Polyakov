package ru.otus.hw06;

import ru.otus.hw06.atm.ATM;
import ru.otus.hw06.atm.ATMImpl;
import ru.otus.hw06.atm.vault.Vault;
import ru.otus.hw06.atm.vault.VaultImpl;
import ru.otus.hw06.banknote.Banknote;
import ru.otus.hw06.denomination.Denomination;
import ru.otus.hw06.banknote.list.BanknoteList;
import ru.otus.hw06.banknote.list.BanknoteListFactory;
import ru.otus.hw06.banknote.list.BanknoteListFactoryImpl;

public class Demo {

    public static void main(String[] args) {
        BanknoteListFactory banknoteListFactory = new BanknoteListFactoryImpl();
        Vault vault = new VaultImpl();
        ATM atm = new ATMImpl(vault, banknoteListFactory);

        BanknoteList banknoteList = banknoteListFactory.from(
                Banknote.with(Denomination.ONE_HUNDRED),
                Banknote.with(Denomination.FIFTY),
                Banknote.with(Denomination.FIVE)
        );
        System.out.printf(">>>> deposit %d currency %s%n", banknoteList.getTotalAmount(), banknoteList);
        atm.deposit(banknoteList);

        int balance = atm.getBalance();
        System.out.printf(">>>> current balance %d%n", balance);

        final int amount = 5;
        System.out.printf(">>>> request to withdraw %d amount %n", amount);
        banknoteList = atm.withdraw(amount);
        System.out.printf(">>>> withdrawn %d amount, currency %s%n", banknoteList.getTotalAmount(), banknoteList);

        balance = atm.getBalance();
        System.out.printf(">>>> current balance %d%n", balance);
    }
}
