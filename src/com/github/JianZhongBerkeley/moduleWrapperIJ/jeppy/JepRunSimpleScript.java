package com.github.JianZhongBerkeley.moduleWrapperIJ.jeppy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.github.JianZhongBerkeley.moduleWrapperIJ.ModuleWrapperIJObject;

import ij.IJ;
import ij.gui.GenericDialog;
import ij.io.DirectoryChooser;
import ij.io.OpenDialog;
import jep.Interpreter;
import jep.NDArray;
import jep.SharedInterpreter;

/**
 * Jep interface used to run python script with automatically generated GUI and macro
 * @author jian zhong
 * @version 1.0
 */
public class JepRunSimpleScript extends ModuleWrapperIJObject {

	private static final String FUNC_TYPE_SINGLEPLOT2SINGLEPLOT = "pt_single_plot_to_single_plot";
	private static final String FUNC_TYPE_SINGLEPLOT2VAL = "pt_single_plot_value";
	private static final String FUNC_TYPE_DOUBLEPLOT2SINGLEPLOT = "pt_double_plot_to_single_plot";
	
	private static final String SINGLEPLOT_PROPERTY_SRCPLOT = "source";
	private static final String DOUBLEPLOT_PROPERTY_SRCPLOT1 = "source1";
	private static final String DOUBLEPLOT_PROPERTY_SRCPLOT2 = "source2";
	
	private static final String MACRO_PYSCRIPTPATH = "pypath";
	
	// read python function name from python script function definition
	private String getPyFuncName(final String pyFuncDefLine) {
		String funcName = null;
		int slowPtr = 0;
		int fastPtr = 0;
		boolean hasDef = false;
		while(fastPtr < pyFuncDefLine.length()) {
			char curChar = pyFuncDefLine.charAt(fastPtr);
			if(curChar == ' ') {
				if(slowPtr < fastPtr) {
					if(hasDef) {
						funcName = pyFuncDefLine.substring(slowPtr, fastPtr);
						break;
					}else {
						if(pyFuncDefLine.substring(slowPtr, fastPtr).equals("def")) {
							hasDef = true;
						}else {
							break;
						}
					}
				}
				slowPtr = fastPtr+1;
			}
			if(curChar == '(') {
				if(slowPtr < fastPtr) {
					funcName = pyFuncDefLine.substring(slowPtr, fastPtr);
					break;
				}
			}
			fastPtr++;
		}
		return funcName;
	}
	
	// register an input parameters in the python script to PlugIn properties
	private String[] getPyFuncParaParts(final String pyFuncDefLine) {
		// Assume the pyFuncDefLine is always a valid python function define
		String[] paraParts = null;
		int paraStart = 0;
		while(paraStart < pyFuncDefLine.length() && pyFuncDefLine.charAt(paraStart) != '(') {
			paraStart++;
		}
		paraStart++;
		int paraEnd = pyFuncDefLine.length() - 1;
		while(paraEnd >= 0 && pyFuncDefLine.charAt(paraEnd) != ')') {
			paraEnd--;
		}
		if(paraStart > paraEnd) return null;
		paraParts = pyFuncDefLine.substring(paraStart, paraEnd).split(",");
		return paraParts;
	}
		
