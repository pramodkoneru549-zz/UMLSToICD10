package org.knoesis.umlstoicd10.rdfmodels;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.knoesis.umlstoicd10.utils.ConfigManager;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;

import de.fuberlin.wiwiss.ng4j.NamedGraph;
import de.fuberlin.wiwiss.ng4j.NamedGraphSet;
import de.fuberlin.wiwiss.ng4j.impl.NamedGraphSetImpl;

/**
 * This class is used for in memory graph storage
 * 
 * NOTE Using NG4J API for this http://wifo5-03.informatik.uni-mannheim.de/bizer/ng4j/
 * @author koneru
 *
 */
public class RdfModelCreator {
	
	private static Log log = LogFactory.getLog(RdfModelCreator.class);
	private static NamedGraphSet graphSet;
	private NamedGraph graph;
	private static ConfigManager configParams;
	private static String tripleFileLocation;
	
	/**
	 * Default constructor: 
	 * Initializes the in memory graph. It gets the graph name from the
	 * config.properties file. Also the location of the triple file is 
	 * fetched here.
	 */
	public RdfModelCreator() {
		configParams = new ConfigManager();
		graphSet = new NamedGraphSetImpl();
		graph = graphSet.createGraph(configParams.getInMemoryGraphName());
		tripleFileLocation = configParams.getTripleFileLocation();
	}
	
	/**
	 * This method will load the triples into the graph.
	 */
	public void loadTriples(){
		Set<Triple> triples = createTriplesFromFile(tripleFileLocation);
		for(Triple triple:triples){
			graph.add(triple);
		}
	}
	
	public void printGraph(){
		System.out.println("The name of the graph is " + graph.getGraphName());
		graphSet.write(System.out, "TRIX", null);
		System.out.println();
	}
	
	/**
	 * This method is used to create a set of jena triples from the file.
	 * @param fileName -- name of the triples file
	 * @return {@link Set}{@link Triple} -- set of jena triples created from that file.
	 */
	public Set<Triple> createTriplesFromFile(String tripleFileLocation){
		Set<Triple> triples = new HashSet<Triple>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("data/triples.nt"));
			String line = reader.readLine();
			while(line != null){
				String uris[] = line.split(" ");
				String subject = uris[0];
				String predicate = uris[1];
				// Replacing the dot at the end.
				String object = uris[2].replace(".", "");
				Node sub = Node.createURI(subject);
				Node prdct = Node.createURI(predicate);
				Node obj = Node.createURI(object);
				Triple triple = new Triple(sub, prdct, obj);
				triples.add(triple);
				line = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			log.error("File not found please check the path and filename");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return triples;
	}
	
	public NamedGraph getGraph() {
		loadTriples();
		return graph;
	}

	public void setGraph(NamedGraph graph) {
		this.graph = graph;
	}
	
	public static void main(String[] args) {
		RdfModelCreator modelCreator = new RdfModelCreator();
		modelCreator.loadTriples();
		modelCreator.printGraph();
	}
}
