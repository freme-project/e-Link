
package eu.freme.eservices.elink;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Milan Dojchinovski <milan.dojchinovski@fit.cvut.cz>
 http://dojchinovski.mk
 */
public class Exporter {
    
    private static Exporter instance = null;
    
    public static Exporter getInstance(){
        if(instance == null) {
            instance = new Exporter();
        }
        return instance;
    }
    
    public JSONArray convertTemplates2JSON(ArrayList<Template> templates) {
        
        JSONArray jTemplates = new JSONArray();
        
        for(Template t : templates) {
            JSONObject jTemplate = new JSONObject();
            jTemplate.put("templateId", t.getId());
            jTemplate.put("query", t.getQuery());
            jTemplate.put("endpoint", t.getEndpoint());
            jTemplates.put(jTemplate);
        }
        return jTemplates;
    }
    
    public JSONObject convertOneTemplate2JSON(Template t) {
        
        JSONObject jTemplate = new JSONObject();
        jTemplate.put("templateId", t.getId());
        jTemplate.put("query", t.getQuery());
        jTemplate.put("endpoint", t.getEndpoint());
        
        return jTemplate;
    }
    
    public Template model2OneTemplate(Model model) {
        StmtIterator iter = model.listStatements(null, RDF.type, model.getResource("http://www.freme-project.eu/ns#Template"));
        Template t = null;
        if(iter.hasNext()) {
            Statement stm = iter.nextStatement();
            Resource tRes = stm.getSubject();
            String templateId = tRes.getProperty(model.getProperty("http://www.freme-project.eu/ns#templateId")).getObject().asLiteral().toString();
            String endpoint = tRes.getProperty(model.getProperty("http://www.freme-project.eu/ns#endpoint")).getObject().asLiteral().toString();
            String query = tRes.getProperty(model.getProperty("http://www.freme-project.eu/ns#query")).getObject().asLiteral().toString();
            t = new Template(templateId, endpoint, query);
            return t;
        }
        return t;        
    }
}