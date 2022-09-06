// Find python script path
pyScriptPath = File.openDialog("Choose ProfileOffset.py");
print("Python Script Path = " + pyScriptPath);

// Create a plot
arrLen = 400;
sinAmp = 100;
sinOffset = 100;
sinFreq = 10;
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
offset = 100;
run("PT-runPythonScript", "pypath="+ pyScriptPath +" source=" + srcPlot + " offset=" + offset);

