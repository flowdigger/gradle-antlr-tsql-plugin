# Gradle ANTLR T-SQL Plugin

![Conformance](https://img.shields.io/badge/Conformance-Check--All%20Passing-brightgreen)

[![Build](https://github.com/flowdigger/gradle-antlr-tsql-plugin/actions/workflows/ci.yml/badge.svg)](https://github.com/flowdigger/gradle-antlr-tsql-plugin/actions/workflows/ci.yml)
[![Release](https://github.com/flowdigger/gradle-antlr-tsql-plugin/actions/workflows/release.yml/badge.svg)](https://github.com/flowdigger/gradle-antlr-tsql-plugin/actions/workflows/release.yml)
[![Coverage CI](https://github.com/flowdigger/gradle-antlr-tsql-plugin/actions/workflows/coverage.yml/badge.svg)](https://github.com/flowdigger/gradle-antlr-tsql-plugin/actions/workflows/coverage.yml)
[![CodeQL](https://github.com/flowdigger/gradle-antlr-tsql-plugin/actions/workflows/codeql.yml/badge.svg)](https://github.com/flowdigger/gradle-antlr-tsql-plugin/actions/workflows/codeql.yml)
[![Dependency Check](https://github.com/flowdigger/gradle-antlr-tsql-plugin/actions/workflows/dependency-check.yml/badge.svg)](https://github.com/flowdigger/gradle-antlr-tsql-plugin/actions/workflows/dependency-check.yml)
[![SpotBugs Security](https://github.com/flowdigger/gradle-antlr-tsql-plugin/actions/workflows/spotbugs-security.yml/badge.svg)](https://github.com/flowdigger/gradle-antlr-tsql-plugin/actions/workflows/spotbugs-security.yml)
[![Dependabot](https://img.shields.io/badge/dependabot-enabled-025E8C?logo=dependabot)](https://github.com/flowdigger/gradle-antlr-tsql-plugin/security/dependabot)
[![Coverage](https://codecov.io/gh/flowdigger/gradle-antlr-tsql-plugin/graph/badge.svg?branch=main)](https://codecov.io/gh/flowdigger/gradle-antlr-tsql-plugin?branch=main)
[![Maven Central](https://img.shields.io/maven-central/v/org.flowdigger.gradle/gradle-antlr-tsql-plugin.svg)](https://search.maven.org/artifact/org.flowdigger.gradle/gradle-antlr-tsql-plugin)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/java-21+-green.svg)](https://www.oracle.com/java/)
[![Gradle](https://img.shields.io/badge/gradle-9.5+-blue.svg)](https://gradle.org/)

`gradle-antlr-tsql-plugin` provides preconfigured XML AST task support for T-SQL parsing workflows.

It builds on `org.flowdigger.antlr` and offers task defaults tailored for `TSqlLexer`/`TSqlParser` use cases.

## Source Grammar

This plugin vendors ANTLR grammars from:

- https://github.com/antlr/grammars-v4/tree/master/sql/tsql

Included grammar files:

- `src/main/antlr/org/flowdigger/parsers/TSqlLexer.g4`
- `src/main/antlr/org/flowdigger/parsers/TSqlParser.g4`

## Use Cases

- Convert large T-SQL corpora to XML AST for lineage extraction
- Validate parser compatibility in CI against SQL fixtures
- Run parser checks with sensible defaults and minimal Gradle setup
- Keep conversion as first-class Gradle tasks (repeatable and automatable)

## Install

```groovy
plugins {
	id 'org.flowdigger.antlr.tsql' version '1.0.0'
}
```


## What Plugin Adds

- `XmlAstTsqlGradleTask` task type
- `tsqlXmlAst` pre-registered task
- Runtime classpath and `classes` dependency wiring when `java` plugin present

Default task conventions:

- `grammar = tsql`
- `parserClassName = org.flowdigger.parsers.TSqlParser`
- `lexerClassName = org.flowdigger.parsers.TSqlLexer`
- `startRule = tsql_file`
- `includes = ['**/*.sql']`

## Quick Start

```groovy
plugins {
	id 'java'
	id 'org.flowdigger.antlr.tsql' version '1.0.0'
}

tasks.named('tsqlXmlAst', org.flowdigger.gradle.antlr.XmlAstTsqlGradleTask) {
	sourceDirectory.set(layout.projectDirectory.dir('src/test/resources/tsql'))
	destinationDirectory.set(layout.buildDirectory.dir('xmlast-tsql'))
	targetExtension.set('.xml')
	continueOnError.set(true)
}
```

S-expression output variant:

```groovy
tasks.named('tsqlXmlAst', org.flowdigger.gradle.antlr.XmlAstTsqlGradleTask) {
	targetExtension.set('.xir')
	outputFormat.set('beautified')
}
```

- `targetExtension`: `.xml` (default) or `.xir`
- `outputFormat`: `compact` (default) or `beautified`

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

## Release to Maven Central

Set credentials and signing material:

```bash
export ORG_GRADLE_PROJECT_mavenCentralUsername=...
export ORG_GRADLE_PROJECT_mavenCentralPassword=...
export ORG_GRADLE_PROJECT_signingKey=...
export ORG_GRADLE_PROJECT_signingPassword=...
export ORG_GRADLE_PROJECT_signingKeyId=...
```

Build signed Central bundle:

```bash
./gradlew clean packageCentralBundle
```

Release workflow (`.github/workflows/release.yml`) uploads bundle to Sonatype Central Publisher API on `v*` tags (or manual dispatch).
Configure repository secrets: `MAVEN_CENTRAL_USERNAME`, `MAVEN_CENTRAL_PASSWORD`, `SIGNING_KEY`, `SIGNING_PASSWORD`, `SIGNING_KEY_ID`.
