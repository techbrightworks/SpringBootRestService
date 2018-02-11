package org.srinivas.siteworks.calculate;

import java.util.List;
import org.srinivas.siteworks.denomination.Coin;


public interface Calculate {
	
 /**
  * Calculate.
  *
  * @param pence the pence
  * @param denominations the denominations
  * @return the list
  * @throws Exception the exception
  */
 public List<Coin> calculate(Integer pence,Integer[] denominations)throws Exception;
 
}
