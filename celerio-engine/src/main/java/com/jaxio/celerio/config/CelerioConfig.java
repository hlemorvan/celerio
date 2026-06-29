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

package com.jaxio.celerio.config;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CelerioConfig {

    @Bean
    public VelocityEngine velocityEngine() throws Exception {
        VelocityEngine engine = new VelocityEngine();
        engine.setProperty("runtime.references.strict", "true");
        engine.setProperty("runtime.log.invalid.references", "true");
        engine.setProperty("resource.loaders", "file, classpath");
        engine.setProperty("resource.loader.file.description", "Velocity File Resource Loader");
        engine.setProperty("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
        engine.setProperty("resource.loader.file.path", "., src/main/velocity");
        engine.setProperty("resource.loader.file.cache", "true");
        engine.setProperty("resource.loader.file.modification_check_interval", "-1");
        engine.setProperty("resource.loader.classpath.description", "Velocity Classpath Resource Loader");
        engine.setProperty("resource.loader.classpath.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        engine.init();
        return engine;
    }
}
