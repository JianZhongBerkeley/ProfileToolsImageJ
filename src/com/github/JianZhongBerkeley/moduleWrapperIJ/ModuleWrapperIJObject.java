package com.github.JianZhongBerkeley.moduleWrapperIJ;

import java.util.HashMap;

import com.github.JianZhongBerkeley.exceptions.WrapperException;
import com.github.JianZhongBerkeley.moduleWrapperIJ.ModuleWrapperIJProperties;
import com.github.JianZhongBerkeley.utils.*;
import ij.IJ;

/**
 * ModuleWrapperIJObject is an abstract class which can be used as super class to integrate calculation modules with Profile Tools PlugIn
 * @author jian zhong
 * @version 1.0
 */
public abstract class ModuleWrapperIJObject implements ModuleWrapperIJ {
	
	protected final int ERRFLAG_SUCCEEDED = +1;
	protected final int ERRFLAG_FAILED = 	-1;
	
	protected final String CMD_MACRO_FIELD_SPLITREG = 	" ";
	protected final String CMD_MACRO_OPVAL_SPLITREG = 	"=";
	
	protected ModuleWrapperIJProperties properties = null;
	
	// constructor
	/**
	 * Constructor, initialize class properties space
	 */
	public ModuleWrapperIJObject() {
		this.properties = new ModuleWrapperIJProperties();
		
	}
	
	
	/**
	 * Automatically generate dialog to update registered properties via GUI
	 * @param dialogTitle : title of automatically generated dialog
	 * @return Error flag, indicates whether operation is successful
	 */
	// update properties from ImageJ GUI
	protected int updatePropertiesFromGUI(String dialogTitle) {
		//IJ.log("create " + dialogTitle + "..."); //Debug Code
		String[] pNames = properties.getProperties();
		if(pNames == null || pNames.length == 0) return ERRFLAG_FAILED;
		// create dialogs
		WrapperGenericDialog userDialog = new WrapperGenericDialog(dialogTitle);
		// add input fields
		for(String pName: pNames) {
			int pType = properties.getPropertyType(pName);
			Object pVal = properties.getPropertyValue(pName);
			String pCmdGUI = properties.getPropertyCmdGUI(pName);
			int pMode = properties.getPropertyMode(pName);
			if(pMode == ModuleWrapperIJProperties.PMODE_HIDE || pMode == ModuleWrapperIJProperties.PMODE_NULL) {
				continue;
			}
			switch(pType) {
			case ModuleWrapperIJProperties.PTYPE_INT:
				userDialog.addNumericField(pCmdGUI, (pVal == null) ? 0 : ((int) pVal));
				break;
			case ModuleWrapperIJProperties.PTYPE_DOUBLE:
				userDialog.addNumericField(pCmdGUI, (pVal == null) ? 0 : ((double) pVal));
				break;
			case ModuleWrapperIJProperties.PTYPE_STRING:
				userDialog.addStringField(pCmdGUI, (pVal == null) ? "" : ((String) pVal));
				break;
			case ModuleWrapperIJProperties.PTYPE_WINSTR:
				try {
					userDialog.addWindowTitleChoices(pCmdGUI);
				}catch(WrapperException wexception) {
					IJ.error("Exception: " + wexception.getMessage());
					return ERRFLAG_FAILED;
				}
				break;
			case  ModuleWrapperIJProperties.PTYPE_PLOTSTR:
				try {
					userDialog.addPlotTitleChoices(pCmdGUI);
				}catch(WrapperException wexception) {
					IJ.error("Exception: " + wexception.getMessage());
					return ERRFLAG_FAILED;
				}
				break;
			case ModuleWrapperIJProperties.PTYPE_BOOL:
				userDialog.addCheckbox(pCmdGUI, (pVal == null) ? false : ((boolean) pVal));
				break;
			case ModuleWrapperIJProperties.PTYPE_STRCHOICE:
				String[] choices = (String[]) this.properties.getPropertyChoices(pName);
				userDialog.addChoice(pCmdGUI, choices, (String)pVal);
				break;
			}	
		}
		//Show GUI Dialog
		userDialog.showDialog();
		if(userDialog.wasCanceled()) {
			return ERRFLAG_FAILED;
		}
		// Get update values from dialog input
		for(String pName: pNames) {
			int pType = properties.getPropertyType(pName);
			Object pVal = properties.getPropertyValue(pName);
			int pMode = properties.getPropertyMode(pName);
			if(pMode == ModuleWrapperIJProperties.PMODE_HIDE || pMode == ModuleWrapperIJProperties.PMODE_NULL) {
				continue;
			}
			switch(pType) {
			case ModuleWrapperIJProperties.PTYPE_INT:
				pVal = (int) userDialog.getNextNumber();
				break;
			case ModuleWrapperIJProperties.PTYPE_DOUBLE:
				pVal = (double) userDialog.getNextNumber();
				break;
			case ModuleWrapperIJProperties.PTYPE_STRING:
				pVal = userDialog.getNextString();
				break;
			case ModuleWrapperIJProperties.PTYPE_WINSTR:
			case ModuleWrapperIJProperties.PTYPE_PLOTSTR:
			case ModuleWrapperIJProperties.PTYPE_STRCHOICE:
				pVal = userDialog.getNextChoice();
				break;
			case ModuleWrapperIJProperties.PTYPE_BOOL:
				pVal = userDialog.getNextBoolean();
				break;
			}
			properties.setPropertyValue(pName, pVal);
		}
		return ERRFLAG_SUCCEEDED;
	}
	
