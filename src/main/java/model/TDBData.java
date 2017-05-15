package model;

import java.io.InputStream;
import java.util.Iterator;

import org.apache.jena.atlas.lib.FileOps;
//import org.apache.jena.atlas.iterator.Filter ;
//import org.apache.jena.atlas.lib.Tuple;
import org.apache.jena.atlas.lib.StrUtils ;
import org.apache.jena.graph.NodeFactory ;
import org.apache.jena.query.* ;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
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


public class TDBData {

	

	// nur für Test main Methode 
    public static void main(String[] args) {
		org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        FileManager fm = FileManager.get();
        fm.addLocatorClassLoader(TDBData.class.getClassLoader());
        InputStream in = fm.open("data/data.nt");

        Location location = Location.create ("target/TDB");
        Dataset dataset = TDBFactory.createDataset(location);
        dataset.begin(ReadWrite.WRITE);
        try {
        	
            dataset.commit();
        } catch (Exception e) {
            dataset.abort();
        } finally {
            dataset.end();
        }

        dataset.begin(ReadWrite.READ);
        try {
            Iterator<Quad> iter = dataset.asDatasetGraph().find();
            while ( iter.hasNext() ) {
                Quad quad = iter.next();
                System.out.println(quad);
            }
        } finally {
            dataset.end();
        }
    }

}
