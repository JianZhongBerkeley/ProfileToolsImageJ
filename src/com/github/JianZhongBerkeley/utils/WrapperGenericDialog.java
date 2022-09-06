package com.github.JianZhongBerkeley.utils;

import ij.gui.GenericDialog;
import ij.WindowManager;
import ij.ImagePlus;

import ij.gui.ImageWindow;
import ij.gui.PlotWindow;

import java.awt.Frame;
import java.util.*;

import com.github.JianZhongBerkeley.exceptions.*;

public class WrapperGenericDialog extends GenericDialog {
	
	// Constructor
	public WrapperGenericDialog(String title) {
		super(title);
	}
	public WrapperGenericDialog(String title, Frame parent) {
		super(title, parent);
	}
	
	// Add all the image titles as choices
	public void addWindowTitleChoices(String label) throws WrapperException {
		final String[] choices = WindowManager.getImageTitles();
		if(choices == null) {
			//this.addChoice(label, choices, null);
			//throw exception here;
			throw new WrapperException("No open plot!");
		}
		this.addChoice(label, choices, WindowManager.getCurrentImage().getTitle());
	}
	
	// Add all the plot titles as choices
	public void addPlotTitleChoices(String label) throws WrapperException{
		final int[] idlist = WindowManager.getIDList();
		if(idlist == null) {
			//TODO: throw exception here
			// this.addChoice(label, null, null);
			//return;
			throw new WrapperException("No open plot!");
		}
		ImagePlus imp = null;
		ImageWindow win = null;
		List<String> plotTitleList = new ArrayList<>();
		for(int i = 0; i < idlist.length; i++) {
			imp = WindowManager.getImage(idlist[i]);
			win = imp.getWindow();
			if((imp.getProperty("YValues") != null) ||
				win!=null && win instanceof PlotWindow
			) {
				plotTitleList.add(imp.getTitle());
			}
		}
		String[] choices = new String[plotTitleList.size()];
		plotTitleList.toArray(choices);
		if(choices == null || choices.length == 0) {
			//TODO: throw exception here
			//this.addChoice(label, choices, null);
			//return;
			throw new WrapperException("No open plot!");
		}
		String curImpTitle = WindowManager.getCurrentImage().getTitle();
		int defaultChoiceIdx = 0;
		for(int i = 0; i < choices.length; i++) {
			if(curImpTitle.equals(choices[i])) {
				defaultChoiceIdx = i;
				break;
			}
		}
		this.addChoice(label, choices, choices[defaultChoiceIdx]);
	}
	
}
