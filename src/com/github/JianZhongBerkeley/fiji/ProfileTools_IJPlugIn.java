package com.github.JianZhongBerkeley.fiji;
import ij.IJ;
import ij.plugin.PlugIn;

import ij.Macro;
import ij.WindowManager;

//import ij.WindowManager;
//import ij.gui.ImageWindow;
//import ij.gui.PlotWindow;
//import ij.ImagePlus;
//import ij.gui.Plot;
//
//import ij.gui.GenericDialog;

import com.github.JianZhongBerkeley.moduleWrapperIJ.*;
import com.github.JianZhongBerkeley.moduleWrapperIJ.pcalculator.*;
import com.github.JianZhongBerkeley.moduleWrapperIJ.jeppy.*;
import com.github.JianZhongBerkeley.moduleWrapperIJ.utils.*;


/**
 * This class is the entrance class for ImageJ to access PlugIn functions
 * @author jian zhong
 * @version 1.0
 */
public class ProfileTools_IJPlugIn implements PlugIn{
	// running modes
	private static final int MODE_NULL = 0;
	private static final int MODE_GUI = 1;
	private static final int MODE_MACRO = 2;
	
	private static final String FUNC_SPLITREG = "\\.";
	private static final String MACO_OP_SPLITREG = " ";
	private static final String FUNC_PKG_PCALC = "pcalc";
	private static final String FUNC_PKG_UTIL = "util";
	private static final String FUNC_TYPE_PADARRAY = "padarr";
	private static final String FUNC_TYPE_CROPARRAY = "croparr";
	private static final String FUNC_TYPE_FIRARRAY = "firarr";
	private static final String FUNC_PKG_SCIPY = "scipy";
	private static final String FUNC_TYPE_SIGNAL = "signal";
	private static final String FUNC_TYPE_NDIMAGE = "ndimage";
	private static final String FUNC_PKG_ABOUT = "about";
	
	/**
	 * ImageJ plug in entrance method
	 * @param args: input arguments for accessing different functions
	 */
	public void run(String args) {
		
		if(args.equals(FUNC_PKG_ABOUT)) {
			About.aboutDialog();
			return;
		}
		
		// Processing input arguments
		int mode = MODE_NULL;

		String macroOptions =  Macro.getOptions();
		String func = "";
		String funcCmdGUI = null;
		String funcCmdMacro = null;
		
		if(macroOptions == null){
			func = args;
			mode = MODE_GUI;
		}else {
			func = args;
			funcCmdMacro = macroOptions;
			mode = MODE_MACRO;
		}
		
//		//Debug Code START
//		IJ.log("macroOptions = " + macroOptions + ";");
//		IJ.log("args = " + args + ";");
//		IJ.log("mode = " + mode);
//		String[] wins = WindowManager.getImageTitles();
//		for(int i = 0; i < wins.length; i++) {
//			IJ.log("wins[" + i + "] = " + wins[i]);
//		}
//		//Debug Code END
	
		// break down function name
		ModuleWrapperIJ module = null;
		final String[] tokens = func.split(FUNC_SPLITREG);
//		IJ.log("tokens.length = " + tokens.length); // Debug Code
		if(tokens == null || tokens.length == 0) {
			return;
		}
		switch(tokens[0]) {
		case FUNC_PKG_PCALC: // profile calculator
			if(tokens.length < 2) {
				return;
			}
			switch(tokens[1]) {
			case "scale": // scaling profile
				//IJ.log("pcalc.scale"); //Debug Code
				module = new PCalScale();
				break;
			case "offset":
				module = new PCalOffset();
				break;
			case "pow":
				module = new PCalPow();
				break;
			case "add":
				module = new PCalAdd();
				break;
			case "subtract":
				module = new PCalSubtract();
				break;
			case "multiply":
				module = new PCalMultiply();
				break;
			case "divide":
				module = new PCalDivide();
				break;
			default:
				break;
			}
			break;
		case FUNC_PKG_UTIL: // Utils
			if(tokens.length < 2) {
				return;
			}
			switch(tokens[1]) {
			case FUNC_TYPE_PADARRAY:
				if(tokens.length < 3) {
					return;
				}
				switch(tokens[2]) {
				case "symmetric":
					module = new UtilsPadArraySymmetric();
					break;
				case "replicate":
					module = new UtilsPadArrayReplicate();
					break;
				case "circular":
					module = new UtilsPadArrayCircular();
					break;
				case "const":
					module = new UtilsPadArrayConst();
					break;
				}
				break;
			case FUNC_TYPE_CROPARRAY:
				if(tokens.length < 3) {
					return;
				}
				switch(tokens[2]) {
				case "idx":
					module = new UtilsCropArrayIdx();
					break;
				}
				break;
			case FUNC_TYPE_FIRARRAY:
				if(tokens.length < 3) {
					return;
				}
				switch(tokens[2]) {
				case "movingavg":
					module = new UtilsArrayMovingAvg();
					break;
				}
				break;
			}
			break;
		case FUNC_PKG_SCIPY: // Scipy access package
			if(tokens.length < 2) {
				return;
			}
			switch(tokens[1]) {
			case FUNC_TYPE_NDIMAGE:
				if(tokens.length < 3) {
					return;
				}
				switch(tokens[2]) {
				case "gaussian_filter1d":
					module = new ScipyNdimageGaussianFilter1D();
				break;
				case "median_filter":
					module = new ScipyNdimageMedianFilter();
				break;
				case "percentile_filter":
					module = new ScipyNdimagePercentileFilter();
				break;
				}
				break;
			case FUNC_TYPE_SIGNAL:
				if(tokens.length < 3) {
					return;
				}
				switch(tokens[2]) {
				case "butter":
					module = new ScipySignalButter();
					break;
				}
				break;
			}
			break;
		case "jeprunpyscript":
			module = new JepRunSimpleScript();
			break;
		default:
			IJ.error("GUI cmd package no found!");
			break;
		}
		// Conduct function calls
		switch(mode) {
		case MODE_GUI:
			module.runGUI(funcCmdGUI);
			break;
		case MODE_MACRO:
			if(funcCmdGUI != null) funcCmdMacro = funcCmdGUI + " " + funcCmdMacro;
			//IJ.log("funcCmdMacro = {" + funcCmdMacro + "}"); // Debug Code
			module.runMacro(funcCmdMacro);
			break;
		default:
			IJ.error("Incorret mode!");
			return;	
		}
		
	}
}
