package org.knoesis.umlstoicd10.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.knoesis.umlstoicd10.db.VirtuosoDBHandler;
import org.knoesis.umlstoicd10.models.Triple;

/**
 * This class is used to load patient graph into virtuoso from the file.
 * @author koneru
 *
 */
public class PatientGraphLoader {
	ConfigManager configParams;
	String patientTripleFileLocation;
	VirtuosoDBHandler dbHandler;
	private Set<String> chiefComplaints;
	
	public PatientGraphLoader() {
		configParams = new ConfigManager();
		patientTripleFileLocation = configParams.getTripleFileLocation();
		dbHandler = new VirtuosoDBHandler();
	}
	
	public PatientGraphLoader(ConfigManager configParams,VirtuosoDBHandler dbHandler){
		this.configParams = configParams;
		patientTripleFileLocation = configParams.getTripleFileLocation();
		this.dbHandler = dbHandler;
	}
	
	public PatientGraphLoader(String tripleFileLocation){
		patientTripleFileLocation = tripleFileLocation;
		dbHandler = new VirtuosoDBHandler();
	}
	
	/**
	 * This method is used to load the triples from the file. It also sets the 
	 * chief complaints which are in the first line of the file.
	 */
	public void load(){
		System.out.println("Reading the file......");
		File triplesFile = new File(patientTripleFileLocation);
		try {
			FileReader reader = new FileReader(triplesFile);
			BufferedReader bufferReader = new BufferedReader(reader);
			
			/** The first line of the file has the comma separated list of chief complaints*/
			String chiefComplaintsLine = bufferReader.readLine();
			/**Setting the chief complaints*/
			setChiefComplaints(commaSeperatedToSet(chiefComplaintsLine));
			
			String tripleLine = bufferReader.readLine();
			while(tripleLine != null){
				/** Each node is seperated by space*/
				String[] nodes = tripleLine.split(" ");
				Triple rdfTriple = new Triple(nodes[0], nodes[1], nodes[2]);
				dbHandler.addPatientTriplesIntoVirtuoso(rdfTriple);
				tripleLine = bufferReader.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(chiefComplaints);
		System.out.println("Completed loading the file");
	}
	
	/**
	 * Transforms the comma separated words into a set
	 * 
	 * @param csw -- comma separated words
	 * @return {@link Set} of chief complaints (Strings)
	 */
	private Set<String> commaSeperatedToSet(String csw) {
		String[] wordArray = csw.split(",");
		Set<String> wordSet = new HashSet<String>();
		
		/*Trimming the elements of the array*/
		for (int i = 0; i < wordArray.length; i++) {
			wordSet.add(wordArray[i].trim().toLowerCase());
		}
		return wordSet;
	}

	public Set<String> getChiefComplaints() {
		return chiefComplaints;
	}

	public void setChiefComplaints(Set<String> chiefComplaints) {
		this.chiefComplaints = chiefComplaints;
	}
	
	public static void main(String[] args) {
		PatientGraphLoader loader = new PatientGraphLoader();
		loader.load();
		System.out.println(loader.getChiefComplaints());
	}
}
