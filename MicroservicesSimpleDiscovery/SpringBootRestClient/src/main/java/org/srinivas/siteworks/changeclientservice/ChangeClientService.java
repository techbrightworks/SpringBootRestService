package org.srinivas.siteworks.changeclientservice;

import java.util.Collection;

import org.srinivas.siteworks.denomination.Coin;


public interface ChangeClientService {
	
	/**
	 * Gets the optimal change for.
	 *
	 * @param pence the pence
	 * @return the optimal change for
	 */
	public Collection<Coin> getOptimalChangeFor(int pence);
	
	/**
	 * Gets the change for.
	 *
	 * @param pence the pence
	 * @return the change for
	 */
	public Collection<Coin> getChangeFor(int pence);
	

}
