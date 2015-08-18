package eu.freme.eservices.elink.api;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import eu.freme.eservices.elink.Template;
import eu.freme.eservices.elink.TemplateDAO;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;


/**
 *
 * @author Milan Dojchinovski <milan.dojchinovski@fit.cvut.cz>
 * http://dojchinovski.mk
 */
public class DataEnricher {
    
    @Autowired
    TemplateDAO templateDAO;
        
    private boolean initialized = false;
    private final String basePath = "http://www.freme-project.eu/data/templates/"; 
    private final String templatesNs = "http://www.freme-project.eu/ns#"; 

    public DataEnricher(){
    }
    
    /**
     * Called to enrich NIF document
     * @param model          The NIF document represented as Jena model.
     * @param templateId     The ID of the template to be used for enrichment.
     * @param templateParams Map of user defined parameters.
     */
    public Model enrichNIF(Model model, int templateId, HashMap<String, String> templateParams) {
        
        StmtIterator iter = model.listStatements(null, model.getProperty("http://www.w3.org/2005/11/its/rdf#taIdentRef"), (RDFNode) null);

        while(iter.hasNext()) {
            Statement stm = iter.nextStatement();
            String entityURI = stm.getObject().asResource().getURI();
            Template t = templateDAO.getTemplateById(templateId+"");
            if(t == null)
                return null;
            String query = t.getQuery().replaceAll("@@@entity_uri@@@", entityURI);
            for (Map.Entry<String, String> entry : templateParams.entrySet()) {
                query = query.replaceAll("@@@"+entry.getKey()+"@@@", entry.getValue());
            }
            QueryExecution e = QueryExecutionFactory.sparqlService(t.getEndpoint(), query);
            Model resModel = e.execConstruct();
            model.add(resModel);
            e.close();
        }
        return model;
    }
}