package org.srinivas.siteworks.data;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.srinivas.siteworks.exception.CalculateException;

@Repository
public class PropertiesReadWriter {
	
	private static final String COIN_INVENTORY_INFORMATION = "Coin-Inventory Information";
	private static final String FILE_NAME_COIN_INVENTORY_PROPERTIES = "coin-inventory.properties";
	private Map<Integer, Integer> inventoryMap;
	public static final Logger log = LoggerFactory.getLogger(PropertiesReadWriter.class);
	private String resourceName;

	/**
	 * Read inventory data.
	 *
	 * @throws Exception the exception
	 */
	public void readInventoryData() throws Exception {
		URL url = this.getClass().getClassLoader().getResource(getResourceName());
		Properties prop = new Properties();
		InputStream input = new FileInputStream(url.getPath());
		try {
			prop.load(input);
			Map<Integer, Integer> inventoryData = extractInventoryData(prop);
			setInventoryMap(inventoryData);
		} catch (IOException ex) {
			log.error("Unable to Read Sucesssfully", ex);
			throw new CalculateException("Unable to Read Sucesssfully", ex);
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException ex) {
				log.error("InputStream Not Closed", ex);
				throw new CalculateException("InputStream Not Closed", ex);
			}

		}
	}

	/**
	 * Write inventory data.
	 *
	 * @param inventoryMap the inventory map
	 * @throws Exception the exception
	 */
	public void writeInventoryData(Map<Integer, Integer> inventoryMap) throws Exception {
		URL url = this.getClass().getClassLoader().getResource(getResourceName());
		Properties prop = new Properties();
		Writer writer = new FileWriter(url.getPath());
		try {
			inventoryMap.entrySet().stream().forEach(coin -> {
				prop.setProperty(String.valueOf(coin.getKey()), String.valueOf(coin.getValue()));
			});
			prop.store(writer, COIN_INVENTORY_INFORMATION);
			writer.close();
		} catch (IOException ex) {
			log.error("Unable to Write Sucesssfully", ex);
			throw new CalculateException("Unable to Read Sucesssfully", ex);
		} finally {

			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException ex) {
				log.error("Writer Not Closed", ex);
				throw new CalculateException("Writer Not Closed", ex);
			}

		}
	}

	/**
	 * Denominations.
	 *
	 * @return the integer[]
	 */
	public Integer[] denominations() {
		Integer[] denominations = getInventoryMap().keySet().stream().toArray(Integer[]::new);
		Arrays.sort(denominations, Collections.reverseOrder());
		return denominations;
	}

	/**
	 * Gets the inventory map.
	 *
	 * @return the inventory map
	 */
	public Map<Integer, Integer> getInventoryMap() {
		if (inventoryMap == null) {
			inventoryMap = new HashMap<Integer, Integer>();
		}
		return inventoryMap;
	}

	/**
	 * Sets the inventory map.
	 *
	 * @param inventoryMap the inventory map
	 */
	public void setInventoryMap(Map<Integer, Integer> inventoryMap) {
		this.inventoryMap = inventoryMap;
	}

	/**
	 * Gets the resource name.
	 *
	 * @return the resource name
	 */
	public String getResourceName() {
		if (resourceName == null) {
			setResourceName(FILE_NAME_COIN_INVENTORY_PROPERTIES);
		}
		return resourceName;
	}

	/**
	 * Sets the resource name.
	 *
	 * @param resourceName the new resource name
	 */
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	
	/**
	 * Extract inventory data.
	 *
	 * @param prop the prop
	 * @return the map
	 */
	private Map<Integer, Integer> extractInventoryData(Properties prop) {
		return prop.entrySet().stream()
		.collect(Collectors.toMap(e -> Integer.valueOf(String.valueOf(e.getKey())), e -> Integer.valueOf(String.valueOf(e.getValue()))));
	}

}
