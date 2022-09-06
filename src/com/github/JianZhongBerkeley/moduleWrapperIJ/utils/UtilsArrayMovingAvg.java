package com.github.JianZhongBerkeley.moduleWrapperIJ.utils;

import com.github.JianZhongBerkeley.moduleWrapperIJ.ModuleWrapperIJObject;

import ij.IJ;

import com.github.JianZhongBerkeley.exceptions.*;
import com.github.JianZhongBerkeley.arrayUtils.*;

public class UtilsArrayMovingAvg extends ModuleWrapperIJObject {
	
	private String opName = "MovingAverage";
	
	protected static final String PROPERTY_SRCPLOT = "srcPlot";
	protected static final String PROPERTY_WINSIZE = "winSize";
	
	public UtilsArrayMovingAvg(){
		super();
		this.properties.addProperty(PROPERTY_SRCPLOT, this.properties.PTYPE_PLOTSTR, "source" , "source", "");
		this.properties.addProperty(PROPERTY_WINSIZE, this.properties.PTYPE_INT, "winsize", "winsize", 1);
	}
	
	private void processData() {
		// get data from plot
		String srcPlotTitle = (String) this.properties.getPropertyValue(PROPERTY_SRCPLOT); 
		double[] srcXs = this.getPlotXs(srcPlotTitle);
		double[] srcYs = this.getPlotYs(srcPlotTitle);
		if(srcXs == null || srcYs == null) {
			IJ.error("Failed to get data from plot!");
			return;
		}
		
		int winSize = (int) this.properties.getPropertyValue(PROPERTY_WINSIZE);
		
		double[] dstXs = null;
		double[] dstYs = null;
		
		ArrayUtilMovingAvg mva = new ArrayUtilMovingAvg();
		try{
			mva.setWinSize(winSize);
			dstYs = mva.movingAvg(srcYs);
		}catch(ArrayUtilException exception) {
			IJ.error("Exception: " + exception.getMessage());
			return;
		}
		
		if(dstYs != null) {
			dstXs = srcXs;
		}
		
		// create plots
		String dstPlotTitle = opName + "_" + this.utilGetShortTitle(srcPlotTitle);
		this.createPlot(dstPlotTitle, "X", dstXs, "Y", dstYs);
		
		// post properties to log
		this.utilPrintPropertiesToIJLog(opName);
	}
	
	@Override
	public void runGUI(String cmd) {
		int errflag = this.ERRFLAG_SUCCEEDED;
		
		// update properties from GUI
		errflag = this.updatePropertiesFromGUI(opName);
		if(errflag != this.ERRFLAG_SUCCEEDED) return;
		
		processData();
	}

	@Override
	public void runMacro(String cmd) {
		int errflag = this.ERRFLAG_SUCCEEDED;
		
		// update properties from Macro
		errflag = this.updatePropertiesFromMacro(cmd);
		if(errflag != this.ERRFLAG_SUCCEEDED) return;
		
		processData();

	}

}
