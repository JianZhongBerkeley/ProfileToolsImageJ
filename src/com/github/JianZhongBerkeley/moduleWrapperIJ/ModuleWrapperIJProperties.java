package com.github.JianZhongBerkeley.moduleWrapperIJ;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

/**
 * Class used to register properties of module, used for automatic value update from GUI and macro
 * @author jian zhong
 * @version 1.0
 */
public class ModuleWrapperIJProperties {
	
	public static final int PTYPE_NULL = 		0;
	public static final int PTYPE_INT = 		1;
	public static final int PTYPE_DOUBLE = 		2;
	public static final int PTYPE_STRING = 		3;
	public static final int PTYPE_WINSTR = 		4;
	public static final int PTYPE_PLOTSTR = 	5;
	public static final int PTYPE_BOOL = 		6;
	public static final int PTYPE_STRCHOICE = 	7;
	
	public static final int PMODE_NULL = 	0;
	public static final int PMODE_SHOW = 	1;
	public static final int PMODE_HIDE = 	2;
		
	private class Property{
		private final String name;
		private final int type;
		private final String cmdGUI;
		private final String cmdMacro;
		private int mode;
		private Object value;
		
		public Property(String name, int type, String cmdGUI, String cmdMacro, Object value){
			this.name = name;
			this.type = type;
			this.cmdGUI = cmdGUI;
			this.cmdMacro = cmdMacro;
			this.value = value;
			this.mode = PMODE_SHOW;
		}
		
		public String getName() {
			return this.name;
		}
		
		public int getType() {
			return this.type;
		}
		
		public String getCmdGUI() {
			return this.cmdGUI;
		}
		
		public String getCmdMacro() {
			return this.cmdMacro;
		}
		
		public Object getValue() {
			return this.value;
		}
		
		public void setValue(Object value) {
			this.value = value;
		}
		
		public int getMode() {
			return this.mode;
		}
		
		public void setMode(int mode) {
			this.mode = mode;
		}
	}
	
	private List<Property> properties = null;
	private HashMap<String, Integer> nameMap = null;
	private HashMap<String, Object> propertyChoices = null;
	
	public ModuleWrapperIJProperties() {
		this.properties = new ArrayList<>();
		this.nameMap = new HashMap<>();
		this.propertyChoices = new HashMap<>();
	}
	
	/**
	 * add property field to class
	 * @param name name of the property, different properties should have different names
	 * @param type type of the property, it is defined by the PTYPE_* static value of this class
	 * @param cmdGUI property label used for GUI input
	 * @param cmdMacro property label used for macro input
	 * @param value initial value of the property
	 */
	public void addProperty(String name, int type, String cmdGUI, String cmdMacro, Object value) {
		Property p = new Property(name, type, cmdGUI, cmdMacro, value);
		this.properties.add(p);
		this.nameMap.put(name, this.properties.size() - 1);
	}
	
	/**
	 * check if class contains a specific property 
	 * @param name name of the property to be checked
	 * @return boolean value indicates if property exists
	 */
	public boolean containsProperty(String name) {
		return this.nameMap.containsKey(name);
	}
	
	/**
	 * remove a property
	 * @param name name of the property to be removed
	 */
	public void rmProperty(String name) {
		if(!this.nameMap.containsKey(name)) return;
		Integer pIdx = this.nameMap.get(name);
		properties.remove(pIdx);
		nameMap.remove(name);
	}
	
	/**
	 * get the names for all the existing properties
	 * @return String array of the property names
	 */
	public String[] getProperties() {
		String[] pNames = new String[this.properties.size()];
		for(int i = 0; i < this.properties.size(); i++) {
			pNames[i] = this.properties.get(i).getName();
		}
		return pNames;
	}
	
	/**
	 * get the type of a property
	 * @param name property name
	 * @return int value indicates the property value, which should be one of the public static PTYPE_* value
	 */
	public int getPropertyType(String name) {
		if(!this.nameMap.containsKey(name)) return PTYPE_NULL;
		Integer pIdx = this.nameMap.get(name);
		return this.properties.get(pIdx).getType();
	}
	
	/**
	 * get the GUI label of a property
	 * @param name property name
	 * @return String of the GUI label 
	 */
	public String getPropertyCmdGUI(String name) {
		if(!this.nameMap.containsKey(name)) return null;
		Integer pIdx = this.nameMap.get(name);
		return this.properties.get(pIdx).getCmdGUI();
	}
	
	/**
	 * get the Macro label of a property
	 * @param name property name
	 * @return String of the Macro label 
	 */
	public String getPropertyCmdMacro(String name) {
		if(!this.nameMap.containsKey(name)) return null;
		Integer pIdx = this.nameMap.get(name);
		return this.properties.get(pIdx).getCmdMacro();
	}
	
	/**
	 * get the value of a property
	 * @param name property name
	 * @return Object value of the property value, needs to casted to appropriate types 
	 */
	public Object getPropertyValue(String name) {
		if(!this.nameMap.containsKey(name)) return null;
		Integer pIdx = this.nameMap.get(name);
		return this.properties.get(pIdx).getValue();
	}
	
	/**
	 * set the value of the property
	 * @param name property name
	 * @param value new value of the property 
	 */
	public void setPropertyValue(String name, Object value) {
		if(!this.nameMap.containsKey(name)) return;
		Integer pIdx = this.nameMap.get(name);
		this.properties.get(pIdx).setValue(value);
	}
	
	/**
	 * get the mode of the property
	 * @param name name of the property
	 * @return int value indicates the mode of the property, it should be one of the public static PMODE_* value
	 */
	public int getPropertyMode(String name) {
		if(!this.nameMap.containsKey(name)) return PMODE_NULL;
		Integer pIdx = this.nameMap.get(name);
		return this.properties.get(pIdx).getMode();
	}
	
	/**
	 * set the mode of the property
	 * @param name name of the property
	 * @param mode property mode, it should be one of the public static PMODE_* value 
	 */
	public void setPropertyMode(String name, int mode) {
		if(!this.nameMap.containsKey(name)) return;
		Integer pIdx = this.nameMap.get(name);
		this.properties.get(pIdx).setMode(mode);
	}
	
	/**
	 * set choices for a choice type properties
	 * @param name name of the property
	 * @param choices choices of property
	 */
	public void setPropertyChoices(String name, Object choices) {
		this.propertyChoices.put(name, choices);
	}
	
	/**
	 * get choices for a choice type properties
	 * @param name name of the property
	 * @return choices for the properties
	 */
	public Object getPropertyChoices(String name) {
		if(!this.propertyChoices.containsKey(name)) return null;
		return this.propertyChoices.get(name);
	}
}
