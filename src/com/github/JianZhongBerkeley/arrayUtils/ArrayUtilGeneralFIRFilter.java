package com.github.JianZhongBerkeley.arrayUtils;

import com.github.JianZhongBerkeley.exceptions.ArrayUtilException;

public class ArrayUtilGeneralFIRFilter {

	private double[] kernel = null;
	
	public void setkernel(double[] kernel) throws ArrayUtilException {
		if(kernel == null || kernel.length == 0) {
			throw new ArrayUtilException("Invalid kernel!");
		}
		this.kernel = new double[kernel.length];
		for(int i = 0; i < kernel.length; i++) {
			this.kernel[i] = kernel[i];
		}
	}
	
	public double[] getkernel() {
		return this.kernel;
	}
	
	public double[] filter(double[] src) throws ArrayUtilException {
		if(kernel == null) {
			throw new ArrayUtilException("Kernel == null!");
		}
		if(src == null || src.length == 0) return null;
		double[] dst = new double[src.length];
		int winOffset = - this.kernel.length/2;
		for(int i = 0; i < src.length; i++) {
			for(int j = 0; j < this.kernel.length; j++) {
				int sampleIdx = i + j + winOffset;
				sampleIdx = Math.max(sampleIdx, 0);
				sampleIdx = Math.min(sampleIdx, src.length-1);
				dst[i] += src[sampleIdx] * this.kernel[j];
			}
		}
		return dst;
	}
	
	
}
