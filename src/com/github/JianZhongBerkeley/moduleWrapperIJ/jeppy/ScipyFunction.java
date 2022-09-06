package com.github.JianZhongBerkeley.moduleWrapperIJ.jeppy;

import com.github.JianZhongBerkeley.moduleWrapperIJ.ModuleWrapperIJObject;

/**
 * abstract class used as a template to encapsulate Scipy functions in Profile Toos
 * @author jian zhong
 * @version 1.0
 */
public abstract class ScipyFunction extends ModuleWrapperIJObject {
	
	private String funcName = "Function";
	
	public ScipyFunction() {
		super();
	}
	
	/**
	 * set name of the function of the wrapper class for different subclasses
	 * @param funcName name of function
	 */
	// call this function in the subclass constructor to rename the function
	protected void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	
	/**
	 * get name of the function of the wrapper class 
	 * @return name of functions
	 */
	protected String getFuncName() {
		return this.funcName;
	}
	
	/**
	 * data processing pipeline needed to implemented in subclassess
	 */
	// implement this method in subclasses
	abstract void processData();
	
	@Override
	public void runGUI(String cmd) {
		int errflag = this.ERRFLAG_SUCCEEDED;
		
		// update properties from GUI
		errflag = this.updatePropertiesFromGUI(funcName);
		if(errflag != this.ERRFLAG_SUCCEEDED) return;
		
		processData();
	}

	@Override
	public void runMacro(String cmd) {
		int errflag = this.ERRFLAG_SUCCEEDED;
		
		// update properties from Macro
		errflag = this.updatePropertiesFromMacro(cmd);
		if(errflag != this.ERRFLAG_SUCCEEDED) return;
		
		processData();

	}

}
