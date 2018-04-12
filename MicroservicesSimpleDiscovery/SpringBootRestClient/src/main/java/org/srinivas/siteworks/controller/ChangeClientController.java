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
import org.srinivas.siteworks.changeclientservice.ChangeClientService;
import org.srinivas.siteworks.denomination.Coin;

@RestController
@RequestMapping(value = "/call")
public class ChangeClientController {

	private static final Logger logger = LoggerFactory.getLogger(ChangeClientController.class);
	
	@Autowired
	ChangeClientService changeClientServiceImpl;

	@RequestMapping(value = "/optimal", method = RequestMethod.GET)
	public @ResponseBody List<Coin> handleGetOptimalCalculation(@RequestParam("pence") Integer pence) {
		logger.info("ChangeController:handleGetOptimalCalculation Method");
		try {
			return changeClientServiceImpl.getOptimalChangeFor(pence).stream().collect(Collectors.toList());
		} catch (Exception e) {
			logger.error("Error:" + e);
			return new ArrayList<Coin>();
		}
	}

	@RequestMapping(value = "/supply", method = RequestMethod.GET)
	public @ResponseBody List<Coin> handleGetSupplyCalculation(@RequestParam("pence") Integer pence) {
		logger.info("ChangeController:handleGetSupplyCalculation Method");
		try {
			return changeClientServiceImpl.getChangeFor(pence).stream().collect(Collectors.toList());
		} catch (Exception e) {
			logger.error("Error:" + e);
			return new ArrayList<Coin>();
		}
	}

}
