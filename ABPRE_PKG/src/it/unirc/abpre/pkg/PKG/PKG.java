package it.unirc.abpre.pkg.PKG;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SerializationUtils;

import it.unirc.abpre.structures.Attribute;
import it.unirc.abpre.structures.DecryptionKey;
import it.unirc.abpre.structures.MasterSecretKey;
import it.unirc.abpre.structures.PublicKey;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class PKG {
	PublicKey pk;
	MasterSecretKey msk;




	public PublicKey getPk() {
		return pk;
	}

	public MasterSecretKey getMsk() {
		return msk;
	}

	public void Setup() {

		HashMap<Integer,String>list=readAttributesUniverse();
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		Pairing pairing =PairingFactory.getPairing("src/Params/a_128_params");

		Field<?> G = pairing.getG1();
		Field<?> GT= pairing.getGT();
		Field<?> Zp = pairing.getZr();

		Element g=G.newRandomElement();
		Element h=G.newRandomElement();
		Element y=Zp.newRandomElement();

		ElementPowPreProcessing gpp =g.getElementPowPreProcessing();
		ElementPowPreProcessing hpp =h.getElementPowPreProcessing();
		Element Y=GT.newElement();

		Y=pairing.pairing(g, h).powZn(y);

		Element tiList[]=new Element[list.size()*3];
		Element TiList[]=new Element[list.size()*3];
		Element Ti1List[]=new Element[list.size()*3];

		for(int i=0;i<list.size()*3;i++) {
			Element ti=Zp.newRandomElement();
			tiList[i]=ti;
			TiList[i]=gpp.powZn(ti);
			Ti1List[i]=hpp.powZn(ti.duplicate().invert());
		}

		msk=new MasterSecretKey(y,tiList);
		pk=new PublicKey(g,h,Y,TiList,Ti1List);

	}	

	public void readKeys(String path) {


		FileReader f;
		BufferedReader b;
		try {
			f=new FileReader(path+"/pk.txt");			
			b=new BufferedReader(f);
			String pkS=b.readLine();
			f=new FileReader(path+"/msk.txt");
			b=new BufferedReader(f);
			String mskS=b.readLine();
			pk=new PublicKey();
			pk.setFromBytes(Hex.decodeHex(pkS));
			msk=new MasterSecretKey();
			msk.setFromBytes(Hex.decodeHex(mskS));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}






	}


	public void saveKeys(String path) {

		String pkS= Hex.encodeHexString(pk.toBytes());
		String mskS=Hex.encodeHexString(msk.toBytes());

		try {

			FileUtils.writeStringToFile(new File(path+"/pk.txt"), pkS);
			FileUtils.writeStringToFile(new File(path+"/msk.txt"), mskS);


		} catch (Exception e) {

			e.printStackTrace();
		}


	}


	public void saveDkey(byte[][] dkey, String path) {
		try {
			File f=new File(path);
			if(!f.exists()) {f.createNewFile();}
			PrintWriter out = new PrintWriter(path);
			out.println(Hex.encodeHexString(dkey[0]));
			
			out.println(Hex.encodeHexString(dkey[1]));
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		
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

		} catch (Exception e) {
			// TODO Auto-generated catch block
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

	public byte[][] KeyGen(Attribute[]attributes, String pathKeys) {
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		Pairing pairing =PairingFactory.getPairing("src/Params/a_128_params");
		HashMap<Integer,String>list=readAttributesUniverse();
		readKeys(pathKeys);

		Field<?> G = pairing.getG1();
		Field<?> Zp = pairing.getZr();

		Element ri[]=new Element[list.size()];
		Element r=Zp.newZeroElement();
		Element Di1[]=new Element[list.size()];
		Element Di2[]=new Element[list.size()];

		for(int i=0;i<list.size();i++) {
			ri[i]=Zp.newRandomElement();
			Di1[i]=G.newElement();
			Di2[i]=G.newElement();
			Attribute a=contains(list.get(i+1),attributes);
			if(a!=null) {
				Di1[i]=pk.getTi1()[i].powZn(ri[i]);
			}
			else {
				Di1[i]=pk.getTi1()[list.size()+i].powZn(ri[i]);
			}
			Di2[i]=pk.getTi1()[2*list.size()+i].powZn(ri[i]);
			r=r.add(ri[i]);
		}

		ElementPowPreProcessing h=pk.getH().getElementPowPreProcessing();
		Element D=h.powZn(msk.getY().duplicate().sub(r));

		DecryptionKey dk= new DecryptionKey(D,Di1,Di2);

		byte[][]result = new byte[2][];
		result[0]=SerializationUtils.serialize(attributes);
		result[1]=dk.toBytes();

		return result;
	}
	private  Attribute contains(String attribute, Attribute[] attributes) {

		for(int i=0;i<attributes.length;i++) {


			if(attributes[i].getValue().equals(attribute)) return attributes[i];
		}
		return null;
	}

	public Attribute[] readAttributesFromFile(String path) {
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

}
