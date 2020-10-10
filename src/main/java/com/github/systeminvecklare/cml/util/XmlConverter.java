package com.github.systeminvecklare.cml.util;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.github.systeminvecklare.cml.ICmlNode;
import com.github.systeminvecklare.cml.IKeyValuePair;

public class XmlConverter implements IConverter<Document> {
	private final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	
	public XmlConverter() throws ParserConfigurationException {
	}

	@Override
	public Document convert(ICmlNode cmlNode) {
		Document document = builder.newDocument();
		Element rootNode = createChild(document, document, cmlNode);
		build(document, rootNode, cmlNode);
		return document;
	}
	
	private Element createChild(Document document, Node owner, ICmlNode cmlNode) {
		return createChild(document, owner, cmlNode.getName());
	}
	
	private Element createChild(Document document, Node owner, String name) {
		Element element = document.createElement(name);
		owner.appendChild(element);
		return element;
	}

	private void build(Document document, Element xmlNode, ICmlNode cmlNode) {
		for(IKeyValuePair<String, String> attribute : cmlNode.getAttributes().getAll()) {
			xmlNode.setAttribute(attribute.getName(), attribute.getValue());
		}
		for(ICmlNode child : cmlNode.getChildren()) {
			Element newChild = createChild(document, xmlNode, child);
			build(document, newChild, child);
		}
		for(IKeyValuePair<String, ICmlNode> property : cmlNode.getProperties().getAll()) {
			Element element = createChild(document, xmlNode, "property");
			element.setAttribute("name", property.getName());
			ICmlNode propValue = property.getValue();
			build(document, createChild(document, element, propValue), propValue);
		}
	}

	public static String toPrettyString(Document document) throws TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(document), new StreamResult(writer));
		return writer.getBuffer().toString();
	}
}
