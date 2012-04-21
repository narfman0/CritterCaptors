package com.blastedstudios.crittercaptors.util;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.badlogic.gdx.Files.FileType;

/**
 * Saves preferences and other user data not related to characters
 */
public class OptionsUtil {
	private static final String OPTIONS_PATH = "Games/CritterCaptors/options.xml";
	private HashMap<OptionEnum,String> options = new HashMap<OptionEnum, String>();
	private final Element documentElement;
	
	public enum OptionEnum{
		Accelerometer, Debug, GestureActions, GetHeightmap, Gps, MovementInvert, TouchMovement;
	}
	
	public OptionsUtil(){
		Document optionsDoc = XMLUtil.parse(OPTIONS_PATH, FileType.External); 
		if(optionsDoc == null){
			optionsDoc = XMLUtil.create();
			Element optionsEle = optionsDoc.createElement("options");
			optionsDoc.appendChild(optionsEle);
		}
		documentElement = optionsDoc.getDocumentElement();
		NodeList optionsList = documentElement.getElementsByTagName("option");
		for(Element optionElement : XMLUtil.iterableElementList(optionsList))
			options.put(OptionEnum.valueOf(optionElement.getAttribute("name")), optionElement.getAttribute("value"));
	}
	
	public boolean getOptionBoolean(OptionEnum option){
		return Boolean.parseBoolean(getOption(option));
	}

	public String getOption(OptionEnum option){
		return options.get(option);
	}

	public void saveOption(OptionEnum option, Boolean value){
		saveOption(option, value.toString());
	}
	
	public void saveOption(OptionEnum option, String value){
		String name = option.name();
		options.put(option, value);
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
