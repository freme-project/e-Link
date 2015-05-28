package eu.freme.eservices.elink;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import eu.freme.eservices.elink.exceptions.TemplateNotFoundException;
import java.util.HashMap;


/**
 *
 * @author Milan Dojchinovski <milan.dojchinovski@fit.cvut.cz>
 * http://dojchinovski.mk
 */
public class DataEnricher {

    private HashMap<Integer, Template> templates = new HashMap<Integer, Template>();
    private boolean initialized = false;
    
    public DataEnricher(){
    	init();
    }
    
    /**
     * Called to enrich NIF document
     * @param model         The NIF document represented as Jena model.
     * @param templateId    The ID of the template to be used for enrichment.
     */
    public Model enrichNIF(Model model, int templateId) throws TemplateNotFoundException {
        
        if(!initialized) {
            System.out.println("Enricher not initialized, initializing ...");
            init();
            initialized = true;
        }
        
        StmtIterator iter = model.listStatements(null, model.getProperty("http://www.w3.org/2005/11/its/rdf#taIdentRef"), (RDFNode) null);

        while(iter.hasNext()) {
            Statement stm = iter.nextStatement();
            String entityURI = stm.getObject().asResource().getURI();
            System.out.println(entityURI);
            Template t = templates.get(templateId);
            if(t == null)
                throw new TemplateNotFoundException("Template with the given id has not been found");
            String query = t.getQuery().replaceAll("@@@entity_uri@@@", entityURI);
//            System.out.println(query);
            QueryExecution e = QueryExecutionFactory.sparqlService(t.getEndpoint(), query);
            Model resModel = e.execConstruct();
            model.add(resModel);
            e.close();
        }
        return model;
    }
    
    /**
     * This method is executed only ones at the beginning.
     * It populates the list of templates.
     */
    private void init() {
        
            Template t1 = new Template();
            t1.setId(1);
            t1.setEndpoint("http://dbpedia.org/sparql");
            String query1 =
                    "PREFIX dbpedia: <http://dbpedia.org/resource/> "
                    + "PREFIX dbpedia-owl: <http://dbpedia.org/ontology/> "
                    + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                    + "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#> "
                    + "CONSTRUCT {"
                    + "?museum <http://xmlns.com/foaf/0.1/based_near> <@@@entity_uri@@@> . "
                    +"} "
                    + "WHERE { "
                    +" <@@@entity_uri@@@> geo:geometry ?citygeo . "
                    + "?museum rdf:type <http://schema.org/Museum> . "
                    + "?museum geo:geometry ?museumgeo ."
                    + "FILTER (<bif:st_intersects>(?museumgeo, ?citygeo, 10)) "
                    + "} LIMIT 10";
            t1.setQuery(query1);
            
            Template t2 = new Template();
            t2.setId(2);
            t2.setEndpoint("http://live.dbpedia.org/sparql");
            String query2 =
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                    + " CONSTRUCT { "
                    + "?event <http://dbpedia.org/ontology/place> <@@@entity_uri@@@> . "
                    +" } "
                    + " WHERE { "
                    + " ?event rdf:type <http://dbpedia.org/ontology/Event> . "
                    + " ?event <http://dbpedia.org/ontology/place> <@@@entity_uri@@@> . "
                    + " } LIMIT 10";
            t2.setQuery(query2);
            
            templates.put(t1.getId(), t1);    
            templates.put(t2.getId(), t2);    
    }
}