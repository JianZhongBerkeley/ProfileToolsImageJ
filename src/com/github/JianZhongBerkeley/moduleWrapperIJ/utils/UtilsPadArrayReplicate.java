package com.github.JianZhongBerkeley.moduleWrapperIJ.utils;

import com.github.JianZhongBerkeley.arrayUtils.ArrayUtilPadArray;
import com.github.JianZhongBerkeley.arrayUtils.*;

public class UtilsPadArrayReplicate extends UtilsPadArray {

	public UtilsPadArrayReplicate() {
		this.setPadName("PadArrayReplicate");
	}
	
	@Override
	protected ArrayUtilPadArray getPadMethod() {
		String padDirection = (String) this.properties.getPropertyValue(PROPRETY_PADDIRECTION);
		return new ArrayUtilPadArrayReplicate(padDirection);
	}

}
