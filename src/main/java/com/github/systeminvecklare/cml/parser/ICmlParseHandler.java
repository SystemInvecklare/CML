package com.github.systeminvecklare.cml.parser;

public interface ICmlParseHandler {
	void onTagName(String name);
	void onTagBodyStart();
	void onTagBodyEnd();
	void onTagAttributesStart();
	void onTagAttribute(String name, String value);
	void onTagAttributesEnd();
	void onProperty(String name);
}
