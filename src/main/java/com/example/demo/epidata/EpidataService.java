package com.example.demo.epidata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;

@Component
public class EpidataService {
	
	public String retrieveURL() throws IOException{
		try
        {
	    	Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + ".\\epidata.lock"));
            System.out.println("epidata.lock has been deleted");
        }
        catch(NoSuchFileException e)
        {
            System.out.println("No such file/directory exists");
        }
		return "Pleast upload your malaria data to https://epidemia.sdstate.edu/upload/.";
	}
	
}
