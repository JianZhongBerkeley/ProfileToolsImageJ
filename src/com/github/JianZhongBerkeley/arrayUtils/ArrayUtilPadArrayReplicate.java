package com.github.JianZhongBerkeley.arrayUtils;

public class ArrayUtilPadArrayReplicate extends ArrayUtilPadArray {

	public ArrayUtilPadArrayReplicate(String direction) {
		super(direction);
	}
	
	@Override
	protected double[] getPrePadArray(double[] src, int padSize) {
		if(src == null || src.length == 0 || padSize < 0) return null;
		double[] dst = new double[padSize];
		for(int i = 0; i < padSize; i++) {
			dst[i] = src[0];
		}
		return dst;
	}

	@Override
	protected double[] getPostPadArray(double[] src, int padSize) {
		if(src == null || src.length == 0 || padSize < 0) return null;
		double[] dst = new double[padSize];
		for(int i = 0; i < padSize; i++) {
			dst[i] = src[src.length - 1];
		}
		return dst;
	}

}
