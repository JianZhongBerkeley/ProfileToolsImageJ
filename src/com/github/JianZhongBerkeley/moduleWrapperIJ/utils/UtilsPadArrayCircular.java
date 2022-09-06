package com.github.JianZhongBerkeley.moduleWrapperIJ.utils;

import com.github.JianZhongBerkeley.arrayUtils.*;

public class UtilsPadArrayCircular extends UtilsPadArray { 
	
	public UtilsPadArrayCircular() {
		this.setPadName("PadArrayCicular");
	}
	
	@Override
	protected ArrayUtilPadArray getPadMethod() {
		String padDirection = (String) this.properties.getPropertyValue(PROPRETY_PADDIRECTION);
		return new ArrayUtilPadArrayCircular(padDirection);
	}

}
