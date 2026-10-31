# Samples (gradle-antlr-tsql-plugin)

![Conformance](https://img.shields.io/badge/Conformance-Check--All%20Passing-brightgreen)

This directory contains Gradle build use-case examples for validating T-SQL sample SQL with `xmlast` task.

## Files

- `use-case-01-direct/build.gradle` - direct parser/lexer/startRule configuration.
- `use-case-02-catalog/build.gradle` - catalog-driven configuration.
- `use-case-02-catalog/catalog.xml` - catalog file used by catalog sample.
- `use-case-03-check-hook/build.gradle` - wiring `xmlast` into `check`.

These files are designed as copy/paste templates for grammar module where:

- parser class: `name.flowdigger.parsers.TSqlParser`
- lexer class: `name.flowdigger.parsers.TSqlLexer`
- parser entry rule: `tsql_file`
- sample directory: `src/test/resources/tsql`
