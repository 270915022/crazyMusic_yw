package com.crazyMusic.common.listener;
import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoaderListener;
public class InitListener extends ContextLoaderListener{
	
	private static Logger logger = Logger.getLogger(InitListener.class);
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		InitConfig.initConf();
	}
}
