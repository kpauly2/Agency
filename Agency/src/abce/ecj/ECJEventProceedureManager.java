package abce.ecj;


import java.util.*;

import abce.ecj.ep.*;
import evoict.*;
import evoict.ep.*;



/**
 * This is an event procedure manger for ECJ events. The constructor must
 * declare all event IDs generated by ECJ and place those values into the
 * event_procedures dictionary. Process method contains information specific to
 * ECJ's procedure execution.
 * 
 * @author ruppmatt
 * 
 */
public class ECJEventProceedureManager extends EventProcedureManager {

	/**
	 * 
	 */
	private static final long						serialVersionUID		= 1L;
	public final static byte						EVENT_PRE_EVALUATION	= 1;
	public final static byte						EVENT_POST_EVALUATION	= 2;
	public final static byte						EVENT_POST_BREEDING		= 3;
	public final static byte						EVENT_DOMAIN			= 4;
	LinkedHashMap<Byte, EventProcedureContainer>	event_procedures		= new LinkedHashMap<Byte, EventProcedureContainer>();



	/**
	 * Construct an empty procedure manager for ECJ. All event types should be
	 * declared in this constructor to create an empty container for each.
	 */
	public ECJEventProceedureManager() {
		event_procedures.put(EVENT_PRE_EVALUATION, new ValuedEventProcedureQueue());
		event_procedures.put(EVENT_POST_EVALUATION, new ValuedEventProcedureQueue());
		event_procedures.put(EVENT_POST_BREEDING, new ValuedEventProcedureQueue());
		event_procedures.put(EVENT_DOMAIN, new ValuedEventProcedureQueue());
	}



	/**
	 * Add event procedures from a file.
	 * 
	 * @param path
	 *            Path to event procedure file.
	 * @throws BadConfiguration
	 */
	public void buildFromFile(String path) throws BadConfiguration {
		EventProcedureDescription[] desc = EventProcedureManager.processFromFile(path);
		for (EventProcedureDescription d : desc) {
			addEvent(d);
		}
	}



	/**
	 * Add a new event to the manager
	 * 
	 * @param desc
	 *            EventProcedureDescription to add to the manager
	 */
	@Override
	public void addEvent(EventProcedureDescription desc) throws BadConfiguration {
		String event_type = desc.getEventType();
		if (event_type.toUpperCase().equals("PRE_EVALUATION")) {
			ValuedEventProcedure vea = new ECJValuedEventProcedure(EVENT_PRE_EVALUATION, desc);
			event_procedures.get(EVENT_PRE_EVALUATION).add(vea);
		} else if (event_type.toUpperCase().equals("POST_EVALUATION")) {
			ValuedEventProcedure vea = new ECJValuedEventProcedure(EVENT_POST_EVALUATION, desc);
			event_procedures.get(EVENT_POST_EVALUATION).add(vea);
		} else if (event_type.toUpperCase().equals("GENERATION")) {
			event_procedures.get(EVENT_DOMAIN).add(buildDomainEvent(desc));
		}
	}



	/**
	 * Process an event with a particular context.
	 * 
	 * 
	 * @param event_id
	 *            The event ID
	 * @param event_context
	 *            The context to examine (particular to a particular event type)
	 * @param Object
	 *            ... execution_context
	 *            The context to pass to the proceedure if triggered
	 */
	@Override
	public void process(byte event_id, Object event_context, Object... execution_context) {
		EventProcedureContainer epc = event_procedures.get(event_id);
		if (epc != null) {
			for (EventProcedure ep : epc.processContext(event_context)) {
				try {
					ep.execute(execution_context);
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		}
	}



	/**
	 * When an event type is EVENT_DOMAIN, a new EventDescription needs to be
	 * built from that will add the event procedure to the
	 * domain model when it is evaluated. The current design of this is to
	 * create a new description, timed with the EVENT_DOMAIN (generation) time,
	 * that adds new procedures to the domain model via the
	 * EPSimpleEvolutionState object through a procedure called AddDomainEP.
	 * AddDomainEP takes a single argument "desc" which is a string description
	 * of the EventProcedure.
	 * 
	 * @param desc
	 *            EventProcedureDescription containing a EVENT_DOMAIN event
	 * @return
	 * @throws BadConfiguration
	 */
	protected ECJValuedEventProcedure buildDomainEvent(EventProcedureDescription desc) throws BadConfiguration {
		String[] event_toks = desc.getEventValue().split("\\s+", 2);
		String event_domain = event_toks[1];
		String ep_proc = desc.getProcedureClass().getCanonicalName() + " " + desc.getProcedureArguments().toString();
		String domain_epd = event_domain + " ~ " + ep_proc;
		EventProcedureArgs domain_args = new EventProcedureArgs();
		domain_args.put("desc", domain_epd);
		EventProcedureDescription domain_desc = new EventProcedureDescription(desc.getEventType(),
				desc.getEventValue(),
				AddDomainEP.class, domain_args);
		System.err.println(domain_desc);
		return new ECJValuedEventProcedure(EVENT_DOMAIN, domain_desc);
	}



	@Override
	public void finish() {
		for (EventProcedureContainer epc : event_procedures.values()) {
			epc.finish();
		}

	}

}
