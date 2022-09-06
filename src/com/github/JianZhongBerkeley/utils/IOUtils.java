package com.github.JianZhongBerkeley.utils;

import ij.WindowManager;
import ij.ImagePlus;

import ij.gui.ImageWindow;
import ij.gui.PlotWindow;
import ij.gui.Plot;

import java.util.Arrays;

import com.github.JianZhongBerkeley.exceptions.*;

import ij.IJ;

public class IOUtils {	
	// Get plot Xs using the ImagePlus reference
	public static double[] getPlotXsFromImage(ImagePlus srcImp) throws WrapperException {
		if(srcImp == null) {
			throw new WrapperException("Src image is null!");
			//return null;
		}
		
		//IJ.log("Src Title: " + srcImp.getTitle()); // Debug Code
		
		float[] srcXvalues = new float[0];
		
		ImageWindow srcWin =srcImp.getWindow();
		if(srcImp.getProperty("XValues") != null) {
			srcXvalues = (float[])srcImp.getProperty("XValues");
		}else if (srcWin!=null && srcWin instanceof PlotWindow) {
			PlotWindow srcPw = (PlotWindow)srcWin;
			srcXvalues = srcPw.getXValues();
		}else {
			throw new WrapperException("Src image is not a plot!");
			//return null;
		}
		
		double[] srcXs = new double[srcXvalues.length];
		for(int i = 0; i < srcXvalues.length; i++) {
			srcXs[i] = srcXvalues[i];
		}
		
		return srcXs;
	}
	// Get plot Xs using the Image title
	public static double[] getPlotXsFromImage(String srcImpTitle) throws WrapperException {
		return getPlotXsFromImage(WindowManager.getImage(srcImpTitle));
	}
	// Get plot Xs using the Image ID
	public static double[] getPlotXsFromImage(int srcImpID) throws WrapperException {
		return getPlotXsFromImage(WindowManager.getImage(srcImpID));
	}
	
	// Get plot Ys using the ImagePlus reference
	public static double[] getPlotYsFromImage(ImagePlus srcImp) throws WrapperException {
		if(srcImp == null) {
			throw new WrapperException("Src image is null!");
			//return null;
		}
		
		float[] srcYvalues = new float[0];
		
		ImageWindow srcWin =srcImp.getWindow();
		if(srcImp.getProperty("YValues") != null) {
			srcYvalues = (float[])srcImp.getProperty("YValues");
		}else if (srcWin!=null && srcWin instanceof PlotWindow) {
			PlotWindow srcPw = (PlotWindow)srcWin;
			srcYvalues = srcPw.getYValues();
		}else {
			throw new WrapperException("Src image is not a plot!");
			//return null;
		}
		
		double[] srcYs = new double[srcYvalues.length];
		for(int i = 0; i < srcYvalues.length; i++) {
			srcYs[i] = srcYvalues[i];
		}
		
		return srcYs;
	}
	// Get plot Ys using the Image title
	public static double[] getPlotYsFromImage(String srcImpTitle) throws WrapperException {
		return getPlotYsFromImage(WindowManager.getImage(srcImpTitle));
	}
	// Get plot Xs using the Image ID
	public static double[] getPlotYsFromImage(int srcImpID) throws WrapperException {
		return getPlotYsFromImage(WindowManager.getImage(srcImpID));
	}
	
	//Create new plot using input Xs and Ys
	public static void createPlot(String title, double[] xs, double[] ys) {
		createPlot(title, "X", xs, "Y", ys);

	}
	
	//Create new plot using input Xs and Ys
	public static void createPlot(String title, String xLabel, double[] xs, String yLabel, double[] ys) {
		// Convert double point XY values to float values for drawing
		float[] pointXs = new float[xs.length];
		for(int i = 0; i < xs.length; i++) {
			pointXs[i] = (float) xs[i];
		}
		float[] pointYs = new float[ys.length];
		for(int i = 0; i < ys.length; i++) {
			pointYs[i] = (float) ys[i];
		}
		// Create Plot
		Plot dstPlot = new Plot(title, xLabel, yLabel);
		dstPlot.addPoints(pointXs, pointYs, Plot.LINE);
		// Show plot
		dstPlot.show();

	}
	
	// process short title
	public static String getShortTitle(String title) {
		int idx = title.lastIndexOf('.');
		if (idx>0 && (title.length()-idx) <= 10)
			title = title.substring(0, idx);
		return title;
	}
}
