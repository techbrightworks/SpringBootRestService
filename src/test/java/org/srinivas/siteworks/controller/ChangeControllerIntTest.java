package org.srinivas.siteworks.controller;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.srinivas.siteworks.data.CoinsInventoryData;
import org.srinivas.siteworks.data.PropertiesReadWriter;
import org.srinivas.siteworks.denomination.Coin;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
@WebAppConfiguration
public class ChangeControllerIntTest {

	@Autowired
	PropertiesReadWriter propertiesReadWriter;

	private static final Logger logger = LoggerFactory.getLogger(ChangeControllerIntTest.class);

	@Autowired
	private ChangeController changeController;

	@Before
	public void setUp() throws Exception {
		propertiesReadWriter.writeInventoryData(CoinsInventoryData.getInventoryData());
	}

	@After
	public void tearDown() throws Exception {
		propertiesReadWriter.writeInventoryData(CoinsInventoryData.getInventoryData());

	}

	@SuppressWarnings({ "rawtypes", "unused" })
	@Test
	public void testChangeOptimalRestCall() {

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(this.changeController).build();
		try {

			MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/change/optimal?pence=576"))
					.andExpect(status().isOk()).andReturn();

			XmlMapper xmlMapper = new XmlMapper();

			List coins = xmlMapper.readValue(result.getResponse().getContentAsString(), List.class);
			assertTrue(coins.size() == 5);
			ObjectMapper jsonMapper = new ObjectMapper();
			String json = jsonMapper.writeValueAsString(coins);

		} catch (Exception e) {
			logger.info(e.getMessage());
			fail("Failed Due to: " + e.getMessage());
		}

	}

	@SuppressWarnings({ "rawtypes", "unused" })
	@Test
	public void testChangeSuppyRestCall() {

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(this.changeController).build();
		try {

			MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/change/optimal?pence=2896"))
					.andExpect(status().isOk()).andReturn();

			XmlMapper xmlMapper = new XmlMapper();
			List coins = xmlMapper.readValue(result.getResponse().getContentAsString(), List.class);
			assertTrue(coins.size() == 5);
			ObjectMapper jsonMapper = new ObjectMapper();
			String json = jsonMapper.writeValueAsString(coins);

		} catch (Exception e) {
			logger.info(e.getMessage());
			fail("Failed Due to: " + e.getMessage());
		}

	}

	/**
	 * The Class HelloWorldControllerTestConfiguration.
	 */
	@Configuration
	static class ChangeControllerTestConfiguration {

		@Bean
		public ChangeController changeController() {
			return new ChangeController();
		}
	}

	@SuppressWarnings("unused")
	private Coin filterByValue(List<Coin> coins, Integer value) {
		return coins.stream().filter(coin -> coin.getValue() == value).findFirst().get();
	}

}
