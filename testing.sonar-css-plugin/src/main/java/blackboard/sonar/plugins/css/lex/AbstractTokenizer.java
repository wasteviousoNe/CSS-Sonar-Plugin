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

import org.apache.commons.lang.ArrayUtils;
import org.sonar.channel.Channel;
import org.sonar.channel.CodeReader;
import org.sonar.channel.EndMatcher;

import blackboard.sonar.plugins.css.node.Node;

/**
 * AbstractTokenizer provides basic token parsing.
 *
 * @author Kevin Weisser
 * @since 1.0
 */
abstract class AbstractTokenizer<T extends List<Node>> extends Channel<T> {

	private final class EndTokenMatcher implements EndMatcher {
		private final CodeReader codeReader;
		
		private EndTokenMatcher(CodeReader codeReader) {
			this.codeReader = codeReader;
		}

		public boolean match(int endFlag) {
			return endFlag == endChars[0] && ArrayUtils.isEquals(codeReader.peek(endChars.length), endChars);
		}
	}

	protected final char[] endChars;
	protected final char[] startChars;

	public AbstractTokenizer(String startChars, String endChars) {
		this.startChars = startChars.toCharArray();
		this.endChars = endChars.toCharArray();
	}

	protected void addNode(List<Node> nodeList, Node node) {
		nodeList.add(node);
	}

	@Override
	public boolean consume(CodeReader codeReader, T nodeList) {
		//Read the first X characters from file without consuming them and compare them to 
		//the starting token
		if (ArrayUtils.isEquals(codeReader.peek(startChars.length), startChars)) {
			Node node = createNode();
			setStartPosition(codeReader, node);
			
			//Create an Appendable object
			StringBuilder stringBuilder = new StringBuilder();
			//Read and consume the characters up to the ending token
			codeReader.popTo(new EndTokenMatcher(codeReader), stringBuilder);
			
			//Read and consume X characters from the file based on the length of the ending token
			for (int i = 0; i < endChars.length; i++) {
				codeReader.pop(stringBuilder);
			}
			
			node.setCode(stringBuilder.toString());
			setEndPosition(codeReader, node);

			addNode(nodeList, node);

			return true;
		} else {
			return false;
		}
	}

	abstract Node createNode();

	protected final void setEndPosition(CodeReader code, Node node) {
		node.setEndLinePosition(code.getLinePosition());
		node.setEndColumnPosition(code.getColumnPosition());
	}

	protected final void setStartPosition(CodeReader code, Node node) {
		node.setStartLinePosition(code.getLinePosition());
		node.setStartColumnPosition(code.getColumnPosition());
	}
}