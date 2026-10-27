package name.jurgenei.gradle.antlr;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class XmlAstTsqlPluginFunctionalTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void registersTsqlXmlAstTask() throws Exception {
        final File projectDir = temporaryFolder.newFolder("functional-registers-tsql-xmlast");
        writeSettings(projectDir);
        writeBuildFile(projectDir, """
                plugins {
                    id 'java'
                    id 'name.jurgenei.gradle.antlr.tsql'
                }
                """);

        final BuildResult result = run(projectDir, "tasks", "--all");

        Assert.assertTrue("Expected tsqlXmlAst task to be listed", result.getOutput().contains("tsqlXmlAst"));
    }

    @Test
    public void preconfiguredDefaultsAreApplied() throws Exception {
        final File projectDir = temporaryFolder.newFolder("functional-tsql-defaults");
        writeSettings(projectDir);
        writeBuildFile(projectDir, """
                plugins {
                    id 'java'
                    id 'name.jurgenei.gradle.antlr.tsql'
                }

                tasks.register('printTsqlDefaults') {
                    doLast {
                        def t = tasks.named('tsqlXmlAst').get()
                        println "grammar=${t.grammar.get()}"
                        println "parserClassName=${t.parserClassName.get()}"
                        println "lexerClassName=${t.lexerClassName.get()}"
                        println "startRule=${t.startRule.get()}"
                        println "targetExtension=${t.targetExtension.get()}"
                        println "outputFormat=${t.outputFormat.get()}"
                    }
                }
                """);

        final BuildResult result = run(projectDir, "printTsqlDefaults");
        final String output = result.getOutput();

        Assert.assertTrue(output.contains("grammar=tsql"));
        Assert.assertTrue(output.contains("parserClassName=name.jurgenei.parsers.TSqlParser"));
        Assert.assertTrue(output.contains("lexerClassName=name.jurgenei.parsers.TSqlLexer"));
        Assert.assertTrue(output.contains("startRule=tsql_file"));
        Assert.assertTrue(output.contains("targetExtension=.xml"));
        Assert.assertTrue(output.contains("outputFormat=compact"));
    }

    @Test
    public void supportsXirOutputConfiguration() throws Exception {
        final File projectDir = temporaryFolder.newFolder("functional-tsql-xir-overrides");
        writeSettings(projectDir);
        writeBuildFile(projectDir, """
                plugins {
                    id 'java'
                    id 'name.jurgenei.gradle.antlr.tsql'
                }

                tasks.named('tsqlXmlAst', name.jurgenei.gradle.antlr.XmlAstTsqlGradleTask) {
                    targetExtension.set('.xir')
                    outputFormat.set('beautified')
                }

                tasks.register('printTsqlXirDefaults') {
                    doLast {
                        def t = tasks.named('tsqlXmlAst').get()
                        println "targetExtension=${t.targetExtension.get()}"
                        println "outputFormat=${t.outputFormat.get()}"
                    }
                }
                """);

        final BuildResult result = run(projectDir, "printTsqlXirDefaults");
        final String output = result.getOutput();

        Assert.assertTrue(output.contains("targetExtension=.xir"));
        Assert.assertTrue(output.contains("outputFormat=beautified"));
    }

    private static BuildResult run(final File projectDir, final String... args) {
        return GradleRunner.create()
                .withProjectDir(projectDir)
                .withArguments(args)
                .withPluginClasspath()
                .build();
    }

    private static void writeSettings(final File projectDir) throws Exception {
        Files.writeString(
                projectDir.toPath().resolve("settings.gradle"),
                "rootProject.name = 'tsql-plugin-functional-test'\n",
                StandardCharsets.UTF_8);
    }

    private static void writeBuildFile(final File projectDir, final String content) throws Exception {
        Files.writeString(
                projectDir.toPath().resolve("build.gradle"),
                content,
                StandardCharsets.UTF_8);
    }
}

