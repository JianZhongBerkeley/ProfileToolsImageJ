package com.github.JianZhongBerkeley.moduleWrapperIJ.jeppy;

import ij.IJ;
import jep.Interpreter;
import jep.NDArray;
import jep.SharedInterpreter;

public class ScipyNdimageMedianFilter extends ScipyFunction {

	protected static final String PROPERTY_SRCPLOT = "srcPlot";
	protected static final String PROPERTY_KERNELSIZE = "kernel_size";
	protected static final String PROPERTY_MODE = "mode";
	protected static final String[] PROPERTY_MODE_CHOICES = {"reflect", "constant", "nearest", "mirror", "wrap"};
	protected static final String PROPERTY_CVAL = "cval";
	protected static final String PROPERTY_ORINGIN = "oringin";
	
	
	public ScipyNdimageMedianFilter() {
		super();
		this.setFuncName("scipy.ndimage.median_filter");
		this.properties.addProperty(PROPERTY_SRCPLOT, this.properties.PTYPE_PLOTSTR, "source" , "source", "");
		this.properties.addProperty(PROPERTY_KERNELSIZE, this.properties.PTYPE_INT, "kernel_size", "kernel_size", 3);
		this.properties.addProperty(PROPERTY_MODE, this.properties.PTYPE_STRCHOICE, "mode", "mode", PROPERTY_MODE_CHOICES[0]);
		this.properties.setPropertyChoices(PROPERTY_MODE, PROPERTY_MODE_CHOICES);
		this.properties.addProperty(PROPERTY_CVAL, this.properties.PTYPE_DOUBLE, "cval", "cval", 0.0);
		this.properties.addProperty(PROPERTY_ORINGIN, this.properties.PTYPE_INT, "origin", "origin", 0);
	}
	
	@Override
	void processData() {
		// get data from plot
		String srcPlotTitle = (String) this.properties.getPropertyValue(PROPERTY_SRCPLOT); 
		double[] srcXs = this.getPlotXs(srcPlotTitle);
		double[] srcYs = this.getPlotYs(srcPlotTitle);
		if(srcXs == null || srcYs == null) {
			IJ.error("Failed to get data from plot!");
			return;
		}
		
		// get data from input
		int kernelSize = (int) this.properties.getPropertyValue(PROPERTY_KERNELSIZE);
		String mode = (String) this.properties.getPropertyValue(PROPERTY_MODE);
		double cval = (double) this.properties.getPropertyValue(PROPERTY_CVAL);
		int origin = (int) this.properties.getPropertyValue(PROPERTY_ORINGIN);
		
		// process data
		try(Interpreter interp = new SharedInterpreter()){
			try {
			double[] dstYs = null;
			double[] dstXs = null;
			interp.exec("from scipy.ndimage import median_filter");
			interp.set("size", kernelSize);
			interp.set("mode", mode);
			interp.set("cval", cval);
			interp.set("origin", origin);
			NDArray<double[]> srcYsND = new NDArray<>(srcYs, srcYs.length);
			interp.set("src", srcYsND);
			interp.exec("dst = median_filter(input=src, size=size, mode=mode, cval=cval, origin=origin)");
			NDArray<double[]> dstYsND = (NDArray<double[]>) interp.getValue("dst");
			dstYs = dstYsND.getData();
			dstXs = srcXs;
			
			// create plots
			String dstPlotTitle = this.getFuncName() + "_" + this.utilGetShortTitle(srcPlotTitle);
			this.createPlot(dstPlotTitle, "X", dstXs, "Y", dstYs);
					
			// post properties to log
			this.utilPrintPropertiesToIJLog(this.getFuncName());
			}finally {
				interp.close();
			}
		}
	}

}
