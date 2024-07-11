package com.ask.covidtracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ask.covidtracker.models.LocationInfo;
import com.ask.covidtracker.services.CoronaVirusDataService;

import java.util.List;

/* 
1. view the details in html page and access the home url. 
2. run html code here and let it open as UI in browser
3. Here we are returning a name which points to a template.
4. we show all the data like province, country and cases by getting the value from service class and place that in model.
*/

@Controller
public class HomeController {

	@Autowired
	CoronaVirusDataService coronaVirusDataService;

	// this method getted mapped to home.html file
	@GetMapping("/")
	public String home(Model model) {
		List<LocationInfo> allLocation = coronaVirusDataService.getAllLocation();
		// converting the list into a stream, mapping the integer and accumulating the values
		int TotalCases = allLocation.stream().mapToInt(LocationInfo::getLatestCases).sum();
		int TotalNewCases = allLocation.stream().mapToInt(LocationInfo::getDiffFromPreviousDay).sum();

		model.addAttribute("LocationStats", allLocation);
		model.addAttribute("TotalCases", TotalCases);
		model.addAttribute("TotalNewCases", TotalNewCases);
		return "home";
	}
}
