package com.github.JianZhongBerkeley.moduleWrapperIJ.jeppy;

import java.util.HashSet;

import ij.IJ;
import jep.Interpreter;
import jep.NDArray;
import jep.SharedInterpreter;

public class ScipySignalButter extends ScipyFunction {

	protected static final String PROPERTY_SRCPLOT = "srcPlot";
	protected static final String PROPERTY_ORDER = "order";
	protected static final String PROPERTY_SAMPLEFREQ = "sampleFreq";
	protected static final String PROPERTY_CUTOFFFREQ = "cutoff";
	protected static final String PROPERTY_BANDWDITH = "bandwidth";
	protected static final String PROPERTY_BTYPE = "btype";
	
	public static final String BTYPE_LOWPASS = "lowpass";
	public static final String BTYPE_HIGHPASS = "highpass";
	public static final String BTYPE_BANDPASS = "bandpass";
	public static final String BTYPE_BANDSTOP = "bandstop";
	
	public ScipySignalButter() {
		super();
		this.setFuncName("scipy.signal.butter");
		this.properties.addProperty(PROPERTY_SRCPLOT, this.properties.PTYPE_PLOTSTR, "source" , "source", "");
		this.properties.addProperty(PROPERTY_ORDER, this.properties.PTYPE_INT, "order", "order", 3);
		this.properties.addProperty(PROPERTY_SAMPLEFREQ, this.properties.PTYPE_DOUBLE, "sample frequecy (Hz)", "samplefreq", 1000.0);
		this.properties.addProperty(PROPERTY_CUTOFFFREQ, this.properties.PTYPE_DOUBLE, "center cutoff frequency (Hz)", "cutoff", 250.0);
		this.properties.addProperty(PROPERTY_BANDWDITH, this.properties.PTYPE_DOUBLE, "bandwidth (Hz)", "bandwith", 0.0);
		String[] btypeChoices = {BTYPE_LOWPASS, BTYPE_HIGHPASS, BTYPE_BANDPASS, BTYPE_BANDSTOP};
		this.properties.addProperty(PROPERTY_BTYPE, this.properties.PTYPE_STRCHOICE, "btype", "btype", BTYPE_LOWPASS);
		this.properties.setPropertyChoices(PROPERTY_BTYPE, btypeChoices);
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
		int order = (int) this.properties.getPropertyValue(PROPERTY_ORDER);
		double sampleFreq = (double) this.properties.getPropertyValue(PROPERTY_SAMPLEFREQ);
		double cutoffFreq = (double) this.properties.getPropertyValue(PROPERTY_CUTOFFFREQ);
		double bandwidth = (double) this.properties.getPropertyValue(PROPERTY_BANDWDITH);
		String btype = (String) this.properties.getPropertyValue(PROPERTY_BTYPE);
		
		// process data
		double[] dstYs = null;
		double[] dstXs = null;
		
		try(Interpreter interp = new SharedInterpreter()){
			try {
				interp.exec("from scipy import signal");
				interp.set("order", order);
				interp.set("fs", sampleFreq);
				interp.set("btype", btype);
				switch(btype) {
				case BTYPE_LOWPASS:
				case BTYPE_HIGHPASS:
					interp.set("Wn", cutoffFreq);
					break;
				case BTYPE_BANDPASS:
				case BTYPE_BANDSTOP:
					interp.set("lowcutoff", sampleFreq - bandwidth/2);
					interp.set("highcutoff", sampleFreq + bandwidth/2);
					interp.exec("Wn = [lowcutoff, highcutoff]");
					break;
				}
				NDArray<double[]> srcYsND = new NDArray<>(srcYs, srcYs.length);
				interp.set("src", srcYsND);
				interp.exec("sos = signal.butter(N=order, Wn=Wn, btype=btype, fs=fs, output=\"sos\")");
				interp.exec("dst = signal.sosfilt(sos, src)");
				NDArray<double[]> dstYsND = (NDArray<double[]>) interp.getValue("dst");
				dstYs = dstYsND.getData();
				
				dstXs = srcXs;
				
				// create plots
				String dstPlotTitle = this.getFuncName() + "." + btype + "_" + this.utilGetShortTitle(srcPlotTitle);
				this.createPlot(dstPlotTitle, "X", dstXs, "Y", dstYs);
						
				// post properties to log
				this.utilPrintPropertiesToIJLog(this.getFuncName());
			}finally {
				interp.close();
			}
		}
	}

}
