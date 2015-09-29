/**
 * Copyright (C) 2015 Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
 * Institut für Angewandte Informatik e. V. an der Universität Leipzig,
 * Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL (http://freme-project.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.freme.eservices.elink;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
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
            jTemplate.put("label", t.getLabel());
            jTemplate.put("description", t.getDescription());
            jTemplates.put(jTemplate);
        }
        return jTemplates;
    }
    
    public JSONObject convertOneTemplate2JSON(Template t) {
        
        JSONObject jTemplate = new JSONObject();
        jTemplate.put("templateId", t.getId());
        jTemplate.put("query", t.getQuery());
        jTemplate.put("endpoint", t.getEndpoint());
        jTemplate.put("label", t.getLabel());
        jTemplate.put("description", t.getDescription());
        
        return jTemplate;
    }
    
    public Template model2OneTemplate(Model model) {
        StmtIterator iter = model.listStatements(null, RDF.type, model.getResource("http://www.freme-project.eu/ns#Template"));
        Template t = null;
        if(iter.hasNext()) {
            Statement stm = iter.nextStatement();
            Resource tRes = stm.getSubject();
//            String templateId = tRes.getProperty(model.getProperty("http://www.freme-project.eu/ns#templateId")).getObject().asLiteral().toString();
            String endpoint = tRes.getProperty(model.getProperty("http://www.freme-project.eu/ns#endpoint")).getObject().asLiteral().toString();
            String query = tRes.getProperty(model.getProperty("http://www.freme-project.eu/ns#query")).getObject().asLiteral().toString();

            String label = tRes.getProperty(RDFS.label).getObject().asLiteral().toString();
            String description = tRes.getProperty(DCTerms.description).getObject().asLiteral().toString();
//            t = new Template(templateId, endpoint, query);
            t = new Template(endpoint, query, label, description);
            return t;
        }
        return t;        
    }
}
