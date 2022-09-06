package com.github.JianZhongBerkeley.moduleWrapperIJ.utils;

import com.github.JianZhongBerkeley.moduleWrapperIJ.ModuleWrapperIJObject;

import ij.IJ;

import com.github.JianZhongBerkeley.arrayUtils.*;

public abstract class UtilsPadArray extends ModuleWrapperIJObject {

	protected static final String PROPERTY_SRCPLOT = "srcPlot";
	protected static final String PROPERTY_PADSIZE = "padSize";
	protected static final String PROPRETY_PADDIRECTION = "padDirection";
	
	private String padName = "PadArray";
	
	private ArrayUtilPadArray padMethod = null;
	
	public UtilsPadArray() {
		super();
		this.properties.addProperty(PROPERTY_SRCPLOT, this.properties.PTYPE_PLOTSTR, "source" , "source", "");
		this.properties.addProperty(PROPERTY_PADSIZE, this.properties.PTYPE_INT, "padsize", "padsize", 0);
		String[] padDirectionChoices = {padMethod.DIRECTION_BOTH, padMethod.DIRECTION_POST, padMethod.DIRECTION_PRE};
		this.properties.addProperty(PROPRETY_PADDIRECTION, this.properties.PTYPE_STRCHOICE, "direction", "direction", padMethod.DIRECTION_BOTH);
		this.properties.setPropertyChoices(PROPRETY_PADDIRECTION, padDirectionChoices);
	}
	
	private void setPadMethod() {
		this.padMethod = getPadMethod();
	}
	
	// implement this method in subclasses
	protected abstract ArrayUtilPadArray getPadMethod();
	
	// call this function in the constructor to rename filter
	protected void setPadName(String padname) {
		this.padName = padname;
	}
	
	private double[] padPlotYs(double[] src) {
		if(src == null) return null;
		int padSize = (int) this.properties.getPropertyValue(PROPERTY_PADSIZE);
		return padMethod.padArray(src, padSize);
	}
	
	private double[] resizePlotXs(double[] srcXs, double[] srcYs) {
		if(srcXs == null || srcYs == null) return null;
		double[] dstXs = new double[srcYs.length]; 
		double srcXsInterval = 1;
		if(srcXs.length > 1) srcXsInterval = srcXs[1] - srcXs[0];
		for(int i = 0; i < dstXs.length; i++) {
			dstXs[i] = i * srcXsInterval + srcXs[0];
		}
		return dstXs;
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
		
		setPadMethod();
		double[] dstYs = padPlotYs(srcYs);
		double[] dstXs = resizePlotXs(srcXs, dstYs);
		
		// create plots
		String dstPlotTitle = padName + "_" + this.utilGetShortTitle(srcPlotTitle);
		this.createPlot(dstPlotTitle, "X", dstXs, "Y", dstYs);
		
		// post properties to log
		this.utilPrintPropertiesToIJLog(padName);
	}
	
	@Override
	public void runGUI(String cmd) {
		int errflag = this.ERRFLAG_SUCCEEDED;
		
		// update properties from GUI
		errflag = this.updatePropertiesFromGUI(padName);
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
