# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

Celerio is a Maven-based code generator for data-driven Java applications. It reverses a database schema (via JDBC) and generates full CRUD applications from Velocity templates. It is a multi-module Maven project targeting Java 8 (currently being migrated to Java 25 — see `JAVA8TO25.md`).

## Build commands

```bash
# Build all modules
mvn install

# Build only celerio-engine (fastest iteration loop)
mvn -pl celerio-engine install

# Build without tests
mvn install -DskipTests

# Run all tests in celerio-engine
mvn -pl celerio-engine test

# Run a single test class
mvn -pl celerio-engine test -Dtest=MiscUtilTest

# Run integration tests (requires -Pit profile)
mvn integration-test -Pit

# Generate JavaDoc
mvn javadoc:javadoc
```

> **Java version:** The project currently compiles under Java 8. Java 25 is installed at `/home/herve/jdk-25.0.2+10`. To use it: `export JAVA_HOME=/home/herve/jdk-25.0.2+10` before running Maven.

## Module structure

| Module | Role |
|---|---|
| `celerio-engine` | Core generation engine — database model, factories, Velocity template runner, XML config marshalling |
| `celerio-maven/celerio-maven-plugin` | Maven plugin exposing `generate` and `cleanGenerated` Mojos |
| `celerio-maven/dbmetadata-maven-plugin` | Maven plugin exposing `extract-metadata` — reverses a live DB into an XML file |
| `celerio-maven/bootstrap-maven-plugin` | Maven plugin exposing `bootstrap` — scaffolds a new project from scratch |
| `celerio-spi-example` | Example of extending Celerio via the SPI |

## Architecture: how generation works

The generation pipeline lives entirely in `celerio-engine`:

1. **XML config loading** — `Celerio` (config) and `Metadata` (DB schema) are deserialised from XML using **JiBX** (bound via `org.springframework.oxm.jibx.JibxMarshaller` in `applicationContext-celerio.xml`). Config classes live in `com.jaxio.celerio.configuration`.

2. **Model building** — `ProjectFactory` + a set of `@Service` factories (`EntityFactory`, `RelationFactory`, `ColumnConfigFactory`, etc. in `com.jaxio.celerio.factory`) convert the raw configuration into a rich domain model (`com.jaxio.celerio.model`: `Project`, `Entity`, `Attribute`, `Relation`, …).

3. **Template execution** — `TemplateEngine` iterates over the model and evaluates Velocity templates (`.vm` files loaded from classpath "packs" by `PackLoader`). The `VelocityGenerator` wraps the Velocity engine; templates are discovered via `ClasspathResourceUncryptedPackLoader`.

4. **Output writing** — `ContentWriter` / `FileTracker` in `com.jaxio.celerio.output` write generated files, tracking what was previously generated to avoid overwriting user modifications.

5. **Spring context** — The whole engine is wired by Spring (`applicationContext-celerio.xml`). Tests use `SpringJUnit4ClassRunner` + `@ContextConfiguration("classpath*:applicationContext-celerio.xml")`. The test suite requires the Spring Instrument agent for AspectJ load-time weaving (configured in `maven-surefire-plugin`'s `<argLine>`).

## SPI extension points

Four Java `ServiceLoader`-based interfaces in `com.jaxio.celerio.spi` allow template packs to inject custom helpers into the Velocity context:

- `ProjectSpi` — bound as `$project.<velocityVar()>`
- `EntitySpi` — bound per-entity
- `AttributeSpi` — bound per-attribute
- `RelationSpi` — bound per-relation

See `celerio-spi-example` for a concrete implementation.

## Key build detail: JiBX bytecode instrumentation

During `process-classes`, two things happen (defined in `celerio-engine/pom.xml`):

1. An Ant task runs `org.jibx.binding.generator.BindGen` to generate binding classes into `target/jibx/` from `src/main/config/customization.xml`.
2. The `maven-jibx-plugin` instruments the compiled classes using BCEL.

These steps are **incompatible with Java 9+** and will be replaced by JAXB during the Java 25 migration.

## Migration in progress

See `JAVA8TO25.md` for the full list of changes needed per module. The migration starts with `celerio-engine`; the biggest blocker is replacing JiBX with JAXB and upgrading Spring 4 → 6.