	// Reigster python function parameters to PlugIn properties
	private int regPyFunParaToProperty(final String pyFuncParaPart) {
		// Decompose paraParts fields
		String[] paraFields = new String[3]; //{paraName : paraType = defaultVal}
		int wField = 0;
		int slowPtr = 0;
		int fastPtr = 0;
		while(fastPtr < pyFuncParaPart.length()) {
			char curChar = pyFuncParaPart.charAt(fastPtr);
			if(curChar == ' ' || curChar == ':' || curChar == '=') {
				if(slowPtr < fastPtr) {
					paraFields[wField++] =  pyFuncParaPart.substring(slowPtr, fastPtr);
				}
				slowPtr = fastPtr+1;
			}
			if(curChar == ':') wField = 1;
			if(curChar == '=') wField = 2;
			fastPtr++;
		}
		if(slowPtr < fastPtr) {
			paraFields[wField] = pyFuncParaPart.substring(slowPtr, fastPtr);
		}
		
		//System.out.print("paraFields = {" +paraFields[0] + "," + paraFields[1] + "," + paraFields[2] + "}"); //Debug Code
		
		// Assign fields to properties
		String pName = paraFields[0];
		if(pName == null) return ERRFLAG_FAILED;
		int pType = this.properties.PTYPE_NULL;
		Object pVal = null;
		if(paraFields[1] == null) paraFields[1] = "";
		switch(paraFields[1]) {
		case "int":
			pType = this.properties.PTYPE_INT;
			pVal = paraFields[2] == null ? 0 : Integer.parseInt(paraFields[2]);
			break;
		case "float":
			pType = this.properties.PTYPE_DOUBLE;
			pVal = paraFields[2] == null ? 0 : Double.parseDouble(paraFields[2]);
			break;
		case "str":
			pType = this.properties.PTYPE_STRING;
			if(paraFields[2] == null) {
				pVal = "";
			}else {
				int head = 0;
				int tail = paraFields[2].length();
				while(head < tail) {
					char startChar = paraFields[2].charAt(head);
					char endChar = paraFields[2].charAt(tail-1);
					if(startChar == endChar && (startChar == '\'' || startChar == '\"')) {
						head++;
						tail--;
					}else {
						break;
					}
				}
				if(head < tail) {
					pVal = paraFields[2].substring(head, tail);
				}else {
					pVal = "";
				}
			}
			break;
		case "bool":
			pType = this.properties.PTYPE_BOOL;
			pVal = paraFields[2] == null ? false : Boolean.parseBoolean(paraFields[2]);
			break;
		default:
			pType = this.properties.PTYPE_DOUBLE;
			pVal = paraFields[2] == null ? 0 : Double.parseDouble(paraFields[2]);
			break;
		}
		this.properties.addProperty(pName, pType, pName, pName, pVal);
		return this.ERRFLAG_SUCCEEDED;
	}
	
	// register an input parameter in the python script to PlugIn properties
	private String processProperties(final String pyscript) {
		String funcType = null;
		try(BufferedReader buffferedReader = new BufferedReader(new FileReader(pyscript))){
			String line;
			while((line = buffferedReader.readLine()) != null) {
				String funcName = getPyFuncName(line);
				if(funcName == null) continue;
				if(funcName.equals(FUNC_TYPE_SINGLEPLOT2SINGLEPLOT) || funcName.equals(FUNC_TYPE_SINGLEPLOT2VAL)) {
					String[] paraParts = getPyFuncParaParts(line);
					//System.out.println("paraParts.length" + paraParts.length); // Debug Code
					if(paraParts != null && paraParts.length >= 2) {
						funcType = funcName;
						this.properties.addProperty(SINGLEPLOT_PROPERTY_SRCPLOT, this.properties.PTYPE_PLOTSTR, SINGLEPLOT_PROPERTY_SRCPLOT, SINGLEPLOT_PROPERTY_SRCPLOT, "");
						for(int i = 2; i < paraParts.length; i++) {
							int err_flag = regPyFunParaToProperty(paraParts[i]);
							if(err_flag == this.ERRFLAG_FAILED) {
								throw new Exception("Failed to register para #" + i);
							}
						}
					}
					break;
				}
				if(funcName.equals(FUNC_TYPE_DOUBLEPLOT2SINGLEPLOT)) {
					String[] paraParts = getPyFuncParaParts(line);
					if(paraParts != null && paraParts.length >= 4) {
						funcType = funcName;
						this.properties.addProperty(DOUBLEPLOT_PROPERTY_SRCPLOT1, this.properties.PTYPE_PLOTSTR, DOUBLEPLOT_PROPERTY_SRCPLOT1, DOUBLEPLOT_PROPERTY_SRCPLOT1, "");
						this.properties.addProperty(DOUBLEPLOT_PROPERTY_SRCPLOT2, this.properties.PTYPE_PLOTSTR, DOUBLEPLOT_PROPERTY_SRCPLOT2, DOUBLEPLOT_PROPERTY_SRCPLOT2, "");
						for(int i = 4; i < paraParts.length; i++) {
							int err_flag = regPyFunParaToProperty(paraParts[i]);
							if(err_flag == this.ERRFLAG_FAILED) {
								throw new Exception("Failed to register para #" + i);
							}
						}
					}
					break;
				}
			}
		}catch(Exception e) {
			IJ.error("Exception in processing properties:" + e);
			return null;
		}
		return funcType;
	}
	
