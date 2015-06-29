
package eu.freme.eservices.elink.exceptions;

/**
 *
 * @author Milan Dojchinovski <milan.dojchinovski@fit.cvut.cz>
 http://dojchinovski.mk
 */
public class WrongTemplateSyntaxException extends Exception {
	private static final long serialVersionUID = 1L;

	public WrongTemplateSyntaxException(String message){
        super(message);
    }
}