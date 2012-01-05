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

import blackboard.sonar.plugins.css.node.TextNode;

import org.sonar.check.IsoCategory;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleRule;

/**
 * Checker to find malformed selectors.
 * 
 * @author Kevin Weisser
 * @since 1.0
 */
@Rule(key = "AvoidMalformedSelectorCheck", name = "Avoid Malformed Selector", 
    description = "Avoid selectors with bad syntax", priority = Priority.CRITICAL, isoCategory = IsoCategory.Efficiency)
public class AvoidMalformedSelectorCheck extends AbstractStyleCheck {

	@Override
	public void text(TextNode node) {
		if (node.getInvalidSelector().booleanValue()) {
			createViolation(node);
		}
	}
}
