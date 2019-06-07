package org.srinivas.siteworks.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.srinivas.siteworks.config.AppConfig;
import org.srinivas.siteworks.controller.ChangeController;
import org.srinivas.siteworks.data.PropertiesReadWriter;
import org.srinivas.siteworks.denomination.Coin;
import org.srinivas.siteworks.data.CoinsInventoryData;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@WebAppConfiguration
public class ChangeControllerIntTest {

	@Autowired
	PropertiesReadWriter propertiesReadWriter;
	private static final String FILE_NAME_TEST_COIN_INVENTORY_PROPERTIES = "test-coin-inventory.properties";
	private static final Logger logger = LoggerFactory.getLogger ( ChangeControllerIntTest.class );

	@Autowired
	private ChangeController changeController;

	@Before
	public void setUp() throws Exception {
		Path path = Paths.get ( "src", "test", "resources", FILE_NAME_TEST_COIN_INVENTORY_PROPERTIES );
		propertiesReadWriter.setResourceName ( path.toAbsolutePath ().toString () );
		propertiesReadWriter.writeInventoryData ( CoinsInventoryData.getInventoryData () );
	}

	@After
	public void tearDown() throws Exception {
		propertiesReadWriter.writeInventoryData ( CoinsInventoryData.getInventoryData () );

	}

	@SuppressWarnings({"rawtypes", "unused"})
	@Test
	public void testChangeOptimalRestCall() {

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup ( this.changeController ).build ();
		try {

			MvcResult result = mockMvc.perform ( MockMvcRequestBuilders.get ( "/change/optimal?pence=576" ) )
					.andExpect ( status ().isOk () ).andReturn ();
			ObjectMapper jsonMapper = new ObjectMapper ();
			String resultString = result.getResponse ().getContentAsString ();
			logger.info ( resultString );
			List<Coin> coins = jsonMapper.readValue ( resultString, jsonMapper.getTypeFactory ().constructCollectionType ( List.class, Coin.class ) );
			assertTrue ( coins.size () == 5 );
		} catch (Exception e) {
			logger.info ( e.getMessage () );
			fail ( "Failed Due to: " + e.getMessage () );
		}

	}

	@SuppressWarnings({"rawtypes", "unused"})
	@Test
	public void testChangeSupplyRestCall() {

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup ( this.changeController ).build ();
		try {

			MvcResult result = mockMvc.perform ( MockMvcRequestBuilders.get ( "/change/supply?pence=2896" ) )
					.andExpect ( status ().isOk () ).andReturn ();

			ObjectMapper jsonMapper = new ObjectMapper ();
			String resultString = result.getResponse ().getContentAsString ();
			logger.info ( resultString );
			List<Coin> coins = jsonMapper.readValue ( resultString, jsonMapper.getTypeFactory ().constructCollectionType ( List.class, Coin.class ) );
			assertTrue ( coins.size () == 5 );

		} catch (Exception e) {
			logger.info ( e.getMessage () );
			fail ( "Failed Due to: " + e.getMessage () );
		}

	}


	@Configuration
	static class ChangeControllerTestConfiguration {

		@Bean
		public ChangeController changeController() {
			return new ChangeController ();
		}
	}


}
