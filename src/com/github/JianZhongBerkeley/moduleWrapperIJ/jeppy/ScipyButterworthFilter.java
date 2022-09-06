package com.github.JianZhongBerkeley.moduleWrapperIJ.jeppy;

import java.util.HashSet;

import com.github.JianZhongBerkeley.moduleWrapperIJ.ModuleWrapperIJObject;

import ij.IJ;

import jep.Interpreter;
import jep.NDArray;
import jep.SharedInterpreter;

public class ScipyButterworthFilter extends ModuleWrapperIJObject {
	
	protected static final String PROPERTY_SRCPLOT = "srcPlot";
	protected static final String PROPERTY_ORDER = "order";
	protected static final String PROPERTY_SAMPLEFREQ = "sampleFreq";
	protected static final String PROPERTY_CUTOFFFREQ = "cutoff";
	protected static final String PROPERTY_HIGHCUTOFFFREQ = "highCutoff";
	protected static final String PROPERTY_LOWCUTOFFFREQ = "lowCutoff";
	
	protected static final int FILTER_TYPE_LOWPASS = 1;
	protected static final int FILTER_TYPE_HIGHPASS = 2;
	protected static final int FILTER_TYPE_BANDPASS = 3;
	protected static final int FILTER_TYPE_BANDSTOP = 4;
	
	public static final String CMD_TYPE_LOWPASS = "lowpass";
	public static final String CMD_TYPE_HIGHPASS = "highpass";
	public static final String CMD_TYPE_BANDPASS = "bandpass";
	public static final String CMD_TYPE_BANDSTOP = "bandstop";
	
	private String iirFilterName = "ScipyButterworthFilter";
	private int filterType = 0;
		
	public ScipyButterworthFilter() {
		super();
		this.filterType = FILTER_TYPE_LOWPASS;
		this.properties.addProperty(PROPERTY_SRCPLOT, this.properties.PTYPE_PLOTSTR, "source" , "source", "");
		this.properties.addProperty(PROPERTY_ORDER, this.properties.PTYPE_INT, "order", "order", 0);
		this.properties.addProperty(PROPERTY_SAMPLEFREQ, this.properties.PTYPE_DOUBLE, "sample frequecy (Hz)", "samplefreq", 1.0);
		this.properties.addProperty(PROPERTY_CUTOFFFREQ, this.properties.PTYPE_DOUBLE, "cutoff frequency (Hz)", "cutoff", 1.0);
		this.properties.addProperty(PROPERTY_LOWCUTOFFFREQ, this.properties.PTYPE_DOUBLE, "low cutoff frequency (Hz)", "lowcutoff", 1.0);
		this.properties.addProperty(PROPERTY_HIGHCUTOFFFREQ, this.properties.PTYPE_DOUBLE, "high cutoff frequency (Hz)", "highcutoff", 1.0);
	}
	
	// call this function in the constructor to rename the filter
	protected void setFilterName(String filterName) {
		this.iirFilterName = filterName;
	}
	
