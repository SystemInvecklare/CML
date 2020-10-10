package com.github.systeminvecklare.cml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CmlNode implements ICmlNode {
	private String name;
	private final KeyValueSet<String, String> attributes = new KeyValueSet<>();
	private final KeyValueSet<String, ICmlNode> properties = new KeyValueSet<>();
	private final List<ICmlNode> children = new ArrayList<ICmlNode>();
	

	public CmlNode(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public KeyValueSet<String, String> getAttributes() {
		return attributes;
	}

	@Override
	public List<ICmlNode> getChildren() {
		return Collections.unmodifiableList(children);
	}

	@Override
	public KeyValueSet<String, ICmlNode> getProperties() {
		return properties;
	}

	public boolean addChild(ICmlNode child) {
		return children.add(child);
	}
	
	public void addChild(int index, ICmlNode child) {
		children.add(index, child);
	}
	
	public boolean removeChild(ICmlNode child) {
		return children.remove(child);
	}
	
	public ICmlNode removeChild(int index) {
		return children.remove(index);
	}
	
	@Override
	public ICmlNode getChild(String name) {
		List<ICmlNode> matchingChildren = getChildren(name, new ArrayList<>(1));
		if(matchingChildren.isEmpty()) {
			return null;
		} else if(matchingChildren.size() > 1) {
			throw new IllegalArgumentException("Multiple children with name "+name+" found.");
		} else {
			return matchingChildren.iterator().next();
		}
	}
	
	@Override
	public List<ICmlNode> getChildren(String name) {
		return getChildren(name, new ArrayList<>());
	}
	
	private <L extends List<? super ICmlNode>> L getChildren(String name, L result) {
		for(ICmlNode child : children) {
			if(name.equals(child.getName())) {
				result.add(child);
			}
		}
		return result;
	}
	
	@Override
	public CmlNode copy() {
		CmlNode copy = new CmlNode(name);
		for(ICmlNode child : children) {
			copy.children.add(child.copy());
		}
		for(IKeyValuePair<String, String> attribute : attributes) {
			copy.attributes.set(attribute.getName(), attribute.getValue());
		}
		for(IKeyValuePair<String, ICmlNode> property : properties) {
			copy.properties.set(property.getName(), property.getValue().copy());
		}
		return copy;
	}
}
