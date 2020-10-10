package com.github.systeminvecklare.cml.parser;

/*package*/ class SourcePosition implements ISourcePosition {
	private final int column;
	private final int row;
	
	public SourcePosition(int column, int row) {
		this.column = column;
		this.row = row;
	}
	
	@Override
	public String toString() {
		return (row+1)+":"+(column+1);
	}
}
