package com.phonebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@SpringBootApplication
public class PhonebookApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(PhonebookApplication.class);
		application.setAddCommandLineProperties(true);
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(new File(args[0])));
			application.setDefaultProperties(properties);
			application.setAdditionalProfiles(properties.getProperty("dataStorage.type"));
			application.run(args);
		} catch (IOException e) {
			System.out.println("Wrong path to .properties file!");
			e.printStackTrace();
		}
	}

}
