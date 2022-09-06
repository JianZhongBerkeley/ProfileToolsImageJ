package com.github.JianZhongBerkeley.moduleWrapperIJ.pcalculator;

public class PCalPow extends PCalSingleSrcOperator {
	private static final String PROPERTY_EXPONENT = "exponent";
	
	public PCalPow() {
		super();
		this.properties.addProperty(PROPERTY_EXPONENT, this.properties.PTYPE_DOUBLE,  "exponent",  "exponent",  1.0);
		setOperatorName("Pow");
	}
	
	@Override
	protected double[] applyOperatorYs(double[] src) {
		double exp = (double) properties.getPropertyValue(PROPERTY_EXPONENT);
		if(src == null) return null;
		double[] dst = new double[src.length];
		for(int i = 0; i < dst.length; i++) {
			dst[i] = Math.pow(src[i], exp);
		}
		return dst;
	}
}
