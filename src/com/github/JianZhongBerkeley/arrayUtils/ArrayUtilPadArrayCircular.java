package com.github.JianZhongBerkeley.arrayUtils;

public class ArrayUtilPadArrayCircular extends ArrayUtilPadArray {

	public ArrayUtilPadArrayCircular(String direction) {
		super(direction);
	}
	
	@Override
	protected double[] getPrePadArray(double[] src, int padSize) {
		if(src == null || src.length == 0 || padSize < 0) return null;
		double[] dst = new double[padSize];
		int srcCnt = 0;
		int dstIdx = padSize-1;
		while(dstIdx >= 0) {
			int srcIdx = srcCnt % src.length;
			srcIdx = src.length - 1 - srcIdx;
			dst[dstIdx] = src[srcIdx];
			dstIdx--;
			srcCnt++;
		}
		return dst;
	}

	@Override
	protected double[] getPostPadArray(double[] src, int padSize) {
		if(src == null || src.length == 0 || padSize < 0) return null;
		double[] dst = new double[padSize];
		int srcCnt = 0;
		int dstIdx = 0;
		while(dstIdx < padSize) {
			int srcIdx = srcCnt % src.length;
			dst[dstIdx] = src[srcIdx];
			dstIdx++;
			srcCnt++;
		}
		return dst;
	}

}
