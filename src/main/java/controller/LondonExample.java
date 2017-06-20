package controller;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.ResultSetFormatter;


public class LondonExample {
    public static void main(String[] args) {
    	//org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
String Token = "Sydney";
    	String qs = new String( " "
    			+ "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
    	        + "PREFIX dbo:     <http://dbpedia.org/ontology/>"
    	        + "\n"
    	        + "select distinct ?resource ?abstract where {\n"
    	        + "  ?resource rdfs:label '" + Token + "'@en.\n"
    	        + "  ?resource dbo:abstract ?abstract.\n"
    	        + "  FILTER (lang(?abstract) = 'de')}");


        System.out.println( qs );

        QueryExecution exec = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", QueryFactory.create(qs) );

        // Normally you'd just do results = exec.execSelect(), but I want to 
        // use this ResultSet twice, so I'm making a copy of it.  
        ResultSet results = ResultSetFactory.copyResults( exec.execSelect() );

        while ( results.hasNext() ) {
            // As RobV pointed out, don't use the `?` in the variable
            // name here. Use *just* the name of the variable.
            QuerySolution soln = results.nextSolution();
            
            System.out.println(soln.get("abstract"));
        }

        // A simpler way of printing the results.
//        ResultSetFormatter.out( results );
    }
}
