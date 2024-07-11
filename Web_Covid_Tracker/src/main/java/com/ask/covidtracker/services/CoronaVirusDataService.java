package com.ask.covidtracker.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ask.covidtracker.models.LocationInfo;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/*
 When the application is launched, we need to get some code to run that calls the URL that uses to get the 
 covid confirmed cases and retrieve the data. This class will provide us with data, and when the application
 loads, it will make a call to code, which will then fetch the data.
 */

@Service
public class CoronaVirusDataService {

	/*
	  This is a.csv file that has all of the confirmed cases up to the most recent data from all of the 
	  different sites.
	 */
	private static String URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

	
	/*
	 constructing an ArrayList of the location info class so that we may add information to it every time 
	 we get a request after making an instance of it
	 */
	private List<LocationInfo> allLocation = new ArrayList<>();

	public List<LocationInfo> getAllLocation() {
		return allLocation;
	}

	
	/*
	 @PostConstruct to a method that must be executed after dependency injection has been 
	 completed in order to do any initialization. 
	 */
	@PostConstruct 
	
	/*
	 When we want to schedule this function so that it runs after a certain amount of time and 
	 obtains the latest data.
	 */
	@Scheduled(cron = "* * 1 * * *")
	public void fetchVirusData() throws IOException, InterruptedException {

		// new list if the existing data is altered or modified
		List<LocationInfo> newLocation = new ArrayList<>();

		// to make http request we should define its own client
		HttpClient client = HttpClient.newHttpClient();
		// where to do http request
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL)).build();

		/*
		  now we send request to the client and get a response that returns a string 
		 */
		HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		/*
		 In some CSV files, the first record specifies the header name. Apache Commons CSV may parse the header 
		 names from the first record if set. When iterating, the values from the first record will be used as 
		 header names, and the first record will be ignored.
		 */
		// this is an instance of reader that passes a string
		StringReader csvBodyReader = new StringReader(httpResponse.body());
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
		
		/*
		 We loop through the records, which were the columns returned when the data was fetched via an HTTP request.
		 Save the data in a model class so we can access it more easily. 
		 */
		for (CSVRecord record : records) {
			LocationInfo locationInfo = new LocationInfo();
			locationInfo.setState(record.get("Province/State"));
            locationInfo.setCountry(record.get("Country/Region"));
			int latestNoCases = Integer.parseInt(record.get(record.size() - 1));
			int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
			// displays the latest updated column
			locationInfo.setLatestCases(latestNoCases);
			locationInfo.setDiffFromPreviousDay(latestNoCases - prevDayCases);
			newLocation.add(locationInfo);
		}
		this.allLocation = newLocation;

	}
}
