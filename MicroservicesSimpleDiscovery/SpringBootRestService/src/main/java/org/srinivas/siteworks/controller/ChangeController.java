package org.srinivas.siteworks.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.srinivas.siteworks.changeservice.ChangeService;
import org.srinivas.siteworks.data.PropertiesReadWriter;
import org.srinivas.siteworks.denomination.Coin;

@RestController
@RequestMapping(value = "/change")
public class ChangeController {

	private static final Logger logger = LoggerFactory.getLogger(ChangeController.class);

	@Autowired
	PropertiesReadWriter propertiesReadWriter;

	@Autowired
	ChangeService changeServiceImpl;

	@RequestMapping(value = "/optimal", method = RequestMethod.GET)
	public @ResponseBody List<Coin> handleGetOptimalCalculation(@RequestParam("pence") Integer pence) {
		logger.info("ChangeController:handleGetOptimalCalculation Method");
		try {
			propertiesReadWriter.readInventoryData();
			return changeServiceImpl.getOptimalChangeFor(pence).stream().collect(Collectors.toList());
		} catch (Exception e) {
			logger.info("Error:" + e.getMessage());
			return new ArrayList<Coin>();
		}
	}

	@RequestMapping(value = "/supply", method = RequestMethod.GET)
	public @ResponseBody List<Coin> handleGetSupplyCalculation(@RequestParam("pence") Integer pence) {
		logger.info("ChangeController:handleGetSupplyCalculation Method");
		try {
			propertiesReadWriter.readInventoryData();
			return changeServiceImpl.getChangeFor(pence).stream().collect(Collectors.toList());
		} catch (Exception e) {
			logger.info("Error:" + e.getMessage());
			return new ArrayList<Coin>();
		}
	}

}
