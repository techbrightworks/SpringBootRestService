package org.srinivas.siteworks.calculate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srinivas.siteworks.data.PropertiesReadWriter;
import org.srinivas.siteworks.denomination.Coin;
import org.srinivas.siteworks.exception.CalculateException;


@Component
public class SupplyCalculator implements Calculate {


	@Autowired
	PropertiesReadWriter propertiesReadWriter;
	

	public static final Logger log = LoggerFactory.getLogger(SupplyCalculator.class);

	/**
	 * Gets the properties read writer.
	 *
	 * @return the properties read writer
	 */
	public PropertiesReadWriter getPropertiesReadWriter() {
		return propertiesReadWriter;
	}

	/**
	 * Sets the properties read writer.
	 *
	 * @param propertiesReadWriter the new properties read writer
	 */
	public void setPropertiesReadWriter(PropertiesReadWriter propertiesReadWriter) {
		this.propertiesReadWriter = propertiesReadWriter;
	}

	/* (non-Javadoc)
	 * @see org.srinivas.siteworks.calculate.Calculate#calculate(java.lang.Integer, java.lang.Integer[])
	 */
	@Override
	public List<Coin> calculate(Integer pence, Integer[] denominations) throws Exception {
		List<Coin> suppliedCoins = new ArrayList<Coin>();
		try {
			Map<Integer, Integer> inventoryMap = propertiesReadWriter.getInventoryMap();
			Integer inventoryTotal = inventoryMapValueTotal(inventoryMap);
			List<Integer> shortSupplyList = new ArrayList<Integer>();
			AtomicInteger remaining = new AtomicInteger(pence);
			if(inventoryTotal >= pence){
			supplyCalculation(denominations, inventoryMap, shortSupplyList, remaining, suppliedCoins);
			propertiesReadWriter.writeInventoryData(inventoryMap);
			}
		} catch (Exception e) {
			suppliedCoins = null;
			suppliedCoins = new ArrayList<Coin>();
			log.error("SupplyCalculation Unsuccessful",e);
		}
		return suppliedCoins;
	}

	/**
	 * Supply calculation.
	 *
	 * @param denominations the denominations
	 * @param inventoryMap the inventory map
	 * @param shortSupplyList the short supply list
	 * @param remaining the remaining
	 * @param suppliedCoins the supplied coins
	 * @throws Exception the exception
	 */
	private void supplyCalculation(Integer[] denominations, Map<Integer, Integer> inventoryMap, List<Integer> shortSupplyList, AtomicInteger remaining, List<Coin> suppliedCoins) throws Exception {
		OptimalCalculator optimalCalculator = new OptimalCalculator();
		if(denominations.length == 0){
			throw new CalculateException("Insufficient Supply of Coins");
		}
		List<Coin> optimalCalculatedCoins = optimalCalculator.calculate(remaining.get(), denominations);
		calculateSupplyCoins(denominations, inventoryMap, shortSupplyList, remaining, suppliedCoins, optimalCalculatedCoins);
	}

	/**
	 * Calculate supply coins.
	 *
	 * @param denominations the denominations
	 * @param inventoryMap the inventory map
	 * @param shortSupplyList the short supply list
	 * @param remaining the remaining
	 * @param suppliedCoins the supplied coins
	 * @param optimalCalculatedCoins the optimal calculated coins
	 * @throws Exception the exception
	 */
	private void calculateSupplyCoins(Integer[] denominations, Map<Integer, Integer> inventoryMap, List<Integer> shortSupplyList, AtomicInteger remaining, List<Coin> suppliedCoins, List<Coin> optimalCalculatedCoins)throws Exception{
		Arrays.stream(denominations).forEach(key -> {
			if(remaining.get() > 0 && denominations.length > 0 && isOneofOptimalValue(optimalCalculatedCoins,key)){
				
			try {
				Coin coin = filterByValue(optimalCalculatedCoins, key);

				if (coin != null && coin.getCount() > inventoryMap.get(key)) {
					insufficientInventoryChanges(inventoryMap, shortSupplyList, remaining, suppliedCoins, key);
				} else {
					inventoryAvailableChanges(inventoryMap, remaining, suppliedCoins, key, coin);
				}
				Integer[] denom = evaluateDenom(denominations, shortSupplyList, remaining);
				recurseSupplyCalculation(inventoryMap, shortSupplyList, remaining, suppliedCoins, denom);
			} catch (Exception e) {
				throw new RuntimeException("Insufficient Supply of Coins",e);
			}
			}
		});
	}

