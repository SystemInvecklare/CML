package com.github.systeminvecklare.cml.parser;

import java.io.IOException;
import java.io.Reader;

import com.github.systeminvecklare.cml.ICmlNode;

public interface ICmlParser {
	void parse(Reader reader, ICmlParseHandler parseHandler) throws IOException;
	ICmlNode parse(Reader reader) throws IOException;
	void parse(ICharSource charSource, ICmlParseHandler parseHandler) throws IOException;
	ICmlNode parse(ICharSource charSource) throws IOException;
}
