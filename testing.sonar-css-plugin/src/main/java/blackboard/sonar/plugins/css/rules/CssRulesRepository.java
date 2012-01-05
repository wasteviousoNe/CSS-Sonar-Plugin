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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.rules.ActiveRuleParam;
import org.sonar.api.rules.AnnotationRuleParser;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleRepository;
import org.sonar.api.utils.AnnotationUtils;
import org.sonar.api.utils.SonarException;
import org.sonar.check.Cardinality;
import blackboard.sonar.plugins.css.checks.AbstractStyleCheck;
import blackboard.sonar.plugins.css.language.Css;

/**
 * Repository for css rules.
 *
 * @author Kevin Weisser
 * @since 1.0
 */
public final class CssRulesRepository extends RuleRepository {

	private static final Logger LOG = LoggerFactory.getLogger(CssRulesRepository.class);

	public static final String REPOSITORY_NAME = "Css";
	public static final String REPOSITORY_KEY = "Css";

	public CssRulesRepository() {
		super(REPOSITORY_KEY, Css.KEY);
		setName(REPOSITORY_NAME);
	}

	@Override
	public List<Rule> createRules() {
		// In Sonar 2.3, the AnnotationRuleParser cannot be injected in the constructor.
		// It would give an exception for unsatisfiable dependencies.
		AnnotationRuleParser annotationRuleParser = new AnnotationRuleParser();

		List<Rule> rules = annotationRuleParser.parse(REPOSITORY_KEY, CheckClasses.getCheckClasses());
		for (Rule rule : rules) {
			rule.setCardinality(Cardinality.MULTIPLE);
		}
		return rules;
	}

	/**
	 * Instantiate checks as defined in the RulesProfile.
	 *
	 * @param profile
	 */
	public static List<AbstractStyleCheck> createChecks(RulesProfile profile) {
		LOG.info("Loading checks for profile " + profile.getName());

		List<AbstractStyleCheck> checks = new ArrayList<AbstractStyleCheck>();

		for (ActiveRule activeRule : profile.getActiveRules()) {
			if (REPOSITORY_KEY.equals(activeRule.getRepositoryKey())) {
				Class<AbstractStyleCheck> checkClass = getCheckClass(activeRule);
				if (checkClass != null) {
					checks.add(createCheck(checkClass, activeRule));
				}
			}
		}

		return checks;
	}

	private static AbstractStyleCheck createCheck(Class<AbstractStyleCheck> checkClass, ActiveRule activeRule) {

		try {
			AbstractStyleCheck check = checkClass.newInstance();
			check.setRule(activeRule.getRule());
			if (activeRule.getActiveRuleParams() != null) {
				for (ActiveRuleParam param : activeRule.getActiveRuleParams()) {
					if (!StringUtils.isEmpty(param.getValue())) {
						LOG.debug("Rule param " + param.getKey() + " = " + param.getValue());
						BeanUtils.setProperty(check, param.getRuleParam().getKey(), param.getValue());
					}
				}
			}

			return check;
		} catch (IllegalAccessException e) {
			throw new SonarException(e);
		} catch (InvocationTargetException e) {
			throw new SonarException(e);
		} catch (InstantiationException e) {
			throw new SonarException(e);
		}
	}

	private static Class<AbstractStyleCheck> getCheckClass(ActiveRule activeRule) {
		for (Class<?> checkClass : CheckClasses.getCheckClasses()) {
			org.sonar.check.Rule ruleAnnotation = AnnotationUtils.getClassAnnotation(checkClass, org.sonar.check.Rule.class);
      
			if (ruleAnnotation.key().equals(activeRule.getConfigKey())) {
				return (Class<AbstractStyleCheck>) checkClass;
			}
		}
		LOG.error("Could not find check class for config key " + activeRule.getConfigKey());
		return null;
	}
}