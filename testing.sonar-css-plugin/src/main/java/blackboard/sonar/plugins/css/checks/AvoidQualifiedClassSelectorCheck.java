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
package blackboard.sonar.plugins.css.checks;

import blackboard.sonar.plugins.css.ProjectConfiguration;
import blackboard.sonar.plugins.css.node.CssNode;

import org.sonar.check.IsoCategory;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import org.w3c.dom.css.CSSStyleRule;

/**
 * Checker to find universal selectors.
 * 
 * @author Kevin Weisser
 * @since 1.0
 */
@Rule(key = "AvoidQualifiedClassSelectorCheck", name = "Avoid Qualified Class Selector", 
    description = "Avoid unnecessarily qualifying class selectors", priority = Priority.MINOR, isoCategory = IsoCategory.Efficiency)
public class AvoidQualifiedClassSelectorCheck extends AbstractStyleCheck {

	@Override
	public void rule(CssNode node) {
		CSSStyleRule _rule = node.getRule();
		
		String selector = _rule.getSelectorText();
		String split[] = selector.split(" ");
		
		for (int x = 0; x < split.length; x++) {
			String str = split[x];
			
			//Parser prefixes unqualified class selectors with a wildcard. Remove this character.
			if (str.length() > 1 && str.startsWith(ProjectConfiguration.UNIVERSAL_SELECTOR)) {
				str = str.substring(1);
			}
			
			if (str != null && str.contains(ProjectConfiguration.CLASS_SELECTOR) && str.charAt(0) != ProjectConfiguration.PERIOD) {
				createViolation(node);
				break;
			}
		}
	}
}
