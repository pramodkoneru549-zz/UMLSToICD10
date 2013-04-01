package org.knoesis.umlstoicd10.main;

import java.io.File;
import java.util.List;

import org.knoesis.umlstoicd10.ontology.OntologyLoader;
import org.knoesis.umlstoicd10.utils.ConfigManager;
import org.knoesis.umlstoicd10.utils.VirtuosoDBHandler;
import org.semanticweb.owlapi.model.OWLClass;

/**
 * This class is used to decode the icd10 codes 
 * @author koneru
 *
 */
public class IcdCodeDecoder {
	
	private List<String> possibleIcdCodes;
	ConfigManager configParams;
	OntologyLoader ontology;
	VirtuosoDBHandler dbHandler;
	
	/** Constructor intializes ontology loader and dbHandler*/
	public IcdCodeDecoder() {
		configParams = new ConfigManager();
		File ontologyFile = new File(configParams.getOntologyFileLocation());
		ontology = new OntologyLoader(ontologyFile);
		dbHandler = new VirtuosoDBHandler();
	}
	
	public void process(String strtCode){
		OWLClass owlClazz = ontology.getClass(strtCode);
		String sparqlQuery = ontology.getSparqlQueryFromComment(owlClazz);
		boolean checkStatus = dbHandler.checkQueryPattern(sparqlQuery);
		System.out.println(checkStatus);
	}
	
	/**
	 * This method is used to conform the icd10 code. First it fires ASK sparql query, which is 
	 * stored as a 'comment' annotation and then it checks whether returned boolean equals to the boolean
	 * stored in 'isDefinedBy' annotation.
	 * 
	 * If these two are equal it means that all the conditions are met. Hence it passed the test for that class, thereby
	 * ICD10 code.
	 * @param clazz
	 * @return {@link Boolean} tells whether the code met the requirements.
	 */
	public Boolean satisfiedClassCond(OWLClass clazz){
		boolean conditionsSatisfied = false;
		boolean shouldPatternExist = ontology.getBooleanConditionForSparqlQuery(clazz);
		String sparqlQuery = ontology.getSparqlQueryFromComment(clazz);
		
		/* Querying virtuoso to check if the pattern exists*/
		boolean patternAvailability = dbHandler.checkQueryPattern(sparqlQuery);
		if(patternAvailability == shouldPatternExist)
			conditionsSatisfied = true;
		
		return conditionsSatisfied;
	}
	public List<String> getPossibleIcdCodes() {
		return possibleIcdCodes;
	}

	public void setPossibleIcdCodes(List<String> possibleIcdCodes) {
		this.possibleIcdCodes = possibleIcdCodes;
	}
	
	public static void main(String[] args) {
		IcdCodeDecoder decoder = new IcdCodeDecoder();
		decoder.process("E08.0");
	}
}
