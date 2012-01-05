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
package blackboard.sonar.plugins.css.lex;

import java.util.List;

import org.sonar.channel.CodeReader;

import blackboard.sonar.plugins.css.ProjectConfiguration;
import blackboard.sonar.plugins.css.node.Node;
import blackboard.sonar.plugins.css.node.TextNode;

/**
 * Tokenizer for content.
 *
 * @author Kevin Weisser
 * @since 1.0
 */
class TextTokenizer extends AbstractTokenizer<List<Node>> {

	public TextTokenizer() {
		super("", "");
	}

	@Override
	public boolean consume(CodeReader codeReader, List<Node> nodeList) {
		Node node = createNode();
		StringBuilder stringBuilder = new StringBuilder();
		
		setStartPosition(codeReader, node);
		
		//Read without consuming the first character
		char ch = codeReader.peek(1)[0];
		int chInt = (int) ch;
		
		//Charcter is printable
		if (chInt > 32) {
			//Read and consume until EOL is reached
			while (chInt != 10) {
				//If opening brace is detected this is likely a selector that could not be matched to
				//any existing style tokens found by the CSS parser
				if (ch == ProjectConfiguration.OPEN_BRACE) {
					node = createCopyNode(node, new Boolean(true));
				}
				codeReader.pop(stringBuilder);
				ch = codeReader.peek(1)[0];
				chInt = (int) ch;
			}
			//Consume EOL character
			codeReader.pop(stringBuilder);
		} else {
			//Read and consume whitespace, tabs, and other system characters until a printable character
			//reached
			while (chInt <= 32 && chInt > 0) {
				codeReader.pop(stringBuilder);
				ch = codeReader.peek(1)[0];
				chInt = (int) ch;
			}
			
			//Pop off the character if it's not printable this was added specifically to handle EOF
			if (chInt < 33) {
				codeReader.pop(stringBuilder);
			}
		}
		
		node.setCode(stringBuilder.toString());
		setEndPosition(codeReader, node);
		//System.out.println(node.toString());
		
		nodeList.add(node);

		return true;
	}

	@Override
	Node createNode() {
		return new TextNode();
	}
	
	private Node createCopyNode(Node node, Boolean invalid) {
		TextNode tNode = new TextNode(invalid);
		tNode.setStartLinePosition(node.getStartLinePosition());
		tNode.setStartColumnPosition(node.getStartColumnPosition());
		return tNode;
	}
}
