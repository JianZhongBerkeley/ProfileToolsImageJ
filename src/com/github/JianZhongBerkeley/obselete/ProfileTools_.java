package com.github.JianZhongBerkeley.obselete;
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

import com.github.JianZhongBerkeley.moduleWrapperIJ.pcalculator.*;

public class ProfileTools_ implements PlugIn{
	// running modes
	private final int MODE_NULL = 0;
	private final int MODE_GUI = 1;
	private final int MODE_MACRO = 2;
	
	public void run(final String args) {
		int mode = MODE_NULL;
		// Processing input arguments
		String cmd = null;
		String macroOptions =  Macro.getOptions();
		if(macroOptions == null){
			cmd = args;
			mode = MODE_GUI;
		}else {
			cmd = macroOptions;
			mode = MODE_MACRO;
		}
		
		//Debug Code START
		IJ.log("macroOptions = " + macroOptions + ";");
		IJ.log("args = " + args + ";");
		IJ.log("mode = " + mode);
		String[] wins = WindowManager.getImageTitles();
		for(int i = 0; i < wins.length; i++) {
			IJ.log("wins[" + i + "] = " + wins[i]);
		}
		//Debug Code END
		
		PCalScale pcalscale = new PCalScale();
		switch(mode) {
		case MODE_GUI:
			pcalscale.runGUI(cmd);
			break;
		case MODE_MACRO:
			pcalscale.runMacro(cmd);
			break;
		default:
			IJ.error("Invalid mode!\n");
			break;
		}
		
	}
}

//public class ProfileProcessing_ implements PlugIn{
//	// running modes
//	private final int MODE_NULL = 0;
//	private final int MODE_INTERACTIVE = 1;
//	private final int MODE_MACRO = 2;
//	
//	public void run(final String cmdArgs) {
//		int mode = MODE_NULL;
//		// Processing input arguments
//		String options =  Macro.getOptions();
//		if(!cmdArgs.equals("")) {
//			options = cmdArgs;
//		}
//		mode = MODE_MACRO;
//		if(options == null) {
//			GenericDialog optionDialog = new GenericDialog("Test Input");
//			optionDialog.addStringField("options = ", "test option");
//			
//			optionDialog.showDialog();
//			if(optionDialog.wasCanceled()) {
//				return;
//			}
//			
//			options = optionDialog.getNextString();
//			mode = MODE_INTERACTIVE;
//		}
//		
//		IJ.log("options = " + options + ";\n"); // Test Output
//		
//		// Obtain input data
//		ImagePlus imp = null;
//		switch(mode) {
//		case MODE_INTERACTIVE:
//			imp = WindowManager.getCurrentImage();
//			break;
//		case MODE_MACRO:
//			int tmpIdx = options.indexOf(" ");
//			String tmpImpName = options.substring(0, tmpIdx);
//			final int[] idlist = WindowManager.getIDList();
//			if(idlist == null) {
//				IJ.error("no window exists!");
//				return;
//			}else {
//				for(int i = 0; i < idlist.length; i++) {
//					imp = WindowManager.getImage(idlist[i]);
//					IJ.log(tmpImpName + ";");
//					IJ.log(imp.getTitle() + ";");
//					IJ.log(String.valueOf(tmpImpName.equals(imp.getTitle())));
//					if(tmpImpName.equals(imp.getTitle())) {
//						IJ.log("profile found");
//						break;
//					}else {
//						imp = null;
//					}
//				}
//			}
//			if(imp == null) {
//				IJ.error("no expected image!\n");
//				return;
//			}
//			break;
//		default:
//			return;	
//		}
//		
//		ImageWindow win =imp.getWindow();
//		
//		// Obtain source plot data
//		float[] srcXvalues = new float[0];
//		float[] srcYvalues = new float[0];
//	
//		if(imp.getProperty("XValues") != null) {
//			srcXvalues = (float[])imp.getProperty("XValues");
//			srcYvalues = (float[])imp.getProperty("YValues");
//		}else if (win!=null && win instanceof PlotWindow) {
//			PlotWindow pw = (PlotWindow)win;
//			srcXvalues = pw.getXValues();
//			srcYvalues = pw.getYValues();
//		}else {
//			IJ.error("No plot");
//			return;
//		}
//		
//		// Debug output source code
//		for(int i = 0; i < srcXvalues.length; i++) {
//			IJ.log("x[" + i + "]=" + srcXvalues[i] + " ");
//		}
//		for(int i = 0; i < srcYvalues.length; i++) {
//			IJ.log("y[" + i + "]=" + srcYvalues[i] + " ");
//		}
//		IJ.log("\n");
//		
//		// Generate example dst values
//		
//		double[] dstXvalues = new double[srcXvalues.length];
//		double[] dstYvalues = new double[srcYvalues.length];
//		
//		for(int i = 0; i < srcXvalues.length; i++) {
//			dstXvalues[i] = srcXvalues[i];
//		}
//		for(int i = 0; i < srcYvalues.length; i++) {
//			dstYvalues[i] = 2*srcYvalues[i];
//		}
//		
//		
//		String title = getImgShortTitle(imp) + " test plot";
//		Plot resultPlot = new Plot(title, "x", "y");
//		resultPlot.addPoints(dstXvalues, dstYvalues, Plot.LINE);
//		resultPlot.show();		
//		
//	}
//	
//	String getImgShortTitle(ImagePlus imp) {
//		String title = imp.getTitle();
//		int idx = title.lastIndexOf('.');
//		if (idx>0 && (title.length()-idx) <= 5)
//			title = title.substring(0, idx);
//		return title;
//	}
//}
