package org.flowdigger.parsers;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class TSqlLexerParserTest {

    private List<File> testSqlFiles;
    private Constructor<?> lexerConstructor;
    private Constructor<?> parserConstructor;
    private Method parseEntryMethod;

    @Before
    public void setupTestFiles() throws Exception {
        testSqlFiles = TestResourceDirectoryProvider.getSqlFilesInDirectory(new File("src/test/resources/tsql"));
        Assert.assertFalse("No SQL test files found in src/test/resources/tsql", testSqlFiles.isEmpty());
        loadParserClasses();
    }

    private void loadParserClasses() throws Exception {
        try {
            final Class<?> lexerClass = Class.forName("org.flowdigger.parsers.TSqlLexer");
            final Class<?> parserClass = Class.forName("org.flowdigger.parsers.TSqlParser");

            lexerConstructor = lexerClass.getConstructor(CharStream.class);
            parserConstructor = parserClass.getConstructor(org.antlr.v4.runtime.TokenStream.class);
            parseEntryMethod = parserClass.getMethod("tsql_file");
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Could not load ANTLR-generated parser classes. Ensure compileAntlrSources task completed.", ex);
        }
    }

    @Test
    public void canParseTsqlTestFiles() throws Exception {
        for (File sqlFile : testSqlFiles) {
            try (InputStream inputStream = Files.newInputStream(sqlFile.toPath())) {
                final CharStream charStream = CharStreams.fromStream(inputStream, StandardCharsets.UTF_8);
                final Lexer lexer = (Lexer) lexerConstructor.newInstance(charStream);
                final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
                final Parser parser = (Parser) parserConstructor.newInstance(tokenStream);

                parser.removeErrorListeners();
                parser.addErrorListener(new BaseErrorListener() {
                    @Override
                    public void syntaxError(final Recognizer<?, ?> recognizer,
                                            final Object offendingSymbol,
                                            final int line,
                                            final int charPositionInLine,
                                            final String msg,
                                            final RecognitionException e) {
                        Assert.fail("Parse error in " + sqlFile.getName() + " at line " + line + ": " + msg);
                    }
                });

                final Object tree = parseEntryMethod.invoke(parser);
                Assert.assertNotNull("Parse tree should not be null for " + sqlFile.getName(), tree);
            }
        }
    }

    @Test
    public void lexerTokenizesInput() throws Exception {
        final File testFile = testSqlFiles.get(0);
        try (InputStream inputStream = Files.newInputStream(testFile.toPath())) {
            final CharStream charStream = CharStreams.fromStream(inputStream, StandardCharsets.UTF_8);
            final Lexer lexer = (Lexer) lexerConstructor.newInstance(charStream);
            final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            tokenStream.fill();

            Assert.assertTrue("Lexer should produce at least one token", tokenStream.getNumberOfOnChannelTokens() > 0);
        }
    }
}

