package com.example.demo.requests;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestsController {
	@Autowired
	private RequestService service;
	
	@RequestMapping("/epidata_update")
	public String epidata_update() throws IOException, InterruptedException {
		return service.parse("epidata_update");
	}
	
	@RequestMapping("/envdata_update")
	public String envdata_update() throws IOException, InterruptedException {
		return service.parse("envdata_update");
	}
	
	@RequestMapping("/data_update")
	public String data_update() throws IOException, InterruptedException {
		return service.parse("data_update");
	}
	
	@RequestMapping("/envdata_transfer")
	public String envdata_transfer() throws IOException, InterruptedException {
		return service.parse("envdata_transfer");
	}
	
	@RequestMapping("/report_generate")
	public String report_generate() throws IOException, InterruptedException {
		return service.parse("report_generate");
	}	
}
