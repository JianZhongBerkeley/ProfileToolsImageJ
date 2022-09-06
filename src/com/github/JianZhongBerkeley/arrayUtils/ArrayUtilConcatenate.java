package com.github.JianZhongBerkeley.arrayUtils;

public class ArrayUtilConcatenate {
	public static double[] concatenate(double[] src1, double[] src2) {
		double[] dst = null;
		if(src1 == null && src2 == null) {
			return null;
		}else if(src1 == null) {
			dst = new double[src2.length];
			for(int i = 0; i < src2.length; i++) {
				dst[i] = src2[i];
			}
		}else if(src2 == null) {
			dst = new double[src1.length];
			for(int i = 0; i < src1.length; i++) {
				dst[i] = src1[i];
			}
		}else {
			dst = new double[src1.length + src2.length];
			for(int i = 0; i < src1.length; i++) {
				dst[i] = src1[i];
			}
			for(int i = 0; i < src2.length; i++) {
				dst[src1.length + i] = src2[i];
			}
		}
		return dst;
	}
}
