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

package pt.up.fe.specs.proposals_solver;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.stream.IntStream;

import pt.up.fe.specs.library.ParseUtils;
import pt.up.fe.specs.proposals_solver.Solution.SolutionState;
import pt.up.fe.specs.xstreamplus.XStreamUtils;

public class PropostasSnippet {

    /*
     * enum Line { LINHA1, LINHA2, LINHA3 }
     */

    enum Line {
	LINHA1,
	LINHA2,
	LINHA3,
	LINHA4,
	LINHA5,
	LINHA6,
	LINHA7,
	LINHA8,
	LINHA9,
	LINHA10,
	LINHA11,
	LINHA12,
	LINHA13,
	LINHA14,
	LINHA15,
	LINHA16,
	LINHA17,
	LINHA18,
	LINHA19,
	LINHA20,
	LINHA21,
	LINHA22,
	LINHA23,
	LINHA24,
	LINHA25,
	LINHA26,
	LINHA27,
	LINHA28,
	LINHA29,
	LINHA30,
	LINHA31,
	LINHA32,
	LINHA33,
	LINHA34,
	LINHA35,
	LINHA36,
	LINHA37,
	LINHA38,
	LINHA39,
	LINHA40,
	LINHA41,
	LINHA42,
	LINHA43,
	LINHA44,
	LINHA45,
	LINHA46,
	LINHA47,
	LINHA48,
	LINHA49,
	LINHA50;

	public static final int SIZE = Line.values().length;
    }

    /*
     * @Test public void test2() { for (int i = 0; i < 50; i++) {
     * System.out.println("LINHA" + (i + 1) + ","); } }
     */

    public static void main(String args[]) {

	long tic = System.nanoTime();
	List<Proposal> randomProposals = getRandomProposals(500);
	long toc = System.nanoTime();
	System.out.println("INITIALIZATION TIME:"
		+ ParseUtils.getTime(toc - tic));

	XStreamUtils.write(randomProposals, new File("proposals-300.xml"));
	// List<Proposal> randomProposals = XStreamUtils.read(new File("proposals-300.xml"));

	/*
	 * try { System.in.read(); } catch (IOException e) {
	 * LoggingUtils.msgWarn("Error message:\n", e); }
	 */

	tic = System.nanoTime();
	Solution minimize = getSolutionV2(randomProposals, new Solution());
	toc = System.nanoTime();
	System.out.println("SOLUTION TIME:" + ParseUtils.getTime(toc - tic));

	System.out.println("MINIMIZE:" + minimize);

	// System.out.println("RANDOM PROPOSALS:");
	// randomProposals.forEach(proposal -> System.out.println(proposal));
	/*
	 * Proposal p1 = new Proposal(50000, LINHA1, LINHA2); Proposal p2 = new
	 * Proposal(5000, LINHA3); Proposal p3 = new Proposal(60000, LINHA1);
	 * 
	 * List<Proposal> propostas = Arrays.asList(p1, p2, p3);
	 * 
	 * Solution minimize = getSolution(propostas, new Solution());
	 * 
	 * System.out.println("MINIMIZE:" + minimize);
	 */
    }

    private static List<Proposal> getRandomProposals(int numberProposals) {
	List<Proposal> proposals = new ArrayList<>(numberProposals);

	Random rand = new Random();
	List<Line> allLines = Arrays.asList(Line.values());
	for (int i = 0; i < numberProposals; i++) {
	    int valuePerLine = rand.nextInt((10000 - 5000) + 1) + 5000;
	    int numberOfLines = rand.nextInt((30 - 3) + 1) + 3;

	    ListIterator<Line> linesPool = new LinkedList<>(allLines)
		    .listIterator();
	    List<Line> lines = new ArrayList<>(numberOfLines);

	    for (int j = 0; j < numberOfLines; j++) {
		int nextNumber = rand.nextInt((7 - 1) + 1) + 1;
		Line nextLine = null;
		boolean goForward = true;
		for (int k = 0; k < nextNumber; k++) {
		    if (goForward) {
			if (!linesPool.hasNext()) {
			    goForward = false;
			    nextLine = linesPool.previous();
			} else {
			    nextLine = linesPool.next();
			}
		    } else {
			if (!linesPool.hasPrevious()) {
			    goForward = true;
			    nextLine = linesPool.next();
			} else {
			    nextLine = linesPool.previous();
			}
		    }

		}

		lines.add(nextLine);
		linesPool.remove();
	    }

	    proposals.add(new Proposal(valuePerLine * numberOfLines, lines));
	}

	return proposals;
    }

