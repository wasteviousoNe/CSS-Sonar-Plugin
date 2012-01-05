/*s
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

import blackboard.sonar.plugins.css.ProjectConfiguration;
import blackboard.sonar.plugins.css.node.CssNode;
import blackboard.sonar.plugins.css.node.Node;

import org.w3c.dom.css.CSSStyleRule;

class StyleTokenizer extends AbstractTokenizer<List<Node>> {
	private final CSSStyleRule rule;
	
	public StyleTokenizer(String startToken, String endToken, CSSStyleRule rule) {
		super(startToken, endToken);
		this.rule = rule;
	}

	Node createNode() {
		CssNode node = new CssNode();
		node.setRule(rule);
		return node;
	}
	
	@Override
	public boolean consume(CodeReader codeReader, List<Node> nodeList) {
		//Read the unprocessed lines from the file
		String linesRead = readToEOF(codeReader);
		int indexForPrintable = -1;
		
		//Ensure that the first character is printable (ASCII value > 32) otherwise the non-printable
		//portions should be handled as a TextNode
		for (int x = 0; x < linesRead.length(); x++) {
			int ch = (int) linesRead.charAt(x);
			if (ch > 32) {
				indexForPrintable = x;
				break;
			}
		}
		
		//If the first character is printable and the remaining lines include an opening brace, read
		//without consuming characters the file up to the opening brace. This was required because
		//otherwise the peekTo API would enter an infinite loop.
		if (indexForPrintable == 0 && linesRead.contains(ProjectConfiguration.OPENING_BRACE)) {
			StringBuilder selector = new StringBuilder();
			codeReader.peekTo(beginStyleDeclarationMatcher, selector);
			
			//CSSParser rule by default omits line breaks, comments, and tabbing. We removed all spacing 
			//from the rule when setting the token.
			String compressed = selector.toString().replaceAll("\\n","").replaceAll("\\t","").replaceAll(" ","").replaceAll("/\\*([^*]|[\r\n]|(\\*+([^*/]|[\r\n])))*\\*+/", "");

			//Blackboard is not consistent with regard to adding/omitting quotes from attribute selectors.
			//Therefore, the starting token has removed all quotes we should remove any quotes that may or
			//may not be present in the file.
			compressed = compressed.replaceAll("=\"", "=").replaceAll("\"]", "]");
				
			if (compressed.equalsIgnoreCase(String.valueOf(super.startChars))) {
				Node node = createNode();
				StringBuilder stringBuilder = new StringBuilder();

				setStartPosition(codeReader, node);
				//Read and consume the characters up to the ending token
				codeReader.popTo(new EndStyleTokenMatcher(codeReader), stringBuilder);
				//Read and consume X characters from the file based on the length of the ending token
				for (int i = 0; i < super.endChars.length; i++) {
					codeReader.pop(stringBuilder);
				}
				
				node.setCode(stringBuilder.toString());
				setEndPosition(codeReader, node);
			
				//System.out.println(node.toString());
				
				addNode(nodeList, node);
			
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Read without consuming the remaining unprocessed file. This was needed to do so we can check if 
	 * any more opening braces are present in the file.
	 * 
	 * @param reader
	 * @return
	 */
	private String readToEOF(CodeReader reader) {
		String str = "", oldStr = "", temp = "";
		int length = 500;
		str = String.valueOf(reader.peek(length));
		temp = str.trim();
		while (!temp.equals(oldStr)){
			oldStr = new String(temp);
			length = length + 500;
			str = String.valueOf(reader.peek(length));
			temp = str.trim();
		}
		
		return str;
	}
	
	private final class BeginStyleDeclarationsMatcher implements EndMatcher {
		public boolean match(int endFlag) {
			return endFlag == ProjectConfiguration.OPEN_BRACE;
		}
	}
	
	private final EndMatcher beginStyleDeclarationMatcher = new BeginStyleDeclarationsMatcher();
	
	private final class EndStyleTokenMatcher implements EndMatcher {
		private final CodeReader codeReader;
		
		private EndStyleTokenMatcher(CodeReader codeReader) {
			this.codeReader = codeReader;
		}

		public boolean match(int endFlag) {
			char[] end = {ProjectConfiguration.CLOSE_BRACE};
			return ArrayUtils.isEquals(codeReader.peek(end.length), end);
		}
	}
}