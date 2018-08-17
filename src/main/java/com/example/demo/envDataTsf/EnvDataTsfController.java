package com.example.demo.envDataTsf;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnvDataTsfController {
	
	@Autowired
	private EnvDataTsfService service;
		
	@RequestMapping("/transfer")
	public String epidata() throws IOException, InterruptedException {
		return service.retrieve();
	}
}
