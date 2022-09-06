package com.github.JianZhongBerkeley.moduleWrapperIJ;

/**
 * Interface defines functions required to be implemented for Profile Tools modules
 * @author jian zhong
 * @version 0.0
 */
public interface ModuleWrapperIJ {
	
	/**
	 * runGUI() defines processing pipeline when Profile Tools runs in GUI mode
	 * @param cmd input command strings from GUI plug in configure calls
	 */
	public void runGUI(String cmd);
	/**
	 * runMarco() defines processing pipeline when Profile Tools runs in macro mode
	 * @param cmd input command strings from GUI plug in configure calls
	 */
	public void runMacro(String cmd);
	
}
