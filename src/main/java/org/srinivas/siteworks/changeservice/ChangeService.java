package org.srinivas.siteworks.changeservice;

import org.srinivas.siteworks.denomination.Coin;

import java.util.Collection;


public interface ChangeService {


    public Collection<Coin> getOptimalChangeFor(int pence);


    public Collection<Coin> getChangeFor(int pence);


}