	/**
	 * Evaluate denom.
	 *
	 * @param denominations the denominations
	 * @param shortSupplyList the short supply list
	 * @param remaining the remaining
	 * @return the integer[]
	 */
	private Integer[] evaluateDenom(Integer[] denominations, List<Integer> shortSupplyList, AtomicInteger remaining) {
		Integer[] denom = new Integer[]{};
		if(remaining.get() > 0 && shortSupplyList.size() > 0){
		denom = Arrays.stream(denominations).filter(den -> (!shortSupplyList.contains(den))).toArray(Integer[]::new);
		}else if(remaining.get() > 0 && shortSupplyList.size() == 0 && denominations.length > 0){
			denom = denominations;
		}
		return denom;
	}

	/**
	 * Recurse supply calculation.
	 *
	 * @param inventoryMap the inventory map
	 * @param shortSupplyList the short supply list
	 * @param remaining the remaining
	 * @param suppliedCoins the supplied coins
	 * @param denom the denom
	 * @throws CalculateException 
	 */
	private void recurseSupplyCalculation(Map<Integer, Integer> inventoryMap, List<Integer> shortSupplyList, AtomicInteger remaining, List<Coin> suppliedCoins, Integer[] denom) throws CalculateException {
		if (remaining.get() > 0) {
			try {
				supplyCalculation(denom,inventoryMap, shortSupplyList, remaining, suppliedCoins);
			} catch (Exception e) {
				log.error("SupplyCalculation Unsuccessful",e);
				throw new CalculateException("Insufficient Supply of Coins",e);
			}
		}
	}

	/**
	 * Insufficient inventory changes.
	 *
	 * @param inventoryMap the inventory map
	 * @param shortSupplyList the short supply list
	 * @param remaining the remaining
	 * @param suppliedCoins the supplied coins
	 * @param key the key
	 */
	private void insufficientInventoryChanges(Map<Integer, Integer> inventoryMap, List<Integer> shortSupplyList, AtomicInteger remaining, List<Coin> suppliedCoins, Integer key) {
		remaining.set(remaining.get() - (inventoryMap.get(key) * key));
		shortSupplyList.add(key);
		addCoin(key, inventoryMap.get(key), suppliedCoins);
		zeroValueInventory(inventoryMap, key);
	}

	/**
	 * Inventory available changes.
	 *
	 * @param inventoryMap the inventory map
	 * @param remaining the remaining
	 * @param suppliedCoins the supplied coins
	 * @param key the key
	 * @param coin the coin
	 */
	private void inventoryAvailableChanges(Map<Integer, Integer> inventoryMap, AtomicInteger remaining, List<Coin> suppliedCoins, Integer key, Coin coin) {
		reduceValueInventory(coin.getCount(), inventoryMap, key);
		remaining.set(remaining.get() - (coin.getCount() * key));
		addCoin(key, coin.getCount(), suppliedCoins);
	}

	/**
	 * Zero value inventory.
	 *
	 * @param inventoryMap the inventory map
	 * @param key the key
	 */
	private void zeroValueInventory(Map<Integer, Integer> inventoryMap, Integer key) {
		if (inventoryMap.containsKey(key)) {
			inventoryMap.put(key, 0);
		}
	}

	/**
	 * Reduce value inventory.
	 *
	 * @param reducyBy the reducy by
	 * @param inventoryMap the inventory map
	 * @param key the key
	 */
	private void reduceValueInventory(Integer reducyBy, Map<Integer, Integer> inventoryMap, Integer key) {
		if (inventoryMap.containsKey(key)) {
			inventoryMap.put(key, (inventoryMap.get(key) - reducyBy));
		}
	}

	/**
	 * Filter by value.
	 *
	 * @param coins the coins
	 * @param value the value
	 * @return the coin
	 * @throws Exception the exception
	 */
	private Coin filterByValue(List<Coin> coins, Integer value) throws Exception {
	  return coins.stream().filter(coin -> coin.getValue() == value).findFirst().get();
		
	}
	
	/**
	 * Checks if is oneof optimal value.
	 *
	 * @param coins the coins
	 * @param value the value
	 * @return true, if is oneof optimal value
	 */
	private boolean isOneofOptimalValue(List<Coin> coins, Integer value){
		
		return coins.stream().anyMatch(coin -> coin.getValue() == value);
	}

	/**
	 * Adds the coin.
	 *
	 * @param value the value
	 * @param count the count
	 * @param coins the coins
	 */
	private void addCoin(Integer value, Integer count, List<Coin> coins) {
		Coin coin = new Coin();
		coin.setValue(value);
		coin.setCount(count);
		if(count > 0){
		coins.add(coin);
		}
	}
	
	/**
	 * Inventory map value total.
	 *
	 * @param inventoryMap the inventory map
	 * @return the integer
	 */
	private Integer inventoryMapValueTotal(Map<Integer, Integer> inventoryMap){
		AtomicInteger total = new AtomicInteger(0);
		inventoryMap.entrySet().stream().forEach(e ->{
			total.set(total.get() + e.getKey() * e.getValue());
		});
		return total.get();
	}

}