	// split file dir path, file name, and extensions
	private String[] splitFileParts(final String fullPath) {
		String[] fileParts = new String[3];
		int lastDirIdx = fullPath.lastIndexOf('\\');
		int extenDotIdx = fullPath.lastIndexOf('.');
		lastDirIdx = Math.max(lastDirIdx, 0);
		extenDotIdx = Math.max(extenDotIdx, 0);
		fileParts[0] = fullPath.substring(0, lastDirIdx);
		fileParts[1] = fullPath.substring(lastDirIdx+1, extenDotIdx);
		fileParts[2] = fullPath.substring(extenDotIdx+1, fullPath.length());
		return fileParts;
	}
	
	// get python script path from Macro input
	private String macroGetPyPath(final String cmdMacro) {
		String pyScriptPath = null; 
		String[] tokens = cmdMacro.split(this.CMD_MACRO_FIELD_SPLITREG);
		if(tokens == null || tokens.length == 0) {
			IJ.error("Invalid macro input!");
			return null;
		}
		for(String token : tokens) {
			int splitIdx = token.indexOf(this.CMD_MACRO_OPVAL_SPLITREG);
			if(splitIdx < 0 || splitIdx >= token.length() -1) {
				continue; // ignore invalid tokens
			}
			String opMacro = token.substring(0, splitIdx);
			String valMacro = token.substring(splitIdx+1); 
			if(opMacro.equals(MACRO_PYSCRIPTPATH)) {
				pyScriptPath = valMacro;
				break;
			}	
		}
		return pyScriptPath;
	}
	
	// run single script with single plot as input and single plot as output
	private void runScriptSinglePlotToSinglePlot(String scriptPath) {
		// init python interpreter and function define
		try(Interpreter interp = new SharedInterpreter()){
			try {
				String[] scriptPathParts = splitFileParts(scriptPath);
				
				interp.exec("import numpy");
				//interp.exec("import os");
				//interp.exec("os.chdir(r\"" + scriptPathParts[0] +"\")"); // change working directory to pyscript directory
				interp.exec("exec(open(r\"" + scriptPath + "\").read())");
				
				// Set values to python interpreter
				StringBuilder pyFuncParaList = new StringBuilder();
				
				String srcPlotTitle = (String) this.properties.getPropertyValue(SINGLEPLOT_PROPERTY_SRCPLOT); 
				double[] srcXs = this.getPlotXs(srcPlotTitle);
				double[] srcYs = this.getPlotYs(srcPlotTitle);
				if(srcXs == null || srcYs == null) {
					IJ.error("Failed to get data from plot!");
					return;
				}
				pyFuncParaList.append("srcXs, srcYs");
				NDArray<double[]> srcXsND = new NDArray<>(srcXs, srcXs.length);
				NDArray<double[]> srcYsND = new NDArray<>(srcYs, srcYs.length);
				interp.set("srcXs", srcXsND);
				interp.set("srcYs", srcYsND);
				// Set other parameters
				String[] pNames = this.properties.getProperties();
				for(int i = 1; i < pNames.length; i++) {
					String pName = pNames[i];
					interp.set(pName, this.properties.getPropertyValue(pName));
					pyFuncParaList.append(", " + pName);
				}
				String pyFunCall = "dstXs, dstYs = " + FUNC_TYPE_SINGLEPLOT2SINGLEPLOT + "(" + pyFuncParaList.toString() + ")";
				interp.exec(pyFunCall);
				
				double[] dstXs = null;
				double[] dstYs = null;
				
				NDArray<double[]> dstXsND = (NDArray<double[]>) interp.getValue("dstXs");
				dstXs = dstXsND.getData();
				NDArray<double[]> dstYsND = (NDArray<double[]>) interp.getValue("dstYs");
				dstYs = dstYsND.getData();
				
				// create plots
				String dstPlotTitle = scriptPathParts[1] + "_" + this.utilGetShortTitle(srcPlotTitle);
				this.createPlot(dstPlotTitle, "X", dstXs, "Y", dstYs);
				
				// post properties to log
				this.utilPrintPropertiesToIJLog(scriptPathParts[1] + "." + scriptPathParts[2]);
			}finally {
				interp.close();
			}
		}
	}
	
