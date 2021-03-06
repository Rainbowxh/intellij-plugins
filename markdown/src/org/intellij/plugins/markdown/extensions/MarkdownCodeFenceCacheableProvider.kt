// Copyright 2000-2018 JetBrains s.r.o.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package org.intellij.plugins.markdown.extensions

import com.intellij.openapi.application.PathManager
import java.io.File

interface MarkdownCodeFenceCacheableProvider : MarkdownCodeFencePluginGeneratingProvider {
  /**
   * Code fence plugin cache path
   */
  fun getCacheRootPath(): String = "${PathManager.getSystemPath()}${File.separator}markdown${File.separator}${getPluginName()}"

  /**
   * This plugin provider name, uses in path to images store
   */
  fun getPluginName(): String = javaClass.simpleName
}