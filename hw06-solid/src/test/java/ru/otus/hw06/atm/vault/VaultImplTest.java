package ru.otus.hw06.atm.vault;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw06.atm.exception.EmptyVaultException;
import ru.otus.hw06.banknote.Banknote;
import ru.otus.hw06.banknote.UnknownBanknote;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

public class VaultImplTest {

    private Vault vault;
    private List<Banknote> banknotes;

    @BeforeEach
    void setUp() {
        vault = new VaultImpl();
        banknotes = List.of(
                UnknownBanknote.ONE_HUNDRED,
                UnknownBanknote.ONE_HUNDRED,
                UnknownBanknote.ONE_HUNDRED
        );
    }

    @Test
    @DisplayName("Should put specified banknotes into vault and calculate balance")
    void shouldPutSpecifiedBanknotesAndCalculateBalance() {
        // given
        int amount = 300;

        //when
        vault.put(banknotes);
        final int balance = vault.calculateBalance();

        //then
        Assertions.assertThat(balance).isEqualTo(amount);
    }

    @Test
    @DisplayName("Should get specified amount from vault")
    void shouldGetSpecifiedAmount() {
        // given
        int amount = 300;
        vault.put(banknotes);

        //when
        final Optional<List<Banknote>> banknotes = vault.get(amount);

        //then
        Assertions.assertThat(banknotes).isPresent().isEqualTo(banknotes);
    }

    @Test
    @DisplayName("Should throw EmptyVaultException if there are no banknotes in vault")
    void shouldThrowEmptyVaultExceptionIfThereAreNoBanknotes() {
        // given
        int amount = 100;

        //when
        final Throwable thrown = catchThrowable(() -> vault.get(amount));

        //then
        Assertions.assertThat(thrown).isInstanceOf(EmptyVaultException.class);
    }
}
