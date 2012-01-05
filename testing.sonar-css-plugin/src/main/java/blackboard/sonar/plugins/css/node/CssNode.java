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
package blackboard.sonar.plugins.css.node;

import org.w3c.dom.css.CSSStyleRule;

/**
 * @author Kevin Weisser
 */
public class CssNode extends Node {

	private CSSStyleRule rule;

	public CssNode() {
		super(NodeType.Css);
	}

	public CSSStyleRule getRule() {
		return rule;
	}

	public void setRule(CSSStyleRule rule) {
		this.rule = rule;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		String NEW_LINE = System.getProperty("line.separator");
		result.append("Node {" + NEW_LINE);
		result.append("     Code: " + super.getCode() + NEW_LINE);
		result.append("     End Column Position: " + super.getEndColumnPosition() + NEW_LINE);
		result.append("     End Line Position: " + super.getEndLinePosition() + NEW_LINE);
		result.append("     Type: " + super.getNodeType().toString() + NEW_LINE);
		result.append("     Start Column Position: " + super.getStartColumnPosition() + NEW_LINE);
		result.append("     Start Line Position: " + super.getStartLinePosition() + NEW_LINE);
		result.append("     Rule: " + rule.getSelectorText() + NEW_LINE);
		result.append("}" + NEW_LINE);
		return result.toString();
	}
	
}