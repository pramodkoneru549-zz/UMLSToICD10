package org.knoesis.umlstoicd10.utils;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.knoesis.umlstoicd10.models.Triple;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

public class VirtuosoDBHandler {
	ConfigManager configParams;
	String username, password;
	String virtuosoJdbcUrl;
	String rdfGraphUrl;
	private VirtGraph graphConnection;
	private static Log log = LogFactory.getLog(VirtuosoDBHandler.class);
	
	public VirtGraph getGraphConnection() {
		return graphConnection;
	}

	public void setGraphConnection(VirtGraph graphConnection) {
		this.graphConnection = graphConnection;
	}

	public VirtuosoDBHandler() {
		configParams = new ConfigManager();
//		virtuosoJdbcUrl = configParams.getVirtuosoJdbcUrl();
		virtuosoJdbcUrl = configParams.getVirtusoUmlsJdbcUrl();
//		username = configParams.getVirtuosoUsername();
//		password = configParams.getVirtuosoPassword();
//		rdfGraphUrl = configParams.getRdfGraphUrl();
		username = configParams.getVirtuosoUmlsUsername();
		password = configParams.getVirtuosoUmlsPassword();
//		rdfGraphUrl = configParams.getVirtuosoUmlsOntologyGraph();
		graphConnection = new VirtGraph (virtuosoJdbcUrl, username, password);
	}
	
	/**
	 * This method is used basically to execute ask sparql query on the dataset.
	 * @param askQuery
	 * @return {@link Boolean} value saying whether that pattern exists in the graph or not.
	 */
	public boolean checkQueryPattern(String askQuery){
		Boolean status = false;
		Query sparql = QueryFactory.create(askQuery);
		log.info(sparql.toString());
		VirtuosoQueryExecution queryExecutor = VirtuosoQueryExecutionFactory.create(sparql, graphConnection);
		status = queryExecutor.execAsk();
		return status;
	}
	
	/**
	 * This method is used to add triple into virtuoso DB
	 * @param triple
	 */
	public void addTripleIntoVirtuoso(Triple triple){
		Node sub = Node.createURI(triple.getSubject());
		Node prdct = Node.createURI(triple.getPredicate());
		Node obj = Node.createURI(triple.getObject());
		com.hp.hpl.jena.graph.Triple jenaTriple = new com.hp.hpl.jena.graph.Triple(sub,prdct,obj);
		graphConnection.add(jenaTriple);
	}
	
	public static void main(String[] args) {
		VirtuosoDBHandler dbHandler = new VirtuosoDBHandler();
//		<http://ww.knoesis.org/icd10mapping/patient> <http://ww.knoesis.org/icd10mapping/suffering_with> <http://ww.knoesis.org/icd10mapping/umls/c03>
		String sub = "http://www.knoesis.org/icd10mapping/patient";
		String pre = "http://www.knoesis.org/icd10mapping/suffering_with";
		String obj = "http://www.ezdi.us/cardio.owl#C1720567";
		
		String askQuery = "PREFIX icd: <http://www.knoesis.org/icd10mapping/>"+
						  "PREFIX ezdi: <http://www.ezdi.us/cardio.owl#>"+
						  "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
						  "ASK " +
						  "FROM NAMED <http://knoesis.wright.edu/healthcare>"+
						  "FROM NAMED <http://knoesis.wright.edu/umls2icd10/sample>"+
						  "{"+
						  "{GRAPH <http://knoesis.wright.edu/healthcare> {?x rdfs:subClassOf ezdi:C0020617.}}"+
						  "{GRAPH <http://knoesis.wright.edu/umls2icd10/sample> {"+
						  "{icd:patient icd:suffering_with ?x.}"+
						   "UNION"+
						  "{icd:patient icd:suffering_with ezdi:C0020617.}}"+
						   "}"+
						   "UNION"+
						  "{GRAPH <http://knoesis.wright.edu/umls2icd10/sample> {"+
						  "{icd:patient icd:suffering_with ?x.}"+
						  "UNION"+
						  "{icd:patient icd:suffering_with ezdi:C0020617.}}"+
						"}"+
				"}";
		
		String testQuery = "PREFIX icd: <http://www.knoesis.org/icd10mapping/>"+
						   "PREFIX ezdi: <http://www.ezdi.us/cardio.owl#>"+
						   "SELECT * FROM NAMED <http://knoesis.wright.edu/umls2icd10/sample> { GRAPH <http://knoesis.wright.edu/umls2icd10/sample> {?x icd:suffering_with ezdi:C0259752.}}";	
//		Triple triple = new Triple(sub, pre, obj);
//		dbHandler.addTripleIntoVirtuoso(triple);
//		System.out.println("Triple "+ triple+ " added");
		System.out.println(dbHandler.checkQueryPattern(askQuery));
	}
}
