# Migration Java 8 → Java 25

## Contexte

- Java 25 installé : `/home/herve/jdk-25.0.2+10` (Temurin 25.0.2+10-LTS)
- Version actuelle compilée : Java 1.8 (`maven.compiler.source/target = 1.8`)
- Stratégie : migration module par module

---

## Modules — état d'avancement

| Module | Statut | Notes |
|---|---|---|
| `celerio-engine` | ✅ Terminé | 94 tests verts |
| `celerio-maven/celerio-maven-plugin` | ✅ Terminé | |
| `celerio-maven/dbmetadata-maven-plugin` | ✅ Terminé | |
| `celerio-maven/bootstrap-maven-plugin` | ✅ Terminé | |
| `celerio-spi-example` | ✅ Terminé | |

---

## Module `celerio-engine`

### Dépendances à migrer / supprimer

#### 🔴 Bloquants critiques

| Dépendance | Version actuelle | Action | Raison |
|---|---|---|---|
| `org.jibx:jibx-run` | 1.2.6 | **Remplacer par JAXB** | JiBX utilise BCEL pour manipuler le bytecode. Incompatible Java 9+ (module system). Projet mort. |
| `org.jibx:jibx-tools` | 1.2.6 | Supprimer | Même raison |
| `org.jibx:jibx-extras` | 1.2.6 | Supprimer | Même raison |
| `org.springframework:spring-*` | 4.2.2.RELEASE | **Migrer vers Spring 6.x** | Spring 4.x ne supporte pas Java 17+. Spring 6.x exige Java 17 minimum. |
| `org.apache.velocity:velocity` | 1.6.2 | **Migrer vers velocity-engine-core 2.x** | L'API a changé, `StringUtils.normalizePath` déplacé |

#### 🟠 Majeurs

| Dépendance | Version actuelle | Action | Raison |
|---|---|---|---|
| `junit:junit` | 4.12 | Migrer JUnit 5 | `@RunWith` → `@ExtendWith`, `SpringJUnit4ClassRunner` → `SpringExtension` |
| `org.mockito:mockito-all` | 1.8.1 | `mockito-core` 5.x | `mockito-all` supprimé, incompatible Java 25 |
| `org.easytesting:fest-assert` | 1.4 | Remplacer par AssertJ | Projet mort, AssertJ est le successeur |
| `org.hibernate:hibernate-validator` | 4.3.0.Final | Migrer 8.x (jakarta) | Dépend de javax.validation |
| `mysql:mysql-connector-java` | 5.1.6 | `com.mysql:mysql-connector-j` 9.x | groupId/artifactId changés |
| `com.h2database:h2` | 1.3.171 | 2.x | Très vieux, API interne changée |
| `cglib:cglib-nodep` | 3.1 | Supprimer | Intégré dans Spring 6 / plus nécessaire |

#### 🟡 Mises à jour

| Dépendance | Version actuelle | Version cible | Notes |
|---|---|---|---|
| `com.google.guava:guava` | 18.0 | 33.x | API stable, mais nombreuses deprecations |
| `commons-io:commons-io` | 1.4 | 2.x | API enrichie |
| `commons-lang:commons-lang` | 2.4 | Migrer `commons-lang3` 3.x | Package `org.apache.commons.lang3` |
| `org.aspectj:aspectjrt/weaver` | 1.8.7 | 1.9.22+ | Java 25 support |
| `org.eclipse.jgit` | 4.5.0 | 7.x | Java 11+ requis depuis v5 |
| `org.tmatesoft.svnkit` | 1.8.13 | 1.10.x | Support Java moderne |
| `org.slf4j:slf4j-*` | 1.5.10 | 2.x | API stable mais très vieille version |
| `log4j:log4j` | 1.2.13 | Supprimer / log4j2 | log4j 1.x abandonné, CVE critique |
| `org.slf4j:slf4j-log4j12` | 1.5.10 | `slf4j-simple` ou logback | Couplé à log4j 1.x |
| `xerces:xercesImpl` | 2.9.1 | Supprimer | Intégré dans le JDK depuis Java 6 |
| `org.hibernate.javax.persistence:hibernate-jpa-2.1-api` | 1.0.0.Final | `jakarta.persistence-api` 3.x | Namespace javax→jakarta |
| `javax.validation:validation-api` | 1.0.0.GA | `jakarta.validation-api` 3.x | Namespace javax→jakarta |
| `org.apache.tomcat:tomcat-jdbc` | 7.0.39 | Supprimer ou 10.x | Très ancien, scope test |

