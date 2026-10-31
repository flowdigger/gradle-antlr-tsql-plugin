package org.flowdigger.gradle.antlr;

import org.gradle.api.model.ObjectFactory;
import org.gradle.work.DisableCachingByDefault;

import javax.inject.Inject;
import java.util.List;

/**
 * T-SQL-flavored {@link XmlAstGradleTask} with parser defaults preconfigured.
 */
@DisableCachingByDefault(because = "XmlAstGradleTask performs external parser loading and file-system driven conversion not yet declared for safe caching")
public abstract class XmlAstTsqlGradleTask extends XmlAstGradleTask {

    /**
     * Creates preconfigured T-SQL XML AST task.
     *
     * @param objects Gradle object factory.
     */
    @Inject
    public XmlAstTsqlGradleTask(final ObjectFactory objects) {
        super(objects);
        LanguageTaskDefaults.of(
                "tsql",
                "org.flowdigger.parsers.TSqlParser",
                "org.flowdigger.parsers.TSqlLexer",
                "tsql_file",
                List.of("**/*.sql"))
            .applyTo(this);
    }
}

