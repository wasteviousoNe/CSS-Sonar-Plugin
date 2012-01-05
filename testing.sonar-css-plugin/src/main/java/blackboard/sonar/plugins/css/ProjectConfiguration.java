/*
 * Sonar Css Plugin
 * Copyright (C) 2010 Kevin Weisser
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package blackboard.sonar.plugins.css;

import java.io.File;

import org.sonar.api.resources.Project;

/**
 * Utilities and constants for the project configuration.
 *
 * @author Kevin Weisser
 */
public final class ProjectConfiguration {

	public static final String FILE_EXTENSIONS = "sonar.css.fileExtensions";
	public static final String SOURCE_DIRECTORY = "sonar.css.sourceDirectory";
	public static final String CLASS_SELECTOR = ".";
	public static final String ID_SELECTOR = "#";
	public static final String UNIVERSAL_SELECTOR = "*";
	public static final String OPENING_BRACE = "{";
	public static final String CLOSING_BRACE = "}";
	public static final String OPENING_COMMENT = "/*";
	public static final String CLOSING_COMMENT = "*/";
	public static final char OPEN_BRACE = '{';
	public static final char CLOSE_BRACE = '}';
	public static final char STAR = '*';
	public static final char POUND = '#';
	public static final char PERIOD = '.';

	private ProjectConfiguration() {
		// cannot instantiate
	}

	public static void configureSourceDir(Project project) {
		String sourceDir = getSourceDir(project);
		if (sourceDir != null) {
			File file = new File(project.getFileSystem().getBasedir() + "/" + sourceDir);

			project.getPom().getCompileSourceRoots().clear();
			project.getFileSystem().addSourceDir(file);
		}
	}

	private static String getSourceDir(Project project) {
		return (String) project.getProperty(ProjectConfiguration.SOURCE_DIRECTORY);
	}
}