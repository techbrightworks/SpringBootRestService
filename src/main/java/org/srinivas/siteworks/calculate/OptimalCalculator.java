package org.srinivas.siteworks.calculate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.srinivas.siteworks.denomination.Coin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class OptimalCalculator implements Calculate {


    public static final Logger log = LogManager.getLogger ( OptimalCalculator.class );


    @Override
    public List<Coin> calculate(Integer pence, Integer[] denominations) {
        List<Coin> coinsMap = new ArrayList<Coin> ();
        try {
            AtomicInteger remainingPence = new AtomicInteger ( pence );
            optimalCalculation ( denominations, coinsMap, remainingPence );
        } catch (Exception e) {
            log.error ( "OptimalCalculation Unsuccessful", e );
        }
        return coinsMap;
    }


    private void optimalCalculation(Integer[] denominations, List<Coin> coinsMap, AtomicInteger remainingPence) {
        Arrays.stream ( denominations ).forEach ( denomination -> {
            if (remainingPence.get () > 0) {
                denominationCalculation ( remainingPence.get (), coinsMap, denomination );
                remainingPence.set ( remainingCalculation ( remainingPence.get (), denomination ) );
            }
        } );
    }


    private void denominationCalculation(Integer pence, List<Coin> coinsMap, Integer denomination) {
        Integer coins = Math.floorDiv ( pence, denomination );
        Coin coin = new Coin ();
        coin.setValue ( denomination );
        coin.setCount ( coins );
        addToCoinsList ( coinsMap, coins, coin );
        coinsListNameSetter ( coinsMap );
    }


    private void addToCoinsList(List<Coin> coinsMap, Integer coins, Coin coin) {
        if (coins > 0) {
            coinsMap.add ( coin );
        }
    }

    private void coinsListNameSetter(List<Coin> coinsMap) {
        coinsMap.stream ().forEach ( e -> {
            if (coinsMap.size () > 0) {
                CoinsNameSetter ( e );
            }
        } );
    }

    private Integer remainingCalculation(Integer pence, Integer denomination) {
        Integer remainingPence = pence % denomination;
        return remainingPence;
    }
}
