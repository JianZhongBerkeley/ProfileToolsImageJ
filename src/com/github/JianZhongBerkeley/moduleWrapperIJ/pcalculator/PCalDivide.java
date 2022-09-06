package com.github.JianZhongBerkeley.moduleWrapperIJ.pcalculator;

public class PCalDivide extends PCalDoubleSrcOperator {
	public PCalDivide() {
		super();
		this.setOperatorName("Divide");
	}
	
	@Override
	protected double[] applyOperatorXs(double[] src1, double[] src2) {
		if(src1 == null || src2 == null) return null;
		return src1.length < src2.length ?  src1 : src2;
	}
	
	@Override
	protected double[] applyOperatorYs(double[] src1, double[] src2) {
		if(src1 == null || src2 == null) return null;
		int dstLen = Math.min(src1.length, src2.length);
		double[] dst = new double[dstLen];
		for(int i = 0; i < dstLen; i++) {
			dst[i] = src1[i] / src2[i];
		}
		return dst;
	}
	
}
