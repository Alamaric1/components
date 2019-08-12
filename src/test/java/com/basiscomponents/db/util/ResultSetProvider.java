package com.basiscomponents.db.util;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;

import com.basiscomponents.db.DataRow;
import com.basiscomponents.db.ResultSet;

public class ResultSetProvider {

	public static ResultSet createDefaultResultSet() throws Exception {
		return createDefaultResultSet(false);
	}

  public static ResultSet createDefaultResultSet(boolean nullAllFields) throws Exception {
    ResultSet result = new ResultSet();
    result.add(DataRowProvider.buildSampleDataRow(nullAllFields));
    return result;
	}
  
  public static ResultSet createMultipleDataRowResultSet() throws Exception {
	    ResultSet result = new ResultSet();
	    result.add(DataRowProvider.buildSampleDataRow(false));
	    result.add(DataRowProvider.buildSampleDataRow(false));
	    result.add(DataRowProvider.buildSampleDataRow(false));
	    return result;
	  }

  public static ResultSet createDefaultResultSetMinMax() throws Exception {
	    ResultSet result = new ResultSet();
	    result.add(DataRowProvider.buildSampleDataRowMinMax());
	    return result;
	  }
  
  public static ResultSet createStringOnlyResultSet() throws Exception {
	  ResultSet result = new ResultSet();
	  result.add(DataRowProvider.buildStringOnlyDataRow());
	  return result;
  }
  
	public static ResultSet createNestedDataRowsResultSet() throws Exception {
		ResultSet result = new ResultSet();
		result.add(DataRowProvider.buildNestedDataRowWithDataRows());
		return result;
	}

	public static ResultSet createNestedResultSetsResultSet() throws Exception {
		ResultSet result = new ResultSet();
		result.add(DataRowProvider.buildNestedDataRowWithResultSets());
		return result;
	}

	public static ResultSet createNestedResultSetsWithMultipleDataRowsResultSet() throws Exception {
		ResultSet result = new ResultSet();
		result.add(DataRowProvider.buildNestedDataRowWithMultipleDataRowsResultSet());
		return result;
	}

	public static ResultSet createToJsonOnlyResultSet() throws Exception {
		ResultSet result = new ResultSet();
		result.add(DataRowProvider.buildToJsonOnlyDataRow());
		return result;
	}

	public static ResultSet createLeftResultSetForLeftJoinTesting() throws ParseException {
		
		ResultSet rs = new ResultSet();
		DataRow dr1 = new DataRow();
		DataRow dr2 = new DataRow();
		DataRow dr3 = new DataRow();
		DataRow dr4 = new DataRow();

		dr1.setFieldValue("Name", "Heinz");
		dr1.setFieldValue("Alter", 53);
		dr1.setFieldValue("PLZ", "66132");
		rs.add(dr1);

		dr2.setFieldValue("Name", "Frank");
		dr2.setFieldValue("Alter", 52);
		dr2.setFieldValue("PLZ", "66122");
		rs.add(dr2);

		dr3.setFieldValue("Name", "Laura");
		dr3.setFieldValue("Alter", 50);
		dr3.setFieldValue("PLZ", "66156");
		rs.add(dr3);
		
		dr4.setFieldValue("Name", "unkown");
		dr4.setFieldValue("Alter", 49);
		dr4.setFieldValue("PLZ", "66134");
		rs.add(dr4);

		return rs;

	}

	public static ResultSet createRightResultSetForLeftJoinTesting() throws ParseException {

		ResultSet rs = new ResultSet();
		DataRow dr1 = new DataRow();
		DataRow dr2 = new DataRow();
		DataRow dr3 = new DataRow();

		dr1.setFieldValue("PLZ", "66132");
		dr1.setFieldValue("Ort", "Saarbruecken");
		dr1.setFieldValue("Buergermeister", "Elias");
		rs.add(dr1);

		dr2.setFieldValue("PLZ", "66122");
		dr2.setFieldValue("Ort", "St. Wendel");
		dr2.setFieldValue("Buergermeister", "Sascha");
		rs.add(dr2);

		dr3.setFieldValue("PLZ", "66156");
		dr3.setFieldValue("Ort", "Dillingen");
		dr3.setFieldValue("Buergermeister", "Dude");
		rs.add(dr3);

		return rs;

	}

	public static ResultSet createAnotherRightResultSetForLeftJoinTesting() throws ParseException {

		ResultSet rs = new ResultSet();
		DataRow dr1 = new DataRow();
		DataRow dr2 = new DataRow();
		DataRow dr3 = new DataRow();

		dr1.setFieldValue("PLZ", "66132");
		dr1.setFieldValue("Stadt", "Moskau");
		dr1.setFieldValue("Minister", "Dimitri");
		rs.add(dr1);

		dr2.setFieldValue("PLZ", "66122");
		dr2.setFieldValue("Stadt", "Wien");
		dr2.setFieldValue("Minister", "Huber");
		rs.add(dr2);

		dr3.setFieldValue("PLZ", "66156");
		dr3.setFieldValue("Stadt", "Konz");
		dr3.setFieldValue("Minister", "Peter");
		rs.add(dr3);

		return rs;

	}

	public static ResultSet createMoreTypesRightResultSetForLeftJoinTesting() throws ParseException {

		ResultSet rs = new ResultSet();
		DataRow dr1 = new DataRow();
		DataRow dr2 = new DataRow();
		DataRow dr3 = new DataRow();

		dr1.setFieldValue("PLZ", "66132");
		dr1.setFieldValue("Double", 54.45);
		dr1.setFieldValue("Long", Long.valueOf("5454544355464354"));
		dr1.setFieldValue("Date", new Date(System.currentTimeMillis()));
		dr1.setFieldValue("List", new ArrayList<String>().add("hi"));
		rs.add(dr1);

		dr2.setFieldValue("PLZ", "66122");
		dr2.setFieldValue("Double", 54.45);
		dr2.setFieldValue("Long", Long.valueOf("5454544355464354"));
		dr2.setFieldValue("Date", new Date(System.currentTimeMillis()));
		dr2.setFieldValue("List", new ArrayList<String>().add("hi"));
		rs.add(dr2);

		dr3.setFieldValue("PLZ", "66156");
		dr3.setFieldValue("Double", 54.45);
		dr3.setFieldValue("Long", Long.valueOf("5454544355464354"));

		dr3.setFieldValue("Date", new Date(System.currentTimeMillis()));
		dr3.setFieldValue("List", new ArrayList<String>().add("hi"));
		rs.add(dr3);

		return rs;

	}

}
