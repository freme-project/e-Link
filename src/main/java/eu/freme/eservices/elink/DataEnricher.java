/**
 * Copyright (C) 2015 Deutsches Forschungszentrum für Künstliche Intelligenz (http://freme-project.eu)
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

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
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