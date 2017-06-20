package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.jena.atlas.lib.FileOps;
//import org.apache.jena.atlas.iterator.Filter ;
//import org.apache.jena.atlas.lib.Tuple;
import org.apache.jena.atlas.lib.StrUtils ;
import org.apache.jena.graph.NodeFactory ;
import org.apache.jena.query.* ;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.core.DatasetGraph ;
import org.apache.jena.sparql.core.Quad ;
import org.apache.jena.sparql.sse.SSE ;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.tdb.TDB ;
import org.apache.jena.tdb.TDBFactory ;
import org.apache.jena.tdb.TDBLoader;
import org.apache.jena.tdb.base.file.Location;
import org.apache.jena.tdb.store.NodeId ;
import org.apache.jena.tdb.sys.SystemTDB ;
import org.apache.jena.tdb.sys.TDBInternal ;
import org.apache.jena.update.GraphStore ;
import org.apache.jena.update.GraphStoreFactory ;
import org.apache.jena.update.UpdateExecutionFactory ;
import org.apache.jena.update.UpdateFactory ;
import org.apache.jena.update.UpdateProcessor ;
import org.apache.jena.update.UpdateRequest ;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.query.ParameterizedSparqlString;


/*
 * Created by Robert
 */

public class Model {
	
	/**
	 * 
	 * @param Token zb "Robert"
	 * @return Info infos �ber robert" Robert (Person) leer blablablablab
	 */
	public static String getMeta(String Token){
/*		String queryString = new String( " "
        		+ "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX dbo:     <http://dbpedia.org/ontology/>"
                + "\n"
                + "select distinct ?resource ?abstract where {\n"
                + "  ?resource rdfs:label '" + Token + "'@en.\n"
                + "  ?resource dbo:abstract ?abstract.\n"
                + "  FILTER (lang(?abstract) = 'de')}");
  */
		String queryString =              
				"PREFIX ex: <http://example.org/> " +
                "SELECT * WHERE { " +
                "    ?s ex:name ?name . " +
                " ?s ex:abstrac ?abstrac " +
                " FILTER regex(str(?name), \"" + Token + "\")" +
                "}";

		Location location = Location.create ("target/TDB");
        Dataset dataset = TDBFactory.createDataset(location);

        dataset.begin(ReadWrite.READ);
/*        try {
            Iterator<Quad> iter = dataset.asDatasetGraph().find();
            while ( iter.hasNext() ) {
                Quad quad = iter.next();
                System.out.println(quad);
            }
        } finally {
            dataset.end();
        }
  */      
        // take this try block for select via query string

        try {
            Query query = QueryFactory.create(queryString);
            QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
            try {
                ResultSet results = qexec.execSelect();
                StringBuffer res = new StringBuffer();
                while ( results.hasNext() ) {
                    QuerySolution soln = results.nextSolution();
                    //Literal name = soln.getLiteral("abstract");
                    res.append(soln.get("abstrac"));
                    
                }
                if (res.toString().isEmpty()) return "Sorry - No additional information was found " + queryString + "!";
                return res.toString();
        	
            } finally {
                qexec.close();
            }
        } finally {
            dataset.end();
        }      
	}
	
	/**
	 * 
	 * @param Token zb "Robert"
	 * @param res infos �ber robert" Robert (Person) leer blablablablab
	 * @return true if successful
	 */
	public static boolean setmeta(String Token, String res){
        Location location = Location.create ("target/TDB");
        Dataset dataset = TDBFactory.createDataset(location);
             dataset.begin(ReadWrite.WRITE);
        try {
        	
        	//Einf�gen in TDB 
        	org.apache.jena.rdf.model.Model model = dataset.getDefaultModel();
        	final Property abstrac = model.createProperty("http://example.org/abstrac" );
        	final Property name = model.createProperty("http://example.org/name" );
            model.createResource("http://example.org/" + Token)
            .addProperty(abstrac, model.createResource("\"" + res + "\""))
            .addProperty(name, model.createResource("\"" + Token + "\""));

            dataset.commit();
        } catch (Exception e) {
            dataset.abort();
        } finally {
            dataset.end();
        }

return false;
	}
}
