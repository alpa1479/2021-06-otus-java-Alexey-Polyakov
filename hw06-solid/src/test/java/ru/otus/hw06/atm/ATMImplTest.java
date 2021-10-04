package ru.otus.hw06.atm;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw06.atm.exception.ATMInputException;
import ru.otus.hw06.atm.exception.WithdrawException;
import ru.otus.hw06.atm.vault.Vault;
import ru.otus.hw06.atm.vault.VaultImpl;
import ru.otus.hw06.banknote.UnknownBanknote;
import ru.otus.hw06.currency.Currency;
import ru.otus.hw06.currency.CurrencyFactory;
import ru.otus.hw06.currency.CurrencyFactoryImpl;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

public class ATMImplTest {

    private ATM atm;
    private Currency currency;
    private CurrencyFactory currencyFactory;

    @BeforeEach
    void setUp() {
        Vault vault = new VaultImpl();
        currencyFactory = new CurrencyFactoryImpl();
        currency = currencyFactory.from(
                UnknownBanknote.ONE_HUNDRED,
                UnknownBanknote.ONE_HUNDRED,
                UnknownBanknote.ONE_HUNDRED
        );
        atm = new ATMImpl(vault, currencyFactory);
    }

    @Test
    @DisplayName("Should deposit specified currency to ATM and return current balance")
    void shouldDepositSpecifiedCurrencyAndReturnCurrentBalance() {
        //when
        atm.deposit(currency);

        //then
        final int balance = atm.getBalance();
        Assertions.assertThat(balance).isEqualTo(300);
    }

    @Test
    @DisplayName("Should deposit specified currency then withdraw specified amount from ATM")
    void shouldDepositCurrencyThenWithdrawSpecifiedAmount() {
        // given
        int amount = 300;
        atm.deposit(currency);

        //when
        final Currency withdrawnCurrency = atm.withdraw(amount);

        //then
        Assertions.assertThat(withdrawnCurrency).isEqualTo(currency);
    }

    @Test
    @DisplayName("Should throw WithdrawException if not possible to withdraw specified amount")
    void shouldThrowWithdrawExceptionForSpecifiedAmount() {
        // given
        int amount = 150;
        atm.deposit(currency);

        //when
        final Throwable thrown = catchThrowable(() -> atm.withdraw(amount));

        //then
        Assertions.assertThat(thrown).isInstanceOf(WithdrawException.class);
    }

    @Test
    @DisplayName("Should throw ATMInputException if input currency is null")
    void shouldThrowATMInputExceptionOnNullCurrency() {
        // given
        Currency currency = null;

        //when
        final Throwable thrown = catchThrowable(() -> atm.deposit(currency));

        //then
        Assertions.assertThat(thrown).isInstanceOf(ATMInputException.class);
    }

    @Test
    @DisplayName("Should throw ATMInputException if banknotes in input currency are empty")
    void shouldThrowATMInputExceptionOnEmptyBanknotes() {
        // given
        Currency currency = currencyFactory.from();

        //when
        final Throwable thrown = catchThrowable(() -> atm.deposit(currency));

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
