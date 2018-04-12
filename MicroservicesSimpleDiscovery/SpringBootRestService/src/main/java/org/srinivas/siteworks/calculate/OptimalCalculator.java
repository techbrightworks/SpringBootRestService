package org.srinivas.siteworks.calculate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.srinivas.siteworks.denomination.Coin;

/**
 * The Class OptimalCalculator.
 */
@Component
public class OptimalCalculator implements Calculate {

	
	public static final Logger log = LoggerFactory.getLogger(SupplyCalculator.class);

	/* (non-Javadoc)
	 * @see org.srinivas.siteworks.calculate.Calculate#calculate(java.lang.Integer, java.lang.Integer[])
	 */
	@Override
	public List<Coin> calculate(Integer pence, Integer[] denominations) {
		List<Coin> coinsMap = new ArrayList<Coin>();
		try {
			AtomicInteger remainingPence = new AtomicInteger(pence);
			optimalCalculation(denominations, coinsMap, remainingPence);
		} catch (Exception e) {
			log.error("OptimalCalculation Unsuccessful", e);
		}
		return coinsMap;
	}

	/**
	 * Optimal calculation.
	 *
	 * @param denominations the denominations
	 * @param coinsMap the coins map
	 * @param remainingPence the remaining pence
	 */
	private void optimalCalculation(Integer[] denominations, List<Coin> coinsMap, AtomicInteger remainingPence) {
		Arrays.stream(denominations).forEach(denomination -> {
			if (remainingPence.get() > 0) {
				denominationCalculation(remainingPence.get(), coinsMap, denomination);
				remainingPence.set(remainingCalculation(remainingPence.get(), denomination));
			}
		});
	}

	/**
	 * Denomination calculation.
	 *
	 * @param pence the pence
	 * @param coinsMap the coins map
	 * @param denomination the denomination
	 */
	private void denominationCalculation(Integer pence, List<Coin> coinsMap, Integer denomination) {
		Integer coins = Math.floorDiv(pence, denomination);
		Coin coin = new Coin();
		coin.setValue(denomination);
		coin.setCount(coins);
		addToCoinsList(coinsMap, coins, coin);
	}

	/**
	 * Adds the to coins list.
	 *
	 * @param coinsMap the coins map
	 * @param coins the coins
	 * @param coin the coin
	 */
	private void addToCoinsList(List<Coin> coinsMap, Integer coins, Coin coin) {
		if (coins > 0) {
			coinsMap.add(coin);
		}
	}

	/**
	 * Remaining Pence calculation.
	 *
	 * @param pence the pence
	 * @param denomination the denomination
	 * @return the integer
	 */
	private Integer remainingCalculation(Integer pence, Integer denomination) {
		Integer remainingPence = pence % denomination;
		return remainingPence;
	}
}
