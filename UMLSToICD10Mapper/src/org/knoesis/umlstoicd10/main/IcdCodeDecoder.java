package org.knoesis.umlstoicd10.main;

import java.io.File;
import java.util.List;

import org.knoesis.umlstoicd10.ontology.OntologyLoader;
import org.knoesis.umlstoicd10.utils.ConfigManager;
import org.knoesis.umlstoicd10.utils.VirtuosoDBHandler;
import org.semanticweb.owlapi.model.OWLClass;

public class IcdCodeDecoder {
	
	private List<String> possibleIcdCodes;
	ConfigManager configParams;
	OntologyLoader ontology;
	VirtuosoDBHandler dbHandler;
	
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
