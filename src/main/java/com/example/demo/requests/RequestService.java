package com.example.demo.requests;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.nio.file.FileSystems;
import java.nio.file.LinkOption;
import java.nio.file.Path;

@Component
public class RequestService {
	
	public String parse(String serviceName) {
	//public static void main(String argv[]) {
		try {
		    File fXmlFile = new File(System.getProperty("user.dir") + "\\src\\main\\java\\com\\example\\demo\\requests\\"+ "Requests.xml");
		    
		    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		    Document doc = dBuilder.parse(fXmlFile);
		    doc.getDocumentElement().normalize();
		    
		    NodeList requestList = doc.getElementsByTagName("request");		    
		    
		    for (int temp = 0; temp < requestList.getLength(); temp++) {		    	
		        Node nNode = requestList.item(temp);
		        //System.out.println("\nCurrent Element :" + nNode.getNodeName());
		        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		            Element eElement = (Element) nNode;		
		            //read the title from requests.xml
		            String title = eElement.getElementsByTagName("Title").item(0).getTextContent();	
		            System.out.println("\nCurrent Title :" + title);
		            
		            if (title.equals(serviceName)) {
		            	sendRequest(eElement);
		            	break;
		            }		            
		        }
		    }
		    
		    // for test
		    String serviceName1 = "epidata_update";
		    String serviceName2 = "envdata_update";
		    String serviceName3 = "data_update";
		    String serviceName4 = "envdata_transfer";
		    String serviceName5 = "report_generate";
		    
		    
		    //RequestService rs = new RequestService();
		    //rs.access(ms);
		    
		    //access(ms);
		    return "Your request is processed.";
		    
		} catch (Exception e) {
			e.printStackTrace();
			
			return "Error";
		}
		 
	}
	// send the request to the ms
	public void sendRequest(Element eElement) throws Exception {
		
		String pattern = eElement.getElementsByTagName("pattern").item(0).getTextContent();	
		//String title = eElement.getElementsByTagName("Title").item(0).getTextContent();	
		//HashMap<String, NodeList> map = new HashMap<>();
		if (pattern.equals("1")) {
        	NodeList msList =  eElement.getElementsByTagName("microservice");		           	            
            //map.put(title, msList);	
            List<String> services = new ArrayList<>();
            for (int i = 0; i<msList.getLength(); i++) {
    			String service = msList.item(i).getTextContent();
            	System.out.println("microservice : " + service);	
            	services.add(service);			            	
            }
            //RequestService rs = new RequestService();
        	//rs.callMSByOrder(services);
            
        	callMSByOrder(services);
        }else if(pattern.equals("2")) {
        	NodeList msList =  eElement.getElementsByTagName("microservice");		           	            
            //map.put(title, msList);	
            for (int i = 0; i<msList.getLength(); i++) {
    			String service = msList.item(i).getTextContent();
            	System.out.println("microservice : " + service);	
            	
            	//RequestService rs = new RequestService();
            	//rs.invoke(service);
            	
            	invoke(service);
            }
        }else if(pattern.equals("3")) {
        	NodeList levelList =  eElement.getElementsByTagName("level");	
        	for (int i = 0; i<levelList.getLength(); i++) {
        		Node n = levelList.item(i);
        		if (n.getNodeType() == Node.ELEMENT_NODE) {
        			Element e = (Element) n;
        			String level =  e.getAttribute("id");
        			System.out.println("Level: " + level);
        			
        			//RequestService rs = new RequestService();
        			//rs.hybrid(e);  
        			
        			hybrid(e);
        		}
        	}        	
        }
	}
	// invoke the ms in the services list by order	
		public void callMSByOrder(List<String> services) throws Exception {
			if (services.isEmpty()) {
				return;
			}
			invoke(services.get(0));			
			
			String lockFile;
			Path path;
			boolean exists;
			
			for (int i = 1; i<services.size(); i++) {
				lockFile = ".\\"+services.get(i-1)+".lock";
				path = FileSystems.getDefault().getPath(System.getProperty("user.dir"), lockFile);
	    		exists = Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
	    		if (!exists) {
	    			System.out.println("no "+lockFile);
	    		}
	    		while (exists) {
	    			Thread.sleep(3000);
	    			System.out.println(lockFile + " is locked");
	    			exists = Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
	    		}
	    		invoke(services.get(i));
			}
		}
	// implement hybrid pattern
	public void hybrid(Element e) throws Exception {
		NodeList childList = e.getChildNodes();	
		List<String> services = new ArrayList<>();
		for (int i=0; i<childList.getLength(); i++) {
			Node n = childList.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				// if parent node
				if (n.getNodeName().equals("parent")) {
					System.out.println(n.getTextContent());
					services.add(n.getTextContent());
				//if microservice node
				}else {
					services.add(n.getTextContent());
					callMSByParent(services);
					services.clear();
				}
			}
		}
	} 
	
	// invoke the ms in the services list by checking its parents
	public void callMSByParent(List<String> services) throws Exception {
		if (services.size() == 1) {
			invoke(services.get(0));
		}else {
			int i;
			String lockFile;
			Path path;
			boolean exists;
			for (i = 0; i<services.size()-1; i++) {
				lockFile = ".\\"+services.get(i)+".lock";
				path = FileSystems.getDefault().getPath(System.getProperty("user.dir"), lockFile);
	    		exists = Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
	    		if (!exists) {
	    			System.out.println("no "+lockFile);
	    		}
	    		while (exists) {
	    			Thread.sleep(1000);
	    			System.out.println(lockFile + " is locked");
	    			exists = Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
	    		};
			}
			invoke(services.get(i));
		}
	}
	

	// invoke the service by the http api
	public void invoke(String service) throws Exception {
		switch(service) {
    	case "epidata":
    		File file1 = new File(System.getProperty("user.dir") + ".\\epidata.lock");
    		if (!file1.createNewFile()){	        
    	        System.out.println("File already exists.");
    	     }
    		execute("http://localhost:8080/epidata");
    		break;
    	
    	case "eastweb":
    		File file2 = new File(System.getProperty("user.dir") + ".\\eastweb.lock");
    		if (!file2.createNewFile()){	        
    	        System.out.println("File already exists.");
    	     }
    		execute("http://localhost:8080/eastweb");
    		break;
    		
    	case "envDataTsf":
    		File file3 = new File(System.getProperty("user.dir") + ".\\envDataTsf.lock");
    		if (!file3.createNewFile()){	        
    	        System.out.println("File already exists.");
    	     }
    		execute("http://localhost:8080/transfer");
    		break;
    		
    	case "forecast":
    		File file4 = new File(System.getProperty("user.dir") + ".\\forecast.lock");
    		if (!file4.createNewFile()){	        
    	        System.out.println("File already exists.");
    	     }
    		execute("http://localhost:8080/forecast");
    		break;
    		
    	case "report":
    		File file5 = new File(System.getProperty("user.dir") + ".\\report.lock");
    		if (!file5.createNewFile()){	        
    	        System.out.println("File already exists.");
    	     }
    		execute("http://localhost:8080/report");
    		break;
    		
    	default: 
    		execute("http://localhost:8080/request");
    		break;	        		
    	}
			
	}
	//execute the service through the url
	public void execute(String url) {
		Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	/*
	public void access(NodeList ms) throws Exception  {	
		String parent = "";
		// run related microservices
		for (int i = 0; i<ms.getLength(); i++) {
			String service = ms.item(i).getTextContent();
        	System.out.println("microservice : " + service);	
        	Node n = ms.item(i);
        	Element e = (Element) n;
        	String level = "";
        	if (n.getNodeType() == Node.ELEMENT_NODE) {
        		level = e.getAttribute("level");
        		System.out.println("\nCurrent level :" + level);
        	}
        	       	
        	if (level.equals("0") ) {
        		invoke(service);         		
        	}else{        		
        		//wait(service, parent, level);
        	}    
        	parent = service;
        }				
	}
	
	// wait until the calling service is done
	 public void wait111(String service, String parent) throws Exception{
		Path path;
		boolean exists;
		switch(parent) {
    	case "epidata":
    		path = FileSystems.getDefault().getPath(System.getProperty("user.dir"), ".\\epidata.lock");
    		exists = Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
    		while (exists) {
    			Thread.sleep(1000);
    			exists = Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
    		};
    		invoke(service);
    		break;
    	
    	case "eastweb": 
    		path = FileSystems.getDefault().getPath(System.getProperty("user.dir"), ".\\eastweb.lock");
    		exists = Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
    		while (exists) {
    			Thread.sleep(5000);
    			exists = Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
    		};
    		invoke(service);
    		break;
    	case "envDataTsf":
    		path = FileSystems.getDefault().getPath(System.getProperty("user.dir"), ".\\envDataTsf.lock");
    		exists = Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
    		while (exists) {
    			Thread.sleep(5000);
    			exists = Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
    		};
    		invoke(service);
    		break;
    		
    	case "forecast":
    		path = FileSystems.getDefault().getPath(System.getProperty("user.dir"), ".\\forecast.lock");
    		exists = Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
    		while (exists) {
    			Thread.sleep(5000);
    			exists = Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
    		};
    		invoke(service);
    		break;
    		
    	case "report":
    		path = FileSystems.getDefault().getPath(System.getProperty("user.dir"), ".\\report.lock");
    		exists = Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
    		while (exists) {
    			Thread.sleep(5000);
    			exists = Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
    		};
    		invoke(service);
    		break;    		

    	default: 
    		break;       		
    	}
	}*/
}
