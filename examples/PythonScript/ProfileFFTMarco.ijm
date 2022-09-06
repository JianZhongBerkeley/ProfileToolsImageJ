// find python script path
pyScriptPath = File.openDialog("Choose ProfileFFT.py");
print("Python Script Path = " + pyScriptPath);

// create a plot
arrLen = 500;
sinAmp = 100;
sinOffset = 100;
sinFreq = 20;
sampleFreq = 1000;
xs = newArray(arrLen);
ys = newArray(arrLen);
for(i = 0; i < arrLen; i++){
	xs[i] = i;
	ys[i] = sinAmp * sin(2*PI*(sinFreq/sampleFreq)*i) + sinOffset;
}
Plot.create("SineWave", "Xs", "Ys", xs, ys);
Plot.show();

// run python script
srcPlot = "SineWave";
run("PT-runPythonScript", "pypath="+ pyScriptPath +" source=" + srcPlot + " fs=" + sampleFreq);
Plot.setXYLabels("Freq (Hz)", "Ys");