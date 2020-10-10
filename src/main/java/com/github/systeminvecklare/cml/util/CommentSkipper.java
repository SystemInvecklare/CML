package com.github.systeminvecklare.cml.util;

import java.io.IOException;
import java.io.Reader;

import com.github.systeminvecklare.cml.parser.ICharSource;
import com.github.systeminvecklare.cml.parser.ReaderCharSource;

public class CommentSkipper implements ICharSource {
	private final ICharSource wrapped;
	private int buffered = -1;
	
	public CommentSkipper(ICharSource wrapped) {
		this.wrapped = wrapped;
	}

	public CommentSkipper(Reader reader) {
		this(new ReaderCharSource(reader));
	}

	@Override
	public int read() throws IOException {
		int read = getNext();
		if(read == -1) {
			return read;
		} else {
			char readChar = (char) read;
			if(readChar == '/') {
				int nextRead = getNext();
				if(nextRead == -1) {
					return read;
				} else {
					char nextReadChar = (char) nextRead;
					if(nextReadChar == '/') {
						//Comment has begun. Read until end of line
						while(true) {
							int comment = getNext();
							if(comment == -1) {
								return -1;
							} else if(((char) comment) == '\n') {
								return comment;
							}
						}
					} else {
						buffered = nextRead;
						return read;
					}
				}
			} else {
				return read;
			}
		}
	}
	
	private int getNext() throws IOException {
		if(buffered != -1) {
			int next = buffered;
			buffered = -1;
			return next;
		} else {
			return wrapped.read();
		}
	}
}
