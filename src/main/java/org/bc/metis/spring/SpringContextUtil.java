package org.bc.metis.spring;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextUtil implements ApplicationContextAware{

	ApplicationContext srpingContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		srpingContext = applicationContext;
		watchContextConfigs();
	}
	
	private void watchContextConfigs(){
		Thread t = new Thread(){
			public void run(){
				while(true){
					doWatch();
				}
			}
		};
		t.start();
	}
	
	private void doWatch(){
		Path myDir = Paths.get("E:\\java\\git\\xzye\\metis\\src\\main\\webapp\\WEB-INF");
		try {  
	           WatchService watcher = myDir.getFileSystem().newWatchService();  
	           myDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,   StandardWatchEventKinds.ENTRY_MODIFY);  
	           WatchKey watckKey = watcher.take();  
	           List<WatchEvent<?>> events = watckKey.pollEvents();  
	           for (WatchEvent event : events) {  
	                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {  
	                    System.out.println("Created: " + event.context().toString());  
	                }  
	                if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {  
	                    System.out.println("Modify: " + event.context().toString());  
	                }  
	            }  
	             
	        } catch (Exception e) {  
	            System.out.println("Error: " + e.toString());  
	        }
	}
}