    private static Solution getSolution(List<Proposal> currentProposals,
	    Solution currentSolution) {
	// Check if there are more proposals to combine
	if (currentProposals.isEmpty()) {
	    return currentSolution;
	}

	// Empty solution, to compare against other solutions
	Solution returnSolution = Solution.EMPTY_SOLUTION;

	// For each element in the proposals list, add it to the current
	// solution
	for (int i = 0; i < currentProposals.size(); i++) {
	    Proposal proposta = currentProposals.get(i);

	    Solution newSolution = currentSolution.addProposal(proposta);

	    // If conflict, skip proposal for current state
	    if (newSolution.getState() == SolutionState.CONFLICT) {
		continue;
	    }

	    // If solution is not complete, try to complete it
	    if (newSolution.getState() != SolutionState.COMPLETE) {
		newSolution = getSolution(currentProposals.subList(i + 1,
			currentProposals.size()), newSolution);
	    }

	    // Compare the new solution again the best solution up to now
	    if (returnSolution == null) {
		returnSolution = newSolution;
	    } else {
		returnSolution = returnSolution.getBest(newSolution);
	    }

	}

	return returnSolution;
    }

    private static Solution getSolutionV2(List<Proposal> currentProposals,
	    Solution currentSolution) {
	// Check if there are more proposals to combine
	if (currentProposals.isEmpty()) {
	    return currentSolution;
	}

	// try (IntStream intStream = getStream(currentProposals)) {

	return IntStream
		.range(0, currentProposals.size())
		.parallel()
		.mapToObj(i -> {
		    // Solution solution = intStream.mapToObj(i -> {
			Proposal proposta = currentProposals.get(i);

			Solution newSolution = currentSolution
				.addProposal(proposta);

			if (newSolution.getState() == Solution.SolutionState.COMPLETE) {
			    return newSolution;
			}

			// If solution is not complete, try to complete it
			return getSolution(currentProposals.subList(i + 1,
				currentProposals.size()), newSolution);
			// return getSolutionV2(currentProposals.subList(i + 1,
			// currentProposals.size()), newSolution);

		    }).reduce((sol1, sol2) -> sol1.getBest(sol2)).get();

	// return solution;
	// }
    }

    private static IntStream getStream(List<Proposal> currentProposals) {
	final int threshold = 20;
	if (currentProposals.size() > threshold) {
	    return IntStream.range(0, currentProposals.size()).parallel();
	}

	return IntStream.range(0, currentProposals.size());
    }

    /*
     * private static Solution getSolutionV2NotParallel(List<Proposal>
     * currentProposals, Solution currentSolution) { // Check if there are more
     * proposals to combine if (currentProposals.isEmpty()) { return
     * currentSolution; }
     * 
     * return IntStream.range(0, currentProposals.size()).mapToObj(i -> {
     * Proposal proposta = currentProposals.get(i);
     * 
     * Solution newSolution = currentSolution.addProposal(proposta);
     * 
     * if (newSolution.getState() == Solution.SolutionState.COMPLETE) { return
     * newSolution; }
     * 
     * // If solution is not complete, try to complete it return
     * getSolutionV2NotParallel(currentProposals.subList(i + 1,
     * currentProposals.size()), newSolution);
     * 
     * }).reduce((sol1, sol2) -> sol1.getBest(sol2)).get();
     * 
     * }
     */
}
