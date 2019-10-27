package it.unirc.abpre.structures;

import java.io.Serializable;

public class Attribute implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String value;
	private boolean positive;
	public Attribute(String value, boolean positive){
		this.value=value;
		this.positive=positive;
	}
	public Attribute(String value){
		this.value=value;
		this.positive=true;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isPositive() {
		return positive;
	}
	public void setPositive(boolean positive) {
		this.positive = positive;
	}

}
