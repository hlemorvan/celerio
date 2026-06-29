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

package com.jaxio.celerio.configuration.database.h2;

import com.jaxio.celerio.configuration.database.support.EnumExtractor;

import java.util.List;

public class H2EnumValuesExtractor {
    // H2 1.x: (COLUMN_NAME IN('v1','v2'))
    private static final String H2_1X = "^\\(\\p{Graph}*\\p{Blank}*IN\\((.*)\\)\\)$";
    // H2 2.x: "COLUMN_NAME" IN('v1','v2')
    private static final String H2_2X = "^\"[^\"]+\"\\s*IN\\((.*)\\)$";

    private final EnumExtractor extractor1x = new EnumExtractor(H2_1X);
    private final EnumExtractor extractor2x = new EnumExtractor(H2_2X);

    public List<String> extract(String content) {
        List<String> result = extractor2x.extract(content);
        if (!result.isEmpty()) {
            return result;
        }
        return extractor1x.extract(content);
    }
}
