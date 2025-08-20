package org.example.homework.atm;

import java.util.Map;

public class Main {
	public static void main(String[] args) {
		ATM atm = new ATM();

		System.out.println("Пополняем банкомат: 1000x3, 500x2, 5000x1, 100x5");
		atm.deposit(Denomination.ONE_THOUSAND, 3);
		atm.deposit(Denomination.FIVE_HUNDRED, 2);
		atm.deposit(Denomination.FIVE_THOUSAND, 1);
		atm.deposit(Denomination.ONE_HUNDRED, 5);

		System.out.println("Баланс: " + atm.getBalance());

		int request = 6600;
		System.out.println("Пробуем выдать " + request + ":");
		try {
			Map<Denomination, Integer> cash = atm.withdraw(request);
			System.out.println("Выдано: " + cash);
			System.out.println("Баланс после выдачи: " + atm.getBalance());
		} catch (IllegalArgumentException ex) {
			System.out.println("Ошибка: " + ex.getMessage());
		}

		int badRequest = 300;
		System.out.println("Пробуем выдать невозможную сумму " + badRequest + ":");
		try {
			atm.withdraw(badRequest);
		} catch (IllegalArgumentException ex) {
			System.out.println("Ожидаемая ошибка: " + ex.getMessage());
		}
	}
}



