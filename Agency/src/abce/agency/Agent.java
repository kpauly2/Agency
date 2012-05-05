package abce.agency;

import java.io.*;
import java.util.*;

import sim.engine.SimState;
import sim.engine.Steppable;

public abstract class Agent implements Steppable {
	private static final long		serialVersionUID	= 1L;
	
	/**
	 * For how many periods should we track short-term values?
	 * XXX number in source code
	 */
	public static final int trackingPeriods = 20;

	// Tracking of individual agents
	private static long					agentIDIndex = 0;
	public final long					agentID;
	
	// Information about the simulation
	// The number of steps this agent has run
	private long numSteps = 0;

	protected Agent() {
		agentID = agentIDIndex++;
	}

	@Override
	public void step(SimState state) {
		numSteps++;
	}

	/**
	 * @return the number of times this agent has been stepped.
	 */
	public long numSteps() {
		return numSteps;
	}
	
	public int shortIndex() {
		return (int) (numSteps % trackingPeriods);
	}
	
	protected int shortIndex(int howLongAgo) {
		// Prevent OOB array accesses
		if (howLongAgo > trackingPeriods)
			throw new RuntimeException("Wanted history for " + howLongAgo + 
					" steps ago, but only " + trackingPeriods + " are being tracked.");
		
		if (howLongAgo < 0)
			throw new RuntimeException("Must be looking at the past...");
		
		int index = shortIndex() - howLongAgo;
		if (index < 0) 
			index = trackingPeriods + index;
		
		return index;
	}

	protected void verifyShortData(int stepsAgo) {
		if (stepsAgo < 0)
			throw new RuntimeException("Cannot get past qty for a future time");
		if (stepsAgo > trackingPeriods)
			throw new RuntimeException("Only keeping records for " + trackingPeriods + " steps, but "
					+ stepsAgo + " were requested.");
	}
	

}
