package eu.freme.eservices.elink.api;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import eu.freme.eservices.elink.DataEnricher;
import eu.freme.eservices.elink.exceptions.TemplateNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ELinkAPI {
    
    @RequestMapping(
            value="/e-link/",
            method = RequestMethod.POST,
            consumes={"text/turtle", "application/rdf+xml"},
            produces={"text/turtle"})
    public ResponseEntity<String> enrich(
            @RequestParam(value = "template", defaultValue = "") String input,
            @RequestParam(value = "informat", defaultValue = "") String informat,
            @RequestParam(value = "outformat", defaultValue = "") String outformat,
            @RequestParam(value = "templateid", defaultValue = "") int templateId,
            @RequestBody String body,
            @RequestHeader(value="Accept") String acceptHeader,
            @RequestHeader(value="Content-Type") String contentTypeHeader) {
        try {
            //        System.out.println(body);
            
            if( body == null || body.trim().length() == 0 ){
                return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
            }
            
            Model model = ModelFactory.createDefaultModel();
            
            switch(contentTypeHeader) {
                case "text/turtle":
                    model.read(new ByteArrayInputStream(body.getBytes()), null, "TTL");
                    break;
                case "application/rdf+xml":
                    model.read(new ByteArrayInputStream(body.getBytes()), null, "RDF/XML");
                    break;
            }
            if(informat != null || informat.trim().length() != 0) {
                switch(informat) {
                    case "turtle":
                        model.read(new ByteArrayInputStream(body.getBytes()), null, "TTL");
                        break;
                    case "rdfxml":
                        model.read(new ByteArrayInputStream(body.getBytes()), null, "RDF/XML");
                        break;
                }
            }
            
            model = DataEnricher.getInstance().enrichNIF(model, templateId);
            
            StringWriter out = new StringWriter();
            
            switch(acceptHeader) {
                case "text/turtle":
                    model.write(out, "TTL");
                    break;
                case "application/rdf+xml":
                    model.write(out, "RDF/XML");
                    break;
            }
            if(outformat != null || outformat.trim().length() != 0) {
                switch(outformat) {
                    case "turtle":
                        model.write(out, "TTL");
                        break;
                    case "rdfxml":
                        model.write(out, "RDF/XML");
                        break;
                }
            }
            
            String result = out.toString();
            return new ResponseEntity<String>(result, HttpStatus.OK);
            
        } catch (TemplateNotFoundException ex) {
            System.out.println("template not found");
//            Logger.getLogger(ELinkAPI.class.getName()).log(Level.INFO, null, ex);
        }
        return new ResponseEntity<String>("problem", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
