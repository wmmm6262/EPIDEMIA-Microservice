package com.example.demo.forecast;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForecastController {
	@Autowired
	private ForecastService service;
	
	@RequestMapping("/forecast")
	public String forecast() throws IOException, InterruptedException {
		return service.retrieve();
	}
}
