
package eu.freme.eservices.elink;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import eu.freme.eservices.elink.exceptions.WrongTemplateSyntaxException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Milan Dojchinovski <milan.dojchinovski@fit.cvut.cz>
 http://dojchinovski.mk
 */
@Controller
public class TemplateDAO {
    
    private Model templatesModel = null;
    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TemplateDAO.class);

    public TemplateDAO() throws WrongTemplateSyntaxException {
        init();
    }
    
    /**
     * This method is executed only ones at the beginning.
     * It populates the list of templates.
     */
    private void init() throws WrongTemplateSyntaxException {
        
        templatesModel = ModelFactory.createDefaultModel();
        try {
//            URL url =  this.getClass().getResource("/data/templates.ttl");
//            templatesModel.read(url.getPath());
            templatesModel.read("/home/u231015/workspace/templates.ttl");
//            templatesModel.read("/Users/Milan/Documents/research/repositories/freme-project-repos/e-Link/src/main/resources/data/templates.ttl");
//            logger.info("The templates have been successfully loaded.");
        } catch (org.apache.jena.riot.RiotException ex) {
            throw new WrongTemplateSyntaxException("The templates could not been loaded. Some of the templates have wrong syntax");
        }
    }
    
    
    // Returns template by its ID.
    public Template getTemplateById(String id) {
        
        StmtIterator iter = templatesModel.listStatements(
        null,
        templatesModel.getProperty("http://www.freme-project.eu/ns#templateId"),
        id);

        if(iter.hasNext()) {
            Template t = new Template(id, getTemplateEndpoint(id), getTemplateQuery(id));
            return t;
        } else {
            return null;
        }
    }
    
    // Return an endpoint for a given template identified with its ID.
    public String getTemplateEndpoint(String id) {
        StmtIterator iter = templatesModel.listStatements(
                null,
                templatesModel.getProperty("http://www.freme-project.eu/ns#templateId"),
                id);

        if(iter.hasNext()) {
            Statement stm = iter.nextStatement();
            Resource subj = stm.getSubject().asResource();
            String endpoint = subj.getProperty(templatesModel.getProperty("http://www.freme-project.eu/ns#endpoint")).getObject().asLiteral().toString();
            return endpoint;
        }
        return null;
    }
    
    // Return a query for a given template identified with its ID.
    public String getTemplateQuery(String id) {
        
        StmtIterator iter = templatesModel.listStatements(
                null,
                templatesModel.getProperty("http://www.freme-project.eu/ns#templateId"),
                id);

        if(iter.hasNext()) {
            Statement stm = iter.nextStatement();
            Resource subj = stm.getSubject().asResource();
            String query = subj.getProperty(templatesModel.getProperty("http://www.freme-project.eu/ns#query")).getObject().asLiteral().toString();
            return query;
        }
        return null;
    }
    
    // Creates a new template in the model.
    public void addTemplate(Template t) {
        
        Resource templateRes = templatesModel.createResource("http://www.freme-project.eu/data/templates/"+t.getId());
        
        // RDF type
        templatesModel.add(
                templateRes,
                RDF.type,
                templatesModel.getResource("http://www.freme-project.eu/ns#Template"));
        
        // ID
        templatesModel.add(
                templateRes,
                templatesModel.getProperty("http://www.freme-project.eu/ns#templateId"),
                t.getId()+"");
        
        // Query
        templatesModel.add(
                templateRes,
                templatesModel.getProperty("http://www.freme-project.eu/ns#query"),
                t.getQuery());
        
        // Endpoint
        templatesModel.add(
                templateRes,
                templatesModel.getProperty("http://www.freme-project.eu/ns#endpoint"),
                t.getEndpoint());
        saveModel();        
    }
    
    // Saves the model.
    public void saveModel() {
        
        String fileName = "/Users/Milan/Documents/research/repositories/freme-project-repos/e-Link/src/main/resources/data/templates.ttl";
        FileWriter out = null;
        try {
            out = new FileWriter( fileName );
            templatesModel.write( out, "TTL" );
        } catch (IOException ex) {
            Logger.getLogger(DataEnricher.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
           try {
               out.close();
           }
           catch (IOException closeException) {
               // ignore
           }
        }
    }
    
    // Removes the templates with a given ID.
    public boolean removeTemplateById(String id) {
        boolean removed = false;
        StmtIterator iter = templatesModel.listStatements(
        null,
        templatesModel.getProperty("http://www.freme-project.eu/ns#templateId"),
        id);

        if(iter.hasNext()) {
            Statement stm = iter.nextStatement();
            Resource subj = stm.getSubject().asResource();
            templatesModel.removeAll(
                subj,
                null,
                null);
            saveModel();
            removed = true;
        }
        return removed;
    }
    
    // Returns all templates from a model.
    // The templates are returned as an array of Template objects.
    public ArrayList<Template> getAllTemplates() {
        
        ArrayList<Template> templates = new ArrayList();
        StmtIterator iter = templatesModel.listStatements(
                null,
                RDF.type,
                templatesModel.getResource("http://www.freme-project.eu/ns#Template")
        );
        
        while(iter.hasNext()) {
            Statement stm = iter.nextStatement();
            Resource tmpRes = stm.getSubject();
            String templateId = tmpRes.getProperty(templatesModel.getProperty("http://www.freme-project.eu/ns#templateId")).getObject().asLiteral().toString();
            String query = tmpRes.getProperty(templatesModel.getProperty("http://www.freme-project.eu/ns#query")).getObject().asLiteral().toString();
            String endpoint = tmpRes.getProperty(templatesModel.getProperty("http://www.freme-project.eu/ns#endpoint")).getObject().asLiteral().toString();
            Template t = new Template(templateId, endpoint, query);
            templates.add(t);

        }
        return templates;
    }
    
    // Updates a template.
    public void updateTemplate(Template t) {
        removeTemplateById(t.getId());
        addTemplate(t);
    }

    // Creates unique ID for a template.
    public String generateTemplateId() {
        int currentId = 0;
        StmtIterator iter = templatesModel.listStatements(
                null,
                RDF.type,
                templatesModel.getResource("http://www.freme-project.eu/ns#Template")
        );
        
        while(iter.hasNext()) {
            Statement stm = iter.nextStatement();
            Resource templRes = stm.getSubject();
            int id = Integer.parseInt(templRes.getProperty(templatesModel.getProperty("http://www.freme-project.eu/ns#templateId")).getObject().asLiteral().toString());
            if(id > currentId) {
                currentId = id;
            }
        }
        currentId += 1;
        return currentId+"";
    }
    
    // Returns templete for a given template ID.
    public Model getTemplateInRDFById(String id) {
        Model model = ModelFactory.createDefaultModel();
        StmtIterator iter = templatesModel.listStatements(
                null,
                templatesModel.getProperty("http://www.freme-project.eu/ns#templateId"),
                id);

        Resource templateRes = null;
        if(iter.hasNext()) {
            Statement stm = iter.nextStatement();
            model.add(stm);
            templateRes = stm.getSubject();
        }
        
        StmtIterator iter2 = templatesModel.listStatements(templateRes, null, (RDFNode) null);
        
        while(iter2.hasNext()) {
            Statement stm2 = iter2.nextStatement();
            model.add(stm2);
        }
        return model;
    }
    
    // Returns the whole model.
    public Model getAllTemplatesInRDF() {
        return templatesModel;
    }
}
