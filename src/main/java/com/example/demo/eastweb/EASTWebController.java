package com.example.demo.eastweb;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class EASTWebController {
	//Auto wiring
	@Autowired
	private EASTWebService service;
	
	@RequestMapping("/eastweb")
	public String eastweb() throws IOException, InterruptedException {
		return service.retrieveMessage();
	}
}