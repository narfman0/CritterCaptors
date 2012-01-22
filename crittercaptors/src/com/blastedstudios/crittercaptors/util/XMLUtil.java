package com.blastedstudios.crittercaptors.util;

import java.io.File;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUtil {
	public static Iterable<Element> iterableElementList(NodeList nodeList) {
		Vector<Element> nodes = new Vector<Element>(nodeList.getLength());
		for(int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if(Element.class.isAssignableFrom(node.getClass()))
				nodes.add((Element)node);
		}
		return nodes;
	}
	
	public static Document parse(String path){
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
	        return docBuilder.parse(new File(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
