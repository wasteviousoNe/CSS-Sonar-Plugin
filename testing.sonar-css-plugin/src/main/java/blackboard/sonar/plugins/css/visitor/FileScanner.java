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

import java.util.ArrayList;
import java.util.List;

import blackboard.sonar.plugins.css.node.CommentNode;
import blackboard.sonar.plugins.css.node.CssNode;
import blackboard.sonar.plugins.css.node.Node;
import blackboard.sonar.plugins.css.node.TextNode;

/**
 * Scans the nodes of a page and send events to the visitors.
 *
 * @author Kevin Weisser
 * @since 1.0
 */
public class FileScanner {

	private final List<DefaultNodeVisitor> visitors = new ArrayList<DefaultNodeVisitor>();

	/**
	 * Add a visitor to the list of visitors.
	 */
	public void addVisitor(DefaultNodeVisitor visitor) {
		visitors.add(visitor);
	}

	/**
	 * Scan a list of Nodes and send events to the visitors.
	 */
	public void scan(List<Node> nodeList, CssSourceCode cssSourceCode) {
		// notify visitors for a new document
		for (DefaultNodeVisitor visitor : visitors) {
			visitor.startDocument(cssSourceCode, nodeList);
		}

		// notify the visitors for start and end of element
		for (Node node : nodeList) {
			for (DefaultNodeVisitor visitor : visitors) {
				scanElement(visitor, node);
			}
		}

		// notify visitors for end of document
		for (DefaultNodeVisitor visitor : visitors) {
			visitor.endDocument();
		}
	}

	/**
	 * Scan a single element and send appropriate event: start element, end element, characters, comment, expression or directive.
	 */
	private void scanElement(DefaultNodeVisitor visitor, Node node) {
		switch (node.getNodeType()) {
			case Css:
				visitor.rule((CssNode)node);
				break;
			case Comment:
				visitor.comment((CommentNode) node);
				break;
			case Text:
				visitor.text((TextNode) node);
				break;
		}
	}
}
