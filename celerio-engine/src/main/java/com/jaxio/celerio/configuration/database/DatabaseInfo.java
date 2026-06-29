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

import lombok.Setter;
import jakarta.xml.bind.annotation.*;

/**
 * Information about the database where celerio extracted the metadata
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DatabaseInfo {
    @Setter
    @XmlAttribute
    private int databaseMajorVersion;
    @Setter
    @XmlAttribute
    private int databaseMinorVersion;
    @Setter
    @XmlAttribute
    private String databaseProductName = "";
    @Setter
    @XmlAttribute
    private String databaseProductVersion = "";
    @Setter
    @XmlAttribute
    private int driverMajorVersion;
    @Setter
    @XmlAttribute
    private int driverMinorVersion;
    @Setter
    @XmlAttribute
    private String driverName = "";
    @Setter
    @XmlAttribute
    private String driverVersion = "";
    @Setter
    @XmlAttribute
    private String extraInfo = "";

    public int getDatabaseMajorVersion() {
        return databaseMajorVersion;
    }

    public int getDatabaseMinorVersion() {
        return databaseMinorVersion;
    }

    public String getDatabaseProductName() {
        return databaseProductName;
    }

    public String getDatabaseProductVersion() {
        return databaseProductVersion;
    }

    public int getDriverMajorVersion() {
        return driverMajorVersion;
    }

    public int getDriverMinorVersion() {
        return driverMinorVersion;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverVersion() {
        return driverVersion;
    }

    public String getExtraInfo() {
        return extraInfo;
    }
}
