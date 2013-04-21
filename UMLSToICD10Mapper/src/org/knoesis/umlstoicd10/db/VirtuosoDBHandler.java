package org.knoesis.umlstoicd10.db;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.knoesis.umlstoicd10.models.Triple;
import org.knoesis.umlstoicd10.utils.ConfigManager;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

import virtuoso.jena.driver.VirtGraph;

/**
 * This class deals with all the things related to virtuoso like querying, adding triples etc
 * @author koneru
 *
 */
public class VirtuosoDBHandler {
	ConfigManager configParams;
	String username, password;
	String virtuosoJdbcUrl;
	String rdfGraphUrl;
	private VirtGraph graphConnection;
	private String sparqlEndpoint;
	private static Log log = LogFactory.getLog(VirtuosoDBHandler.class);
	
	public VirtGraph getGraphConnection() {
		return graphConnection;
	}

	public void setGraphConnection(VirtGraph graphConnection) {
		this.graphConnection = graphConnection;
	}
	
	/** DEFAULT constructor gets all the parameters to connect to virtuoso*/
	public VirtuosoDBHandler() {
		configParams = new ConfigManager();
		virtuosoJdbcUrl = configParams.getVirtusoUmlsJdbcUrl();
		username = configParams.getVirtuosoUmlsUsername();
		password = configParams.getVirtuosoUmlsPassword();
		rdfGraphUrl = configParams.getPatientGraphUrl();
		sparqlEndpoint = configParams.getSparqlEndpoint();
		graphConnection = new VirtGraph (rdfGraphUrl,virtuosoJdbcUrl, username, password);
	}
	
	/**
	 * This method is used basically to execute ask sparql query on the dataset.
	 * @param askQuery
	 * @return {@link Boolean} value saying whether that pattern exists in the graph or not.
	 */
	public boolean checkQueryPattern(String askQuery){
		Boolean status = false;
		
		/** USED this way before but jena is unable to parse SPARQL queries which involves querying from two graphs*/
//		Query sparql = QueryFactory.create(askQuery);
//		VirtuosoQueryExecution queryExecutor = VirtuosoQueryExecutionFactory.create(sparql, graphConnection);
		log.info("ASK query " + askQuery);
		QueryEngineHTTP queryEngine = new QueryEngineHTTP(sparqlEndpoint, askQuery);
		status = queryEngine.execAsk();
		return status;
	}
	
	/**
	 * This method is used to add triple into virtuoso DB
	 * @param triple
	 */
	public void addPatientTriplesIntoVirtuoso(Triple triple){
		Node sub = Node.createURI(triple.getSubject());
		Node prdct = Node.createURI(triple.getPredicate());
		Node obj = Node.createURI(triple.getObject());
		
		// Converting our triple to jena triple -- Better way would be to completely use jena triple.
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
		
		String subQuery = "PREFIX icd: <http://www.knoesis.org/icd10mapping/>"+
						  "PREFIX ezdi: <http://www.ezdi.us/cardio.owl#>"+
						  "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
						  "ASK "+
						  "FROM <http://knoesis.wright.edu/umls2icd10/sample>"+
						  "WHERE"+
						  "{"+
						  "{"+
						  "{SELECT ?x FROM <http://knoesis.wright.edu/healthcare> WHERE"+ 
						  "{ {?x rdfs:subClassOf ezdi:C0010674.}"+ 
						  "UNION"+ 
						  "{?x rdfs:subClassOf ezdi:C0035921.}"+ 
       						"}"+
							"}"+
							"{icd:patient icd:suffering_with ?x.}"+
							"}"+
							"UNION"+
							"{icd:patient icd:suffering_with ezdi:C0010674.}"+
							"UNION"+
							"{icd:patient icd:suffering_with ezdi:C0035921.}"+
							"}";
//		Triple triple = new Triple(sub, pre, obj);
//		dbHandler.addTripleIntoVirtuoso(triple);
//		System.out.println("Triple "+ triple+ " added");
		System.out.println(dbHandler.checkQueryPattern(subQuery));
	}
}
