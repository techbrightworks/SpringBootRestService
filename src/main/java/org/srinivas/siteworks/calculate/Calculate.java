package org.srinivas.siteworks.calculate;

import org.srinivas.siteworks.denomination.Coin;

import java.util.List;


public interface Calculate {

    public List<Coin> calculate(Integer pence, Integer[] denominations) throws Exception;


    default void CoinsNameSetter(Coin e) {
        if (e.getValue () == 100) {
            e.setName ( "Hundred" );
        } else if (e.getValue () == 50) {
            e.setName ( "Fifty" );
        } else if (e.getValue () == 20) {
            e.setName ( "Twenty" );
        } else if (e.getValue () == 10) {
            e.setName ( "Ten" );
        } else if (e.getValue () == 5) {
            e.setName ( "Five" );
        } else if (e.getValue () == 2) {
            e.setName ( "Two" );
        } else if (e.getValue () == 1) {
            e.setName ( "One" );
        } else {
            // Do Nothing
        }
    }

}
