package com.example.demo.forecast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;

@Component
public class ForecastService {
	
	public String retrieve() throws IOException, InterruptedException {		
		return runModel();
	}
	public String runModel() throws IOException, InterruptedException {	
		
		//CHANGE TO YOU SCRIPT PATH
		String RPath = "D:\\Projects\\EPIDEMIA\\R\\test\\SDSU_run_models_rscript.R";
        File f = new File(RPath);
        String output;
        if (f.exists() && !f.isDirectory()) {
            //CHANGE TO YOUR RSCRIPT PATH
            String cmd = "C:\\Program Files\\R\\R-3.5.0\\bin\\Rscript";
            ProcessBuilder pb = new ProcessBuilder(cmd, RPath, "--save");
            pb.redirectOutput(Redirect.INHERIT);
            pb.redirectError(Redirect.INHERIT);
            Process p = pb.start();
            
            try (BufferedReader br = new BufferedReader(new java.io.InputStreamReader(p.getInputStream()))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String result = sb.toString();
                System.out.println(result);
                output = result;
                br.close();
            }
            p.waitFor();
            System.out.println("R model terminated.");
            output += "R model terminated.";
            try
            {
    	    	Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + ".\\forecast.lock"));
                System.out.println("forecast.lock has been deleted");
            }
            catch(NoSuchFileException e)
            {
                System.out.println("No such file/directory exists");
            }
            
            return output;
        } else {
            System.out.println("ERROR: Could not find " + RPath);
            return "ERROR: Could not find " + RPath;
        }		
	}
}
