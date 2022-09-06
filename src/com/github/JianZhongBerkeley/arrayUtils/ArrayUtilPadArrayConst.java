package com.github.JianZhongBerkeley.arrayUtils;

public class ArrayUtilPadArrayConst extends ArrayUtilPadArray {

	private double padconst = 0;
	
	public ArrayUtilPadArrayConst(String direction, double padconst) {
		super(direction);
		this.padconst = padconst;
	}
	
	@Override
	protected double[] getPrePadArray(double[] src, int padSize) {
		if(src == null || src.length == 0 || padSize < 0) return null;
		double[] dst = new double[padSize];
		for(int i = 0; i < padSize; i++) {
			dst[i] = padconst;
		}
		return dst;
	}

	@Override
	protected double[] getPostPadArray(double[] src, int padSize) {
		if(src == null || src.length == 0 || padSize < 0) return null;
		double[] dst = new double[padSize];
		for(int i = 0; i < padSize; i++) {
			dst[i] = padconst;
		}
		return dst;
	}

}