	// run single script with single plot as input and single value as output
	private void runScriptSinglePlotToSingleVal(String scriptPath) {
		// init python interpreter and function define
		try(Interpreter interp = new SharedInterpreter()){
			try {
				
				String[] scriptPathParts = splitFileParts(scriptPath);

				interp.exec("import numpy");
				//interp.exec("import os");
				//interp.exec("os.chdir(r\"" + scriptPathParts[0] +"\")"); // change working directory to pyscript directory
				interp.exec("exec(open(r\"" + scriptPath + "\").read())");
		
				// Set values to python interpreter
				StringBuilder pyFuncParaList = new StringBuilder();

				String srcPlotTitle = (String) this.properties.getPropertyValue(SINGLEPLOT_PROPERTY_SRCPLOT); 
				double[] srcXs = this.getPlotXs(srcPlotTitle);
				double[] srcYs = this.getPlotYs(srcPlotTitle);
				if(srcXs == null || srcYs == null) {
					IJ.error("Failed to get data from plot!");
					return;
				}
				pyFuncParaList.append("srcXs, srcYs");
				NDArray<double[]> srcXsND = new NDArray<>(srcXs, srcXs.length);
				NDArray<double[]> srcYsND = new NDArray<>(srcYs, srcYs.length);
				interp.set("srcXs", srcXsND);
				interp.set("srcYs", srcYsND);
				// Set other parameters
				String[] pNames = this.properties.getProperties();
				for(int i = 1; i < pNames.length; i++) {
					String pName = pNames[i];
					interp.set(pName, this.properties.getPropertyValue(pName));
					pyFuncParaList.append(", " + pName);
				}
				String pyFunCall = "dstVal = " + FUNC_TYPE_SINGLEPLOT2VAL + "(" + pyFuncParaList.toString() + ")";
				interp.exec(pyFunCall);
				interp.exec("dstValStr = str(dstVal)");
				
				String dstValStr = (String) interp.getValue("dstValStr");
				
				// post properties to log
				this.utilPrintPropertiesToIJLog(scriptPathParts[1] + "." + scriptPathParts[2]);
			
				// post result to log
				IJ.log("result = " + dstValStr);
			}finally {
				interp.close();
			}
		}
	}
	
	
	// run single script with single plot as input and single plot as output
	private void runScriptDoublePlotToSinglePlot(String scriptPath) {
		// init python interpreter and function define
		try(Interpreter interp = new SharedInterpreter()){
			try {
				String[] scriptPathParts = splitFileParts(scriptPath);
				
				interp.exec("import numpy");
				//interp.exec("import os");
				//interp.exec("os.chdir(r\"" + scriptPathParts[0] +"\")"); // change working directory to pyscript directory
				interp.exec("exec(open(r\"" + scriptPath + "\").read())");
		
				// Set values to python interpreter
				StringBuilder pyFuncParaList = new StringBuilder();

				String srcPlotTitle1 = (String) this.properties.getPropertyValue(DOUBLEPLOT_PROPERTY_SRCPLOT1); 
				double[] srcXs1 = this.getPlotXs(srcPlotTitle1);
				double[] srcYs1 = this.getPlotYs(srcPlotTitle1);
				if(srcXs1 == null || srcYs1 == null) {
					IJ.error("Failed to get data from plot!");
					return;
				}
				pyFuncParaList.append("srcXs1, srcYs1");
				NDArray<double[]> srcXs1ND = new NDArray<>(srcXs1, srcXs1.length);
				NDArray<double[]> srcYs1ND = new NDArray<>(srcYs1, srcYs1.length);
				interp.set("srcXs1", srcXs1ND);
				interp.set("srcYs1", srcYs1ND);
				
				pyFuncParaList.append(", ");
				
				String srcPlotTitle2 = (String) this.properties.getPropertyValue(DOUBLEPLOT_PROPERTY_SRCPLOT2); 
				double[] srcXs2 = this.getPlotXs(srcPlotTitle2);
				double[] srcYs2 = this.getPlotYs(srcPlotTitle2);
				if(srcXs2 == null || srcYs2 == null) {
					IJ.error("Failed to get data from plot!");
					return;
				}
				pyFuncParaList.append("srcXs2, srcYs2");
				NDArray<double[]> srcXs2ND = new NDArray<>(srcXs2, srcXs2.length);
				NDArray<double[]> srcYs2ND = new NDArray<>(srcYs2, srcYs2.length);
				interp.set("srcXs2", srcXs2ND);
				interp.set("srcYs2", srcYs2ND);
				
				// Set other parameters
				String[] pNames = this.properties.getProperties();
				for(int i = 2; i < pNames.length; i++) {
					String pName = pNames[i];
					interp.set(pName, this.properties.getPropertyValue(pName));
					pyFuncParaList.append(", " + pName);
				}
				String pyFunCall = "dstXs, dstYs = " + FUNC_TYPE_DOUBLEPLOT2SINGLEPLOT + "(" + pyFuncParaList.toString() + ")";
				interp.exec(pyFunCall);
					
				double[] dstXs = null;
				double[] dstYs = null;
					
				NDArray<double[]> dstXsND = (NDArray<double[]>) interp.getValue("dstXs");
				dstXs = dstXsND.getData();
				NDArray<double[]> dstYsND = (NDArray<double[]>) interp.getValue("dstYs");
				dstYs = dstYsND.getData();
					
				// create plots
				String dstPlotTitle = scriptPathParts[1] + "_" + this.utilGetShortTitle(srcPlotTitle1) + "_" + this.utilGetShortTitle(srcPlotTitle2);
				this.createPlot(dstPlotTitle, "X", dstXs, "Y", dstYs);
					
				// post properties to log
				this.utilPrintPropertiesToIJLog(scriptPathParts[1] + "." + scriptPathParts[2]);
			}finally {
				interp.close();
			}
		}
	}
	
