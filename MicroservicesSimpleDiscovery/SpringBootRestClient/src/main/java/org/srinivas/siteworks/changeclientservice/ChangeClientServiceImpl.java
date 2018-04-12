package org.srinivas.siteworks.changeclientservice;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.srinivas.siteworks.denomination.Coin;


@Service
public class ChangeClientServiceImpl implements ChangeClientService {
	
	@Autowired
    @LoadBalanced
    public RestTemplate restTemplate;
	private String serviceUrl = "http://SPRINGBOOTRESTSERVICE";



	public static final Logger log = LoggerFactory.getLogger(ChangeClientServiceImpl.class);
	
	/* (non-Javadoc)
	 * @see org.srinivas.siteworks.changeservice.ChangeService#getOptimalChangeFor(int)
	 */
	@Override
	public Collection<Coin> getOptimalChangeFor(int pence)  {
		
		try {
		Coin[] coins = restTemplate.getForObject(getServiceUrl()+"/change/optimal?pence="+pence, Coin[].class);
		return Arrays.asList(coins);
		} catch (Exception e) {
			log.error("Optimal Call not Successful",e);
			return Collections.emptyList();
		}
	}

	/* (non-Javadoc)
	 * @see org.srinivas.siteworks.changeservice.ChangeService#getChangeFor(int)
	 */
	@Override
	public Collection<Coin> getChangeFor(int pence) {
		try {
			Coin[] coins = restTemplate.getForObject(getServiceUrl()+"/change/supply?pence="+pence, Coin[].class);
			return Arrays.asList(coins);
		} catch (Exception e) {
			log.error("Supply Call not Successful",e);
			return Collections.emptyList();
		}
	}
	
	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

}
