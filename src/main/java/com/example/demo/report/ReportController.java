package com.example.demo.report;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {
	@Autowired
	private ReportService service;
	
	@RequestMapping("/report")
	public String report() throws IOException, InterruptedException {
		return service.retrieve();
	}
}