	@Override
	public void runGUI(String cmd) {
		// TODO Auto-generated method stub
		int errflag = this.ERRFLAG_SUCCEEDED;
		
		// get py script file path
		OpenDialog pyPathDialog = new OpenDialog("Choose python script");
		
		
		String filePath = null;
		filePath = pyPathDialog.getPath();
		
		if(filePath == null) return;
		
		// check if it is python file
		String[] fileParts = splitFileParts(filePath);
		if(!fileParts[2].equals("py")) {
			IJ.error("Invalid file!");
			return;
		}
		
		// process Properties according to python script function definition
		String funcType = null;
		funcType = processProperties(filePath);
		
		if(funcType == null) {
			IJ.error("Invalid python script");
			return;
		}
		
		// update properties from GUI
		errflag = this.updatePropertiesFromGUI(fileParts[1]);
		if(errflag != this.ERRFLAG_SUCCEEDED) return;
		
		switch(funcType) {
		case FUNC_TYPE_SINGLEPLOT2SINGLEPLOT:
			runScriptSinglePlotToSinglePlot(filePath);
			break;
		case FUNC_TYPE_SINGLEPLOT2VAL:
			runScriptSinglePlotToSingleVal(filePath);
			break;
		case FUNC_TYPE_DOUBLEPLOT2SINGLEPLOT:
			runScriptDoublePlotToSinglePlot(filePath);
			break;
		}

	}

	@Override
	public void runMacro(String cmd) {
		// TODO Auto-generated method stub
		int errflag = this.ERRFLAG_SUCCEEDED;
		
		//IJ.log("cmd = " + cmd); // Debug Code
		
		// get file path
		String filePath = null;
		filePath = macroGetPyPath(cmd);
		
		//IJ.log("filePath = " + filePath); // Debug Code
		
		// Check if it is python script
		String[] fileParts = splitFileParts(filePath);
		if(!fileParts[2].equals("py")) {
			IJ.error("Invalid file!");
			return;
		}
		
		String funcType = null;
		funcType = processProperties(filePath);
		
		if(funcType == null) {
			IJ.error("Invalid python script");
			return;
		}
		
		errflag = this.updatePropertiesFromMacro(cmd);
		if(errflag != this.ERRFLAG_SUCCEEDED) return;
		
		switch(funcType) {
		case FUNC_TYPE_SINGLEPLOT2SINGLEPLOT:
			runScriptSinglePlotToSinglePlot(filePath);
			break;
		case FUNC_TYPE_SINGLEPLOT2VAL:
			runScriptSinglePlotToSingleVal(filePath);
			break;
		case FUNC_TYPE_DOUBLEPLOT2SINGLEPLOT:
			runScriptDoublePlotToSinglePlot(filePath);
			break;
		}
		
	}

}
