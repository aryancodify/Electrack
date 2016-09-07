package com.electra.utils;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ElectraUtils {

	public static LocalDate getDate(){
		return LocalDateTime.now(Clock.systemDefaultZone()).plusDays(1).toLocalDate();
	}
}
