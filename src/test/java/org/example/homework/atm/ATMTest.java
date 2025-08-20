package org.example.homework.atm;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ATMTest {

	@Test
	void deposit_and_balance() {
		ATM atm = new ATM();
		atm.deposit(Denomination.ONE_THOUSAND, 3);
		atm.deposit(Denomination.FIVE_HUNDRED, 2);

		assertEquals(3 * 1000 + 2 * 500, atm.getBalance());
	}

	@Test
	void withdraw_greedy_success() {
		ATM atm = new ATM();
		atm.deposit(Denomination.FIVE_THOUSAND, 1);
		atm.deposit(Denomination.ONE_THOUSAND, 2);
		atm.deposit(Denomination.FIVE_HUNDRED, 3);
		atm.deposit(Denomination.ONE_HUNDRED, 5);

		Map<Denomination, Integer> withdrawn = atm.withdraw(6600);

		assertEquals(1, withdrawn.getOrDefault(Denomination.FIVE_THOUSAND, 0).intValue());
		assertEquals(1, withdrawn.getOrDefault(Denomination.ONE_THOUSAND, 0).intValue());
		assertEquals(1, withdrawn.getOrDefault(Denomination.FIVE_HUNDRED, 0).intValue());
		assertEquals(1, withdrawn.getOrDefault(Denomination.ONE_HUNDRED, 0).intValue());
		assertEquals(5000 + 2000 + 1500 + 500 - 6600, atm.getBalance());
	}

	@Test
	void withdraw_exact_unavailable_error() {
		ATM atm = new ATM();
		atm.deposit(Denomination.ONE_THOUSAND, 1);
		atm.deposit(Denomination.FIVE_HUNDRED, 1);

		Exception e1 = assertThrows(IllegalArgumentException.class, () -> atm.withdraw(300));
		assertTrue(e1.getMessage().contains("Cannot dispense"));
	}

	@Test
	void insufficient_funds_error() {
		ATM atm = new ATM();
		atm.deposit(Denomination.ONE_THOUSAND, 1);

		Exception e2 = assertThrows(IllegalArgumentException.class, () -> atm.withdraw(2000));
		assertTrue(e2.getMessage().contains("Insufficient funds"));
	}

	@Test
	void negative_inputs_are_invalid() {
		ATM atm = new ATM();
		assertThrows(IllegalArgumentException.class, () -> atm.deposit(Denomination.ONE_HUNDRED, 0));
		assertThrows(IllegalArgumentException.class, () -> atm.withdraw(-1));
	}
}

