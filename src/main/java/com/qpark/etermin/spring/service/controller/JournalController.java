package com.qpark.etermin.spring.service.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.qpark.etermin.ETerminService;
import com.qpark.etermin.entity.JournalEntry;

@RestController
@EnableWebMvc 
public class JournalController {
	
	private static Logger logger = LoggerFactory.getLogger(JournalController.class);

	@RequestMapping(path = "/ping", method = RequestMethod.GET)
    public Map<String, String> ping() {
        Map<String, String> pong = new HashMap<>();
        pong.put("pong", "Hello, World!");
        return pong;
    }
	
	@CrossOrigin(origins = "*")
	@RequestMapping(path = "/journal", method = RequestMethod.GET)
	public List<JournalEntry> journal(@RequestParam String startDate, @RequestParam String endDate){
		
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("X-Custom-Header", "application/json");
		headers.put("charset", "utf-8");
		//TODO CORS - to be set to AWS
		headers.put("Access-Control-Allow-Origin", "*");
		
		ETerminService ets = new ETerminService();
		List<JournalEntry> jeList = new ArrayList<JournalEntry>();
		
		LocalDate start = LocalDate.parse(startDate);
		LocalDate end = LocalDate.parse(endDate);
		
		if (start.isAfter(end)) {
			logger.error("Start after end: '{}' '{}'", start, end);
			return jeList;
		}
		
		try {		
			jeList = ets.getJournalEntry(start, end);
		} catch (IOException e) {
			logger.error("Errror accessing eTermin: ",e);
		}
		return jeList;
	}
	
}
