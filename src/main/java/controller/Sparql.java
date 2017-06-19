package controller;

import java.util.ArrayList;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.ResultSetFormatter;

/*
 * Created by Robert
 */

public class Sparql {

	public String lookup(String Token){
		String qs = new String( " " +
                "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "\n" +
                "select ?resource where {\n" +
                "  ?resource rdfs:label \""+Token+"\"@en\n" +
                "}" );

        System.out.println( qs );

        QueryExecution exec = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", QueryFactory.create(qs) );

        // Normally you'd just do results = exec.execSelect(), but I want to 
        // use this ResultSet twice, so I'm making a copy of it.  
        ResultSet results = ResultSetFactory.copyResults( exec.execSelect() );
        
        StringBuffer res = new StringBuffer();
        while ( results.hasNext() ) {
            // As RobV pointed out, don't use the `?` in the variable
            // name here. Use *just* the name of the variable.
            res.append(results.next().get( "resource" ).toString());
        }
        ResultSetFormatter.out( results );
        return res.toString();
	}
}