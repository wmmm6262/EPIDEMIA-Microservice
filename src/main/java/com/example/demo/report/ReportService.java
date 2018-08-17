package com.example.demo.report;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;

@Component
public class ReportService {

	String output;
	public String retrieve() throws IOException, InterruptedException {
		runRNW();		
		return output;
	}
	public void runRNW() throws IOException, InterruptedException {
		//CHANGE TO YOUR RNW FILE PATH
		String rnwPath = "D:\\Projects\\EPIDEMIA\\R\\epidemia_sdsu\\report\\SDSU_epidemia_report.Rnw";
        File f = new File(rnwPath);
        if (f.exists() && !f.isDirectory()) {
			//CHANGE TO YOU RSCRIPT PATH AND RNW PATH
            ProcessBuilder rnwPb = new ProcessBuilder("C:\\Program Files\\R\\R-3.5.0\\bin\\Rscript", "-e", "\"library(knitr); knit('D:/Projects/EPIDEMIA/R/epidemia_sdsu/report/SDSU_epidemia_report.Rnw')\"");
            //CHANGE TO YOUR SAVED DIRECTORY
            rnwPb.directory(new File("D:/Projects/EPIDEMIA/R/test"));
            rnwPb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            rnwPb.redirectError(ProcessBuilder.Redirect.INHERIT);
            Process rnw = rnwPb.start();

            try (BufferedReader br = new BufferedReader(new java.io.InputStreamReader(rnw.getInputStream()))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String result = sb.toString();
                System.out.println(result);
                br.close();
            }
            rnw.waitFor();
            toPDF();
        } else {
            System.out.println("ERROR: Could not find " + rnwPath);
            output = "ERROR: Could not find " + rnwPath;
        }
	}
	public void toPDF() throws IOException, InterruptedException {
		System.out.println("Generating PDF report...");
		//CHANGE TO YOUR TEX FILE PATH
        String texPath = "D:/Projects/EPIDEMIA/R/test/SDSU_epidemia_report.tex";
        File f = new File(texPath);
        if (f.exists() && !f.isDirectory()) {
        	//CHANGE TO YOUR PDFLATEX AND TEX FILE PATHS
            ProcessBuilder pdfPb = new ProcessBuilder("C:\\Program Files\\MiKTeX 2.9\\miktex\\bin\\x64\\pdflatex", "D:/Projects/EPIDEMIA/R/test/SDSU_epidemia_report.tex");
            pdfPb.directory(new File("D:/Projects/EPIDEMIA/R/test"));
            pdfPb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            pdfPb.redirectError(ProcessBuilder.Redirect.INHERIT);
            Process pdf = pdfPb.start();

            try (BufferedReader br = new BufferedReader(new java.io.InputStreamReader(pdf.getInputStream()))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String result = sb.toString();
                System.out.println(result);
                br.close();
            }
            pdf.waitFor();
            
            System.out.println("Please find the report in D:/Projects/EPIDEMIA/R/test.");
            output = "The PDF report is generated in D:/Projects/EPIDEMIA/R/test.";
            
            try
            {
    	    	Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + ".\\report.lock"));
                System.out.println("report.lock has been deleted");
            }
            catch(NoSuchFileException e)
            {
                System.out.println("No such file/directory exists");
            }
        } else {
            System.out.println("ERROR: Could not find " + texPath);
            output = "ERROR: Could not find " + texPath;
        }
	}

}
