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
package eu.freme.eservices.elink.api;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.rdf.model.*;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.persistence.dao.TemplateDAO;
import eu.freme.common.persistence.model.Template;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


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
        try {
            StmtIterator ex = model.listStatements((Resource)null, model.getProperty("http://www.w3.org/2005/11/its/rdf#taIdentRef"), (RDFNode)null);

            while(ex.hasNext()) {
                Statement stm = ex.nextStatement();
                String entityURI = stm.getObject().asResource().getURI();
                Template t = this.templateDAO.findOneById(templateId + "");
                if(t == null) {
                    return null;
                }

                String query = t.getQuery().replaceAll("@@@entity_uri@@@", entityURI);

                Map.Entry resModel;
                for(Iterator e = templateParams.entrySet().iterator(); e.hasNext(); query = query.replaceAll("@@@" + (String)resModel.getKey() + "@@@", (String)resModel.getValue())) {
                    resModel = (Map.Entry)e.next();
                }

                QueryExecution e1 = QueryExecutionFactory.sparqlService(t.getEndpoint(), query);
                Model resModel1 = e1.execConstruct();
                model.add(resModel1);
                e1.close();
            }

            return model;
        } catch (Exception var11) {
            throw new BadRequestException("It seems your SPARQL template is not correctly defined.");
        }
    }
}