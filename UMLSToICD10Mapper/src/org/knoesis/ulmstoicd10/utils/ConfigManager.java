package org.knoesis.ulmstoicd10.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class ConfigManager {
	private Properties prop = new Properties();
	
	/* HARD CODED PLEASE CHECK THIS LATER*/
	private String propertiesFile = "config.properties";
	private String propertiesFileFolder = "data";
	
	
	public ConfigManager(){
		try{
			prop.load(new FileInputStream(propertiesFileFolder + "/" + propertiesFile));
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getOntologyName(){
		return prop.getProperty("ontology.name");
	}
	
	public static void main(String[] args) {
		ConfigManager manager =  new ConfigManager();
		System.out.println(manager.getOntologyName());
	}
}
