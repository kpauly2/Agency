package abce.agency.ec.ecj.terminals;


import abce.agency.ec.*;
import abce.agency.ec.ecj.types.*;
import ec.*;
import ec.gp.*;
import ec.util.*;
import evoict.*;
import evoict.reflection.*;



public class NumericalStimulusERC extends ERC {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	String						path				= null;
	RestrictedMethodDictionary	dict				= null;



	@Override
	public String toString() {
		return "NumericalStimulusGP";
	}



	@Override
	public void eval(EvolutionState state, int thread, GPData input, ADFStack stack, GPIndividual individual,
			Problem problem) {

		StimulusResponse sr = (StimulusResponse) problem;

		// Always reset the dictionary to avoid references to really old
		// dictionary references
		dict = (RestrictedMethodDictionary) sr.dictionary();

		if (path == null) {
			resetNode(state, thread);
		}

		/*
		 * The resolved stored in result value needs to be either a Double or
		 * Integer; because the value is stored in an object, any primitives
		 * (int, double) are converted to Integer or Double. Integer values need
		 * to be converted to a double, which is done automatically, to be
		 * stored in DoubleGP's value field.
		 */
		try {
			Object result = dict.evaluate(path, problem);
			if (result.getClass().isAssignableFrom(Double.class)) {
				input = new DoubleGP();
				((DoubleGP) input).value = (Double) result;
			} else if (result.getClass().isAssignableFrom(Integer.class)) {
				input = new DoubleGP();
				((DoubleGP) input).value = ((Integer) result);
			} else {
				throw new UnresolvableException("Incorrect type: received + " + result.getClass() + " from path "
						+ path);
			}
		} catch (UnresolvableException e) {
			System.err.println("Unable to resolve method path: " + path + " with root object "
					+ problem.getClass().getCanonicalName());
			System.err.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

	}



	@Override
	public void mutateERC(EvolutionState state, int thread) {
		resetNode(state, thread);
	}



	@Override
	public void resetNode(EvolutionState state, int thread) {
		if (dict == null) {
			return;
		} else {
			String[] possible = dict.enumerate();
			int len = possible.length;
			path = possible[state.random[thread].nextInt(len)];
		}
	}



	@Override
	public boolean nodeEquals(GPNode node) {
		if (node instanceof NumericalStimulusERC) {
			NumericalStimulusERC comapre_to = (NumericalStimulusERC) node;
			if (path.equals(comapre_to.path)) {
				return true;
			}
		}
		return false;
	}



	@Override
	/**
	 * TODO: Not sure about what's being read initiallly; but I followed the pattern to create this magic.
	 */
	public boolean decode(DecodeReturn dret) {
		int pos = dret.pos;
		String data = dret.data;
		Code.decode(dret);
		if (dret.type != DecodeReturn.T_STRING) {
			dret.data = data;
			dret.pos = pos;
			return false;
		}
		this.path = dret.data;
		return true;
	}



	@Override
	public String encode() {
		return Code.encode(this.path);
	}
}
