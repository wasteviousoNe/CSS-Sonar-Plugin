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
package blackboard.sonar.plugins.css.rules;

import java.util.Arrays;
import java.util.List;

import blackboard.sonar.plugins.css.checks.AvoidUniversalSelectorCheck;
import blackboard.sonar.plugins.css.checks.AvoidMalformedSelectorCheck;
import blackboard.sonar.plugins.css.checks.AvoidDeepSelectorCheck;
import blackboard.sonar.plugins.css.checks.AvoidQualifiedClassSelectorCheck;
import blackboard.sonar.plugins.css.checks.AvoidQualifiedIDSelectorCheck;

/**
 * Provides a list of available checks.
 *
 * @author Kevin Weisser
 * @since 1.0
 */
final class CheckClasses {

	private static final Class[] CLASSES = new Class[] {
		AvoidUniversalSelectorCheck.class,
		AvoidMalformedSelectorCheck.class,
		AvoidDeepSelectorCheck.class,
		AvoidQualifiedClassSelectorCheck.class,
		AvoidQualifiedIDSelectorCheck.class
	};

	/**
	 * Gets the list of XML checks.
	 */
	public static List<Class> getCheckClasses() {
		return Arrays.asList(CLASSES);
	}

	private CheckClasses() { }
}
