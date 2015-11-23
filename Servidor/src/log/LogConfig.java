package log;

import java.io.File;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;


public class LogConfig {
	private static LogConfig instance = null;
	
	public static  LogConfig getInstance(){
		if (instance == null){
			instance = new LogConfig();
		}
		return instance;
	}
	
	
	private LogConfig(){
		
	}
	
	// llamar en un preDestroy
	public void shutdown(){
		LogManager.getLogger("LogConfig").fatal("*** Finaliza el servidor!!!! ***");
		LogManager.shutdown();
	}
	
	
	public void mostrarLogInfo(){
		Logger logger = Logger.getRootLogger(); //Defining the Logger
		FileAppender appender = (FileAppender)logger.getAppender("mainAppender");
		System.out.println("Secundary (Buffered) Log Path: " +  new File(appender.getFile()).getAbsolutePath());
		System.out.println("It will be flushed after the buffer is full or at shutdown.");
	}

}
