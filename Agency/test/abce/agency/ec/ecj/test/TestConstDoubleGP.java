package abce.agency.ec.ecj.test;

import static org.junit.Assert.*;

import org.junit.Test;

import abce.agency.ec.ecj.terminals.Const100DoubleGP;
import abce.agency.ec.ecj.terminals.Const10DoubleGP;
import abce.agency.ec.ecj.terminals.Const1DoubleGP;
import abce.agency.ec.ecj.terminals.Const20DoubleGP;
import abce.agency.ec.ecj.terminals.Const2DoubleGP;
import abce.agency.ec.ecj.terminals.Const50DoubleGP;
import abce.agency.ec.ecj.terminals.Const5DoubleGP;
import abce.agency.ec.ecj.types.DoubleGP;

public class TestConstDoubleGP {

	@Test
	public void test() {
		Const1DoubleGP t_1 = new Const1DoubleGP();
		Const2DoubleGP t_2 = new Const2DoubleGP();
		Const5DoubleGP t_5 = new Const5DoubleGP();
		Const10DoubleGP t_10 = new Const10DoubleGP();
		Const20DoubleGP t_20 = new Const20DoubleGP();
		Const50DoubleGP t_50 = new Const50DoubleGP();
		Const100DoubleGP t_100 = new Const100DoubleGP();
		
		DoubleGP input = new DoubleGP();
		
		t_1.eval(null, 0, input, null, null, null);
		assertEquals(input.value, 1.0, 0.0);
		
		t_2.eval(null, 0, input, null, null, null);
		assertEquals(input.value, 2.0, 0.0);
		
		t_5.eval(null, 0, input, null, null, null);
		assertEquals(input.value, 5.0, 0.0);
		
		t_10.eval(null, 0, input, null, null, null);
		assertEquals(input.value, 10.0, 0.0);
		
		t_20.eval(null, 0, input, null, null, null);
		assertEquals(input.value, 20.0, 0.0);
		
		t_50.eval(null, 0, input, null, null, null);
		assertEquals(input.value, 50.0, 0.0);
		
		t_100.eval(null, 0, input, null, null, null);
		assertEquals(input.value, 100.0, 0.0);
		
		
	}

}
