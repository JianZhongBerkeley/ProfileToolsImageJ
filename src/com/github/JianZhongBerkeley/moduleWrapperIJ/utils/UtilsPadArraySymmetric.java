package com.github.JianZhongBerkeley.moduleWrapperIJ.utils;

import com.github.JianZhongBerkeley.arrayUtils.ArrayUtilPadArray;
import com.github.JianZhongBerkeley.arrayUtils.*;

public class UtilsPadArraySymmetric extends UtilsPadArray {
	
	public UtilsPadArraySymmetric() {
		this.setPadName("PadArraySymmetric");
	}
	
	@Override
	protected ArrayUtilPadArray getPadMethod() {
		String padDirection = (String) this.properties.getPropertyValue(PROPRETY_PADDIRECTION);
		return new ArrayUtilPadArraySymmetric(padDirection);
	}

}
