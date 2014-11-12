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
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.CharTokenizer;
import org.apache.lucene.util.Version;

/**
 * An Analyzer that uses {@link TibWhitespaceTokenizer} and filters with StopFilter
 * <p>
 * <a name="version">You must specify the required {@link Version} compatibility when creating {@link CharTokenizer}:
 * <ul>
 * <li>As of 3.1, {@link WhitespaceTokenizer} uses an int based API to normalize and detect token codepoints. See {@link CharTokenizer#isTokenChar(int)} and
 * {@link CharTokenizer#normalize(int)} for details.</li>
 * </ul>
 * <p>
 * Derived from Lucene 4.4.0 analysis.core.WhitespaceAnalyzer.java
 **/
public final class TibetanAnalyzer extends Analyzer {
	/**
	 * An unmodifiable set containing some common English words that are not usually useful for searching.
	 */
	public static final CharArraySet TIBETAN_STOP_WORDS_SET;

	static {
		final List<String> stopWords = Arrays.asList(
				// // "gi", "kyi", "gyi", "yi",
				// "\u0F42\u0F72", "\u0F40\u0FB1\u0F72", "\u0F42\u0FB1\u0F72", "\u0F61\u0F72",
				// "gis", "kyis", "gyis", "yis", "na"
				"\u0F42\u0F72\u0F66", "\u0F40\u0FB1\u0F72\u0F66", "\u0F42\u0FB1\u0F72\u0F66", "\u0F61\u0F72\u0F66", "\u0F53"
				// // "su", "ru", "ra", "du", "na", "la", "tu",
				// "\u0F66\u0F74", "\u0F62\u0F74", "\u0F62", "\u0F51\u0F74", "\u0F53", "\u0F63",
				// "\u0F4F\u0F74",
				// // "go", "ngo", "do", "no", "po",
				// "\u0F42\u0F7C", "\u0F44\u0F7C", "\u0F51\u0F7C", "\u0F53\u0F7C", "\u0F54\u0F7C",
				// // "mo", "ro", "lo", "so", "to",
				// "\u0F58\u0F7C", "\u0F62\u0F7C", "\u0F63\u0F7C", "\u0F66\u0F7C", "\u0F4F\u0F7C",
				// // "dang"
				// "\u0F51\u0F44"
				);

		final CharArraySet stopSet = new CharArraySet(Version.LUCENE_44, stopWords, false);

		TIBETAN_STOP_WORDS_SET = CharArraySet.unmodifiableSet(stopSet);
	}

	private final Version matchVersion;

	/**
	 * Creates a new {@link WhitespaceAnalyzer}
	 * 
	 * @param matchVersion
	 *            Lucene version to match See {@link <a href="#version">above</a>}
	 */
	public TibetanAnalyzer(Version matchVersion) {
		this.matchVersion = matchVersion;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected TokenStreamComponents createComponents(final String fieldName, final Reader reader) {
		Tokenizer source = new TibWhitespaceTokenizer(matchVersion, reader);

		TokenFilter filter = new TibEndingFilter(source);
		filter = new StopFilter(Version.LUCENE_43, filter, TIBETAN_STOP_WORDS_SET);
		((StopFilter) filter).setEnablePositionIncrements(false);

		return new TokenStreamComponents(source, filter);
	}
}
