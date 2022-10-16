package com.thesis.tattootopy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class TattootopyApplication {

	public static List<Thread> threadList = new ArrayList<>();

	public static void main(String[] args) {
		SpringApplication.run(TattootopyApplication.class, args);

	}

}
