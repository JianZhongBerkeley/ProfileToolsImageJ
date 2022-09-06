package com.github.JianZhongBerkeley.playground;

import com.github.JianZhongBerkeley.moduleWrapperIJ.ModuleWrapperIJ;

import ij.IJ;
import ij.gui.GenericDialog;


public class IJGenericDialogTest implements ModuleWrapperIJ {

	enum testEnum{
		monday,
		tuesday,
		wednesday;
	}
	
	@Override
	public void runGUI(String cmd) {
		// TODO Auto-generated method stub
		testEnum testVal = testEnum.monday;
		GenericDialog testDialog = new GenericDialog("test dialog");
		testDialog.addEnumChoice(cmd, testVal);
		testDialog.showDialog();
		if(testDialog.wasCanceled()) {
			return;
		}
		
		//testVal = testDialog.getNextEnumChoice(testEnum);
	}

	@Override
	public void runMacro(String cmd) {
		// TODO Auto-generated method stub

	}

}
