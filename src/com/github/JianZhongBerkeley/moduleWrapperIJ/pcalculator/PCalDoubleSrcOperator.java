package com.github.JianZhongBerkeley.moduleWrapperIJ.pcalculator;

import com.github.JianZhongBerkeley.moduleWrapperIJ.ModuleWrapperIJObject;

import ij.IJ;

public class PCalDoubleSrcOperator extends ModuleWrapperIJObject {

	protected static final String PROPERTY_SRCPLOT1 = "srcPlot1";
	protected static final String PROPERTY_SRCPLOT2 = "srcPlot2";
	
	private String operatorName = "Operator";
	
	public PCalDoubleSrcOperator() {
		super();
		this.properties.addProperty(PROPERTY_SRCPLOT1, this.properties.PTYPE_PLOTSTR, "source1" , "source1", "");
		this.properties.addProperty(PROPERTY_SRCPLOT2, this.properties.PTYPE_PLOTSTR, "source2" , "source2", "");
	}
	
	// call this method in subclass constructor to rename operator
	protected void setOperatorName(String opName) {
		this.operatorName = opName;
	}
	
	//Override this method in subclass
	protected double[] applyOperatorXs(double[] src1, double[] src2) {
		return src1;
	}
	//Override this method in subclass
	protected double[] applyOperatorYs(double[] src1, double[] src2) {
		return src1;
	}
	
	
	private void processData() {
		// get data from plot
		String srcPlotTitle1 = (String) this.properties.getPropertyValue(PROPERTY_SRCPLOT1); 
		double[] src1Xs = this.getPlotXs(srcPlotTitle1);
		double[] src1Ys = this.getPlotYs(srcPlotTitle1);
		if(src1Xs == null || src1Ys == null) {
			IJ.error("Failed to get data from plot!");
			return;
		}
		String srcPlotTitle2 = (String) this.properties.getPropertyValue(PROPERTY_SRCPLOT2); 
		double[] src2Xs = this.getPlotXs(srcPlotTitle2);
		double[] src2Ys = this.getPlotYs(srcPlotTitle2);
		if(src2Xs == null || src2Ys == null) {
			IJ.error("Failed to get data from plot!");
			return;
		}
		
		// conduct calculation
		double[] dstXs = applyOperatorXs(src1Xs, src2Xs);
		double[] dstYs = applyOperatorYs(src1Ys, src2Ys);
		
		//IJ.log("dstXs.length = " + dstXs.length); // debug code 
		//IJ.log("dstYs.length = " + dstYs.length); // debug code
		
		// create plots
		String dstPlotTitle = operatorName + "_" + this.utilGetShortTitle(srcPlotTitle1) + "_" +  this.utilGetShortTitle(srcPlotTitle2);
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
