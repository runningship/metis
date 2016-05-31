package org.bc.metis.spring;

import java.io.File;
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
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SpringContextWatcher implements ApplicationContextAware{

	static ApplicationContext srpingContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		srpingContext = applicationContext;
	}
	
	public static void startWatch(List<String> paths){
		for(String path : paths){
			innerStartWatcher(Paths.get(path));
		}
	}
	
	private static void innerStartWatcher(final Path dir){
		Thread t = new Thread(){
			public void run(){
				while(true){
					doWatch(dir);
				}
			}
		};
		t.start();
	}
	
	private static void doWatch(Path dir){
		try {  
	           WatchService watcher = dir.getFileSystem().newWatchService();  
	           dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,   StandardWatchEventKinds.ENTRY_MODIFY);  
	           WatchKey watckKey = watcher.take();  
	           List<WatchEvent<?>> events = watckKey.pollEvents();  
	           for (WatchEvent event : events) {  
	                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {  
	                    System.out.println("Created: " + event.context().toString());  
	                }  
	                if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {  
	                    System.out.println("Modify: " + event.context().toString());
	                    refreshConfigToContext(dir+ File.separator+ event.context().toString());
	                }  
	            }  
	             
	        } catch (Exception e) {  
	            System.out.println("Error: " + e.toString());  
	        }
	}
	
	private static void refreshConfigToContext(String file){
		try{
			FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(file);
			context.getBeanDefinitionNames();
		}catch(Exception ex){
			System.out.println("refresh "+file+ " failed");
		}
		
	}
}
