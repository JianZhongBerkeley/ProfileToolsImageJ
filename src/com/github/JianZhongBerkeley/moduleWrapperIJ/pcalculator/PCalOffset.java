package com.github.JianZhongBerkeley.moduleWrapperIJ.pcalculator;

public class PCalOffset extends PCalSingleSrcOperator {
	
	private static final String PROPERTY_OFFSET = 	"offset";
	
	public PCalOffset() {
		super();
		this.properties.addProperty(PROPERTY_OFFSET, this.properties.PTYPE_DOUBLE, "offset", "offset", 0.0);
		setOperatorName("Offset");
	}
	
	@Override
	protected double[] applyOperatorYs(double[] src) {
		double offset = (double) properties.getPropertyValue(PROPERTY_OFFSET);
		if(src == null) return null;
		double[] dst = new double[src.length];
		for(int i = 0; i < dst.length; i++) {
			dst[i] = src[i] + offset;
		}
		return dst;
	}

}
