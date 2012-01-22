package com.blastedstudios.crittercaptors.util;

import java.util.Vector;

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
}
