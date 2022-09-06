package com.github.JianZhongBerkeley.moduleWrapperIJ.jeppy;

import ij.IJ;
import jep.Interpreter;
import jep.NDArray;
import jep.SharedInterpreter;

public class ScipyNdimageGaussianFilter1D extends ScipyFunction {

	protected static final String PROPERTY_SRCPLOT = "srcPlot";
	private static final String PROPERTY_SIGMA = "sigma";
	private static final String PROPERTY_ORDER = "order";
	private static final String PROPERTY_MODE = "mode";
	private static final String[] PROPERTY_MODE_CHOICES = {"reflect", "constant", "nearest", "mirror", "wrap"};
	private static final String PROPERTY_CVAL = "cval";
	private static final String PROPERTY_TRUNCATE = "truncate";
	
	public ScipyNdimageGaussianFilter1D() {
		super();
		this.setFuncName("scipy.ndimage.gaussian_filter1d");
		this.properties.addProperty(PROPERTY_SRCPLOT, this.properties.PTYPE_PLOTSTR, "source" , "source", "");
		this.properties.addProperty(PROPERTY_SIGMA, this.properties.PTYPE_DOUBLE, "sigma", "sigma", 1.0);
		this.properties.addProperty(PROPERTY_ORDER, this.properties.PTYPE_INT, "order", "order", 0);
		this.properties.addProperty(PROPERTY_MODE, this.properties.PTYPE_STRCHOICE, "mode", "mode", PROPERTY_MODE_CHOICES[0]);
		this.properties.setPropertyChoices(PROPERTY_MODE, PROPERTY_MODE_CHOICES);
		this.properties.addProperty(PROPERTY_CVAL, this.properties.PTYPE_DOUBLE, "cval", "cval", 0.0);
		this.properties.addProperty(PROPERTY_TRUNCATE, this.properties.PTYPE_DOUBLE, "truncate", "truncate", 4.0);
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
		double sigma = (double) this.properties.getPropertyValue(PROPERTY_SIGMA);
		int order = (int) this.properties.getPropertyValue(PROPERTY_ORDER);
		String mode = (String) this.properties.getPropertyValue(PROPERTY_MODE);
		double cval = (double) this.properties.getPropertyValue(PROPERTY_CVAL);
		double truncate = (double) this.properties.getPropertyValue(PROPERTY_TRUNCATE);
		
		// process data
		try(Interpreter interp = new SharedInterpreter()){
			try {
				double[] dstYs = null;
				double[] dstXs = null;
				interp.exec("from scipy.ndimage import gaussian_filter1d");
				interp.set("sigma", sigma);
				interp.set("order", order);
				interp.set("mode", mode);
				interp.set("cval", cval);
				interp.set("truncate", truncate);
				NDArray<double[]> srcYsND = new NDArray<>(srcYs, srcYs.length);
				interp.set("src", srcYsND);
				interp.exec("dst = gaussian_filter1d(input=src, sigma=sigma, axis=-1, order=order, output=None, mode=mode, cval=cval, truncate=truncate)");
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
