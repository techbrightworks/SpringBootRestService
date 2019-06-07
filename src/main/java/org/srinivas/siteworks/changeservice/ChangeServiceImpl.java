package org.srinivas.siteworks.changeservice;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.srinivas.siteworks.calculate.Calculate;
import org.srinivas.siteworks.data.PropertiesReadWriter;
import org.srinivas.siteworks.denomination.Coin;

import java.util.Collection;
import java.util.Collections;

@Service
public class ChangeServiceImpl implements ChangeService {


    @Autowired
    public PropertiesReadWriter propertiesReadWriter;


    @Autowired
    public Calculate supplyCalculator;


    @Autowired
    public Calculate optimalCalculator;


    public static final org.apache.logging.log4j.Logger log = LogManager.getLogger ( ChangeServiceImpl.class );


    @Override
    public Collection<Coin> getOptimalChangeFor(int pence) {

        try {
            return optimalCalculator.calculate ( pence, propertiesReadWriter.denominations () );
        } catch (Exception e) {
            log.error ( "Optimal Calculation not Successful", e );
            return Collections.emptyList ();
        }
    }


    @Override
    public Collection<Coin> getChangeFor(int pence) {
        try {
            return supplyCalculator.calculate ( pence, propertiesReadWriter.denominations () );
        } catch (Exception e) {
            log.error ( "Supply Calculation not Successful", e );
            return Collections.emptyList ();
        }
    }

}
