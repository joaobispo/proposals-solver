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

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.StringJoiner;

import pt.up.fe.specs.proposals_solver.PropostasSnippet.Line;

public class Proposal {
	private final Set<Line> linhas;
	private final int valor;

	public Proposal(int valor, Line... linhas) {
		this(valor, Arrays.asList(linhas));
	}

	public Proposal(int valor, Collection<Line> linhas) {
		this.linhas = EnumSet.copyOf(linhas);
		this.valor = valor;
	}

	public Set<Line> getLines() {
		return linhas;
	}

	public int getValor() {
		return valor;
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ", "{", "}");
		linhas.forEach(line -> joiner.add(line.name()));

		return joiner.toString() + " -> " + valor;
	}
}
