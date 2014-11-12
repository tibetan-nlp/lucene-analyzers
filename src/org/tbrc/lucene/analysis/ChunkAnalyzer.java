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
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.CharTokenizer;
import org.apache.lucene.util.Version;

/**
 * Uses {@link WylieTokenizer} to create streams of simple Wylie tokens. The '+' character is currently included. In a later version the '+' will be deleted
 * from the token so that pad+ma and padma will be identical for search and indexing.
 * <p>
 * <a name="version">You must specify the required {@link Version} compatibility when creating {@link CharTokenizer}:
 * <ul>
 * <li>As of 3.1, {@link WylieTokenizer} uses an int based API to normalize and detect token codepoints. See {@link CharTokenizer#isTokenChar(int)} and
 * {@link CharTokenizer#normalize(int)} for details.</li>
 * </ul>
 * Derived from Lucene 4.4.0 analysis.core.SimpleAnalyzer.java
 * <p>
 **/
public final class ChunkAnalyzer extends Analyzer {
	/**
	 * An unmodifiable set containing some common English words that are not usually useful for searching.
	 */
	public static final CharArraySet WYLIE_STOP_WORDS_SET;

	static {
		final List<String> stopWords = Arrays.asList(
				"gi", "kyi", "gyi", "yi", "gis", "kyis", "gyis", "yis",
				"su", "ru", "ra", "du", "na", "la", "tu",
				"go", "ngo", "do", "no", "po", "mo", "ro", "lo", "so", "to",
				"dang"
				);

		final CharArraySet stopSet = new CharArraySet(Version.LUCENE_44, stopWords, false);

		WYLIE_STOP_WORDS_SET = CharArraySet.unmodifiableSet(stopSet);
	}

	private final Version matchVersion;

	/**
	 * Creates a new {@link SimpleAnalyzer}
	 * 
	 * @param matchVersion
	 *            Lucene version to match See {@link <a href="#version">above</a>}
	 */
	public ChunkAnalyzer(Version matchVersion) {
		this.matchVersion = matchVersion;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected TokenStreamComponents createComponents(final String fieldName, final Reader reader)
	{
		Tokenizer source = new WylieTokenizer(matchVersion, reader);

		TokenFilter filter = new PlusFilter(source);
		filter = new EndingFilter(filter);
		filter = new StopFilter(Version.LUCENE_43, filter, WYLIE_STOP_WORDS_SET);
		((StopFilter) filter).setEnablePositionIncrements(false);

		return new TokenStreamComponents(source, filter);
	}
}
