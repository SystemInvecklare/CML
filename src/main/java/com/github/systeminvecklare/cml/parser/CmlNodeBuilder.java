package com.github.systeminvecklare.cml.parser;

import java.util.ArrayList;
import java.util.List;

import com.github.systeminvecklare.cml.CmlNode;
import com.github.systeminvecklare.cml.KeyValueSet;

/*protected*/ class CmlNodeBuilder implements ICmlParseHandler {
	private CmlNode rootNode = null;
	private List<CmlNode> currentNode = new ArrayList<CmlNode>();
	private CmlNode latestNode = null;
	private KeyValueSet<String, String> latestAttributes;
	private String latestProperty = null;

	@Override
	public void onTagName(String name) {
		latestNode = new CmlNode(name);
		if(rootNode == null) {
			rootNode = latestNode;
		}
		if(!currentNode.isEmpty()) {
			CmlNode activeNode = currentNode.get(currentNode.size()-1);
			if(latestProperty != null) {
				activeNode.getProperties().set(latestProperty, latestNode);
				latestProperty = null;
			} else {
				activeNode.addChild(latestNode);
			}
		}
	}

	@Override
	public void onTagBodyStart() {
		currentNode.add(latestNode);
	}

	@Override
	public void onTagBodyEnd() {
		currentNode.remove(currentNode.size()-1);
	}

	@Override
	public void onTagAttributesStart() {
		latestAttributes = latestNode.getAttributes();
	}

	@Override
	public void onTagAttribute(String name, String value) {
		latestAttributes.set(name, value);
	}

	@Override
	public void onTagAttributesEnd() {
		latestAttributes = null;
	}

	@Override
	public void onProperty(String name) {
		latestProperty = name;
	}
	
	public CmlNode getRootNode() {
		return rootNode;
	}
}
