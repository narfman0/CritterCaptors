package com.blastedstudios.crittercaptors.util;

import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Saves preferences and other user data not related to characters
 */
public class OptionsUtil {
	private static final String OPTIONS_PATH = "data/options.xml";
	public static final String USE_GPS = "Use GPS";
	private HashMap<String,String> options = new HashMap<String, String>();
	private final Element documentElement;
	
	public OptionsUtil(){
		documentElement = XMLUtil.parse(OPTIONS_PATH).getDocumentElement();
		NodeList optionsList = documentElement.getElementsByTagName("option");
		for(Element optionElement : XMLUtil.iterableElementList(optionsList))
			options.put(optionElement.getAttribute("name"), optionElement.getAttribute("value"));
	}
	
	public boolean getOptionBoolean(String option){
		return Boolean.parseBoolean(getOption(option));
	}

	public String getOption(String option){
		return options.get(option);
	}

	public void saveOption(String name, Boolean value){
		saveOption(name, value.toString());
	}
	
	public void saveOption(String name, String value){
		options.put(name, value);
		NodeList optionsList = documentElement.getElementsByTagName("option");
		for(Element optionElement : XMLUtil.iterableElementList(optionsList))
			if(optionElement.getAttribute("name").equals(name))
				documentElement.removeChild(optionElement);
		Element optionElement = documentElement.getOwnerDocument().createElement("option");
		optionElement.setAttribute("name", name);
		optionElement.setAttribute("value", value);
		documentElement.appendChild(optionElement);
		XMLUtil.writeToFile(documentElement.getOwnerDocument(), OPTIONS_PATH);
	}
}