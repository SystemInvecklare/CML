package com.github.systeminvecklare.cml.parser;

/*package*/ class AbstractToken implements IToken {
	private final ISourcePosition start;
	private final ISourcePosition end;
	
	public AbstractToken(ISourcePosition start, ISourcePosition end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public ISourcePosition startPos() {
		return start;
	}

	@Override
	public ISourcePosition endPos() {
		return end;
	}
}
