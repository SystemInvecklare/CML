package com.github.systeminvecklare.cml.parser;

/*package*/ class SyntaxToken extends AbstractToken {
	private final char syntax;
	
	public SyntaxToken(ISourcePosition start, ISourcePosition end, char syntax) {
		super(start, end);
		this.syntax = syntax;
	}

	public char getSyntax() {
		return syntax;
	}
}