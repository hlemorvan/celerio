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

package com.jaxio.celerio.configuration.util;

import com.jaxio.celerio.util.PackageUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.jaxio.celerio.util.PackageUtil.assemblePackage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PackageUtilTest {

    @Test
    public void nullPackage() {
        assertThrows(IllegalArgumentException.class, () -> assemblePackage((String) null));
    }

    @Test
    public void justRootPackage() {
        assertThat(assemblePackage("com.edy")).isEqualTo("com.edy");
    }

    @Test
    public void rootAndSubPackage() {
        assertThat(assemblePackage("com.edy", "oho")).isEqualTo("com.edy.oho");
    }

    @Test
    public void nullRootAndSubPackage() {
        assertThat(assemblePackage(null, "oho")).isEqualTo("oho");
    }

    @Test
    public void emptyRootAndSubPackage() {
        assertThat(assemblePackage("", "oho")).isEqualTo("oho");
    }

    @Test
    public void rootAndNullSubPackage() {
        assertThat(assemblePackage("com.edy", null)).isEqualTo("com.edy");
    }

    @Test
    public void rootAndEmptySubPackage() {
        assertThat(assemblePackage("com.edy", " ")).isEqualTo("com.edy");
    }

    @Test
    public void esotericCase() {
        assertThat(assemblePackage("", "  ", null, " com", null, "edy ", " ", null, "mitchel")).isEqualTo("com.edy.mitchel");
    }

    @Test
    public void isPackagNameValid() {
        // valid package names
        Assertions.assertTrue(PackageUtil.isPackageNameValid("com"));
        Assertions.assertTrue(PackageUtil.isPackageNameValid("com.jaxio.celerio"));
        Assertions.assertTrue(PackageUtil.isPackageNameValid("com.jaxio2.celerio"));
        Assertions.assertTrue(PackageUtil.isPackageNameValid("_com.jaxio2.celerio"));
        Assertions.assertTrue(PackageUtil.isPackageNameValid("$com.jaxio2.celerio"));

        // invalid package names
        Assertions.assertFalse(PackageUtil.isPackageNameValid(""));
        Assertions.assertFalse(PackageUtil.isPackageNameValid("com.2jaxio.celerio"));
        Assertions.assertFalse(PackageUtil.isPackageNameValid("com. jaxio.celerio"));
        Assertions.assertFalse(PackageUtil.isPackageNameValid("com.jaxio-celerio"));
        Assertions.assertFalse(PackageUtil.isPackageNameValid("java.lang"));
        Assertions.assertFalse(PackageUtil.isPackageNameValid("javax.lang"));
    }
}
