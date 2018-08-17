package com.example.demo.eastweb;

import org.springframework.stereotype.Component;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

import java.nio.file.*;

@Component
public class EASTWebService {	

	public String retrieveMessage() throws IOException, InterruptedException {		
		return runEASTWeb();
	}
	
	public String runEASTWeb() throws IOException, InterruptedException {
		
		// processbuilder to run eastweb	
		// CHANGE TO YOUR JAVA PATH AND EASTWEB PATH
		//test is the project name you are going to run
		ProcessBuilder pb = new ProcessBuilder("C:\\Program Files\\Java\\jre1.8.0_171\\bin\\java.exe", "-jar", "D:\\EASTWeb-cmd\\EASTWeb-cmd.jar", "test");
		//CHANGE TO YOUR EASTWEB DIRECTORY
		pb.directory(new File("D:\\EASTWeb-cmd"));
		String filePath = System.getProperty("user.dir")+".\\log.txt";
		File output = new File(filePath);
		pb.redirectOutput(output);
        pb.redirectError(output);
        pb.redirectOutput(Redirect.INHERIT);
        pb.redirectError(Redirect.INHERIT);      
        Process p = pb.start();        
        p.waitFor();      
		return retrieveOutput(filePath);
	}
	
	public String retrieveOutput(String filePath) throws IOException {
		String content ="";
		FileReader file = new FileReader(filePath);
	    BufferedReader reader = new BufferedReader(file);
	    String line = reader.readLine();
	    while (line != null) {	    	
	    	content += line;
	        line = reader.readLine();
	    }
	    reader.close();
	    content += "EASTWeb teminated!";	
	    try
        {
	    	Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + ".\\eastweb.lock"));
            System.out.println("eastweb.lock has been deleted");
        }
        catch(NoSuchFileException e)
        {
            System.out.println("No such file/directory exists");
        }
	    return content;
	}	
}
