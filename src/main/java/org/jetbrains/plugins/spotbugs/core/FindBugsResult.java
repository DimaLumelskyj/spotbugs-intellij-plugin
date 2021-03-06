/*
 * Copyright 2020 SpotBugs plugin contributors
 *
 * This file is part of IntelliJ SpotBugs plugin.
 *
 * IntelliJ SpotBugs plugin is free software: you can redistribute it 
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of 
 * the License, or (at your option) any later version.
 *
 * IntelliJ SpotBugs plugin is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with IntelliJ SpotBugs plugin.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package org.jetbrains.plugins.spotbugs.core;

import edu.umd.cs.findbugs.Project;
import edu.umd.cs.findbugs.ProjectStats;
import edu.umd.cs.findbugs.SortedBugCollection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.spotbugs.common.util.New;

import java.util.Collection;
import java.util.Map;

public final class FindBugsResult {
	@NotNull
	private final Map<Project, SortedBugCollection> results;

	public FindBugsResult() {
		this.results = New.map();
	}

	public void put(@NotNull final Project project, @Nullable final SortedBugCollection bugCollection) {
		if (results.put(project, bugCollection) != null) {
			throw new IllegalStateException("Duplicate project " + project);
		}
	}

	public boolean isBugCollectionEmpty() {
		for (final SortedBugCollection bugCollection : results.values()) {
			if (!bugCollection.getCollection().isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public int getAnalyzedClassCountSafe() {
		final Integer ret = getAnalyzedClassCount();
		if (ret != null) {
			return ret;
		}
		return 0;
	}

	@Nullable
	public Integer getAnalyzedClassCount() {
		int ret = -1;
		for (final SortedBugCollection bugCollection : results.values()) {
			final ProjectStats stats = bugCollection.getProjectStats();
			if (stats != null) {
				if (ret == -1) {
					ret = 0;
				}
				ret += stats.getNumClasses();
			}
		}
		if (ret != -1) {
			return ret;
		}
		return null;
	}

	@NotNull
	public Collection<Project> getProjects() {
		return results.keySet();
	}

	@NotNull
	public Map<Project, SortedBugCollection> getResults() {
		return results;
	}
}
