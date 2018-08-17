package com.example.demo.epidata;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EpidataController {
	
	@Autowired
	private EpidataService service;
	
	@RequestMapping("/epidata")
	public String epidata() throws IOException, InterruptedException {
		return service.retrieveURL();
	}
}
