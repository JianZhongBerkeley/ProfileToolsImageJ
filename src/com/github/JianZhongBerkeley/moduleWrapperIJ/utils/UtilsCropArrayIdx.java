package com.github.JianZhongBerkeley.moduleWrapperIJ.utils;

import com.github.JianZhongBerkeley.moduleWrapperIJ.ModuleWrapperIJObject;
import com.github.JianZhongBerkeley.arrayUtils.*;

import ij.IJ;

public class UtilsCropArrayIdx extends ModuleWrapperIJObject {

	protected static final String PROPERTY_SRCPLOT = "srcPlot";
	protected static final String PROPERTY_STARTIDX = "startIdx";
	protected static final String PROPRETY_ENDIDX = "endIdx";
	
	private String methodName = "CropArray";
	
	public UtilsCropArrayIdx() {
		super();
		this.properties.addProperty(PROPERTY_SRCPLOT, this.properties.PTYPE_PLOTSTR, "source" , "source", "");
		this.properties.addProperty(PROPERTY_STARTIDX, this.properties.PTYPE_INT, "start Idx", "startidx", 0);
		this.properties.addProperty(PROPRETY_ENDIDX, this.properties.PTYPE_INT, "end Idx", "endidx", 0);
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
		
		// process data
		int startIdx = (int) this.properties.getPropertyValue(PROPERTY_STARTIDX);
		int endIdx = (int) this.properties.getPropertyValue(PROPRETY_ENDIDX);
		
		startIdx = Math.max(startIdx, 0);
		endIdx = Math.min(endIdx, srcYs.length);
		
		double[] dstXs = null;
		double[] dstYs = null;
		if(endIdx > startIdx) {
			dstXs = ArrayUtilSubarray.subarray(srcXs, 0, endIdx - startIdx);
			dstYs = ArrayUtilSubarray.subarray(srcYs, startIdx, endIdx);
		}
		
		// create plots
		String dstPlotTitle = methodName + "_" + this.utilGetShortTitle(srcPlotTitle);
		this.createPlot(dstPlotTitle, "X", dstXs, "Y", dstYs);
		
		// post properties to log
		this.utilPrintPropertiesToIJLog(methodName);
		
	}
	
	@Override
	public void runGUI(String cmd) {
		int errflag = this.ERRFLAG_SUCCEEDED;
		
		// update properties from GUI
		errflag = this.updatePropertiesFromGUI(methodName);
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
