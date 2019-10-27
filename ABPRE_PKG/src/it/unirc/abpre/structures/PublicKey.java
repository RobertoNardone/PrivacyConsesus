package it.unirc.abpre.structures;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class PublicKey {
	public static String pathPairing="Params\\a_128_params";
	private static Pairing e;
	private Element g;
	private Element h;
	private Element Y;
	private Element[] Ti;
	private Element[] Ti1;

	public PublicKey() {
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		e =PairingFactory.getPairing(pathPairing);
	}



	public PublicKey(Element g, Element h, Element Y, Element[] Ti,Element Ti1[]) {

		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		e =PairingFactory.getPairing(pathPairing);
        this.g=g;
        this.h=h;
        this.Y=Y;
        this.Ti=Ti;
        this.Ti1=Ti1;
	}

	
	
	public Element getG() {
		return g;
	}
	public void setG(Element g) {
		this.g = g;
	}
	public Element getH() {
		return h;
	}
	public void setH(Element h) {
		this.h = h;
	}
	public Element getY() {
		return Y;
	}
	public void setY(Element y) {
		Y = y;
	}
	public Element[] getTi() {
		return Ti;
	}
	public void setTi(Element[] ti) {
		Ti = ti;
	}
	public Element[] getTi1() {
		return Ti1;
	}
	public void setTi1(Element[] ti1) {
		Ti1 = ti1;
	}
	public byte[] toBytes() {
		byte[]res=null;
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

			outputStream.write( g.toBytes() );
			outputStream.write( h.toBytes() );
			outputStream.write( Y.toBytes() );
			for(int i=0;i<Ti.length;i++) {
				outputStream.write( Ti[i].toBytes() );
				outputStream.write( Ti1[i].toBytes() );
			}
		

			res = outputStream.toByteArray( );}
		catch(Exception e) {}
		return res;	
	}

	public void setFromBytes(byte[]pk) {

		Field<?> G=e.getG1();
		Field<?> GT=e.getGT();
	

		g=G.newElement();
		g.setFromBytes(Arrays.copyOfRange(pk,0,384));
		
		h=G.newElement();
		h.setFromBytes(Arrays.copyOfRange(pk,384,768));
		
		Y=GT.newElement();
		Y.setFromBytes(Arrays.copyOfRange(pk,768,1152));
		
		Ti=new Element[(pk.length-1152)/768];
		Ti1=new Element[(pk.length-1152)/768];
		int index=0;
		for(int i=1152;i<pk.length;i=i+768) {
			index=(i-1152)/768; 
			Ti[index]=G.newElement();
			Ti[index].setFromBytes(Arrays.copyOfRange(pk,i,i+384));
			Ti1[index]=G.newElement();
			Ti1[index].setFromBytes(Arrays.copyOfRange(pk,i+384,i+768));
		}
		
	}

}
