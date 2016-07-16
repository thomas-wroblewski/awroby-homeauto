package com.awroby.auto.config;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.awroby.auto.dao.OutletRepository;

@Component
@ConfigurationProperties(prefix="homeauto")
public class Properties {
	private static final Logger logger = LoggerFactory.getLogger(Properties.class);
	@Autowired private OutletRepository outletRepo;
	
	
	private List<String> outlets = new ArrayList<String>();

	public List<String> getOutlets() {
		return outlets;
	}

	public void setOutlets(List<String> outlets) {
		this.outlets = outlets;
	}
	
	
	
}
