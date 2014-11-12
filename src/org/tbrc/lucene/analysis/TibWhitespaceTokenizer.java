/*******************************************************************************
 * Copyright (c) 2014 Tibetan Buddhist Resource Center (TBRC)
 * 
 * If this file is a derivation of another work the license header will appear 
 * below; otherwise, this work is licensed under the Apache License, Version 2.0 
 * (the "License"); you may not use this file except in compliance with the 
 * License.
 * 
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.tbrc.lucene.analysis;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.CharTokenizer;
import org.apache.lucene.util.Version;

/** 
 * A TibWhitespaceTokenizer divides text between sequences of Tibetan Letter and/or Digit 
 * characters and sequences of all other characters - typically some sort of white space 
 * but other punctuation and characters from other language code-pages are not considered
 * as constituents of tokens for the purpose of search and indexing.
 * <p>
 * Adjacent sequences of Tibetan Letter and/or Digit characters form tokens.
 * <p>
 * Derived from Lucene 4.4.0 analysis.core.WhitespaceTokenizer.java
 */
public final class TibWhitespaceTokenizer extends CharTokenizer {
  
  /**
   * Construct a new TibWhitespaceTokenizer. 
   * @param matchVersion Lucene version
   * to match See {@link <a href="#version">above</a>}
   * 
   * @param in
   *          the input to split up into tokens
   */
  public TibWhitespaceTokenizer(Version matchVersion, Reader in) {
    super(matchVersion, in);
  }

  /**
   * Construct a new TibWhitespaceTokenizer using a given
   * {@link org.apache.lucene.util.AttributeSource.AttributeFactory}.
   *
   * @param
   *          matchVersion Lucene version to match See
   *          {@link <a href="#version">above</a>}
   * @param factory
   *          the attribute factory to use for this {@link Tokenizer}
   * @param in
   *          the input to split up into tokens
   */
  public TibWhitespaceTokenizer(Version matchVersion, AttributeFactory factory, Reader in) {
    super(matchVersion, factory, in);
  }
  
  protected boolean isTibLetterOrDigit(int c) {
	  return ('\u0F40' <= c && c <= '\u0F83') || ('\u0F90' <= c && c <= '\u0FBC') || ('\u0F20' <= c && c <= '\u0F33') || (c == '\u0F00');
  }
  
  /** Collects only Tibetan Letter or Digit characters.*/
  protected boolean isTokenChar(int c) {
    return isTibLetterOrDigit(c);
  }
}
