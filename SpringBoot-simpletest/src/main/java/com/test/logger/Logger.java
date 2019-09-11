package com.test.logger;

import org.springframework.stereotype.Component;

@Component
public class Logger {
	public void log(Object content) {
		System.out.println("LOGGER --- " + content);
	}
}
