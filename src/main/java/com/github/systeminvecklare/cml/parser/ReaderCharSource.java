package com.github.systeminvecklare.cml.parser;

import java.io.IOException;
import java.io.Reader;

public class ReaderCharSource implements ICharSource {
	private final Reader reader;

	public ReaderCharSource(Reader reader) {
		this.reader = reader;
	}

	@Override
	public int read() throws IOException {
		return reader.read();
	}
}
