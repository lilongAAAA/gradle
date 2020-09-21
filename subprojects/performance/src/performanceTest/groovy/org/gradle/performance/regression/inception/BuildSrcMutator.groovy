/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.performance.regression.inception

import org.gradle.profiler.BuildContext
import org.gradle.profiler.BuildMutator
import org.gradle.profiler.InvocationSettings

import java.util.function.Function

class BuildSrcMutator implements Function<InvocationSettings, BuildMutator> {
    @Override
    BuildMutator apply(InvocationSettings invocationSettings) {
        return new BuildMutator() {
            @Override
            void beforeBuild(BuildContext context) {
                new File(invocationSettings.projectDir, "buildSrc/src/main/groovy/ChangingClass.groovy").tap {
                    parentFile.mkdirs()
                    text = """
                        class ChangingClass {
                            void changingMethod${context.phase}${context.iteration}() {}
                        }
                    """.stripIndent()
                }
            }
        }
    }
}
