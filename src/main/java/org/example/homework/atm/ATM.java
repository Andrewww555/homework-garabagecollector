package org.example.homework.atm;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ATM {
	private final EnumMap<Denomination, Integer> denominationToCount = new EnumMap<>(Denomination.class);

	public ATM() {
		for (Denomination denomination : Denomination.values()) {
			denominationToCount.put(denomination, 0);
		}
	}

	public void deposit(Denomination denomination, int count) {
		Objects.requireNonNull(denomination, "denomination");
		if (count <= 0) {
			throw new IllegalArgumentException("count must be positive");
		}
		denominationToCount.merge(denomination, count, Integer::sum);
	}

	public int getBalance() {
		int sum = 0;
		for (Map.Entry<Denomination, Integer> entry : denominationToCount.entrySet()) {
			sum += entry.getKey().getValue() * entry.getValue();
		}
		return sum;
	}

	public Map<Denomination, Integer> withdraw(int amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("amount must be positive");
		}
		if (amount > getBalance()) {
			throw new IllegalArgumentException("Insufficient funds");
		}

		List<Denomination> sorted = List.of(Denomination.values())
				.stream()
				.sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
				.collect(Collectors.toList());

		EnumMap<Denomination, Integer> toDispense = new EnumMap<>(Denomination.class);
		int remaining = amount;
		for (Denomination denom : sorted) {
			int availableNotes = denominationToCount.getOrDefault(denom, 0);
			if (availableNotes <= 0) {
				continue;
			}
			int useNotes = Math.min(availableNotes, remaining / denom.getValue());
			if (useNotes > 0) {
				toDispense.put(denom, useNotes);
				remaining -= useNotes * denom.getValue();
			}
		}

		if (remaining != 0) {
			throw new IllegalArgumentException("Cannot dispense the requested amount with available denominations");
		}

		// Apply changes
		for (Map.Entry<Denomination, Integer> e : toDispense.entrySet()) {
			denominationToCount.merge(e.getKey(), -e.getValue(), Integer::sum);
		}
		return Collections.unmodifiableMap(toDispense);
	}

	public Map<Denomination, Integer> getInventorySnapshot() {
		return Collections.unmodifiableMap(new EnumMap<>(denominationToCount));
	}
}

