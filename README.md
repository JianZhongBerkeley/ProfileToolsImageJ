# Profile Tools: an ImageJ plugin for profile processing

## Description
<html>
<body>
<p style="margin-left:1%; margin-right:0%;text-indent:0%">
Profile Tools is a plugin dedicated for profile processing and analyzing in ImageJ. Profile Tools is accessible via both ImageJ GUI and macro. 
</p>
<p style="margin-left:1%; margin-right:0%;text-indent:0%">
<b>Highlights</b> of the current version:  
</p>
<ul>
<li> Supporting Python (Python 3) profile processing scripts using external Python libraries, including Numpy and Scipy </li>   
<li> Automatic generation of ImageJ GUI and macro interfaces for Python scripts </li>  
<li> Scipy profile processing functions  </li>
<li> Java profile processing utility functions </li>
</ul>
</body>
</html>

## Installation Guide
<html>
<body>
<p style="margin-left:1%; margin-right:0%; text-indent:0%">
Profile Tools requires Jep and Numpy to run Python scripts.
</p>  
<p style="margin-left:1%; margin-right:0%; text-indent:0%"><b>
 Prerequisite  
 </b></p>
 <p style="margin-left:2%; margin-right:0%; text-indent:1%">
Before installing Jep, make sure the following are completed.  
<ul>
<li>Java has been added to the PATH environment variable</li>
<li>Python (Python 3) has been installed</li>  
<li>Numpy has been installed (numpy is required to run python scripts in Profile Tools, it is recommended to install numpy before installing Jep)</li>  
</ul>
</p> 
<p style="margin-left:1%; margin-right:0%; text-indent:0%"><b>
Setup Java embedded Python (Jep)
 </b></p>
<p style="margin-left:2%; margin-right:0%; text-indent:0%">
Install Jep (e.g. install Jep using <code>pip install</code>)<br>  
Detailed installation instructions can be found on Jep github repo.<br>  
Add Jep to PATH environment variable (Tip: use <code>pip show</code> to display the Jep package location if Jep was installed using pip)<br>  
</p>
<p style="margin-left:1%; margin-right:0%; text-indent:0%"><b>
Install Profile Tools  
</b></p>
<p style="margin-left:2%; margin-right:0%; text-indent:0%">
Copy ProfileTools_.jar and the Jep JAR packages to the ImageJ plugin directory.<br>
Restart ImageJ  
</p>
</body>
</html>


## Tested dependencies:  
&nbsp; Java SE 1.8  
&nbsp; Jep 4.0.6  
&nbsp; Python 3.10.6  
&nbsp; Numpy 1.23.6  
&nbsp; Scipy 1.9.0  

## Profile Tools Python scripts guide
<html>
<body>
<p style="margin-left:1%; margin-right:0%;text-indent:0%">
Profile tools currently supports three types of python scripts:  
</p>
<p style="margin-left:1%; margin-right:0%;text-indent:0%"><b>
Python script with a single profile plot as input and a single profile plot as output  
</b></p>
<p style="margin-left:2%; margin-right:0%;text-indent:1%">
To run such a python script, <code>pt_single_plot_to_single_plot</code> function is required to be defined in the python script and the profile processing pipeline should be included in the body of the function.The first and second input arguments are X- and Y-axis data from the input ImageJ profile plot. The return value of this function should be a tuple containing X-axis and Y-axis data for the result profile plot. All X- and Y-axis data for input and output profile plots are 1-dimensional Numpy arrays. You may add more input arguments in the function definition but please make sure to use Python 3 type hint to specify the input argument type. Currently int, float, bool, and str are supported by Profile Tools. Profile Tools will automatically generate ImageJ GUI and macro interface according to the function definition. The variable names in the ImageJ interface will be the names of the input arguments defined in the python function. The result will be displayed as an ImageJ plot.
</p> 
<p style="margin-left:2%; margin-right:0%;text-indent:1%">
Please check <i>ProfileOffset.py</i> and <i>ProfileOffsetMacro.ijm</i> in the <i>/examples/PythonScript</i> directory for examples.
</p>  
<p style="margin-left:1%; margin-right:0%;text-indent:0%"><b>
Python script with a single profile plot as input and a single value as output
</b></p>  
<p style="margin-left:2%; margin-right:0%;text-indent:1%">
To run such a python script, <code>pt_single_plot_value</code> function is required to be defined in the python script and the profile processing pipeline should be included in the body of the function.The first and second input arguments are X- and Y-axis data from the input ImageJ profile plot. And the return value should be a value (of any type that can be converted into string). All the X- and Y-axis data for input profile plots are 1-dimensional numpy arrays. You may add more input arguments in the function definition but please make sure to use Python 3 type hint to specify the input argument type. Currently int, float, bool, and str types are supported by Profile Tools. Profile Tools will automatically generate ImageJ GUI and macro interface according to the function definition. The variable names in the ImageJ interface will be the names of the input arguments defined in the python function. The result plot will be displayed in the Log window of ImageJ.  
</p>
<p style="margin-left:2%; margin-right:0%;text-indent:1%">
Please check <i>ProfileMean.py</i> and <i>ProfileMeanMacro.ijm</i> in the <i>examples/PythonScript</i> directory for examples.  
</p>
<p style="margin-left:1%; margin-right:0%;text-indent:0%"><b>
Python script with two profile plots as input and a single profile plot as output
</b></p>  
<p style="margin-left:2%; margin-right:0%;text-indent:1%">
To run such a python script, <code>pt_double_plot_to_single_plot</code> function is required to be defined in the python script and the data processing pipeline should be included in the body of the function.The first and second input arguments are X- and Y-axis data from the first input ImageJ profile plot.The third and fourth input arguments are X- and Y-axis data from the second input ImageJ profile plot. And the return value of this function should be a tuple containing X- and Y-axis data for the result profile plot. All the X- and Y-axis data for input and output profile plots are 1-dimensional numpy arrays. You may add more input arguments in the function definition but please make sure to use Python 3 type hint to specify the input argument type. Currently only int, float, bool, and str types are supported by Profile Tools. Profile Tools will automatically generate ImageJ GUI and macro interface according to the function definition. The variable names in the ImageJ interface will be the names of the input arguments defined in the python function. The result plot will be displayed as an ImageJ plot.
</p>
<p style="margin-left:2%; margin-right:0%;text-indent:1%">  
Please check <i>ProfileWeightedSum.py</i> and <i>ProfileWeightedSumMacro.ijm</i> in the <i>examples/PythonScript</i> directory for examples.  
</p>
</body>
</html>

## Documentation
&nbsp;&nbsp; [Java Documentation](./javadoc/)

## Examples
&nbsp;&nbsp; [Example Directory](./examples)

## License: 
&nbsp;&nbsp; [MIT License](./LICENSE)


I welcome comments, suggestions, bug reports, etc.

Jian Zhong

