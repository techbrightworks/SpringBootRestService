package org.srinivas.siteworks.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.srinivas.siteworks.changeservice.ChangeService;
import org.srinivas.siteworks.data.PropertiesReadWriter;
import org.srinivas.siteworks.denomination.Coin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/change")
public class ChangeController {

    private static final Logger logger = LogManager.getLogger ( ChangeController.class );

    @Autowired
    public PropertiesReadWriter propertiesReadWriter;

    @Autowired
    public ChangeService changeServiceImpl;

    @RequestMapping(value = "/optimal", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    List<Coin> handleGetOptimalCalculation(@RequestParam("pence") Integer pence) {
        logger.info ( "ChangeController:handleGetOptimalCalculation Method" );
        try {
            propertiesReadWriter.readInventoryData ();
            return changeServiceImpl.getOptimalChangeFor ( pence ).stream ().collect ( Collectors.toList () );
        } catch (Exception e) {
            logger.info ( "Error:" + e.getMessage () );
            return new ArrayList<Coin> ();
        }
    }

    @RequestMapping(value = "/supply", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    List<Coin> handleGetSupplyCalculation(@RequestParam("pence") Integer pence) {
        logger.info ( "ChangeController:handleGetSupplyCalculation Method" );
        try {
            propertiesReadWriter.readInventoryData ();
            return changeServiceImpl.getChangeFor ( pence ).stream ().collect ( Collectors.toList () );
        } catch (Exception e) {
            logger.info ( "Error:" + e.getMessage () );
            return new ArrayList<Coin> ();
        }
    }

}
