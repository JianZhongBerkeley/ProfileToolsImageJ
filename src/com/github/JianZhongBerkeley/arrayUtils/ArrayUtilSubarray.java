package com.github.JianZhongBerkeley.arrayUtils;

public class ArrayUtilSubarray {

	public static double[] subarray(double[] src, int startIdx) {
		if(src == null || startIdx >= src.length) return null;
		return subarray(src, startIdx, src.length);
	}
	
	public static double[] subarray(double[] src, int startIdx, int endIdx) {
		if(src == null || endIdx <= startIdx) return null;
		startIdx = Math.max(startIdx, 0);
		endIdx = Math.min(endIdx, src.length);
		int dstLen = endIdx - startIdx;
		double[] dst = new double[dstLen];
		for(int i = 0; i < dstLen; i++) {
			dst[i] = src[startIdx + i];
		}
		return dst;
	}
	
}
