package com.github.JianZhongBerkeley.arrayUtils;

public abstract class ArrayUtilPadArray {
	public static final String DIRECTION_BOTH = "both";
	public static final String DIRECTION_POST = "post";
	public static final String DIRECTION_PRE = "pre";
	
	private String direction = null;
	
	public ArrayUtilPadArray() {
		setDirection(DIRECTION_BOTH);
	}
	
	public ArrayUtilPadArray(String diretion) {
		setDirection(diretion);
	}
	
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	// implement this method in subclass
	protected abstract double[] getPrePadArray(double[] src, int padSize);
	
	// implement this method in subclass
	protected abstract double[] getPostPadArray(double[] src, int padSize);
	
	public double[] padArray(double[] src, int padSize) {
		if(src == null || src.length == 0) return null;
		double[] dst = new double[src.length];
		for(int i = 0; i < src.length; i++) {
			dst[i] = src[i];
		}
		if(padSize < 0) return dst;
		if(this.direction.equals(DIRECTION_PRE) || this.direction.equals(DIRECTION_BOTH)) {
			double[] prePadArray = getPrePadArray(src, padSize);
			dst = ArrayUtilConcatenate.concatenate(prePadArray, dst);
		}
		if(this.direction.equals(DIRECTION_POST) || this.direction.equals(DIRECTION_BOTH)) {
			double[] postPadArray = getPostPadArray(src, padSize);
			dst = ArrayUtilConcatenate.concatenate(dst, postPadArray);
		}
		return dst;
	}
	
}	
