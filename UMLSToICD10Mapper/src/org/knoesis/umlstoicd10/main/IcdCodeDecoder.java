package org.knoesis.umlstoicd10.main;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.knoesis.umlstoicd10.ontology.OntologyLoader;
import org.knoesis.umlstoicd10.utils.ConfigManager;
import org.knoesis.umlstoicd10.utils.VirtuosoDBHandler;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;

/**
 * This class is used to decode the icd10 codes.
 * 
 * @author koneru
 *
 */
public class IcdCodeDecoder {
	
	private List<String> possibleIcdCodes;
	ConfigManager configParams;
	OntologyLoader ontology;
	VirtuosoDBHandler dbHandler;
	private static Log log = LogFactory.getLog(IcdCodeDecoder.class);
	
	/** Constructor intializes ontology loader and dbHandler*/
	public IcdCodeDecoder() {
		configParams = new ConfigManager();
		File ontologyFile = new File(configParams.getOntologyFileLocation());
		ontology = new OntologyLoader(ontologyFile);
		dbHandler = new VirtuosoDBHandler();
	}
	
	public void process(String strtCode){
		OWLClass owlClazz = ontology.getClass(strtCode);
		getICD10Codes(strtCode);
//		System.out.println(satisfiedClassCond(owlClazz));
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
		log.info("Entered this class "+ clazz.toString());
		/* Querying virtuoso to check if the pattern exists*/
		boolean patternAvailability = dbHandler.checkQueryPattern(sparqlQuery);
		if(patternAvailability == shouldPatternExist)
			conditionsSatisfied = true;
		
		return conditionsSatisfied;
	}
	
	/**
	 * This method, given the icd code of the starting class that we get from the index,
	 * traverses the tree (ontology here) checks for all the conditions in each and every class
	 * and returns the billable code (i.e., leaf node here).
	 * @param startingClassCode
	 * @return {@link OWLClass} Billable ICD10 class
	 */
	public OWLClass getICD10Codes(String startingClassCode){
		
		/* GETTING THE CLASS FROM STRING*/
		OWLClass strtClazz = ontology.getClass(startingClassCode);
		Boolean startClsCond = satisfiedClassCond(strtClazz);
		OWLClass icdCodeClass = strtClazz;
		if(startClsCond){
			/* Checking if it is the leaf node*/
			while(!ontology.isLeafNode(icdCodeClass)){
				NodeSet<OWLClass> subClz = ontology.getSubClasses(icdCodeClass);
				for(Node<OWLClass> node : subClz){
					OWLClass clz = node.getRepresentativeElement();
					log.info("Entered the following class "+ clz.toString());
					if(satisfiedClassCond(clz))
					{
						icdCodeClass = clz;
						break;
					}
				}
				/* This means that it has checked all the subclasses and none of them satisfies the cond*/
				if(strtClazz.equals(icdCodeClass))
					break;
			}
			System.out.println("This is the billable ICD Code " + icdCodeClass.toString());
		}
		return icdCodeClass;
	}
	
	public List<String> getPossibleIcdCodes() {
		return possibleIcdCodes;
	}

	public void setPossibleIcdCodes(List<String> possibleIcdCodes) {
		this.possibleIcdCodes = possibleIcdCodes;
	}
	
	public static void main(String[] args) {
		IcdCodeDecoder decoder = new IcdCodeDecoder();
		decoder.process("E08");
	}
}