### Plugins Maven à mettre à jour

| Plugin | Version actuelle | Version cible | Notes |
|---|---|---|---|
| `maven-compiler-plugin` | 3.13.0 | 3.13.0 ✅ | Déjà récent, changer `<release>8</release>` → `<release>25</release>` |
| `maven-surefire-plugin` | 2.4.3 | 3.5.x | **Critique** : 2.4.3 ne supporte pas Java 25 |
| `maven-jibx-plugin` | 1.2.6 | **Supprimer** | Remplacé par JAXB |
| `maven-antrun-plugin` | 1.2 | **Supprimer** | Utilisé uniquement pour JiBX BindGen |
| `buildnumber-maven-plugin` | 1.2 | 3.x | Vieille version |
| `build-helper-maven-plugin` | 1.5 | 3.x | Vieille version |
| `maven-resources-plugin` | 2.4.1 | 3.x | Vieille version |
| `maven-jar-plugin` | 2.3 | 3.x | Vieille version |
| `maven-plugin-plugin` | 3.4 | 3.x | Vieille version |

### Changements de code source

#### 1. JiBX → JAXB (impact majeur)

- **Supprimer** : `maven-antrun-plugin` (BindGen task) + `maven-jibx-plugin`
- **Supprimer** : `src/main/config/customization.xml`
- **Ajouter** : dépendance `org.glassfish.jaxb:jaxb-runtime` 4.x
- **Modifier** : `applicationContext-celerio.xml` — remplacer `JibxMarshaller` par `Jaxb2Marshaller`
- **Annoter** : les classes de configuration (`Celerio`, `Metadata`, `Profiles`, `CelerioPack`, etc.) avec `@XmlRootElement`, `@XmlElement`, `@XmlAttribute`

Classes concernées (package `com.jaxio.celerio.configuration`) :
- `Celerio`
- `configuration.database.Metadata`, `Table`, `Column`, `ImportedKey`, `Index`
- `configuration.eclipse.Profiles`, `Setting`
- `configuration.pack.CelerioPack`
- `configuration.Pattern`, `Include`, `MetaAttribute`, `Pack`, `EclipseFormatter`
- `configuration.convention.*Override` (4 classes)
- `configuration.entity.*` (`EntityConfig`, `EnumValue`, `EnumConfig`, etc.)

#### 2. Spring 4.x → Spring 6.x

- Mettre à jour la version Spring : `4.2.2.RELEASE` → `6.2.x`
- `applicationContext-celerio.xml` : schema URLs `spring-beans-4.2.xsd` → `6.x`
- Supprimer `org.springframework.ui.velocity.VelocityEngineFactoryBean` (supprimé dans Spring 4.3) → configurer Velocity directement via `@Bean`
- `org.springframework.oxm.jibx.JibxMarshaller` → `org.springframework.oxm.jaxb.Jaxb2Marshaller`

#### 3. javax.* → jakarta.*

Imports à renommer dans 36 fichiers :

| Ancien | Nouveau |
|---|---|
| `javax.persistence.*` | `jakarta.persistence.*` |
| `javax.validation.*` | `jakarta.validation.*` |
| `javax.xml.*` | inchangé (JDK standard) |
| `javax.sql.DataSource` | inchangé (JDK standard) |
| `javax.lang.model.SourceVersion` | inchangé (JDK standard) |

> `javax.xml.*`, `javax.sql.*`, `javax.lang.model.*` sont des APIs JDK standard — elles restent `javax.*` même en Java 25.

#### 4. Velocity 1.6.2 → velocity-engine-core 2.x

- `org.apache.velocity.util.StringUtils.normalizePath` → `org.apache.velocity.util.StringUtils.normalizePath` (vérifier déplacement)
- Configurer `VelocityEngine` directement sans `VelocityEngineFactoryBean`
- Clé de config `runtime.log.logsystem.class` → `runtime.log.logsystem.log4j.logger` renommées en Velocity 2.x
- Remplacer `Log4JLogChute` par `Slf4jLogChute` ou logger SLF4J natif

#### 5. Tests

- `@RunWith(SpringJUnit4ClassRunner.class)` → `@ExtendWith(SpringExtension.class)` (JUnit 5)
- `import org.junit.runner.RunWith` → `import org.junit.jupiter.api.extension.ExtendWith`
- `import org.junit.Test` → `import org.junit.jupiter.api.Test`
- `import org.junit.Assert.*` → `import org.junit.jupiter.api.Assertions.*`
- `fest-assert` → AssertJ (`org.assertj:assertj-core`)
- `mockito-all` → `mockito-core` 5.x + `mockito-junit-jupiter`

