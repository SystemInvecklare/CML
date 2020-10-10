package com.github.systeminvecklare.cml.parser;


/*package*/ class StringToken extends AbstractToken {
	private final String text;

	public StringToken(ISourcePosition start, ISourcePosition end, String text) {
		super(start, end);
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
