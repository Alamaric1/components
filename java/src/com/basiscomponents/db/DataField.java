package com.basiscomponents.db;

import java.math.BigDecimal;

import java.net.URL;

import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.google.gson.annotations.Expose;

/**
 * The DataField class is an object container class which provides multiple cast methods 
 * to retrieve the initially stored object in different formats / types.
 */
public class DataField implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Expose
	private Object Value;

	private HashMap<String, String> Attributes = new HashMap<String, String>();

	/**
	 * Creates the DatField object with the given object as the DataField's value
	 * 
	 * @param value The value object of the DataField to be created 
	 */
	public DataField(Object value) {
		setValue(value);
	}

	/**
	 * Return's the DataField's value.
	 * 
	 * @return value the DataField's value
	 */
	public Object getValue() {
		return this.Value;
	}

	/**
	 * Returns <code>true</code> in case the given DataField object equals the current DataField object, <code>false</code> otherwise.
	 * The method does only compare the value object of both DataField objects, not the attributes.
	 * 
	 * @param dataField The DataField object to compare with.
	 * @return equal The boolean value indicating whether the given DataField object equals this DataField object.
	 */
	public Boolean equals(DataField dataField) {
		return this.getString().equals(dataField.getString());
	}
	
	/**
	 * Returns <code>true</code> in case the given DataField object equals the current DataField object, <code>false</code> otherwise.
	 * The method does only compare the value object of both DataField objects, not the attributes.
	 * 
	 * @param dataField The DataField object to compare with.
	 * @return equal The boolean value indicating whether the given DataField object equals this DataField object.
	 */
	public Boolean equals(String pattern) {
		
		Boolean isStringPar = false;
		if (pattern.startsWith("'") && pattern.endsWith("'"))
		{
			pattern=pattern.substring(1, pattern.length()-1);
			isStringPar=true;
		}

		String cn = getClassName();
		switch (cn){
		case "java.lang.Integer":
				return getInt().equals(Integer.parseInt(pattern));
				
		case "java.lang.String":
				return getString().trim().equals(pattern);
				
		default:
			System.err.println("unimplemented: field type "+cn);
			return false;
		}		
		
	}	

	/**
	 * Sets the given object as the DataField's value
	 * 
	 * @param value The object to set as the DataField's value
	 */
	public void setValue(Object value) {
		this.Value = value;
	}

	/**
	 * Returns the class name of the DataField's value object.
	 * 
	 * @return className The DataField value's class name.
	 */
	public String getClassName() {
		return this.Value.getClass().getCanonicalName();
	}

	/**
	 * Return's the DataField's value.
	 * 
	 * @return value The DataField's value.
	 */
	public Object getObject() {
		return this.Value;
	}

	/**
	 * Returns the DataField's value as <code>java.lang.String</code> object.
	 * 
	 * @return value The DataField's value as <code>java.lang.String</code> object.
	 */
	public String getString() {
		if (this.Value != null && getClassName() == "java.lang.Boolean") {
			// make this work the same as STR(Boolean.TRUE) in BBj
			// for compatibility reasons.
			// If it's a problem, we might introduce a COMPAT flag later.
			return (boolean) this.Value ? "1" : "0";
		}
		try {
			return this.Value.toString();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Returns the DataField's value as <code>java.lang.Integer</code> object.
	 * 
	 * @return value The DataField's value as <code>java.lang.Integer</code> object.
	 */
	public Integer getInt() {
		return (Integer) this.Value;
	}

	/**
	 * Returns the DataField's value as <code>java.lang.Byte</code> object.
	 * 
	 * @return value The DataField's value as <code>java.lang.Byte</code> object.
	 */
	public Byte getByte() {
		return (Byte) this.Value;
	}

	/**
	 * Returns the DataField's value as <code>java.lang.Short</code> object.
	 * 
	 * @return value The DataField's value as <code>java.lang.Short</code> object.
	 */
	public Short getShort() {
		return (Short) this.Value;
	}

	/**
	 * Returns the DataField's value as <code>java.lang.Long</code> object.
	 * 
	 * @return value The DataField's value as <code>java.lang.Long</code> object.
	 */
	public Long getLong() {
		return (Long) this.Value;
	}

	/**
	 * Returns the DataField's value as <code>java.math.BigDecimal</code> object.
	 * 
	 * @return value The DataField's value as <code>java.math.BigDecimal</code> object.
	 */
	public BigDecimal getBigDecimal() {
		return (BigDecimal) this.Value;
	}

	/**
	 * Returns the DataField's value as <code>java.lang.Double</code> object.
	 * 
	 * @return value The DataField's value as <code>java.lang.Double</code> object.
	 */
	public Double getDouble() {
		return (Double) this.Value;
	}

	/**
	 * Returns the DataField's value as <code>java.lang.Float</code> object.
	 * 
	 * @return value The DataField's value as <code>java.lang.Float</code> object.
	 */
	public Float getFloat() {
		return (Float) this.Value;
	}

	/**
	 * Returns the DataField's value as <code>java.sql.Date</code> object.
	 * 
	 * @return value The DataField's value as <code>java.sql.Date</code> object.
	 */
	public Date getDate() {
		
		if (this.Value != null){
			if (getClassName() == "java.sql.Timestamp") {
				long ms = ((java.sql.Timestamp) this.Value).getTime();
				return new java.sql.Date(ms);
			}
			if (getClassName() == "java.lang.Integer") {
				
				java.util.Date d = (java.util.Date) com.basis.util.BasisDate.date((Integer)this.Value);
				return new java.sql.Date(d.getTime());
			}
			if (getClassName() == "java.lang.Double") {
				
				java.util.Date d = (java.util.Date) com.basis.util.BasisDate.date(((Double)this.Value).intValue());
				return new java.sql.Date(d.getTime());
			}
			
		}
		return (Date) this.Value;
	}

	/**
	 * Returns the DataField's value as <code>java.sql.Time</code> object.
	 * 
	 * @return value The DataField's value as <code>java.sql.Time</code> object.
	 */
	public Time getTime() {
		return (Time) this.Value;
	}

	/**
	 * Returns the DataField's value as <code>java.sql.Timestamp</code> object.
	 * 
	 * @return value The DataField's value as <code>java.sql.Timestamp</code> object.
	 */
	public Timestamp getTimestamp() {
		if (this.Value != null && getClassName() == "java.sql.Date") {
			long ms = ((java.sql.Date) this.Value).getTime();
			return new java.sql.Timestamp(ms);
		}
		return (Timestamp) this.Value;
	}

	/**
	 * Returns the DataField's value as byte array.
	 * 
	 * @return value The DataField's value as byte array.
	 */
	public byte[] getBytes() {
		return (byte[]) this.Value;
	}

	/**
	 * Returns the DataField's value as <code>java.sql.Array</code> object.
	 * 
	 * @return value The DataField's value as <code>java.sql.Array</code> object.
	 */
	public Array getArray() {
		return (Array) this.Value;
	}

	/**
	 * Returns the DataField's value as <code>java.sql.Blob</code> object.
	 * 
	 * @return value The DataField's value as <code>java.sql.Blob</code> object.
	 */
	public Blob getBlob() {
		return (Blob) this.Value;
	}

	/**
	 * Returns the DataField's value as <code>java.sql.Clob</code> object.
	 * 
	 * @return value The DataField's value as <code>java.sql.Clob</code> object.
	 */
	public Clob getClob() {
		return (Clob) this.Value;
	}

	/**
	 * Returns the DataField's value as <code>java.sql.NClob</code> object.
	 * 
	 * @return value The DataField's value as <code>java.sql.NClob</code> object.
	 */
	public NClob getNClob() {
		return (NClob) this.Value;
	}

	/**
	 * Returns the DataField's value as <code>java.lang.Boolean</code> object.
	 * 
	 * @return value The DataField's value as <code>java.lang.Boolean</code> object.
	 */
	public Boolean getBoolean() {
		
		if (this.Value!=null){
			if (this.Value.getClass().equals(java.lang.String.class)){
				return ("trueTRUE1".indexOf((String)this.Value) > 0);
			};
			if (this.Value.getClass().equals(java.lang.Integer.class)){
				return ((Integer)this.Value > 0);
			};	
			if (this.Value.getClass().equals(java.lang.Double.class)){
				return ((Double)this.Value > 0);
			};
		}
		return (Boolean) this.Value;
	}

	/**
	 * Returns the DataField's value as <code>java.sql.Ref</code> object.
	 * 
	 * @return value The DataField's value as <code>java.sql.Ref</code> object.
	 */
	public Ref getRef() {
		return (Ref) this.Value;
	}

	/**
	 * Returns the DataField's value as <code>java.net.URL</code> object.
	 * 
	 * @return value The DataField's value as <code>java.net.URL</code> object.
	 */
	public URL getURL() {
		return (URL) this.Value;
	}

	/**
	 * Returns the DataField's value as <code>java.sql.SQLXML</code> object.
	 * 
	 * @return value The DataField's value as <code>java.sql.SQLXML</code> object.
	 */
	public SQLXML getSQLXML() {
		return (SQLXML) this.Value;
	}

	/**
	 * Sets the value of the attribute with the given name.
	 * Creates the attribute if it doesn't exist already.
	 * 
	 * @param attributeName The attribute's name.
	 * @param attributeValue The attribute's value.
	 */
	public void setAttribute(String attributeName, String attributeValue) {
		this.Attributes.put(attributeName, attributeValue);
	}

	/**
	 * Returns the value for the given attribute name.
	 * <code>null</code> is returned if no attribute matches the given name.
	 * 
	 * @param attributeName The attribute's name.
	 * @return attributeValue The attribute's value.
	 */
	public String getAttribute(String attributeName) {
		return this.Attributes.get(attributeName);
	}

	/**
	 * Returns a <code>java.util.HashMap</code> object with the DataField's 
	 * attributes and their corresponding value.
	 * 
	 * @return attributesMap The <code>java.util.HashMap</code> object containing the DataField's attributes and their values.
	 */
	public HashMap<String, String> getAttributes() {
		return new HashMap<String, String>(this.Attributes);
	}

	/**
	 * Sets the attributes for this DataField.
	 * 
	 * @param attributes The attribute's <code>java.util.HashMap</code> object with the attributes and their corresponding values.
	 */
	public void setAttributes(HashMap<String, String> attributes) {
		this.Attributes = attributes;
	}

	/**
	 * Removes the attribute with the given name. 
	 * Nothing happens in case the name doesn't exist.
	 * 
	 * @param attributeName The name of the attribute to remove.
	 */
	public void removeAttribute(String attributeName) {
		this.Attributes.remove(attributeName);
	}

	@Override
	public DataField clone() {
		DataField f = null;
		try {
			f = new DataField(this.Value);
		} catch (Exception e) {
			// Auto-generated catch block
			e.printStackTrace();
		}

		if (f != null) {
			Set<String> ks = this.Attributes.keySet();
			Iterator<String> it = ks.iterator();
			while (it.hasNext()) {
				String k = it.next();
				String v = this.Attributes.get(k);
				f.setAttribute(k, v);
			}
		}
		return f;
	}

	@Override
	public String toString() {
		if (this.Value == null)
			return null;
		return this.Value.toString();
	}

	/**
	 * Sets the DataField's value to <code>null</code>
	 */
	public void clear() {
		setValue(null);
	}

}
