package com.github.systeminvecklare.cml.printer;

import com.github.systeminvecklare.cml.ICmlNode;
import com.github.systeminvecklare.cml.IKeyValuePair;

public final class StringPrinter implements IStringPrinter {
	public static final IStringPrinter DEFAULT = builder().build();
	
	public static final class Builder {
		public static final String DEFAULT_INDENT = "\t";
		public static final String DEFAULT_LINEBREAK = "\n";
		public static final boolean DEFAULT_USE_LINE_BREAKS = true;
		
		private String indent = DEFAULT_INDENT;
		private String linebreak = DEFAULT_LINEBREAK;
		private boolean useLineBreaks = DEFAULT_USE_LINE_BREAKS;
		
		public String getIndent() {
			return indent;
		}
		
		public String getLinebreak() {
			return linebreak;
		}
		
		public boolean isUseLineBreaks() {
			return useLineBreaks;
		}
		
		public Builder setIndent(String indent) {
			this.indent = indent;
			return this;
		}
		
		public Builder setLinebreak(String linebreak) {
			this.linebreak = linebreak;
			return this;
		}
		
		public Builder setUseLineBreaks(boolean useLineBreaks) {
			this.useLineBreaks = useLineBreaks;
			return this;
		}
		
		public StringPrinter build() {
			return new StringPrinter(indent, linebreak, useLineBreaks);
		}
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	private final String indent;
	private final String linebreak;
	private final boolean useLineBreaks;
	
	public StringPrinter() {
		this(Builder.DEFAULT_INDENT, Builder.DEFAULT_LINEBREAK, Builder.DEFAULT_USE_LINE_BREAKS);
	}
	
	private StringPrinter(String indent, String linebreak, boolean useLineBreaks) {
		this.indent = indent;
		this.linebreak = linebreak;
		this.useLineBreaks = useLineBreaks;
	}
	
	@Override
	public String toString(ICmlNode cmlNode) {
		StringBuilder builder = new StringBuilder();
		printNode(cmlNode, builder, "", "");
		return builder.toString();
	}
	
	private void printNode(ICmlNode cmlNode, StringBuilder builder, String firstRowIndent, String currentIndent) {
		if(useLineBreaks) {
			builder.append(firstRowIndent);
		}
		builder.append(cmlNode.getName());
		if(!cmlNode.getAttributes().isEmpty()) {
			builder.append("(");
			if(cmlNode.getAttributes().asMap().size() == 1 && cmlNode.getAttributes().has("value")) {
				builder.append(asOutputString(cmlNode.getAttributes().get("value")));
			} else {
				boolean first = true;
				for(IKeyValuePair<String, String> attribute : cmlNode.getAttributes().getAll()) {
					if(first) {
						first = false;
					} else {
						builder.append(",");
					}
					builder.append(attribute.getName());
					builder.append("=");
					builder.append(asOutputString(attribute.getValue()));
				}
			}
			builder.append(")");
		}
		boolean needsSpace = false;
		if(!cmlNode.getProperties().isEmpty() || !cmlNode.getChildren().isEmpty()) {
			builder.append(" {");
			if(useLineBreaks) {
				builder.append(linebreak);
			}
			String bodyIndent = currentIndent+indent;
			for(IKeyValuePair<String, ICmlNode> property : cmlNode.getProperties().getAll()) {
				if(useLineBreaks) {
					builder.append(bodyIndent);
				} else if(needsSpace) {
						builder.append(" ");
				}
				builder.append(property.getName()).append(" = ");
				printNode(property.getValue(), builder, "", bodyIndent);
				needsSpace = true;
			}
			for(ICmlNode child : cmlNode.getChildren()) {
				if(useLineBreaks) {
					builder.append(bodyIndent);
				} else if(needsSpace) {
					builder.append(" ");
				} 
				printNode(child, builder, "", bodyIndent);
				needsSpace = true;
			}
			if(useLineBreaks) {
				builder.append(currentIndent);
			}
			builder.append("}");
			if(useLineBreaks) {
				builder.append(linebreak);
			}
		} else if(useLineBreaks) {
			builder.append(linebreak);
		}
	}

	private static String asOutputString(String string) {
		return "\""+string.replace("\\", "\\\\").replace("\"", "\\\"")+"\"";
	}
	
	public static String toStringDefault(ICmlNode node) {
		return DEFAULT.toString(node);
	}
}
