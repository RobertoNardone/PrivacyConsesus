package it.unirc.abpre.structures;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class ReEncryptionKey {
	public static String pathPairing="Params\\a_128_params";
	private static Pairing e;
	Element D;
	Element[] Di1;
	Element[] Di2;
	
	
	
	public ReEncryptionKey() {
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		e =PairingFactory.getPairing(pathPairing);
	}
	public ReEncryptionKey(Element d, Element[] di1, Element[] di2) {
		
		D = d;
		Di1 = di1;
		Di2 = di2;
	}
	public Element getD() {
		return D;
	}
	public void setD(Element d) {
		D = d;
	}
	public Element[] getDi1() {
		return Di1;
	}
	public void setDi1(Element[] di1) {
		Di1 = di1;
	}
	public Element[] getDi2() {
		return Di2;
	}
	public void setDi2(Element[] di2) {
		Di2 = di2;
	}
	
	
	public byte[] toBytes() {
		byte[]res=null;
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

			outputStream.write( D.toBytes() );
			
			for(int i=0;i<Di1.length;i++) {
				outputStream.write( Di1[i].toBytes() );
				outputStream.write( Di2[i].toBytes() );
				}

			
			res = outputStream.toByteArray( );}
		catch(Exception e) {}
		return res;	
	}

	public void setFromBytes(byte[]dk) {

		Field<?> G=e.getG1();
		


		D=G.newElement();
		D.setFromBytes(Arrays.copyOfRange(dk,0,384));

		

		int index=0;
		Di1=new Element[(dk.length-384)/768];
		Di2=new Element[(dk.length-384)/768];
		for(int i=384;i<dk.length;i=i+768) {
			index=(i-384)/768; 
			Di1[index]=G.newElement();
			Di1[index].setFromBytes(Arrays.copyOfRange(dk,i,i+384));
			Di2[index]=G.newElement();
			Di2[index].setFromBytes(Arrays.copyOfRange(dk,i+384,i+768));
		}
		}

}
