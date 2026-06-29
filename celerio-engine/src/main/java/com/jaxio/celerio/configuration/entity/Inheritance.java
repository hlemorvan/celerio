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

package com.jaxio.celerio.configuration.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import jakarta.persistence.InheritanceType;

import static org.springframework.util.StringUtils.hasLength;
import jakarta.xml.bind.annotation.*;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Inheritance {
    @XmlAttribute
    private String discriminatorColumn;
    @XmlAttribute
    private String discriminatorValue;
    @XmlAttribute
    private String parentEntityName;
    @XmlAttribute
    private InheritanceType strategy;

    public boolean hasDiscriminatorColumn() {
        return hasLength(discriminatorColumn);
    }

    public boolean hasDiscriminatorValue() {
        return hasLength(discriminatorValue);
    }

    public boolean hasParentEntityName() {
        return StringUtils.hasLength(parentEntityName);
    }

    public boolean hasStrategy() {
        return strategy != null;
    }

    public boolean is(InheritanceType strategy) {
        return this.strategy == strategy;
    }
}
