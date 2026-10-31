package org.flowdigger.gradle.antlr;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Assert;
import org.junit.Test;

public class XmlAstTsqlPluginTest {

    @Test
    public void registersPreconfiguredTaskType() {
        final Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("java");

        new XmlAstTsqlPlugin().apply(project);

        final Object task = project.getTasks().getByName("tsqlXmlAst");
        Assert.assertNotNull(task);
        Assert.assertTrue(task instanceof XmlAstTsqlGradleTask);
    }

    @Test
    public void preconfiguresTsqlDefaults() {
        final Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("java");

        new XmlAstTsqlPlugin().apply(project);

        final XmlAstTsqlGradleTask task = (XmlAstTsqlGradleTask) project.getTasks().getByName("tsqlXmlAst");
        Assert.assertEquals("tsql", task.getGrammar().get());
        Assert.assertEquals("org.flowdigger.parsers.TSqlParser", task.getParserClassName().get());
        Assert.assertEquals("org.flowdigger.parsers.TSqlLexer", task.getLexerClassName().get());
        Assert.assertEquals("tsql_file", task.getStartRule().get());
        Assert.assertTrue(task.getIncludes().get().contains("**/*.sql"));
        Assert.assertEquals(".xml", task.getTargetExtension().get());
        Assert.assertEquals("compact", task.getXirFormat().get());
    }

    @Test
    public void supportsXirOutputConfiguration() {
        final Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("java");

        new XmlAstTsqlPlugin().apply(project);

        final XmlAstTsqlGradleTask task = (XmlAstTsqlGradleTask) project.getTasks().getByName("tsqlXmlAst");
        task.getTargetExtension().set(".xir");
        task.getXirFormat().set("beautified");

        Assert.assertEquals(".xir", task.getTargetExtension().get());
        Assert.assertEquals("beautified", task.getXirFormat().get());
    }
}