	/**
	 * update registered properties from ImageJ macro input
	 * @param cmdMacro macro string input
	 * @return Error flag, indicates whether operation is successful
	 */
	// update property values from ImageJ Macro
	protected int updatePropertiesFromMacro(String cmdMacro) {
		// split fields
		//IJ.log("cmdMacro = {" + cmdMacro + "}"); // Debug Code
		String[] tokens = cmdMacro.split(CMD_MACRO_FIELD_SPLITREG);
		if(tokens == null || tokens.length == 0) {
			IJ.error("Invalid macro input!");
			return ERRFLAG_FAILED;
		}
		// Create cmdMacro to name map
		HashMap<String, String> macroNameMap = new HashMap<>();
		String[] pNames = properties.getProperties();
		for(String pName : pNames) {
			macroNameMap.put(properties.getPropertyCmdMacro(pName),pName);
		}
		// Split token and update values for properties
		for(String token : tokens) {
			int splitIdx = token.indexOf(CMD_MACRO_OPVAL_SPLITREG);
			if(splitIdx < 0 || splitIdx >= token.length() -1) {
				continue; // ignore invalid tokens
			}
			String opMacro = token.substring(0, splitIdx);
			String valMacro = token.substring(splitIdx+1); 
			if(macroNameMap.containsKey(opMacro)) {
				String pName = macroNameMap.get(opMacro);
				int pType = properties.getPropertyType(pName);
				Object pVal = properties.getPropertyValue(pName);
				int pMode = properties.getPropertyMode(pName);
				if(pMode == ModuleWrapperIJProperties.PMODE_HIDE || pMode == ModuleWrapperIJProperties.PMODE_NULL) {
					continue;
				}
				switch(pType) {
				case ModuleWrapperIJProperties.PTYPE_INT:
					pVal = Integer.valueOf(valMacro);
					break;
				case ModuleWrapperIJProperties.PTYPE_DOUBLE:
					pVal = Double.valueOf(valMacro);
					break;
				case ModuleWrapperIJProperties.PTYPE_STRING:
				case ModuleWrapperIJProperties.PTYPE_WINSTR:
				case ModuleWrapperIJProperties.PTYPE_PLOTSTR:
				case ModuleWrapperIJProperties.PTYPE_STRCHOICE:
					pVal = valMacro;
					break;
				case ModuleWrapperIJProperties.PTYPE_BOOL:
					pVal = Boolean.valueOf(valMacro);
					break;
				}
				properties.setPropertyValue(pName, pVal);
			}
		}
		return ERRFLAG_SUCCEEDED;
	}
	
	/**
	 * Get the X-axis data from plot
	 * @param srcPlotTitle: title of plot whose data is going to be retrieved 
	 * @return array of the X-axis data of plot
	 */
	protected double[] getPlotXs(String srcPlotTitle) {
		double[] srcXs = null;
		try{
			srcXs = IOUtils.getPlotXsFromImage(srcPlotTitle);
		}catch(WrapperException wexception) {
			IJ.error("PCalScale: Exception: " + wexception.getMessage());
		}
		return srcXs;
	}
	
	/**
	 * Get the Y-axis data from plot
	 * @param srcPlotTitle: title of plot whose data is going to be retrieved 
	 * @return array of the Y-axis data of plot
	 */
	protected double[] getPlotYs(String srcPlotTitle) {
		double[] srcYs = null;
		try{
			srcYs = IOUtils.getPlotYsFromImage(srcPlotTitle);
		}catch(WrapperException wexception) {
			IJ.error("PCalScale: Exception: " + wexception.getMessage());
		}
		return srcYs;
	}
	
	/**
	 * Draw and display a plot
	 * @param srcPlotTile title of plot
	 * @param xLabel label of the X axis
	 * @param xs data for X axis
	 * @param yLabel label of the Y axis
	 * @param ys data for Y axis
	 */
	protected void createPlot(String srcPlotTile, String xLabel, double[] xs, String yLabel, double[] ys) {
		IOUtils.createPlot(srcPlotTile, xLabel, xs, yLabel, ys);
	}
	
	/**
	 * Get title without extensions
	 * @param title input title
	 * @return processed title without extensions
	 */
	protected String utilGetShortTitle(String title) {
		int idx = title.lastIndexOf('.');
		if (idx>0 && (title.length()-idx) <= 10)
			title = title.substring(0, idx);
		return title;
	}
	
	/**
	 * print registered properties and their values to log window of ImageJ
	 * @param title title displayed at the beginning of the log field
	 */
	protected void utilPrintPropertiesToIJLog(String title) {
		IJ.log("Profile Tool Module Name: " + title);
		String[] pNames = properties.getProperties();
		for(String pName : pNames) {
			IJ.log(pName + " = " + properties.getPropertyValue(pName).toString());
		}
		IJ.log(" "); // Change line
	}
	
	/**
	 * processing pipeline when running in GUI mode, which requires to be implemented in subclasses
	 */
	// implement this method in subclasses
	@Override
	public abstract void runGUI(String cmd); 
	
	/**
	 * processing pipeline when running in macro mode, which requires to be implemented in subclasses
	 */
	// implement this method in subclasses
	@Override
	public abstract void runMacro(String cmd);

}
