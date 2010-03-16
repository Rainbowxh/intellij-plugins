/*
 * Copyright 2010 The authors
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellij.struts2.jsp;

import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.lang.javascript.JSLanguageInjector;
import com.intellij.lang.javascript.JavaScriptSupportLoader;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.StandardPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.struts2.StrutsConstants;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static com.intellij.patterns.PlatformPatterns.virtualFile;
import static com.intellij.patterns.StandardPatterns.*;
import static com.intellij.patterns.XmlPatterns.xmlAttributeValue;
import static com.intellij.patterns.XmlPatterns.xmlTag;

/**
 * Adds JavaScript support for Struts UI/jQuery-plugin tags.
 *
 * @author Yann C&eacute;bron
 */
public class TaglibJavaScriptInjector implements MultiHostInjector {

  // everything with "onXXX"
  private static final ElementPattern<XmlAttributeValue> JS_ELEMENT_PATTERN =
    xmlAttributeValue()
      .withLocalName(
        StandardPatterns.and(
          or(string().startsWith("on"),
             string().startsWith("doubleOn")),  // **TransferSelect-tags
          not(string().endsWith("Topics"))))    // exclude jQuery-plugin "onXXXTopics"
      .inVirtualFile(or(virtualFile().ofType(StdFileTypes.JSP),
                        virtualFile().ofType(StdFileTypes.JSPX)))
      .withSuperParent(2, xmlTag().withNamespace(StrutsConstants.TAGLIB_STRUTS_UI_URI,
                                                 StrutsConstants.TAGLIB_JQUERY_PLUGIN_URI));

  // 
  private static final ElementPattern<XmlAttributeValue> JS_JQUERY_PATTERN =
    xmlAttributeValue()
      .withLocalName("effectOptions",
                     // dialog
                     "buttons",    
                     // datepicker
                     "showOptions",
                     // grid
                     "filterOptions", "navigatorAddOptions", "navigatorDeleteOptions",
                     "navigatorEditOptions", "navigatorSearchOptions", "navigatorViewOptions",
                     // gridColumn
                     "editoptions", "editrules", "searchoptions",
                     // tabbedPanel
                     "disabledTabs")
      .inVirtualFile(or(virtualFile().ofType(StdFileTypes.JSP),
                        virtualFile().ofType(StdFileTypes.JSPX)))
      .withSuperParent(2, xmlTag().withNamespace(StrutsConstants.TAGLIB_JQUERY_PLUGIN_URI));

  public void getLanguagesToInject(@NotNull final MultiHostRegistrar registrar, @NotNull final PsiElement host) {
    if (JS_ELEMENT_PATTERN.accepts(host)) {
      JSLanguageInjector.injectJSIntoAttributeValue(registrar, (XmlAttributeValue) host, false);
      return;
    }

    // "pseudo" JS
    if (JS_JQUERY_PATTERN.accepts(host)) {
      registrar.startInjecting(JavaScriptSupportLoader.JAVASCRIPT.getLanguage())
        .addPlace("(", ")", (PsiLanguageInjectionHost) host,
                  TextRange.from(1, host.getTextLength() - 2))
        .doneInjecting();
    }
  }

  @NotNull
  public List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
    return Arrays.asList(XmlAttributeValue.class);
  }

}