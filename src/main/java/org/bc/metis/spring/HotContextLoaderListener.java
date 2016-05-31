package org.bc.metis.spring;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

public class HotContextLoaderListener extends ContextLoaderListener{

	private static List<String> springConfigDirs = new ArrayList<String>();
	
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		String[] paths = StringUtils.tokenizeToStringArray(event.getServletContext().getInitParameter(ContextLoader.CONFIG_LOCATION_PARAM),
				ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
		for(String path : paths){
			String realPath = event.getServletContext().getRealPath(path);
			File f = new File(realPath);
			String dir = f.getParent();
			if(!springConfigDirs.contains(dir)){
				springConfigDirs.add(dir);
			}
		}
		SpringContextUtil.startWatch(springConfigDirs);
	}
	
}
