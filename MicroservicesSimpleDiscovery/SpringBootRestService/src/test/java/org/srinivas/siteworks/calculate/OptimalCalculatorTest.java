package org.srinivas.siteworks.calculate;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.srinivas.siteworks.config.AppConfig;
import org.srinivas.siteworks.data.PropertiesReadWriter;
import org.srinivas.siteworks.denomination.Coin;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class})
@WebAppConfiguration
public class OptimalCalculatorTest {
	
	@Autowired
	private Calculate optimalCalculator;
	
	@Autowired
	PropertiesReadWriter propertiesReadWriter;


	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);	
		
	}

	@After
	public void tearDown() throws Exception {
		propertiesReadWriter = null;
		optimalCalculator = null;
	}

	@Test
	public void testCalculate() throws Exception {
		propertiesReadWriter.readInventoryData();
		List<Coin>coins = optimalCalculator.calculate(576, propertiesReadWriter.denominations());
		assertTrue(coins.size() == 5);
		assertEquals(5,filterByValue(coins,100).getCount().intValue());
		assertEquals(1,filterByValue(coins,50).getCount().intValue());
		assertEquals(1,filterByValue(coins,20).getCount().intValue());
		assertEquals(1,filterByValue(coins,5).getCount().intValue());
		assertEquals(1,filterByValue(coins,1).getCount().intValue());
	}
	
	@Test
	public void testErrorScenario() throws Exception {
		propertiesReadWriter.readInventoryData();
		List<Coin>coins = optimalCalculator.calculate(576, null);
		assertTrue(coins.size() == 0);
	}

	
	private Coin filterByValue(List<Coin> coins, Integer value) {
		return coins.stream().filter(coin -> coin.getValue() == value).findFirst().get();
	}

}
