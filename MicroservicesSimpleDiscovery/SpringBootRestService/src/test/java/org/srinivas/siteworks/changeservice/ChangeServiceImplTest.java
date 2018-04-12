package org.srinivas.siteworks.changeservice;

import static org.junit.Assert.*;

import java.util.Collection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.srinivas.siteworks.calculate.Calculate;
import org.srinivas.siteworks.calculate.SupplyCalculator;
import org.srinivas.siteworks.config.AppConfig;
import org.srinivas.siteworks.data.CoinsInventoryData;
import org.srinivas.siteworks.data.PropertiesReadWriter;
import org.srinivas.siteworks.denomination.Coin;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class})
@WebAppConfiguration
public class ChangeServiceImplTest {

	private SupplyCalculator supplyCalculator;
	private ChangeServiceImpl changeServiceImpl;
	@Autowired
	private Calculate optimalCalculator;
	@Autowired
	PropertiesReadWriter propertiesReadWriter;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);	
		supplyCalculator = new SupplyCalculator();
		supplyCalculator.setPropertiesReadWriter(propertiesReadWriter);
		propertiesReadWriter.setResourceName("test-coin-inventory.properties");
		propertiesReadWriter.writeInventoryData(CoinsInventoryData.getInventoryData());
		changeServiceImpl = new ChangeServiceImpl();
		changeServiceImpl.supplyCalculator = supplyCalculator;
		changeServiceImpl.propertiesReadWriter = propertiesReadWriter;
		changeServiceImpl.optimalCalculator = optimalCalculator;
	}

	@After
	public void tearDown() throws Exception {
		propertiesReadWriter.writeInventoryData(CoinsInventoryData.getInventoryData());
		propertiesReadWriter = null;
		supplyCalculator = null;
		changeServiceImpl  = null;
	}

	@Test
	public void testGetOptimalChangeFor() throws Exception {
		propertiesReadWriter.readInventoryData();
		Collection<Coin>coins = changeServiceImpl.getOptimalChangeFor(576);
		assertTrue(coins.size() == 5);
		assertEquals(5,filterByValue(coins,100).getCount().intValue());
		assertEquals(1,filterByValue(coins,50).getCount().intValue());
		assertEquals(1,filterByValue(coins,20).getCount().intValue());
		assertEquals(1,filterByValue(coins,5).getCount().intValue());
		assertEquals(1,filterByValue(coins,1).getCount().intValue());
	}

	@Test
	public void testGetChangeFor() throws Exception {
		propertiesReadWriter.readInventoryData();
		Collection<Coin>coins = changeServiceImpl.getChangeFor(2896);
		assertTrue(coins.size() == 5);
		assertEquals(11,filterByValue(coins,100).getCount().intValue());
		assertEquals(24,filterByValue(coins,50).getCount().intValue());
		assertEquals(59,filterByValue(coins,10).getCount().intValue());
		assertEquals(1,filterByValue(coins,5).getCount().intValue());
		assertEquals(1,filterByValue(coins,1).getCount().intValue());
	}
	
	
	private Coin filterByValue(Collection<Coin> coins, Integer value) {
		return coins.stream().filter(coin -> coin.getValue() == value).findFirst().get();
	}
}
