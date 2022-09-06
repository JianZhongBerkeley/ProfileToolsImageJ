// create plots
arrLen = 500;
sampleFreq = 1000;

sinAmp = 100;
sinOffset = 200;
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
cosOffset = 200;
cosFreq = 10;
cosXs = newArray(arrLen);
cosYs = newArray(arrLen);
for(i = 0; i < arrLen; i++){
	cosXs[i] = i;
	cosYs[i] = cosAmp * cos(2*PI*(cosFreq/sampleFreq)*i) + cosOffset;
}
Plot.create("CosWave", "Xs", "Ys", cosXs, cosYs);
Plot.show();

// run macros

// profile calculators

//scale input profile: dstYs[i] = scale * srcYs[i]
run("PT-Scale", "source=SineWave scale=0.5");

//add offset to input profile: dstYs[i] = srcYs[i] + offset 
run("PT-Offset", "source=SineWave offset=50");

//apply java Math.pow() to input profile: dstYs[i] = Math.pow(srcYs[i], exponent)
run("PT-Pow", "source=SineWave exponent=2");

//add two input profiles: dstYs[i] = srcYs1[i] + srcYs2[i]
run("PT-Add", "source1=SineWave source2=CosWave");

//subtract input profile2 from input profile1: dstYs[i] = srcYs1[i] - srcYs2[i]
run("PT-Subtract", "source1=SineWave source2=CosWave");

//mutiply two input profiles: dstYs[i] = srcYs1[i] * srcYs2[i]
run("PT-Multiply", "source1=SineWave source2=CosWave");

//divided input profile2 from input profile1: dstYs[i] = srcYs1[i] / srcYs2[i]
run("PT-Divide", "source1=SineWave source2=CosWave");

// Utils

//pad array with mirror reflections of the array along the border 
run("PT-PadArraySymmetric", "source=SineWave padsize=50 direction=both");

//pad array with circular repetition of elements within the dimension.
run("PT-PadArrayCircular", "source=SineWave padsize=50 direction=both");

//pad array by repeating its border elements
run("PT-PadArrayReplicate", "source=SineWave padsize=50 direction=both");

//pad array with elements of constant value.
run("PT-PadArrayConstant", "source=SineWave padsize=50 direction=both padconst=0");

//crop array from startidx(included) to endidx(excluded), startidx and endidx are zero-indexed
run("PT-CropArrayIdx", "source=SineWave startidx=50 endidx=150");

//perform moving array of array
run("PT-MovingAverageArray", "source=SineWave winsize=20");

//Scipy functions

//interface for scipy.ndimage.gaussian_filter1d function
run("PT-scipy.ndimage.gaussian_filter1d", "source=SineWave sigma=10 order=0 mode=reflect cval=0 truncate=4");

//interface for scipy.ndimage.median_filter function
run("PT-scipy.ndimage.median_filter", "source=SineWave kernel_size=10 mode=reflect cval=0 origin=0");

//interface for scipy.ndimage.percentile_filter function
run("PT-scipy.ndimage.percentile_filter", "source=SineWave percentile=80 kernel_size=10 mode=reflect cval=0 origin=0");

//interface for scipy.signal.butter function
//for btype = lowpass or highpass, cutoff frequency is cutoff, bandwidth is ignored
//for btype = bandpass or bandstop, lower cutoff freqnecy is (cutoff-0.5*bandwith) and upper cutoff frequency is (cutoff+0.5*bandwith)
run("PT-scipy.signal.butter", "source=SineWave order=3 samplefreq=1000 cutoff=250 bandwith=0 btype=lowpass");

