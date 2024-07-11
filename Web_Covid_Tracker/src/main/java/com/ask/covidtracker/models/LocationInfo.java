package com.ask.covidtracker.models;

/*
Here we are storing all the data which we can get by fetching it from the URL and then by reading 
from the .csv file.
 */

public class LocationInfo {

	private String state;
	private String country;
	private int latestCases;
	private int diffFromPreviousDay;

	public int getDiffFromPreviousDay() {
		return diffFromPreviousDay;
	}

	public void setDiffFromPreviousDay(int diffFromPreviousDay) {
		this.diffFromPreviousDay = diffFromPreviousDay;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getLatestCases() {
		return latestCases;
	}

	public void setLatestCases(int latestCases) {
		this.latestCases = latestCases;
	}

	@Override
	public String toString() {
		return "LocationInfo{" + "state='" + state + '\'' + ", country='" + country + '\'' + ", latestCases="
				+ latestCases + '}';
	}
}
