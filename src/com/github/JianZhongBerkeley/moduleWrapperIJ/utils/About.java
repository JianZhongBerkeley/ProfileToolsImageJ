package com.github.JianZhongBerkeley.moduleWrapperIJ.utils;

import ij.gui.GenericDialog;

public class About {
	
	private final static String mesage = "Profile tools is an ImageJ plugin which provides toolkits for processing and analyzing ImageJ 1D profiles.\n"
			+ "Functions are accessible via both GUI and Macro.\n"
			+ "\n"
			+ "Current supported functions:\n"
			+ "\t1.Profile Calcurlator\n"
			+ "\t2.Array Operations\n"
			+ "\t3.Scipy Data Processing (jep-4.0.0)\n"
			+ "\t4.Simple Python Script (jep-4.0.0)\n"
			+ "\n"
			+ "Developer: Jian Zhong (jian.zhong@berkeley.edu)\n"
			+ "Comments, suggestions, bug reports, etc. are welcome!\n";
	
	public static void aboutDialog() {
		GenericDialog aboutDialog = new GenericDialog("About dialog");
		aboutDialog.addMessage(mesage);
		
		aboutDialog.showDialog();
		
	}
}
