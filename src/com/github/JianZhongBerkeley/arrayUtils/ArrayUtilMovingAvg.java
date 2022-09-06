package com.github.JianZhongBerkeley.arrayUtils;

import java.util.Arrays;

import com.github.JianZhongBerkeley.exceptions.ArrayUtilException;
 

public class ArrayUtilMovingAvg {
	
	private ArrayUtilGeneralFIRFilter firFilter = null;
	
	public ArrayUtilMovingAvg() {
		this.firFilter = new ArrayUtilGeneralFIRFilter();
	}
	
	public void setWinSize(int winSize) throws ArrayUtilException{
		if(winSize <= 0) {
			throw new ArrayUtilException("winSize should be larger than 0!");
		}
		double coef = 1/((double) winSize);
		double[] maKernel = new double[winSize];
		Arrays.fill(maKernel, coef);
		this.firFilter.setkernel(maKernel);
	}
	
	public int getWinSize(){
		double[] maKernel = this.firFilter.getkernel();
		if(maKernel == null) return 0;
		return maKernel.length;
	}
	
	public double[] movingAvg(double[] src) throws ArrayUtilException{
		return this.firFilter.filter(src);
	}
	
}
