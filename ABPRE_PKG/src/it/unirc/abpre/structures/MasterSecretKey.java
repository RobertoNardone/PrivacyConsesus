package it.unirc.abpre.structures;


import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;



public class MasterSecretKey{
	public static String pathPairing="Params\\a_128_params";
	private static Pairing e;
	private Element y;
	private Element[] ti;

	public MasterSecretKey(Element y, Element []ti) {

		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		e =PairingFactory.getPairing(pathPairing);
        this.y=y;
        this.ti=ti;
	}

	public MasterSecretKey() {
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		e =PairingFactory.getPairing(pathPairing);
	}

	public Element getY() {
		return y;
	}
	public void setY(Element y) {
		this.y = y;
	}
	public Element[] getTi() {
		return ti;
	}
	public void setTi(Element[] ti) {
		this.ti = ti;
	}
	public byte[] toBytes() {
		byte[]res=null;
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
			outputStream.write( y.toBytes() );

			for(int i=0;i<ti.length;i++) {
				outputStream.write( ti[i].toBytes() );
				
			}



			res = outputStream.toByteArray( );}
		catch(Exception e) {}
		
		return res;	
	}
	public void setFromBytes(byte[]msk) {

		Field<?> Zr=e.getZr();

		y=Zr.newElement();
		y.setFromBytes(Arrays.copyOfRange(msk,0,32));

		ti=new Element[(msk.length-32)/32];
		
		
		int index=0;
		for(int i=32;i<msk.length;i=i+32) {
			index=(i-32)/32; 
			
			ti[index]=Zr.newElement();
			ti[index].setFromBytes(Arrays.copyOfRange(msk,i,i+32));

		}

	}
}
