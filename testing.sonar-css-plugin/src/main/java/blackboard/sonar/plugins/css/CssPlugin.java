/*
 *  Copyright 2010 Kevin Weisser
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package blackboard.sonar.plugins.css;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.Extension;
import org.sonar.api.Plugin;
import org.sonar.api.Properties;
import org.sonar.api.Property;

import blackboard.sonar.plugins.css.language.Css;
import blackboard.sonar.plugins.css.rules.BlackboardCssProfile;
import blackboard.sonar.plugins.css.rules.CssRulesRepository;

/**
 * @author Kevin Weisser
 * 
 * This class is the entry point for all extensions
 */
public final class CssPlugin implements Plugin {
	
	private static final String KEY = "sonar-css-plugin";

	public String getDescription() {
		return getName() + " collects metrics on css code, such as lines of code, violations, documentation level...";
	}

	public List<Class<? extends Extension>> getExtensions() {
		List<Class<? extends Extension>> list = new ArrayList<Class<? extends Extension>>();

		// css language
		list.add(Css.class);
		
		// css files importer
		list.add(CssSourceImporter.class);

		// css rules repository
		list.add(CssRulesRepository.class);
		list.add(BlackboardCssProfile.class);

		// css sensor
	    list.add(CssSensor.class);

	    return list;
	}

	public String getKey() {
		return KEY;
	}

	public String getName() {
		return "Css plugin";
	}

	@Override
	public String toString() {
		return getKey();
	}
}