#### 6. Eclipse JDT (code formatting)

Les dépendances Eclipse OSGi (`org.eclipse.equinox`, `org.eclipse.tycho`, `org.eclipse.text`, `org.eclipse.core`) sont utilisées pour formater le code Java généré. Vérifier la compatibilité avec Java 25 ou envisager de remplacer par `google-java-format`.

#### 7. SVNKit

- Vérifier si SVNKit 1.8.13 compile et fonctionne en Java 25 (accès aux APIs internes via réflexion)
- Sinon, mettre à jour vers 1.10.x

---

## pom.xml racine — changements

- `maven.compiler.source/target` : `1.8` → `25`
- `<release>8</release>` dans `maven-compiler-plugin` → `<release>25</release>`
- `spring.version` : `4.2.2.RELEASE` → `6.2.x`
- Supprimer `<prerequisites><maven>3.1.1</maven></prerequisites>` (déprécié pour multi-module, utiliser `maven-enforcer-plugin`)
- Ajouter `maven-enforcer-plugin` pour forcer Java 25 et Maven 3.9+

---

## Modifications effectuées

### celerio-engine

- [x] pom.xml : source/target Java 25, JAVA_HOME vers JDK 25
- [x] Supprimer JiBX + Ant task (pom.xml)
- [x] Ajouter JAXB runtime (`jakarta.xml.bind-api 4.0.2` + `jaxb-runtime 4.0.5`)
- [x] Annoter toutes les classes de configuration avec JAXB (`@XmlRootElement`, `@XmlAccessorType`, `@XmlAttribute`, `@XmlElement`, `@XmlElementWrapper`, `@XmlTransient`)
- [x] Remplacer JibxMarshaller → Jaxb2Marshaller dans `applicationContext-celerio.xml`
- [x] Spring 4.2.2 → 6.2.7 (pom.xml parent + Assert API fixes)
- [x] Supprimer `spring-context-support` + `aspectjweaver` test (LTW obsolète)
- [x] SLF4J 1.5.10 → 1.7.36 (requis par Spring 6)
- [x] Supprimer `VelocityEngineFactoryBean` (supprimé dans Spring 5) → `CelerioConfig.java` `@Configuration`
- [x] Mettre à jour schémas Spring XML (4.2 → versionless)
- [x] `aspectjrt/weaver` 1.8.7 → 1.9.22 (Java 25 compatible)
- [x] javax.persistence → jakarta.persistence (étape 4)
- [x] javax.validation → jakarta.validation (étape 4)
- [x] Velocity 1.6.2 → velocity-engine-core 2.4.1, property keys Velocity 2.x, `normalizePath` → `normalize` (étape 5)
- [x] log4j 1.x supprimé → logback-classic 1.5.18, SLF4J 1.7.36 → 2.0.16 (étape 6)
- [x] Mettre à jour guava 33.4.8-jre, commons-io 2.18.0, commons-lang→commons-lang3 3.17.0, h2 2.3.232, mysql-connector-j 9.2.0, svnkit 1.10.11, jgit 7.7.0, xstream 1.4.20 (étape 7)
- [x] XStream 1.4.20 : `AnyTypePermission.ANY` pour FileTracker (étape 7)
- [x] Xerces supprimé, XmlCodeFormatter réécrit avec javax.xml.transform (étape 7)
- [x] H2 2.x : TABLE_TYPE "BASE TABLE", IDENTITY syntax, INFORMATION_SCHEMA skip, check constraints via CHECK_CONSTRAINTS view (étape 7)
- [x] commons-lang3 : migration de package (42 fichiers), WordUtils → StringUtils (étape 7)
- [x] JUnit 4 → JUnit 5 (`@ExtendWith(SpringExtension)`, `@Disabled`, `@BeforeEach`, `assertThrows`) (étape 8)
- [x] mockito-all → mockito-core 5.15.2 (étape 8)
- [x] fest-assert → AssertJ 3.27.3 (`assertThat`, `doesNotContain`) (étape 8)
- [x] hibernate-validator 8.0.2.Final jakarta (déjà fait étape précédente)

### Résultat tests actuels (après étapes 1-8)

```
Tests run: 94, Failures: 0, Errors: 0, Skipped: 3
```
