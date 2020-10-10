package com.github.systeminvecklare.cml.builder;

import com.github.systeminvecklare.cml.CmlNode;
import com.github.systeminvecklare.cml.ICmlNode;

public class CmlNodeBuilder implements ICmlNodeBuilder {
	private final CmlNode node;
	
	public CmlNodeBuilder(String tagName) {
		this(new CmlNode(tagName));
	}
	
	private CmlNodeBuilder(CmlNode node) {
		this.node = node;
	}
	
	@Override
	public ICmlNodeBuilder addProperty(String name, String tagName) {
		CmlNode property = new CmlNode(tagName);
		node.getProperties().set(name, property);
		return new CmlNodeBuilder(property);
	}

	@Override
	public void setAttribute(String name, String value) {
		node.getAttributes().set(name, value);
	}

	@Override
	public ICmlNodeBuilder addChild(String name) {
		CmlNode child = new CmlNode(name);
		node.addChild(child);
		return new CmlNodeBuilder(child);
	}

	@Override
	public ICmlNode getNode() {
		return node;
	}
}
