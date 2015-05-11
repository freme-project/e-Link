
package eu.freme.eservices.elink.exceptions;

/**
 *
 * @author Milan Dojchinovski <milan.dojchinovski@fit.cvut.cz>
 * http://dojchinovski.mk
 */
public class TemplateNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public TemplateNotFoundException(String message){
        super(message);
    }
}