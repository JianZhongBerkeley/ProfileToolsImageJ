package com.github.JianZhongBerkeley.playground;

import jep.Interpreter;
import jep.SharedInterpreter;

public class JEPTest {

	public static void main() {
		Interpreter interp = new SharedInterpreter();
	    interp.exec("import numpy");
	    // any of the following work, these are just pseudo-examples

	    // using exec(String) to invoke methods
	    int inputX = 1;
	    int inputY = 1;
	    interp.set("x", inputX);
	    interp.set("y", inputY);
	    interp.exec("result = x + y");
	    long result1 = (long) interp.getValue("result");

	    // using getValue(String) to invoke methods
	    //Object result2 = interp.getValue("somePyModule.foo2()");

	    // using invoke to invoke methods
	    //interp.exec("foo3 = somePyModule.foo3")
	    //Object result3 = interp.invoke("foo3", obj);
	    System.out.println(result1);
	}
	
}
