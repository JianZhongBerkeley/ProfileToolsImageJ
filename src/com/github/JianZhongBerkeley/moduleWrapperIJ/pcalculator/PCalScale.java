package com.github.JianZhongBerkeley.moduleWrapperIJ.pcalculator;

import com.github.JianZhongBerkeley.utils.*;

import ij.IJ;

import com.github.JianZhongBerkeley.exceptions.*;
import com.github.JianZhongBerkeley.obselete.ModuleWrapperIJSingleSrc;

public class PCalScale extends PCalSingleSrcOperator {
	private static final String PROPERTY_SCALE = "scale";
	
	public PCalScale() {
		super();
		this.properties.addProperty(PROPERTY_SCALE, this.properties.PTYPE_DOUBLE, "scale", "scale", 1.0);
		setOperatorName("Scale");
	}
	
	@Override
	protected double[] applyOperatorYs(double[] src) {
		double scale = (double) properties.getPropertyValue(PROPERTY_SCALE);
		if(src == null) return null;
		double[] dst = new double[src.length];
		for(int i = 0; i < dst.length; i++) {
			dst[i] = scale * src[i];
		}
		return dst;
	}

}
