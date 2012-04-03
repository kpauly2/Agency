package abce.agency.ec;


import evoict.reflection.*;



/**
 * StimulusResponse provides a method dictionary object to evaluate string paths
 * to method calls. Responses at this point are reflected on demand by searching
 * the class for a @Response tagged method.
 * 
 * @author ruppmatt
 * 
 */

public interface StimulusResponse {

	public MethodDictionary dictionary();

}
