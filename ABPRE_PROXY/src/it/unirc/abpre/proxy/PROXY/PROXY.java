package it.unirc.abpre.proxy.PROXY;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.SerializationUtils;

import it.unirc.abpre.structures.Attribute;
import it.unirc.abpre.structures.ChiperText;
import it.unirc.abpre.structures.ReChiperText;
import it.unirc.abpre.structures.ReEncryptionKey;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class PROXY {
	public  byte[][] ReEncrypt(byte[][]rkey,byte[][]chipertext){

		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		Pairing pairing =PairingFactory.getPairing("src/Params/a_128_params");
		HashMap<Integer,String>list=readAttributesUniverse();
		
		ChiperText ct=new ChiperText();
		ct.setFromBytes(chipertext[1]);
		ReEncryptionKey rk=new ReEncryptionKey();
		rk.setFromBytes(rkey[2]);
		
		Attribute[] attributes=(Attribute[])SerializationUtils.deserialize(rkey[0]);
		Attribute[] policyVecchia=(Attribute[])SerializationUtils.deserialize(chipertext[0]);

		if(!satisfy(attributes,policyVecchia)) return null;
	
		Field<?> GT = pairing.getGT();
		Element Ei[]= new Element[list.size()];
		Element E=GT.newOneElement();
		
		for(int i=0;i<list.size();i++) {
			Attribute a=contains(list.get(i+1),policyVecchia);
			if(a!=null) {
				Ei[i]=pairing.pairing(ct.getCi()[i], rk.getDi1()[i]);
			}
			else {
				Ei[i]=pairing.pairing(ct.getCi()[i], rk.getDi2()[i]);
			}
			E=E.mul(Ei[i]);
		}
		
		Element C2=pairing.pairing(ct.getC2(), rk.getD()).mul(E);

		ReChiperText rct=new ReChiperText(ct.getC1(),C2,ct.getC3());

		byte[][]result = new byte[3][];
		result[0]=rkey[1];
		result[1]=rct.toBytes();
		result[2]=rkey[3];
		

		return result;
	}

	
	private  Attribute contains(String attribute, Attribute[] attributes) {

		for(int i=0;i<attributes.length;i++) {


			if(attributes[i].getValue().equals(attribute)) return attributes[i];
		}
		return null;
	}
	private  boolean satisfy(Attribute[]attributes, Attribute[]policy) {

		for(int i=0;i<policy.length;i++) {
			boolean found=false;
			for(int j=0;j<attributes.length;j++) {
				if(policy[i].getValue().equals(attributes[j].getValue())) {
					found=true;
				}
			}

			if(found && !policy[i].isPositive()) {return false;}
			if(!found && policy[i].isPositive()) {return false;}

		}
		return true;
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



	public void saveReEncryptedFile(byte[][] encryptedFile, String path) {
		try {
			File f=new File(path);
			if(!f.exists()) {f.createNewFile();}
			PrintWriter out = new PrintWriter(path);
			out.println(Hex.encodeHexString(encryptedFile[0]));
			out.println(Hex.encodeHexString(encryptedFile[1]));
			out.println(Hex.encodeHexString(encryptedFile[2]));
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public byte[][] readReKey(String path) {

		FileReader f;
		BufferedReader b;
		byte[][] result=new byte[4][];
		try {
			f=new FileReader(path);			
			b=new BufferedReader(f);
			String s1=b.readLine();

			String s2=b.readLine();
			String s3=b.readLine();
			String s4=b.readLine();

			result[0]=(Hex.decodeHex(s1));
			result[1]=(Hex.decodeHex(s2));
			result[2]=(Hex.decodeHex(s3));
			result[3]=(Hex.decodeHex(s4));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
