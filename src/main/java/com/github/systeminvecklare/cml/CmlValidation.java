package com.github.systeminvecklare.cml;

public final class CmlValidation {
	private CmlValidation() {}
	
	public static boolean isValidNodeName(String name) {
		if(name == null) {
			return false;
		}
		for(int i = 0; i < name.length(); ++i) {
			char c = name.charAt(i);
			if(Character.isWhitespace(c)) {
				return false;
			} else if(isSyntax(c)) {
				return false;
			} else if(c == '"') {
				return false;
			}
		}
		return true;
	}
	
	/*package-protected*/ static String validateNodeName(String name) throws IllegalArgumentException {
		if(!isValidNodeName(name)) {
			throw new IllegalArgumentException("\""+name+"\" is not a valid node name");
		}
		return name;
	}
	
	public static boolean isSyntax(char c) {
		return c == '{' || c == '}' || c == ',' || c == '(' || c == ')' || c == '=';
	}
}
