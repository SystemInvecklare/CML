package com.github.systeminvecklare.cml.builder;

import com.github.systeminvecklare.cml.ICmlNode;

public interface ICmlNodeBuilder {
	ICmlNodeBuilder addProperty(String name, String tagName);
	ICmlNodeBuilder setAttribute(String name, String value);
	ICmlNodeBuilder addChild(String name);
	ICmlNodeBuilder setValue(String value);
	ICmlNode getNode();
}
