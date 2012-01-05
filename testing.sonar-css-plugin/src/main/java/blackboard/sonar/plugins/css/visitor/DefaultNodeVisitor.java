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
package blackboard.sonar.plugins.css.visitor;

import java.util.List;

import blackboard.sonar.plugins.css.node.CommentNode;
import blackboard.sonar.plugins.css.node.CssNode;
import blackboard.sonar.plugins.css.node.Node;
import blackboard.sonar.plugins.css.node.TextNode;

/**
 * Defines interface for node visitor with default dummy implementations.
 * 
 * @author Kevin Weisser
 */
public abstract class DefaultNodeVisitor implements NodeVisitor {

	private CssSourceCode cssSourceCode;

	public void rule(CssNode cssNode) { }
	
	public void comment(CommentNode node) { }
  
	public void text(TextNode node) { }
	
	public void endDocument() { }

	public CssSourceCode getCssSourceCode() {
		return cssSourceCode;
	}

	public void startDocument(CssSourceCode cssSourceCode, List<Node> nodes) {
		startDocument(cssSourceCode);
	}

	public void startDocument(CssSourceCode cssSourceCode) {
		this.cssSourceCode = cssSourceCode;
	}
}
