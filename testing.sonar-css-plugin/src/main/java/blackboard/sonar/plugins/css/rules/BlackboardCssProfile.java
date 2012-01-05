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
package blackboard.sonar.plugins.css.rules;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.profiles.XMLProfileParser;
import org.sonar.api.utils.ValidationMessages;

/**
 * Default Blackboard CSS profile with all available checks.
 *
 * @author Kevin Weisser
 * @since 1.0
 */
public final class BlackboardCssProfile extends ProfileDefinition {

	public static final String ALL_RULES = "blackboard/sonar/plugins/css/rules/css/css-rules.xml";

	private final XMLProfileParser profileParser;

	public BlackboardCssProfile(XMLProfileParser profileParser) {
		this.profileParser = profileParser;
	}

	@Override
	public RulesProfile createProfile(ValidationMessages validation) {
		Reader reader = new InputStreamReader(BlackboardCssProfile.class.getClassLoader().getResourceAsStream(ALL_RULES), Charset.forName(CharEncoding.UTF_8));

		try {
			RulesProfile profile = profileParser.parse(reader, validation);
			profile.setDefaultProfile(true);
			return profile;
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}
}