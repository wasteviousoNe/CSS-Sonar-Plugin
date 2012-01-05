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

package blackboard.sonar.plugins.css.analyzers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.sonar.api.measures.CoreMetrics;

import blackboard.sonar.plugins.css.node.Node;
import blackboard.sonar.plugins.css.node.TextNode;
import blackboard.sonar.plugins.css.visitor.DefaultNodeVisitor;
import blackboard.sonar.plugins.css.visitor.CssSourceCode;

/**
 * Count lines of code in css files.
 *
 * @author Kevin Weisser
 * @since 1.0
 */
public class PageCountLines extends DefaultNodeVisitor {

	private static final Logger LOG = LoggerFactory.getLogger(PageCountLines.class);

	private int blankLines;
	private int commentLines;
	private int linesOfCode;

	@Override
	public void startDocument(CssSourceCode cssSourceCode, List<Node> nodes) {
		super.startDocument(cssSourceCode, nodes);

		linesOfCode = 0;
		blankLines = 0;
		commentLines = 0;

		count(nodes);
	}

	private void addMeasures() {

		getCssSourceCode().addMeasure(CoreMetrics.LINES, (double) linesOfCode + commentLines + blankLines);
		getCssSourceCode().addMeasure(CoreMetrics.NCLOC, linesOfCode);
		getCssSourceCode().addMeasure(CoreMetrics.COMMENT_LINES, commentLines);

		LOG.debug("CssSensor: " + getCssSourceCode().toString() + ":" + linesOfCode + "," + commentLines + "," + blankLines);
	}

	private void count(List<Node> nodeList) {
		for (int i = 0; i < nodeList.size(); i++) {
			Node node = nodeList.get(i);
			Node previousNode = i > 0? nodeList.get(i - 1) : null;
			Node nextNode = i < nodeList.size() - 1 ? nodeList.get(i) : null;
			handleToken(node, previousNode, nextNode);
		}
		addMeasures();
	}

	private void handleToken(Node node, Node previousNode, Node nextNode) {

		int linesOfCodeCurrentNode = node.getLinesOfCode();
		if (nextNode == null) {
			linesOfCodeCurrentNode ++;
		}

		switch (node.getNodeType()) {
			case Css:
				linesOfCode += linesOfCodeCurrentNode;
				break;
			case Text:
				handleTextToken((TextNode) node, previousNode, linesOfCodeCurrentNode);
				break;
			case Comment:
				commentLines += linesOfCodeCurrentNode;
				break;
			default:
				break;
		}
	}
	
	private void handleTextToken(TextNode textNode, Node previousNode, int linesOfCodeCurrentNode) {

		if (textNode.isBlank() && linesOfCodeCurrentNode > 0) {
			int nonBlankLines = 0;
			// add one newline to the previous node
		    if (previousNode != null) {
		    	switch (previousNode.getNodeType()) {
		        	case Comment:
		        		commentLines++;
		        		nonBlankLines++;
		        		break;
		        	case Css:
		        		linesOfCode++;
		        		nonBlankLines++;
		        		break;
		        	default:
		        		break;
		        }
		    }

		    // remaining newlines are added to blanklines
		    if (linesOfCodeCurrentNode > 0) {
		    	blankLines += linesOfCodeCurrentNode - nonBlankLines;
		    }
		} else {
			linesOfCode += linesOfCodeCurrentNode;
		}
	}	
}