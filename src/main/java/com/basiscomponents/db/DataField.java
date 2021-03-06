package com.basiscomponents.db;

import com.basis.startup.type.BBjNumber;
import com.basis.util.common.BasisNumber;
import com.basiscomponents.db.model.Attribute;
import com.basiscomponents.db.util.DataFieldConverter;
import com.google.gson.annotations.Expose;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The DataField class is an object container class which provides multiple cast
 * methods to retrieve the initially stored object in different formats / types.
 */
public class DataField implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Expose
	private Object Value;

	private Map<String, Attribute> attributes = new HashMap<>();
	private static final Logger LOGGER = Logger.getLogger(DataField.class.getName());
	/**
	 * Creates the DataField object with the given object as the DataField's value
	 * 
	 * @param value
	 *            The value object of the DataField to be created
	 */
	public DataField(Object value) {
		this.Value = value;
	}

	/**
	 * Returns the DataField's value.
	 * 
	 * @return value the DataField's value
	 */
	public Object getValue() {
		return this.Value;
	}

	/**
	 * Returns <code>true</code> in case the given DataField object equals the
	 * current DataField object, <code>false</code> otherwise. The method does only
	 * compare the value object of both DataField objects, not the attributes.
	 * 
	 * @param dataField
	 *            The DataField object to compare with.
	 * @return equal The boolean value indicating whether the given DataField object
	 *         equals this DataField object.
	 */
	public Boolean equals(DataField dataField) {
		return this.getString().equals(dataField.getString());
	}

	/**
	 * Returns <code>true</code> in case the given DataField object equals the
	 * current DataField object, <code>false</code> otherwise. The method does only
	 * compare the value object of both DataField objects, not the attributes.
	 * 
	 * @param pattern
	 *            The DataField object to compare with.
	 * @return equal The boolean value indicating whether the given DataField object
	 *         equals this DataField object.
	 */
	public Boolean equals(String pattern) {
		return equals(pattern, false, true);
	}

	/**
	 * Returns <code>true</code> in case the given DataField object equals the
	 * current DataField object, <code>false</code> otherwise. The method does only
	 * compare the value object of both DataField objects, not the attributes.
	 * 
	 * @param pattern
	 *            The DataField object to compare with.
	 * @return equal The boolean value indicating whether the given DataField object
	 *         equals this DataField object.
	 */
	public Boolean equals(String pattern, final boolean caseSensitive, final boolean trimmed) {

		if (pattern.startsWith("'") && pattern.endsWith("'")) {
			pattern = pattern.substring(1, pattern.length() - 1);
		}

		String cn = getClassName();
		switch (cn) {
		case "java.lang.Integer":
			return getInt().equals(Integer.parseInt(pattern));
		case "java.lang.String":
			return compareString(getString(), pattern, caseSensitive, trimmed);
		default:
			LOGGER.log(Level.WARNING, "unimplemented: field type " + cn);
			return false;
		}

	}

	private static boolean compareString(final String dataRowString, final String pattern, boolean caseSensitive,
			boolean trimmed) {
		String compareString = dataRowString;
		if (trimmed) {
			compareString = compareString.trim();
		}
		if (caseSensitive) {
			return compareString.equals(pattern);
		} else {
			return compareString.equalsIgnoreCase(pattern);
		}
	}

	/**
	 * Sets the given object as the DataField's value
	 * 
	 * @param value
	 *            The object to set as the DataField's value
	 */
	public void setValue(Object value) {
		this.attributes.remove("ETAG");
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
	 * Returns the DataField's value.
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

		try {
			return (String) convertType(this.Value, java.sql.Types.VARCHAR);
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
		if (this.Value instanceof Short)
			return ((Short) this.Value).intValue();
		if (this.Value instanceof Long)
			return ((Long) this.Value).intValue();
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
		if (this.Value instanceof Integer)
			return ((Integer) this.Value).shortValue();
		return (Short) this.Value;
	}

	/**
	 * Returns the DataField's value as <code>java.lang.Long</code> object.
	 * 
	 * @return value The DataField's value as <code>java.lang.Long</code> object.
	 */
	public Long getLong() {
		if (this.Value != null && (getClassName() == "java.lang.Integer")) {
			// make this work the same as STR(Boolean.TRUE) in BBj
			// for compatibility reasons.
			// If it's a problem, we might introduce a COMPAT flag later.
			return Integer.toUnsignedLong((Integer) this.Value);
		}
		return (Long) this.Value;
	}

	/**
	 * Returns the DataField's value as <code>java.math.BigDecimal</code> object.
	 * 
	 * @return value The DataField's value as <code>java.math.BigDecimal</code>
	 *         object.
	 */
	public BigDecimal getBigDecimal() {
		
		if (this.Value != null && (getClassName() == "java.lang.Double")) {
			// make this work the same as STR(Boolean.TRUE) in BBj
			// for compatibility reasons.
			// If it's a problem, we might introduce a COMPAT flag later.
			return new BigDecimal((Double) this.Value);
		}
		if (this.Value != null && (getClassName().contains("BasisNumber"))) {
			BasisNumber val = BasisNumber.getBasisNumber((BBjNumber) this.Value);
			return new BigDecimal(val.doubleValue());
		}
		
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
		if (this.Value != null && (getClassName() == "java.lang.Double" || getClassName().contains("BasisNumber") || getClassName().contains("BasisInt"))) {
			// make this work the same as STR(Boolean.TRUE) in BBj
			// for compatibility reasons.
			// If it's a problem, we might introduce a COMPAT flag later.
			return new Float((Double) this.Value);
		}

		return (Float) this.Value;
	}

	/**
	 * Returns the DataField's value as <code>java.sql.Date</code> object.
	 * 
	 * @return value The DataField's value as <code>java.sql.Date</code> object.
	 */
	public Date getDate() {
		if (this.Value != null) {
			if (getClassName() == "java.sql.Timestamp") {
				long ms = ((java.sql.Timestamp) this.Value).getTime();
				return new java.sql.Date(ms);
			}
			if (getClassName() == "java.lang.Integer") {
				java.util.Date d = com.basis.util.BasisDate.date((Integer) this.Value);
				if (d != null)
					return new java.sql.Date(d.getTime());
				else
					return null;
			}
			if (getClassName() == "java.lang.Double") {
				java.util.Date d = com.basis.util.BasisDate.date(((Double) this.Value).intValue());
				if (d != null)
					return new java.sql.Date(d.getTime());
				else
					return null;
			}
			if (getClassName() == "java.lang.String") {
				String s = (String) this.Value;
				if (s.isEmpty() || s.equals("-1"))
					return null;
			}
		}
		if (getClassName().equals("java.sql.Date"))
			return (Date)this.Value;
	
		return null;
	}

	/**
	 * Returns the DataField's value as <code>java.sql.Time</code> object.
	 * 
	 * @return value The DataField's value as <code>java.sql.Time</code> object.
	 */
	public Time getTime() {
		if (getClassName().equals("java.sql.Time"))
			return (Time)this.Value;

		return null;		
	}

	/**
	 * Returns the DataField's value as <code>java.sql.Timestamp</code> object.
	 * 
	 * @return value The DataField's value as <code>java.sql.Timestamp</code>
	 *         object.
	 */
	public Timestamp getTimestamp() {
		if (this.Value != null && getClassName() == "java.sql.Date") {
			long ms = ((java.sql.Date) this.Value).getTime();
			return new java.sql.Timestamp(ms);
		}
		if (this.Value != null && getClassName() == "java.lang.String") {
			String s = (String) this.Value;
			if (s.isEmpty())
				return null;
		}
		if (getClassName().equals("java.sql.Timestamp"))
			return (Timestamp)this.Value;

		return null;						
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


		if (this.Value != null) {
			if (this.Value.getClass().equals(java.lang.String.class)) {
				return ("trueTRUE1".indexOf((String) this.Value) >= 0);
			}

			if (this.Value.getClass().equals(java.lang.Integer.class)) {
				return ((Integer) this.Value > 0);
			}

			if (this.Value.getClass().equals(java.lang.Double.class)) {
				return ((Double) this.Value > 0);
			}
			
			if (this.Value != null && (getClassName().contains("BasisNumber"))) {
				BasisNumber val = BasisNumber.getBasisNumber((BBjNumber) this.Value);
				return (val.doubleValue() > 0);
			}
			

		}
	
		if (this.Value.getClass().equals(java.lang.Integer.class)) {
			return ((Integer) this.Value > 0);
		}
	
		if (this.Value.getClass().equals(java.lang.Double.class)) {
			return ((Double) this.Value > 0);
		}
		
		return (boolean) this.Value;

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

	public String getEtag() {
		if (this.attributes.containsKey("ETAG")) {
			return this.attributes.get("ETAG").getValue();
		}
		return createEtag();

	}
	/**
	 * Sets the value of the attribute with the given name. Creates the attribute if
	 * it doesn't exist.
	 * 
	 * @param attributeName
	 *            The attribute's name.
	 * @param attributeValue
	 *            The attribute's value.
	 */
	public void setAttribute(String attributeName, String attributeValue) {
		this.attributes.put(attributeName, Attribute.createString(attributeValue));
	}

	public void setAttribute(String attributeName, Attribute attributeValue) {
		this.attributes.put(attributeName, attributeValue);
	}

	/**
	 * Returns the value for the given attribute name. <code>null</code> is returned
	 * if no attribute matches the given name.
	 * 
	 * @param attributeName
	 *            The attribute's name.
	 * @return attributeValue The attribute's value.
	 */
	public String getAttribute(String attributeName) {
		Attribute at = this.attributes.get(attributeName);
		if (at == null)
			return null;
		else
			return at.getValue();
	}

	/**
	 * Returns a <code>java.util.HashMap</code> object with the DataField's
	 * attributes and their corresponding value.
	 * 
	 * @return attributesMap The <code>java.util.HashMap</code> object containing
	 *         the DataField's attributes and their values.
	 */
	public Map<String, String> getAttributes() {
		return this.attributes.entrySet().stream()
				.collect(Collectors.toMap(Entry::getKey, e -> e.getValue().getValue()));

	}

	/**
	 * Sets the attributes for this DataField.
	 * 
	 * @param attributes
	 *            The attribute's <code>java.util.HashMap</code> object with the
	 *            attributes and their corresponding values.
	 */
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes.entrySet().stream()
				.collect(Collectors.toMap(Entry::getKey, e -> Attribute.createString(e.getValue())));
	}

	/**
	 * Removes the attribute with the given name. Nothing happens in case the name
	 * doesn't exist.
	 * 
	 * @param attributeName
	 *            The name of the attribute to remove.
	 */
	public void removeAttribute(String attributeName) {
		this.attributes.remove(attributeName);
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
			for (Entry<String, Attribute> e : attributes.entrySet()) {
				f.setAttribute(e.getKey(), e.getValue());
			}
		}
		return f;
	}

	@Override
	public String toString() {
		if (this.Value == null)
			return "";
		return this.Value.toString();
	}

	/**
	 * Sets the DataField's value to <code>null</code>
	 */
	public void clear() {
		setValue(null);
	}

	public static Object convertType(Object o, int targetType) {
		return DataFieldConverter.convertType(o, targetType);
	}

	public Map<String, Attribute> getAttributes2() {
		return new HashMap<>(this.attributes);
	}

	public String createEtag() {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);) {
			oos.writeObject(Value);
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(baos.toByteArray());
			String etag = DatatypeConverter.printHexBinary(thedigest);
			this.attributes.put("ETAG", Attribute.createString(etag));

			return DatatypeConverter.printHexBinary(thedigest);
		} catch (NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
		}
		return "";
	}

}
