package model;

//import org.apache.jena.atlas.iterator.Filter ;
//import org.apache.jena.atlas.lib.Tuple;
import org.apache.jena.atlas.lib.StrUtils ;


import org.apache.jena.graph.NodeFactory ;
import org.apache.jena.query.* ;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.core.DatasetGraph ;
import org.apache.jena.sparql.core.Quad ;
import org.apache.jena.sparql.sse.SSE ;
import org.apache.jena.tdb.TDB ;
import org.apache.jena.tdb.TDBFactory ;
import org.apache.jena.tdb.store.NodeId ;
import org.apache.jena.tdb.sys.SystemTDB ;
import org.apache.jena.tdb.sys.TDBInternal ;
import org.apache.jena.update.GraphStore ;
import org.apache.jena.update.GraphStoreFactory ;
import org.apache.jena.update.UpdateExecutionFactory ;
import org.apache.jena.update.UpdateFactory ;
import org.apache.jena.update.UpdateProcessor ;
import org.apache.jena.update.UpdateRequest ;


public class TDBData {

	
	private final static String directory = "DKEDatabase/" ;
    private final static Dataset dataset = TDBFactory.createDataset(directory) ;

	// nur für Test main Methode 
	public static void main (String[] args)
	{
		Model model = dataset.getDefaultModel() ;
	    boolean readable = readTDB();
	    boolean writeable = writeTDB();
	    
	    System.out.println(readable +", ");
    }
	
	
	
	private static boolean writeTDB() {
		boolean result = false;
	    dataset.begin(ReadWrite.WRITE) ;
        try
        {
            GraphStore graphStore = GraphStoreFactory.create(dataset) ;
            // Do a SPARQL Update.
            String sparqlUpdateString = StrUtils.strjoinNL(
                 "PREFIX . <http://example/>",
                 "INSERT { :s :p ?now } WHERE { BIND(now() AS ?now) }"
                 ) ;

            execUpdate(sparqlUpdateString, graphStore) ;
            dataset.commit() ;
            // Or call .abort()
            
        } finally
        {
        	result = true;
            // Notify the end of the transaction.
            // The transaction was finished at the point .commit or .abort was called.
            // .end will force an abort() if no previous call to .commit() or .abort()
            // has occurred, so .end() help manage track the state of the transaction.
            // .end() can be called multiple times for the same .begin(WRITE)
            dataset.end() ;
        }
	
		return result;
	}



	private static boolean readTDB() {
		boolean result = false;
	    dataset.begin(ReadWrite.READ) ;
	    try
	    {
	        // Do some queries
	        String sparqlQueryString1 = "SELECT (count(*) AS ?count) { ?s ?p ?o }" ;
	        execQuery(sparqlQueryString1, dataset) ;
	        
	        String sparqlQueryString2 = "SELECT * { ?s ?p ?o }" ;
	        execQuery(sparqlQueryString2, dataset) ;
	        
	        // Can also call dataset.abort() or dataset.commit() here 
	    } finally
	    {
	    	result = true;
	        // Notify the end of the READ transaction.
	        // Any use of dataset.abort() or dataset.commit() or dataset.end()
	        // .end() can be called multiple times for the same .begin(READ)
	        dataset.end();
	    }
		return result;
	}



	public static void execQuery(String sparqlQueryString, Dataset dataset)
	{
	    Query query = QueryFactory.create(sparqlQueryString) ;
	    QueryExecution qexec = QueryExecutionFactory.create(query, dataset) ;
	    try {
	        ResultSet results = qexec.execSelect() ;
	        for ( ; results.hasNext() ; )
	        {
	            QuerySolution soln = results.nextSolution() ;
	            int count = soln.getLiteral("count").getInt() ;
	            System.out.println(count + " Daten oida") ;
	        }
	      } finally { qexec.close() ; }
	}
	
	 public static void execUpdate(String sparqlUpdateString, GraphStore graphStore)
	    {
	        UpdateRequest request = UpdateFactory.create(sparqlUpdateString) ;
	        UpdateProcessor proc = UpdateExecutionFactory.create(request, graphStore) ;
	        proc.execute() ;
	    }
	
}
