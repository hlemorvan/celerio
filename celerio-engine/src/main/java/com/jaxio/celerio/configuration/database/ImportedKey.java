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

import lombok.Getter;
import lombok.Setter;
import jakarta.xml.bind.annotation.*;

/*
 * Description of the primary key columns that are referenced by a table's foreign key columns (the primary keys imported by a table).
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportedKey {
    @Setter
    @XmlAttribute
    protected String fkColumnName;
    @Setter
    @XmlAttribute
    protected String fkName;

    @Setter
    @XmlAttribute
    protected String pkColumnName;
    @Setter
    @XmlAttribute
    protected String pkTableName;
    @Setter
    @Getter
    @XmlAttribute
    protected String pkTableSchema;
    @Setter
    @Getter
    @XmlAttribute
    protected String pkTableCatalog;



    /*
     * Foreign key column name
     */
    public String getFkColumnName() {
        return fkColumnName;
    }

    /*
     * Foreign key name
     */
    public String getFkName() {
        return fkName;
    }

    /*
     * Primary key column name being imported
     */
    public String getPkColumnName() {
        return pkColumnName;
    }

    /*
     * Primary key table name being imported
     */
    public String getPkTableName() {
        return pkTableName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("fkColumnName=").append(fkColumnName).append(", ");
        sb.append("fkName=").append(fkName).append(", ");
        sb.append("pkColumnName=").append(pkColumnName).append(", ");
        sb.append("pkTableName=").append(pkTableName);
        return sb.toString();
    }
}
