/**
 * Copyright 2014 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package org.specs.proposals_solver;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.specs.proposals_solver.PropostasSnippet.Line;

public class Solution {

	enum SolutionState {
		EMPTY, INCOMPLETE, COMPLETE, CONFLICT;
	}

	private final List<Proposal> proposals;
	private int totalValue;
	private Set<Line> lines;
	private SolutionState state;

	// Conflict state
	private static final Solution CONFLICT_SOLUTION;
	static {
		CONFLICT_SOLUTION = new Solution();
		CONFLICT_SOLUTION.state = SolutionState.CONFLICT;
	}

	// Conflict state
	public static final Solution EMPTY_SOLUTION;
	static {
		EMPTY_SOLUTION = new Solution();
	}

	public Solution() {
	proposals = new ArrayList<>();
	totalValue = 0;
	lines = EnumSet.noneOf(Line.class);
	state = SolutionState.EMPTY;
    }

	private Solution(Solution solution, Proposal proposal) {
	proposals = new ArrayList<>(solution.proposals.size() + 1);
	proposals.addAll(solution.proposals);
	proposals.add(proposal);

	totalValue = solution.totalValue + proposal.getValor();

	// Add lines of solution and proposal
	lines = EnumSet.copyOf(solution.lines);
	lines.addAll(proposal.getLines());
	/*
	// Check if lines are all new
	for (Line line : proposal.getLinhas()) {
	    if (!lines.add(line)) {
		throw new RuntimeException("Line '" + line + "' was already in the solution");
	    }
	}
	*/

	if (lines.size() == Line.SIZE) {
	    state = SolutionState.COMPLETE;
	} else {
	    state = SolutionState.INCOMPLETE;
	}
    }

	/**
	 * Creates a new solution by adding the given proposal.
	 * 
	 * @param proposal
	 * @return
	 */
	public Solution addProposal(Proposal proposal) {
		// TODO: TEMPORARY, JUST TO TEST TIME
		// return this;

		// Check if this proposal has a conflict with current solution

		for (Line line : proposal.getLines()) {
			if (lines.contains(line)) {
				return CONFLICT_SOLUTION;
			}
		}

		return new Solution(this, proposal);

	}

	public SolutionState getState() {
		return state;
	}

	public int getTotalValue() {
		return totalValue;
	}

	public List<Proposal> getProposals() {
		return proposals;
	}

	public Set<Line> getLines() {
		return lines;
	}

	public Solution getBest(Solution solution) {
		// If given solution is empty or is a conflict, discard it
		if (solution.getState() == SolutionState.EMPTY
				|| solution.getState() == SolutionState.CONFLICT) {
			return this;
		}

		// If both solutions are incomplete or complete, return the one with
		// lowest total value
		if (solution.getState() == this.getState()) {
			return solution.getTotalValue() < this.getTotalValue() ? solution
					: this;
		}

		// If current solution is complete, return it. Otherwise, return given
		// solution
		if (solution.getState() == SolutionState.COMPLETE) {
			return solution;
		}

		return this;

	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("State: " + state + "\n");
		builder.append("Value: " + getTotalValue() + "\n");
		builder.append("Proposals: " + getProposals() + "\n");

		return builder.toString();
	}
}
