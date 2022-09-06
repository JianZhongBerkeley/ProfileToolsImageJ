package com.github.JianZhongBerkeley.moduleWrapperIJ.utils;

import com.github.JianZhongBerkeley.arrayUtils.ArrayUtilPadArray;
import com.github.JianZhongBerkeley.arrayUtils.ArrayUtilPadArrayConst;

public class UtilsPadArrayConst extends UtilsPadArray {

	private static final String PROPERTY_PADCONST = "padConst";
	
	public UtilsPadArrayConst() {
		this.setPadName("PadArrayConst");
		this.properties.addProperty(PROPERTY_PADCONST, this.properties.PTYPE_DOUBLE, "padconst", "padconst", 0.0);
	}
	
	@Override
	protected ArrayUtilPadArray getPadMethod() {
		String padDirection = (String) this.properties.getPropertyValue(PROPRETY_PADDIRECTION);
		double padConst = (double) this.properties.getPropertyValue(PROPERTY_PADCONST);
		return new ArrayUtilPadArrayConst(padDirection, padConst);
	}

}
