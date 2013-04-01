//package org.knoesis.umlstoicd10.ontology;
//
//import java.io.File;
//import java.util.Iterator;
//import java.util.Set;
//
//import org.semanticweb.owlapi.apibinding.OWLManager;
//import org.semanticweb.owlapi.model.IRI;
//import org.semanticweb.owlapi.model.OWLAnnotation;
//import org.semanticweb.owlapi.model.OWLAnnotationProperty;
//import org.semanticweb.owlapi.model.OWLClass;
//import org.semanticweb.owlapi.model.OWLClassExpression;
//import org.semanticweb.owlapi.model.OWLDataFactory;
//import org.semanticweb.owlapi.model.OWLLiteral;
//import org.semanticweb.owlapi.model.OWLOntology;
//import org.semanticweb.owlapi.model.OWLOntologyCreationException;
//import org.semanticweb.owlapi.model.OWLOntologyManager;
//import org.semanticweb.owlapi.reasoner.InferenceType;
//import org.semanticweb.owlapi.reasoner.NodeSet;
//import org.semanticweb.owlapi.reasoner.OWLReasoner;
//import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
//import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
//
//import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
//
//
//
//
//public class ReadOntology {
//	OWLOntologyManager manager;
//	OWLClass startingClass = null;
//	OWLOntology ontology = null;
////	OWLDataFactory dataFactory = manager.getOWLDataFactory();
//	
//	/**
//	 * This method loads the ontology file.
//	 */
//	public void loadOntology() {
//		File ontologyFile = new File("ICD10Initial.owl");
//		manager = OWLManager.createOWLOntologyManager();
//		OWLReasonerFactory reasonerFactory = new PelletReasonerFactory();
//		try {
//			ontology = manager.loadOntologyFromOntologyDocument(ontologyFile);
//			System.out.println("Loaded Ontology: " + ontology);
//			System.out.println();
//			// Getting the ontology IRI
//			String OntologyIRI = ontology.getOntologyID().getOntologyIRI().toString();
//			
//			OWLDataFactory factory = manager.getOWLDataFactory();
//			
//			OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);
//			reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
//			
////			
////			for (OWLClass cls : ontology.getClassesInSignature()){
////				
////				for(OWLAnnotation annotation : cls.getAnnotations(ontology, comment)){
////					OWLLiteral val = (OWLLiteral)annotation.getValue();
////					System.out.println(val.getLiteral());
////				}
////			}
//			startingClass = factory.getOWLClass(IRI.create(OntologyIRI+"#E08.00"));
//			
//			//System.out.println(startingClass.getSubClasses(ontology));
//			NodeSet<OWLClass> subClases = reasoner.getSubClasses(startingClass, true);
//			System.out.println(subClases.isBottomSingleton());
//			for(OWLClass clazz : subClases.getFlattened()){
//				System.out.println(clazz.toString());
//				OWLAnnotationProperty comment = factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI());
//				OWLAnnotationProperty isDefinedBy = factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_IS_DEFINED_BY.getIRI());
//				Set<OWLAnnotation> annotations = clazz.getAnnotations(ontology, comment);
//				if(!annotations.isEmpty())
//					for(OWLAnnotation annotation : clazz.getAnnotations(ontology, comment)){
//						OWLLiteral val = (OWLLiteral)annotation.getValue();
//						System.out.println(val);
//					}
//				else
//					System.out.println("No Annotation");
//			}
////			System.out.println("Reasoner subclasses: " + reasoner.getSubClasses(startingClass, true));
//		} catch (OWLOntologyCreationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	/**
//	 * This method returns you all the direct subclasses of a given class in
//	 * the given ontology.
//	 * @param clazz
//	 * @return
//	 */
//	public Set<OWLClass> getSubClasses(OWLClass clazz){
//
//		Set<OWLClass> subClasses = null;
//		Set<OWLClassExpression> classExpressions = clazz.getSubClasses(ontology);
//		for (Iterator iterator = classExpressions.iterator(); iterator
//				.hasNext();) {
//			OWLClassExpression owlClassExpression = (OWLClassExpression) iterator
//					.next();
//			
//		}
//		return subClasses;
//	}
//	
//	public void getSparqlFromAnnotation(){
//		
//	}
//	
//	public void printHierarchy(OWLReasoner reasoner, OWLClass clazz, int level, Set<OWLClass> visited){
//		if(!visited.contains(clazz) && reasoner.isSatisfiable(clazz)){
//			
//		}
//	}
//	
//	public static void main(String[] args) {
//		ReadOntology test = new ReadOntology();
//		test.loadOntology();
//	}
//}
