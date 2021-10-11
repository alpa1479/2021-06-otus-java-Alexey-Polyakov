package ru.otus.hw06.atm;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw06.atm.exception.ATMInputException;
import ru.otus.hw06.atm.exception.WithdrawException;
import ru.otus.hw06.atm.vault.Vault;
import ru.otus.hw06.atm.vault.VaultImpl;
import ru.otus.hw06.banknote.Banknote;
import ru.otus.hw06.denomination.Denomination;
import ru.otus.hw06.banknote.list.BanknoteList;
import ru.otus.hw06.banknote.list.BanknoteListFactory;
import ru.otus.hw06.banknote.list.BanknoteListFactoryImpl;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

public class ATMImplTest {

    private ATM atm;
    private BanknoteList banknoteList;
    private BanknoteListFactory banknoteListFactory;

    @BeforeEach
    void setUp() {
        Vault vault = new VaultImpl();
        banknoteListFactory = new BanknoteListFactoryImpl();
        banknoteList = banknoteListFactory.from(
                Banknote.with(Denomination.ONE_HUNDRED),
                Banknote.with(Denomination.ONE_HUNDRED),
                Banknote.with(Denomination.ONE_HUNDRED)
        );
        atm = new ATMImpl(vault, banknoteListFactory);
    }

    @Test
    @DisplayName("Should deposit specified currency to ATM and return current balance")
    void shouldDepositSpecifiedCurrencyAndReturnCurrentBalance() {
        //when
        atm.deposit(banknoteList);

        //then
        final int balance = atm.getBalance();
        Assertions.assertThat(balance).isEqualTo(300);
    }

    @Test
    @DisplayName("Should deposit specified currency then withdraw specified amount from ATM")
    void shouldDepositCurrencyThenWithdrawSpecifiedAmount() {
        // given
        int amount = 300;
        atm.deposit(banknoteList);

        //when
        final BanknoteList withdrawnBanknoteList = atm.withdraw(amount);

        //then
        Assertions.assertThat(withdrawnBanknoteList).isEqualTo(banknoteList);
    }

    @Test
    @DisplayName("Should throw WithdrawException if not possible to withdraw specified amount")
    void shouldThrowWithdrawExceptionForSpecifiedAmount() {
        // given
        int amount = 150;
        atm.deposit(banknoteList);

        //when
        final Throwable thrown = catchThrowable(() -> atm.withdraw(amount));

        //then
        Assertions.assertThat(thrown).isInstanceOf(WithdrawException.class);
    }

    @Test
    @DisplayName("Should throw ATMInputException if input currency is null")
    void shouldThrowATMInputExceptionOnNullCurrency() {
        // given
        BanknoteList banknoteList = null;

        //when
        final Throwable thrown = catchThrowable(() -> atm.deposit(banknoteList));

        //then
        Assertions.assertThat(thrown).isInstanceOf(ATMInputException.class);
    }

    @Test
    @DisplayName("Should throw ATMInputException if banknotes in input currency are empty")
    void shouldThrowATMInputExceptionOnEmptyBanknotes() {
        // given
        BanknoteList banknoteList = banknoteListFactory.from();

        //when
        final Throwable thrown = catchThrowable(() -> atm.deposit(banknoteList));

        //then
        Assertions.assertThat(thrown).isInstanceOf(ATMInputException.class);
    }

    @Test
    @DisplayName("Should throw ATMInputException if input amount is negative")
    void shouldThrowATMInputExceptionOnNegativeAmount() {
        // given
        int amount = -10;

        //when
        final Throwable thrown = catchThrowable(() -> atm.withdraw(amount));

        //then
        Assertions.assertThat(thrown).isInstanceOf(ATMInputException.class);
    }
}
