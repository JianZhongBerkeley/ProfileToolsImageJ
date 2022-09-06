package com.github.JianZhongBerkeley.moduleWrapperIJ.pcalculator;

import com.github.JianZhongBerkeley.moduleWrapperIJ.ModuleWrapperIJObject;

import ij.IJ;

public class PCalSingleSrcOperator extends ModuleWrapperIJObject {
	protected static final String PROPERTY_SRCPLOT = "srcPlot";
	
	private String operatorName = "Operator";
	
	public PCalSingleSrcOperator() {
		super();
		this.properties.addProperty(PROPERTY_SRCPLOT, this.properties.PTYPE_PLOTSTR, "source" , "source", "");
	}
	
	// call this method in subclass constructor to rename operator
	protected void setOperatorName(String opName) {
		this.operatorName = opName;
	}
	
	//Override this method in subclass
	protected double[] applyOperatorYs(double[] src) {
		return src;
	}
	//Override this method in subclass
	protected double[] applyOperatorXs(double[] src) {
		return src;
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
		
		// conduct calculation
		double[] dstXs = applyOperatorXs(srcXs);
		double[] dstYs = applyOperatorYs(srcYs);
		
		// create plots
		String dstPlotTitle = operatorName + "_" + this.utilGetShortTitle(srcPlotTitle);
		this.createPlot(dstPlotTitle, "X", dstXs, "Y", dstYs);
		
		this.utilPrintPropertiesToIJLog(operatorName);
	}
	
	@Override
	public void runGUI(String cmd) {
		int errflag = this.ERRFLAG_SUCCEEDED;
		
		// update properties from GUI
		errflag = this.updatePropertiesFromGUI(operatorName);
		if(errflag != this.ERRFLAG_SUCCEEDED) return;
		
		processData();

	}

	@Override
	public void runMacro(String cmd) {
		int errflag = this.ERRFLAG_SUCCEEDED;
		
		// update properties from Marco
		errflag = this.updatePropertiesFromMacro(cmd);
		if(errflag != this.ERRFLAG_SUCCEEDED) return;
		
		processData();
	}
	
	
}
