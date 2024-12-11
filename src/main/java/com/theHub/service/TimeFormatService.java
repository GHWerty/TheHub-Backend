package com.theHub.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import java.time.Duration;

import org.springframework.stereotype.Service;

@Service
public class TimeFormatService {
	
	public String calculateTimeElapsed(Instant postDate) {
	    String timeElapsed = "";

	    Instant currentInstant = Instant.now();
	    Duration duration = Duration.between(postDate, currentInstant);
	    long secondsDifference = duration.getSeconds();
	    
	    if (secondsDifference < 60) {
	        timeElapsed = secondsDifference + "s";
	    } else if (secondsDifference < 60 * 60) {
	        long minutesDifference = duration.toMinutes();
	        timeElapsed = minutesDifference + "m";
	    } else if (secondsDifference < 60 * 60 * 24) {
	        long hoursDifference = duration.toHours();
	        timeElapsed = hoursDifference + "h";
	    } else if (secondsDifference < 60 * 60 * 24 * 7) {
	        long daysDifference = duration.toDays();
	        timeElapsed = daysDifference + "d";
	    } else if (secondsDifference < 60 * 60 * 24 * 364) {
	        LocalDate postDateFormated = postDate.atZone(ZoneOffset.UTC).toLocalDate();
	        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("dd - MMM");
	        timeElapsed = postDateFormated.format(monthFormatter);
	    } else {
	        LocalDate postDateFormated = postDate.atZone(ZoneOffset.UTC).toLocalDate();
	        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("MMM - yyyy");
	        timeElapsed = postDateFormated.format(yearFormatter);
	    }

	    return timeElapsed;
	}
}
