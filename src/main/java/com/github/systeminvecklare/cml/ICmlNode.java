package com.github.systeminvecklare.cml;

import java.util.List;

public interface ICmlNode {
	String getName();
	IKeyValueSet<String, String> getAttributes();
	List<ICmlNode> getChildren();
	IKeyValueSet<String, ICmlNode> getProperties();
	ICmlNode getChild(String name);
	List<ICmlNode> getChildren(String name);
	CmlNode copy();
}
