package com.github.systeminvecklare.cml.parser;

/*package*/ class NameToken extends AbstractToken {
	private final String text;

	public NameToken(ISourcePosition start, ISourcePosition end, String text) {
		super(start, end);
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	@Override
	public String toString() {
		return text;
	}
}
