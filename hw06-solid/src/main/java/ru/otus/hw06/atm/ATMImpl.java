package ru.otus.hw06.atm;

import ru.otus.hw06.atm.exception.ATMInputException;
import ru.otus.hw06.atm.exception.WithdrawException;
import ru.otus.hw06.atm.vault.Vault;
import ru.otus.hw06.banknote.Banknote;
import ru.otus.hw06.banknote.list.BanknoteList;
import ru.otus.hw06.banknote.list.BanknoteListFactory;

import java.util.List;

import static java.lang.String.format;
import static ru.otus.hw06.util.Validations.requireNonEmpty;
import static ru.otus.hw06.util.Validations.requireNonNegative;
import static ru.otus.hw06.util.Validations.requireNonNull;

public class ATMImpl implements ATM {

    private final Vault vault;
    private final BanknoteListFactory banknoteListFactory;

    public ATMImpl(Vault vault, BanknoteListFactory banknoteListFactory) {
        this.vault = vault;
        this.banknoteListFactory = banknoteListFactory;
    }

    @Override
    public void deposit(BanknoteList banknoteList) {
        requireNonNull(banknoteList, () -> new ATMInputException("Currency is null"));
        final List<Banknote> banknotes = banknoteList.getBanknotes();
        requireNonEmpty(banknotes, () -> new ATMInputException("Input banknotes are empty"));
        vault.put(banknotes);
    }

    @Override
    public BanknoteList withdraw(int amount) {
        requireNonNegative(amount, () -> new ATMInputException(format("Negative input %d", amount)));
        List<Banknote> banknotes = vault.get(amount);
        requireNonEmpty(banknotes, () -> new WithdrawException(format("Not able to withdraw %d amount", amount)));
        return banknoteListFactory.from(banknotes);
    }

    @Override
    public int getBalance() {
        return vault.calculateBalance();
    }
}
