# Gradle ANTLR T-SQL Plugin

![Conformance](https://img.shields.io/badge/Conformance-Check--All%20Passing-brightgreen)

[![Release](https://img.shields.io/github/v/release/flowdigger/gradle-antlr-tsql-plugin?sort=semver)](https://central.sonatype.com/search?q=g:org.flowdigger.gradle%20AND%20a:gradle-antlr-tsql-plugin)
[![Maven Central](https://img.shields.io/maven-central/v/org.flowdigger.gradle/gradle-antlr-tsql-plugin?label=Maven%20Central)](https://central.sonatype.com/search?q=g:org.flowdigger.gradle%20AND%20a:gradle-antlr-tsql-plugin)

[![Build and Test](https://github.com/flowdigger/gradle-antlr-tsql-plugin/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/flowdigger/gradle-antlr-tsql-plugin/actions/workflows/ci.yml?query=branch%3Amain)
[![Coverage CI](https://github.com/flowdigger/gradle-antlr-tsql-plugin/actions/workflows/coverage.yml/badge.svg?branch=main)](https://github.com/flowdigger/gradle-antlr-tsql-plugin/actions/workflows/coverage.yml?query=branch%3Amain)
[![Coverage](https://codecov.io/gh/flowdigger/gradle-antlr-tsql-plugin/graph/badge.svg?branch=main)](https://app.codecov.io/gh/flowdigger/gradle-antlr-tsql-plugin?branch=main)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/java-21+-green.svg)](https://www.oracle.com/java/)
[![Gradle](https://img.shields.io/badge/gradle-8+-blue.svg)](https://gradle.org/)

`gradle-antlr-tsql-plugin` provides preconfigured XML AST task support for T-SQL parsing workflows.

It builds on `name.flowdigger.gradle.antlr` and offers task defaults tailored for `TSqlLexer`/`TSqlParser` use cases.

## Source Grammar

This plugin vendors ANTLR grammars from:

- https://github.com/antlr/grammars-v4/tree/master/sql/tsql

Included grammar files:

- `src/main/antlr/name/flowdigger/parsers/TSqlLexer.g4`
- `src/main/antlr/name/flowdigger/parsers/TSqlParser.g4`

## Use Cases

- Convert large T-SQL corpora to XML AST for lineage extraction
- Validate parser compatibility in CI against SQL fixtures
- Run parser checks with sensible defaults and minimal Gradle setup
- Keep conversion as first-class Gradle tasks (repeatable and automatable)

## Install

```groovy
plugins {
	id 'name.flowdigger.gradle.antlr.tsql' version '0.1.0'
}
```

## What Plugin Adds

- `XmlAstTsqlGradleTask` task type
- `tsqlXmlAst` pre-registered task
- Runtime classpath and `classes` dependency wiring when `java` plugin present

Default task conventions:

- `grammar = tsql`
- `parserClassName = name.flowdigger.parsers.TSqlParser`
- `lexerClassName = name.flowdigger.parsers.TSqlLexer`
- `startRule = tsql_file`
- `includes = ['**/*.sql']`

## Quick Start

```groovy
plugins {
	id 'java'
	id 'name.flowdigger.gradle.antlr.tsql' version '0.1.0'
}

tasks.named('tsqlXmlAst', name.flowdigger.gradle.antlr.XmlAstTsqlGradleTask) {
	sourceDirectory.set(layout.projectDirectory.dir('src/test/resources/tsql'))
	destinationDirectory.set(layout.buildDirectory.dir('xmlast-tsql'))
	targetExtension.set('.xml')
	continueOnError.set(true)
}
```

S-expression output variant:

```groovy
tasks.named('tsqlXmlAst', name.flowdigger.gradle.antlr.XmlAstTsqlGradleTask) {
	targetExtension.set('.xir')
	xirFormat.set('beautified')
}
```

- `targetExtension`: `.xml` (default) or `.xir`
- `xirFormat`: `compact` (default) or `beautified`

Run:

```bash
./gradlew tsqlXmlAst
```

## Common Workflow

```bash
./gradlew clean check tsqlXmlAst
```

Supporting tasks used in repository:

- `generateLexerSources`
- `generateParserSources`
- `compileAntlrSources`
- `verifyGrammarSources`
- `xmlast` (wrapper task for sample conversion)

## Development

```bash
./gradlew clean test
./gradlew publishToMavenLocal
```

## Troubleshooting

- `ClassNotFoundException` for parser/lexer classes:
  - Ensure `compileAntlrSources` ran
  - Ensure runtime classpath includes generated classes
- Parse starts but no output files:
  - Verify `sourceDirectory` and `includes`
  - Set `force = true` for full pass
- Start rule issues:
  - Confirm parser entry method exists (default `tsql_file`)
