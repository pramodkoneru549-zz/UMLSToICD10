package org.knoesis.umlstoicd10.utils;

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
	
	public String getOntologyFileLocation(){
		return propertiesFileFolder + "/" + getOntologyName();
	}
	
	public String getOntologyName(){
		return prop.getProperty("ontology.name");
	}

	public String getVirtuosoJdbcUrl() {
		return prop.getProperty("JdbcVirtuosoUrl");
	}

	public String getVirtuosoUsername() {
		return prop.getProperty("virtuoso.username");
	}

	public String getVirtuosoPassword() {
		return prop.getProperty("virtuoso.password");
	}

	public String getRdfGraphUrl() {
		return prop.getProperty("virtuoso.rdfGraph");
	}
	
	public String getInMemoryGraphName(){
		return prop.getProperty("inmemory.graphname");
	}

	public String getTripleFileLocation(){
		return prop.getProperty("inmemory.tripleFile");
	}
	
	public String getVirtusoUmlsJdbcUrl(){
		return prop.getProperty("virtuoso.umlsontology.jdbc");
	}
	
	public String getVirtuosoUmlsOntologyGraph(){
		return prop.getProperty("virtuoso.umlsontology.graph");
	}
	
	public static void main(String[] args) {
		ConfigManager manager =  new ConfigManager();
		System.out.println(manager.getOntologyName());
	}

}
