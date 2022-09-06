// find python script path
pyScriptPath = File.openDialog("Choose ProfileWeightedSum.py");
print("Python Script Path = " + pyScriptPath);

// create plots
arrLen = 500;
sampleFreq = 1000;

sinAmp = 100;
sinOffset = 100;
sinFreq = 20;
sinXs = newArray(arrLen);
sinYs = newArray(arrLen);
for(i = 0; i < arrLen; i++){
	sinXs[i] = i;
	sinYs[i] = sinAmp * sin(2*PI*(sinFreq/sampleFreq)*i) + sinOffset;
}
Plot.create("SineWave", "Xs", "Ys", sinXs, sinYs);
Plot.show();

cosAmp = 100;
cosOffset = 100;
cosFreq = 10;
cosXs = newArray(arrLen);
cosYs = newArray(arrLen);
for(i = 0; i < arrLen; i++){
	cosXs[i] = i;
	cosYs[i] = cosAmp * cos(2*PI*(cosFreq/sampleFreq)*i) + cosOffset;
}
Plot.create("CosWave", "Xs", "Ys", cosXs, cosYs);
Plot.show();

// run python script
weight1 = 1;
weight2 = 1;
srcPlot1 = "SineWave";
srcPlot2 = "CosWave";
run("PT-runPythonScript", "pypath="+ pyScriptPath +" source1=" + srcPlot1 + " source2=" + srcPlot2 + " weight1=" + weight1 + " weight2=" + weight2);

