// Jericho HTML Parser - Java based library for analysing and manipulating HTML
// Version 3.4
// Copyright (C) 2004-2013 Martin Jericho
// http://jericho.htmlparser.net/
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of either one of the following licences:
//
// 1. The Eclipse Public License (EPL) version 1.0,
// included in this distribution in the file licence-epl-1.0.html
// or available at http://www.eclipse.org/legal/epl-v10.html
//
// 2. The GNU Lesser General Public License (LGPL) version 2.1 or later,
// included in this distribution in the file licence-lgpl-2.1.txt
// or available at http://www.gnu.org/licenses/lgpl.txt
//
// 3. The Apache License version 2.0,
// included in this distribution in the file licence-apache-2.0.html
// or available at http://www.apache.org/licenses/LICENSE-2.0.html
//
// This library is distributed on an "AS IS" basis,
// WITHOUT WARRANTY OF ANY KIND, either express or implied.
// See the individual licence texts for more details.

package net.htmlparser.jericho;

import java.io.*;
import java.nio.*;

final class StreamedParseText extends CharSequenceParseText {
	private final StreamedText streamedText;

	public StreamedParseText(final StreamedText streamedText) {
		super(streamedText);
		this.streamedText=streamedText;
	}

	@Override
	protected int getEnd() {
		return streamedText.getEnd();
	}

	@Override
	protected String substring(final int begin, final int end) {
		return streamedText.substring(begin,end).toLowerCase();
	}

	@Override
	protected boolean atEndOfStream() {
		return streamedText.atEndOfStream();
	}
}
