package com.basiscomponents.db.util;

import com.basiscomponents.db.DataRow;

public class SimpleDataRowMatcher extends DataRowMatcher {

	private Object criteria;

	SimpleDataRowMatcher(final String fieldname, final Object criteria) {
		super(fieldname);
		this.criteria = criteria;
	}

	@Override
	public boolean matches(DataRow dr) {
		return dr.getField(fieldName, true).getString().equals(criteria);
	}

}