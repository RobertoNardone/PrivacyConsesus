package it.unirc.abpre.user.USER;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.SerializationUtils;

import it.unirc.abpre.structures.Attribute;
import it.unirc.abpre.structures.ChiperText;
import it.unirc.abpre.structures.DecryptionKey;
import it.unirc.abpre.structures.PublicKey;
import it.unirc.abpre.structures.ReChiperText;
import it.unirc.abpre.structures.ReEncryptionKey;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class USER {

	public byte[][] encrypt(byte[] M, Attribute[] policy, byte[] publicKey) {


		HashMap<Integer,String>list=readAttributesUniverse();
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		Pairing pairing =PairingFactory.getPairing("src/Params/a_128_params");

		PublicKey pk=new PublicKey();
		pk.setFromBytes(publicKey);

		Field<?> G = pairing.getG1();
		Field<?> Zp = pairing.getZr();
		Field<?> GT = pairing.getGT();



		Element message=GT.newElementFromBytes(M);

		Element s=Zp.newRandomElement();

		Element C1=message.mul(pk.getY().powZn(s));
		Element C2=pk.getG().powZn(s);
		Element C3=pk.getH().powZn(s);


		Element Ci[]=new Element[list.size()];
		for(int i=0;i<list.size();i++) {
			Ci[i]=G.newElement();
			Attribute a=contains(list.get(i+1),policy);
			if(a!=null) {
				if(a.isPositive()) {
					Ci[i]=pk.getTi()[i].powZn(s);
				}
				if(!a.isPositive()) {
					Ci[i]=pk.getTi()[list.size()+i].powZn(s);
				}
			}

			else {Ci[i]=pk.getTi()[2*list.size()+i].powZn(s);}
		}
		ChiperText ct= new ChiperText(C1,C2,C3,Ci);


		byte[][]result = new byte[2][];
		result[0]=SerializationUtils.serialize(policy);
		result[1]=ct.toBytes();
		return result;
	}
	public Attribute[] readPolicyFromFile(String path) {
		BufferedReader reader;
		List<String> attributes=new ArrayList<String>();
		Attribute[] result=null;
		try {
			reader = new BufferedReader(new FileReader(
					path));
			String line = reader.readLine();
			while (line != null) {
				attributes.add(line);


				line = reader.readLine();
			}

			result=new Attribute[attributes.size()];
			for(int i=0;i<result.length;i++) {
				String att=attributes.get(i);
				if(att.contains("not")) {
					result[i]=new Attribute(att.replace("not ", ""),false);		
				}
				else {
					result[i]=new Attribute(att,true);		
				}


			}


			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
	private  HashMap<Integer, String> readAttributesUniverse(){
		BufferedReader reader;
		HashMap<Integer, String> result= new HashMap<Integer,String>();
		try {
			reader = new BufferedReader(new FileReader("src/Params/AttributesUniverse"));
			String line = reader.readLine();
			int index=-1;
			while (line != null) {
				index=line.indexOf("=");
				result.put(Integer.parseInt(line.substring(index+1)), line.substring(0, index));
				line = reader.readLine();	
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
	private static Attribute contains(String attribute, Attribute[] attributes) {

		for(int i=0;i<attributes.length;i++) {


			if(attributes[i].getValue().equals(attribute)) return attributes[i];
		}
		return null;
	}
	public void saveEncryptedFile(byte[][] encryptedFile, String path) {
		try {
			File f=new File(path);
			if(!f.exists()) {f.createNewFile();}
			PrintWriter out = new PrintWriter(path);
			out.println(Hex.encodeHexString(encryptedFile[0]));
			out.println(Hex.encodeHexString(encryptedFile[1]));
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public byte[] readPK(String path) {
		FileReader f;
		BufferedReader b;
		try {
			f=new FileReader(path);			
			b=new BufferedReader(f);
			String pkS=b.readLine();
			f.close();
			return Hex.decodeHex(pkS);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public  byte[] decrypt(byte[][] chipertext, byte[][] deck) {


		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		Pairing pairing =PairingFactory.getPairing("src/Params/a_128_params");
		HashMap<Integer,String>list=readAttributesUniverse();

		ChiperText ct=new ChiperText();
		ct.setFromBytes(chipertext[1]);

		DecryptionKey dk=new DecryptionKey();
		dk.setFromBytes(deck[1]);

		Field<?> GT = pairing.getGT();

		Element[]Ei= new Element[list.size()];
		Element E=GT.newOneElement();

		Attribute[] policy= (Attribute[])SerializationUtils.deserialize(chipertext[0]);
		for(int i=0;i<list.size();i++) {
			Ei[i]=GT.newElement();
			Attribute a= contains(list.get(i+1),policy);
			if(a!=null) {
				Ei[i]=pairing.pairing(ct.getCi()[i], dk.getDi1()[i]);
			}
			else {	Ei[i]=pairing.pairing(ct.getCi()[i], dk.getDi2()[i]);
			}
			E=E.mul(Ei[i]);
		}

		Element m=ct.getC1().div(pairing.pairing(ct.getC2(), dk.getD()).mul(E));
		byte[]mex=m.toBytes();



		return trim(mex);
	}

	public byte[][] readEncryptedFile(String path) {

		FileReader f;
		BufferedReader b;
		byte[][] result=new byte[2][];
		try {
			f=new FileReader(path);			
			b=new BufferedReader(f);
			String s1=b.readLine();

			String s2=b.readLine();

			result[0]=(Hex.decodeHex(s1));
			result[1]=(Hex.decodeHex(s2));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public byte[][] readDkey(String path) {

		FileReader f;
		BufferedReader b;
		byte[][] result=new byte[2][];
		try {
			f=new FileReader(path);			
			b=new BufferedReader(f);
			String attributes=b.readLine();

			String data=b.readLine();

			result[0]=(Hex.decodeHex(attributes));
			result[1]=(Hex.decodeHex(data));
			b.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private byte[] trim(byte[] bytes)
	{

		int i=0;
		while (bytes[i] == 0)
		{
			i++;
		}
		bytes=Arrays.copyOfRange(bytes, i, bytes.length);
		return bytes;



	}

	public byte[][] RKGen(byte[][]deck,Attribute[]policy, byte[] publicKey) {
		HashMap<Integer,String>list=readAttributesUniverse();
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		Pairing pairing =PairingFactory.getPairing("src/Params/a_128_params");

		PublicKey pk=new PublicKey();
		pk.setFromBytes(publicKey);
		DecryptionKey dk=new DecryptionKey();
		dk.setFromBytes(deck[1]);


		Field<?> G = pairing.getG1();

		Field<?> Zp = pairing.getZr();

		Element d=Zp.newRandomElement();
		Element Di1[]=new Element[list.size()];
		Element Di2[]=new Element[list.size()];
		System.out.println();
		Attribute[] attributes=(Attribute[])SerializationUtils.deserialize(deck[0]);


		for(int i=0;i<list.size();i++) {
			Di1[i]=G.newElement();
			Di2[i]=G.newElement();
			Attribute a=contains(list.get(i+1),attributes);
			if(a!=null) {
				Di1[i]=dk.getDi1()[i].mul(pk.getTi1()[i].powZn(d));
			}
			else {
				Di1[i]=dk.getDi1()[i].mul(pk.getTi1()[list.size()+i].powZn(d));
			}
			Di2[i]=dk.getDi2()[i].mul(pk.getTi1()[2*list.size()+i].powZn(d));
		}
		Element DD=pk.getG().powZn(d);

		Element D=dk.getD();
		ReEncryptionKey rk = new ReEncryptionKey(D,Di1,Di2);

		byte[][]result = new byte[4][];
		result[0]=deck[0];
		result[1]=SerializationUtils.serialize(policy);
		result[2]=rk.toBytes();
		result[3]=encrypt(DD.toBytes(),policy,publicKey)[1];
		return result;
	}
	
	
	public byte[] decryptRC(byte[][] chipertext, byte[][] deck) {

		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		Pairing pairing =PairingFactory.getPairing("src/Params/a_128_params");
		HashMap<Integer,String>list=readAttributesUniverse();
		
		byte[][] ct=new byte[2][];
		ct[0]=chipertext[0];
		ct[1]=chipertext[2];

		ReChiperText rct= new ReChiperText();
		rct.setFromBytes(chipertext[1]);

		byte[] gd=decrypt(ct,deck);
		Element gdok=pairing.getG1().newElementFromBytes(gd);
		Element m=rct.getC1().mul(pairing.pairing(gdok,rct.getC3()).pow(BigInteger.valueOf(list.size()))).div(rct.getC2());
        byte[]mex=m.toBytes();
		return trim(mex);
	}

	public void saveReKey(byte[][] reKey, String path) {
		try {
			File f=new File(path);
			if(!f.exists()) {f.createNewFile();}
			PrintWriter out = new PrintWriter(path);
			out.println(Hex.encodeHexString(reKey[0]));
			out.println(Hex.encodeHexString(reKey[1]));
			out.println(Hex.encodeHexString(reKey[2]));
			out.println(Hex.encodeHexString(reKey[3]));
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public byte[][] readRE_EncryptedFile(String path) {

		FileReader f;
		BufferedReader b;
		byte[][] result=new byte[3][];
		try {
			f=new FileReader(path);			
			b=new BufferedReader(f);
			String s1=b.readLine();

			String s2=b.readLine();
			String s3=b.readLine();

			result[0]=(Hex.decodeHex(s1));
			result[1]=(Hex.decodeHex(s2));
			result[2]=(Hex.decodeHex(s3));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}


}
