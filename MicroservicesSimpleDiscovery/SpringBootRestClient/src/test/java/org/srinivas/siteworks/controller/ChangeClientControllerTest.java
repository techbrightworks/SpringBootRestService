package org.srinivas.siteworks.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;
import org.srinivas.siteworks.changeclientservice.ChangeClientServiceImpl;
import org.srinivas.siteworks.config.AppConfig;
import org.srinivas.siteworks.denomination.Coin;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class})
@WebAppConfiguration
@SpringBootTest
public class ChangeClientControllerTest {
	
    private ChangeClientController changeClientContoller;

	private ChangeClientServiceImpl changeClientServiceImpl;
    
	Coin[] resultOptimalCoins = new Coin[]{new Coin(),new Coin(),new Coin()};
	Coin[] resultSupplyCoins = new Coin[]{new Coin(),new Coin(),new Coin(),new Coin()};
	
	@Mock
    public RestTemplate mockRestTemplate;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);	
		changeClientServiceImpl = new ChangeClientServiceImpl();
		changeClientServiceImpl.restTemplate = mockRestTemplate;
		when(mockRestTemplate.getForObject(Mockito.contains("/change/optimal"),Mockito.any())).thenReturn(resultOptimalCoins);
		when(mockRestTemplate.getForObject(Mockito.contains("/change/supply"),Mockito.any())).thenReturn(resultSupplyCoins);
		changeClientContoller = new ChangeClientController(); 
		changeClientContoller.changeClientServiceImpl = changeClientServiceImpl;
	}

	@After
	public void tearDown() throws Exception {
		
		changeClientServiceImpl  = null;
	}

	@Test
	public void testGetOptimalCall() throws Exception {
		Collection<Coin>coins = changeClientContoller.handleGetOptimalCalculation(576);
		assertTrue(coins.size() == 3);
	}
	
	@Test
	public void testGetSupplyCall() throws Exception {
		Collection<Coin>coins = changeClientContoller.handleGetSupplyCalculation(2896);
		assertTrue(coins.size() == 4);
	}
	
}