	private void setFilterType(String cmdFilterType) {
		switch(cmdFilterType) {
		case CMD_TYPE_LOWPASS:
			this.filterType = FILTER_TYPE_LOWPASS;
			break;
		case CMD_TYPE_HIGHPASS:
			this.filterType = FILTER_TYPE_HIGHPASS;
			break;
		case CMD_TYPE_BANDPASS:
			this.filterType = FILTER_TYPE_BANDPASS;
			break;
		case CMD_TYPE_BANDSTOP:
			this.filterType = FILTER_TYPE_BANDSTOP;
			break;
		}
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
		
		try(Interpreter interp = new SharedInterpreter()){
			try {
				interp.exec("from scipy import signal");
				interp.exec("import numpy");
				
				int order = (int) this.properties.getPropertyValue(PROPERTY_ORDER);
				double sampleFreq = (double) this.properties.getPropertyValue(PROPERTY_SAMPLEFREQ);
				double cutoffFreq = (double) this.properties.getPropertyValue(PROPERTY_CUTOFFFREQ);
				double lowCutoffFreq = (double) this.properties.getPropertyValue(PROPERTY_LOWCUTOFFFREQ);
				double highCutoffFreq = (double) this.properties.getPropertyValue(PROPERTY_HIGHCUTOFFFREQ);
				
				double[] dstYs = null;
				switch (filterType) {
				case FILTER_TYPE_LOWPASS:
					interp.exec("filterMode = \"lowpass\"");
					interp.set("cutoff", cutoffFreq);
					interp.exec("Wn = cutoff");
					break;
				case FILTER_TYPE_HIGHPASS:
					interp.exec("filterMode = \"highpass\"");
					interp.set("cutoff", cutoffFreq);
					interp.exec("Wn = cutoff");
					break;
				case FILTER_TYPE_BANDPASS:
					interp.exec("filterMode = \"bandpass\"");
					interp.set("lowcutoff", lowCutoffFreq);
					interp.set("highcutoff", highCutoffFreq);
					interp.exec("Wn = [lowcutoff highcutoff]");
					break;
				case FILTER_TYPE_BANDSTOP:
					interp.exec("filterMode = \"bandstop\"");
					interp.set("lowcutoff", lowCutoffFreq);
					interp.set("highcutoff", highCutoffFreq);
					interp.exec("Wn = [lowcutoff highcutoff]");
					break;
				}
				
				NDArray<double[]> srcYsND = new NDArray<>(srcYs, srcYs.length);
				interp.set("rawData", srcYsND);
				interp.set("sampleFreq", sampleFreq);
				interp.set("order", order);
				interp.exec("sos = signal.butter(order, Wn, filterMode, fs = sampleFreq, output = \"sos\")");
				interp.exec("bwData = signal.sosfilt(sos, rawData)");
				NDArray<double[]> dstYsND = (NDArray<double[]>) interp.getValue("bwData");
				dstYs = dstYsND.getData();
				
				// create plots
				String dstPlotTitle = iirFilterName + "_" + this.utilGetShortTitle(srcPlotTitle);
				this.createPlot(dstPlotTitle, "X", srcXs, "Y", dstYs);
				
				// post properties to log
				this.utilPrintPropertiesToIJLog(this.iirFilterName + "." + filterType);
			}finally {
				interp.close();
			}
		}
		
	}
	
	private void configPropertiesMode() {
		switch (filterType) {
		case FILTER_TYPE_LOWPASS:
		case FILTER_TYPE_HIGHPASS:
			this.properties.setPropertyMode(PROPERTY_LOWCUTOFFFREQ, this.properties.PMODE_HIDE);
			this.properties.setPropertyMode(PROPERTY_HIGHCUTOFFFREQ, this.properties.PMODE_HIDE);
			break;
		case FILTER_TYPE_BANDPASS:
		case FILTER_TYPE_BANDSTOP:
			this.properties.setPropertyMode(PROPERTY_CUTOFFFREQ, this.properties.PMODE_HIDE);
			break;
		}
	}
	
	@Override
	public void runGUI(String cmd) {
		int errflag = this.ERRFLAG_SUCCEEDED;
		
		setFilterType(cmd);
		// configure properties mode for different filters
		configPropertiesMode();
		
		// update properties from GUI
		errflag = this.updatePropertiesFromGUI(iirFilterName);
		if(errflag != this.ERRFLAG_SUCCEEDED) return;
		
		processData();

	}

	@Override
	public void runMacro(String cmd) {
		int errflag = this.ERRFLAG_SUCCEEDED;
		
		// decode filter type from Macro input
		HashSet<String> filterTypeCmdSet = new HashSet<>();
		filterTypeCmdSet.add(CMD_TYPE_LOWPASS);
		filterTypeCmdSet.add(CMD_TYPE_HIGHPASS);
		filterTypeCmdSet.add(CMD_TYPE_BANDPASS);
		filterTypeCmdSet.add(CMD_TYPE_BANDSTOP);
		String[] tokens = cmd.split(this.CMD_MACRO_FIELD_SPLITREG);
		if(tokens == null || tokens.length == 0) {
			IJ.error("Invalid macro input!");
			return;
		}
		for(String token : tokens) {
			if(filterTypeCmdSet.contains(token)) {
				setFilterType(token);
			}
		}
		// configure properties mode for different filters
		configPropertiesMode();
		
		// update properties from Macro
		errflag = this.updatePropertiesFromMacro(cmd);
		if(errflag != this.ERRFLAG_SUCCEEDED) return;
		
		processData();
	}

}
