package com.github.JianZhongBerkeley.arrayUtils;

public class ArrayUtilPadArraySymmetric extends ArrayUtilPadArray {
	
	public ArrayUtilPadArraySymmetric(String direction) {
		super(direction);
	}
	
	protected double[] getPrePadArray(double[] src, int padSize) {
		if(src == null || src.length == 0 || padSize < 0) return null;
		double[] dst = new double[padSize];
		int srcCnt = 0;
		int dstIdx = padSize-1;
		while(dstIdx >= 0) {
			int srcIdx = srcCnt % src.length;
			if((srcCnt/src.length)%2 == 1) {
				srcIdx = src.length - 1 - srcIdx;
			}
			dst[dstIdx] = src[srcIdx];
			dstIdx--;
			srcCnt++;
		}
		return dst;
	}

	protected double[] getPostPadArray(double[] src, int padSize) {
		if(src == null || src.length == 0 || padSize < 0) return null;
		double[] dst = new double[padSize];
		int srcCnt = 0;
		int dstIdx = 0;
		while(dstIdx < padSize) {
			int srcIdx = srcCnt % src.length;
			if((srcCnt/src.length)%2 == 0) {
				srcIdx = src.length - 1 - srcIdx;
			}
			dst[dstIdx] = src[srcIdx];
			dstIdx++;
			srcCnt++;
		}
		return dst;
	}
}
