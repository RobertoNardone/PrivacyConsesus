package it.unirc.abpre.structures;

import java.io.ByteArrayOutputStream;


import java.util.Arrays;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;



public class ReChiperText {
	public static String pathPairing="Params\\a_128_params";
	private static Pairing e;
	private Element C1;
	private Element C2;
	private Element C3;


public ReChiperText() {
	PairingFactory.getInstance().setUsePBCWhenPossible(true);
	e =PairingFactory.getPairing(pathPairing);
}
	
	public ReChiperText(Element c1, Element c2, Element c3) {
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		e =PairingFactory.getPairing(pathPairing);
		C1 = c1;
		C2 = c2;
		C3 = c3;
		
	}
	public Element getC1() {
		return C1;
	}
	public void setC1(Element c1) {
		C1 = c1;
	}
	public Element getC2() {
		return C2;
	}
	public void setC2(Element c2) {
		C2 = c2;
	}
	public Element getC3() {
		return C3;
	}
	public void setC3(Element c3) {
		C3 = c3;
	}


	public byte[] toBytes() {
		byte[]res=null;
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

			outputStream.write( C1.toBytes() );
			outputStream.write( C2.toBytes() );
			outputStream.write( C3.toBytes() );
	

			res = outputStream.toByteArray( );}
		catch(Exception e) {}
		return res;	
	}

	public void setFromBytes(byte[]ct) {

		Field<?> G=e.getG1();
		Field<?> GT=e.getGT();
		


		C1=GT.newElement();
		C1.setFromBytes(Arrays.copyOfRange(ct,0,384));

		C2=GT.newElement();
		C2.setFromBytes(Arrays.copyOfRange(ct,384,768));

		C3=G.newElement();
		C3.setFromBytes(Arrays.copyOfRange(ct,768,1152));

	

	}
	
}
