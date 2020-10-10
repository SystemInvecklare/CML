package com.github.systeminvecklare.cml.builder;

import com.github.systeminvecklare.cml.ICmlNode;

public interface ICmlNodeBuilder {
	ICmlNodeBuilder addProperty(String name, String tagName);
	void setAttribute(String name, String value);
	ICmlNodeBuilder addChild(String name);
	ICmlNode getNode();
}
