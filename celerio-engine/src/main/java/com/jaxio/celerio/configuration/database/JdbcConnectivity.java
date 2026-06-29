/*
 * Copyright 2015 JAXIO http://www.jaxio.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jaxio.celerio.configuration.database;

import com.jaxio.celerio.util.StringUtil;
import lombok.Setter;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.jaxio.celerio.configuration.Util.nonNull;
import static org.springframework.util.StringUtils.hasLength;
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class JdbcConnectivity {
    @Setter
    @XmlAttribute
    private String driver;
    @Setter
    @XmlAttribute
    private String driverGroupId;
    @Setter
    @XmlAttribute
    private String driverArtifactId;
    @Setter
    @XmlAttribute
    private String driverArtifactIdVersion;
    @Setter
    @XmlAttribute
    private String hibernateDialect;
    @Setter
    @XmlAttribute
    private String sqlDelimiter;

    @Setter
    @XmlAttribute
    private String password;
    @Setter
    @XmlAttribute
    private String url;
    @Setter
    @XmlAttribute
    private String user;
    @Setter
    @XmlAttribute
    private String schemaName;
    @Setter
    @XmlElementWrapper(name = "tableNamePatterns")
    @XmlElement(name = "tableNamePattern")
    private List<String> tableNamePatterns = newArrayList();
    @Setter
    @XmlAttribute
    private boolean oracleRetrieveRemarks;
    @Setter
    @XmlAttribute
    private boolean oracleRetrieveSynonyms;
    @Setter
    @XmlAttribute
    private Boolean reverseIndexes = true;
    @Setter
    @XmlAttribute
    private Boolean reverseOnlyUniqueIndexes = true;
    @Setter
    @XmlAttribute
    private String catalog;
    @XmlElementWrapper(name = "tableTypes")
    @XmlElement(name = "tableType")
    private List<TableType> tableTypes = newArrayList();

    public JdbcConnectivity() {
    }

    public JdbcConnectivity(TableType tableType) {
        add(tableType);
    }

    /*
     * Jdbc driver name<br>
     * Example: org.h2.Driver
     */
    public String getDriver() {
        return driver;
    }

    /*
     * Jdbc driver group id<br>
     * Example:
     */
    public String getDriverGroupId() {
        return driverGroupId;
    }

    /*
     * Jdbc driver artifact id<br>
     * Example:
     */
    public String getDriverArtifactId() {
        return driverArtifactId;
    }

    /*
     * Jdbc driver artifact id version<br>
     * Example:
     */
    public String getDriverArtifactIdVersion() {
        return driverArtifactIdVersion;
    }

    /*
     * Jdbc hibernate dialect<br>
     * Example:
     */
    public String getHibernateDialect() {
        return hibernateDialect;
    }

    /*
     * SQL delimiter<br>
     * Example: ; or /
     */
    public String getSqlDelimiter() {
        return sqlDelimiter;
    }


    /*
     * Jdbc url connection<br>
     * Example: Jdbc:h2:~/mydatabase
     */
    public String getUrl() {
        return url;
    }

    /*
     * Jdbc user<br>
     * Example: myuser
     */
    public String getUser() {
        return user;
    }

    /*
     * Jdbc password<br>
     * Example: mypassword
     */
    public String getPassword() {
        return password;
    }

    public String getSchemaName() {
        return schemaName;
    }

    /*
     * you can restrict table extraction using a pattern<br>
     * Example: PROJECT_%
     */
    public List<String> getTableNamePatterns() {
        return tableNamePatterns;
    }

    /*
     * Table types to retrieve
     */
    public List<TableType> getTableTypes() {
        return tableTypes;
    }

    /*
     * Should Celerio retrieve remarks on oracle, beware this is a very time consuming operation
     */
    public boolean isOracleRetrieveRemarks() {
        return oracleRetrieveRemarks;
    }

    /*
     * Should Celerio retrieve synonyms on oracle
     */
    public boolean isOracleRetrieveSynonyms() {
        return oracleRetrieveSynonyms;
    }

    /*
     * When false, no indexes is reversed at all.
     * @return
     */
    public Boolean getReverseIndexes() {
        return reverseIndexes;
    }

    public boolean shouldReverseIndexes() {
        // we assume null is by default TRUE.        
        return reverseIndexes == null || reverseIndexes;
    }

    /*
     * when true, reverse only indexes for unique values; when false, reverse indexes regardless of whether unique or not.
     */
    public Boolean getReverseOnlyUniqueIndexes() {
        return reverseOnlyUniqueIndexes;
    }

    public boolean shouldReverseOnlyUniqueIndexes() {
        // we assume null is by default TRUE.
        return shouldReverseIndexes() && (reverseOnlyUniqueIndexes == null || reverseOnlyUniqueIndexes);
    }

    /*
     * Catalog name; must match the catalog name as it is stored in the database.<br>
     * "" retrieves those without a catalog<br>
     * empty means that the catalog name should not be used to narrow the search
     */
    public String getCatalog() {
        return catalog;
    }

    public void setTableTypes(List<TableType> tableTypes) {
        this.tableTypes = nonNull(tableTypes);
    }

    public void add(TableType tableType) {
        tableTypes.add(tableType);
    }

    public boolean invalid() {
        // password can be empty
        return isBlank(driver, url, user);
    }

    public boolean isValid() {
        return !invalid();
    }

    private boolean isBlank(String... args) {
        for (String arg : args) {
            if (!hasLength(arg)) {
                return true;
            }
        }
        return false;
    }

    public WellKnownDatabase getWellKownDatabase() {
        return WellKnownDatabase.fromJdbcUrl(getUrl());
    }

    public boolean isWellKownDatabase() {
        return getWellKownDatabase() != null;
    }

}
