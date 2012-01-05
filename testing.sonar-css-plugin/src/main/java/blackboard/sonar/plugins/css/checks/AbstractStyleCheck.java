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

import org.apache.commons.lang.StringUtils;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.Violation;

import blackboard.sonar.plugins.css.node.Node;
import blackboard.sonar.plugins.css.visitor.DefaultNodeVisitor;

/**
 * @author Kevin Weisser
 */
public abstract class AbstractStyleCheck extends DefaultNodeVisitor {
	
	private Rule rule;

	protected final void createViolation(int linePosition) {
		createViolation(linePosition, rule.getDescription());
	}

	protected final void createViolation(int linePosition, String message) {
		Violation violation = Violation.create(rule, getCssSourceCode().getResource());
		violation.setMessage(message);
		violation.setLineId(linePosition);
		getCssSourceCode().addViolation(violation);
	}

	protected final void createViolation(Node node) {
		createViolation(node.getStartLinePosition());
	}

	public final Rule getRule() {
		return rule;
	}

	public final String getRuleKey() {
		return rule.getConfigKey();
	}

	public final void setRule(Rule rule) {
		this.rule = rule;
	}
}