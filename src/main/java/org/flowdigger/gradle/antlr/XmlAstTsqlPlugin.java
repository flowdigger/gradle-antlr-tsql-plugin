package org.flowdigger.gradle.antlr;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * Registers a T-SQL-specific XML AST task type preconfigured from {@link XmlAstTsqlGradleTask}.
 */
public final class XmlAstTsqlPlugin implements Plugin<Project> {

    /**
     * Creates T-SQL XML AST plugin.
     */
    public XmlAstTsqlPlugin() {
    }

    @Override
    public void apply(final Project project) {
        LanguagePluginSupport.registerXmlAstTask(
                project,
                "tsqlXmlAst",
                XmlAstTsqlGradleTask.class,
                "Convert T-SQL file trees to XML AST output.");
        LanguagePluginSupport.wireJavaRuntimeClasspath(project, XmlAstTsqlGradleTask.class);
    }
}

