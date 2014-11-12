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

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/** 
 * Removes <tt>+</tt> characters for use in the ChunkAnalyzer or WylieAnalyzer.
 * <p>
 * The <tt>+</tt> in Extended Wylie is used to indicate Tibetanized Sanskrit
 * stacking and by removing these characters the index and search will be more
 * relaxed allowing "padma" to match "pad+ma" or "siddhi" to match "sid+d+hi".
 * <p>
 * Derived from Lucene 4.4.0 analysis.standard.ClassicFilter
 */

public class PlusFilter extends TokenFilter {
	static char PLUS = '+';

	public PlusFilter(TokenStream in) {
		super(in);
	}

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

	/** Returns the next token in the stream, or null at EOS.
	 * <p>Removes <tt>'s</tt> from the end of words.
	 * <p>Removes dots from acronyms.
	 */
	@Override
	public final boolean incrementToken() throws java.io.IOException {
		if (!input.incrementToken()) {
			return false;
		}

		final char[] buffer = termAtt.buffer();
		final int bufferLength = termAtt.length();

		int upto = 0;

		for(int i=0;i<bufferLength;i++) {
			char c = buffer[i];
			if (c != PLUS)
				buffer[upto++] = c;
		}

		termAtt.setLength(upto);

		return true;
	}
}
