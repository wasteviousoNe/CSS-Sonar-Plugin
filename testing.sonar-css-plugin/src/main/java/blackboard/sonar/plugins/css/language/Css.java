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
package blackboard.sonar.plugins.css.language;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import org.sonar.api.resources.AbstractLanguage;
import org.sonar.api.resources.Project;

import blackboard.sonar.plugins.css.ProjectConfiguration;

/**
 * This class defines the CSS language.
 * 
 * @author Kevin Weisser
 */
public class Css extends AbstractLanguage {

	/** All the valid css files suffixes. */
	private static final String[] DEFAULT_SUFFIXES = { "css" };

	/** A css instance. */
	public static final Css INSTANCE = new Css();

	/** The css language key. */
	public static final String KEY = "css";

	/** The css language name */
	private static final String CSS_LANGUAGE_NAME = "Css";

	private String[] fileSuffixes;

	/**
	 * Default constructor.
	 */
	public Css() {
		super(KEY, CSS_LANGUAGE_NAME);
	}

	public Css(Project project) {
		this();

	    List<?> extensions = project.getConfiguration().getList(ProjectConfiguration.FILE_EXTENSIONS);

	    if (extensions != null && extensions.size() > 0 && !StringUtils.isEmpty((String) extensions.get(0))) {
	    	fileSuffixes = new String[extensions.size()];
	    	for (int i = 0; i < extensions.size(); i++) {
	    		fileSuffixes[i] = extensions.get(i).toString().trim();
	    	}
	    }
	}

	/**
	 * Gets the file suffixes.
	 *
	 * @return the file suffixes
	 * @see org.sonar.api.resources.Language#getFileSuffixes()
	 */
	public String[] getFileSuffixes() {
		return fileSuffixes == null ? DEFAULT_SUFFIXES : fileSuffixes;
	}
}
