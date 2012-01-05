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
package blackboard.sonar.plugins.css;

import java.io.FileReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.checks.NoSonarFilter;
import org.sonar.api.design.Dependency;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.RangeDistributionBuilder;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.File;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.Violation;

import blackboard.sonar.plugins.css.analyzers.PageCountLines;
import blackboard.sonar.plugins.css.checks.AbstractStyleCheck;
import blackboard.sonar.plugins.css.language.Css;
import blackboard.sonar.plugins.css.lex.StyleLexer;
import blackboard.sonar.plugins.css.node.Node;
import blackboard.sonar.plugins.css.rules.CssRulesRepository;
import blackboard.sonar.plugins.css.visitor.FileScanner;
import blackboard.sonar.plugins.css.visitor.CssSourceCode;


/**
 * @author Kevin Weisser
 */
public final class CssSensor implements Sensor {
	
	private final static Number[] FILES_DISTRIB_BOTTOM_LIMITS = { 0, 5, 10, 20, 30, 60, 90 };

	private static final Logger LOG = LoggerFactory.getLogger(CssSensor.class);

	private final RulesProfile profile;

	public CssSensor(RulesProfile profile) {
		this.profile = profile;
	}

	public void analyse(Project project, SensorContext sensorContext) {
		ProjectConfiguration.configureSourceDir(project);

	    // configure the lexer
		final StyleLexer lexer = new StyleLexer();

	    // configure page scanner and the visitors
	    final FileScanner scanner = setupScanner();

	    for (java.io.File cssFile : project.getFileSystem().getSourceFiles(new Css(project))) {
	    	try {
	    		File resource = File.fromIOFile(cssFile, project.getFileSystem().getSourceDirs());
	    		
	    		CssSourceCode sourceCode = new CssSourceCode(resource);
	    		List<Node> nodeList = lexer.parse(new FileReader(cssFile), cssFile);
	    		scanner.scan(nodeList, sourceCode);
	    		saveMetrics(sensorContext, sourceCode);
	    	} catch (Exception e) {
	    		LOG.error("Could not analyze the file " + cssFile.getAbsolutePath(), e);
	    	}
	    }
	}

	private void saveMetrics(SensorContext sensorContext, CssSourceCode sourceCode) {
		saveComplexityDistribution(sensorContext, sourceCode);

		for (Measure measure : sourceCode.getMeasures()) {
			sensorContext.saveMeasure(sourceCode.getResource(), measure);
		}

		for (Violation violation : sourceCode.getViolations()) {
			sensorContext.saveViolation(violation);
		}
		
		for (Dependency dependency : sourceCode.getDependencies()) {
			sensorContext.saveDependency(dependency);
		}
	}

	private void saveComplexityDistribution(SensorContext sensorContext, CssSourceCode sourceCode) {
		if (sourceCode.getMeasure(CoreMetrics.COMPLEXITY) != null) {
			RangeDistributionBuilder complexityFileDistribution = new RangeDistributionBuilder(CoreMetrics.FILE_COMPLEXITY_DISTRIBUTION, FILES_DISTRIB_BOTTOM_LIMITS);
			complexityFileDistribution.add(sourceCode.getMeasure(CoreMetrics.COMPLEXITY).getValue());
			sensorContext.saveMeasure(sourceCode.getResource(), complexityFileDistribution.build().setPersistenceMode(PersistenceMode.MEMORY));
		}
	}

	/**
	 * Create FileScanner with Visitors.
	 */
	private FileScanner setupScanner() {
		FileScanner scanner = new FileScanner();
		
		for (AbstractStyleCheck check : CssRulesRepository.createChecks(profile)) {
			scanner.addVisitor(check);
		}
		
		scanner.addVisitor(new PageCountLines());
		
		return scanner;
	}

	/**
	 * This sensor only executes on Css projects.
	 */
	public boolean shouldExecuteOnProject(Project project) {
		return Css.KEY.equals(project.getLanguageKey());
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}