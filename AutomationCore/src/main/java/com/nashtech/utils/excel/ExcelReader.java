package com.nashtech.utils.excel;

import java.util.ArrayList;
import java.util.HashMap;

import com.nashtech.utils.report.*;

public class ExcelReader extends ExcelUtility{

	public static ExcelReader instance;

	public ExcelReader(String excelPath, String excelFileName) {
		
		super(excelPath, excelFileName);
		
	}
	
	public static ExcelReader getInstance(String excelPath, String excelFileName) {
		if(instance == null) {
			instance = new ExcelReader(excelPath, excelFileName);
		}
		
		return instance;
	}
	
	/**
	 * This function is used to return all data of selected sheet to an object
	 * array
	 * 
	 * @param excelSheetName
	 *            The Sheet name
	 * @return The object array as data table
	 * @throws Exception
	 *             Throw an Exception if can't get file data
	 */
	public Object[][] getTableArray(String excelSheetName) throws Exception {
//		String filePath = excelPath + excelFileName;

		String[][] tabArray = null;
		try {
			
			int startRow = 1;
			int ci = 0;
			int totalRows = getRowUsed(excelSheetName);
			int totalCols = getColUsed(excelSheetName);

			tabArray = new String[totalRows][totalCols];
			int startCol = 0;
			int cj = 0;
			for (int i = startRow; i <= totalRows; i++, ci++) {
				startCol = 0;
				cj = 0;
				for (int j = startCol; j < totalCols; j++, cj++)
					tabArray[ci][cj] = getCellData(excelSheetName, i, j);
			}

		} catch (Exception e) {
			Log.error("Can't get the data of the sheet: ["+ excelSheetName + "]" );
			throw e;
		}
		return (tabArray);
	}

	/**
	 * This function is used to return the header row of selected sheet
	 * array
	 * 
	 * @param excelSheetName
	 *            The Sheet name
	 * @return The object array
	 * @throws Exception
	 *             Throw an Exception if can't get file data
	 */
	public Object[] getHeader(String excelSheetName) throws Exception {

		String[] headerArray = null;
		try {

			int headerIndex = 0;
			int totalCols = getColUsed(excelSheetName);

			headerArray = new String[totalCols];
			int startCol = 0;
			
			for (int i = startCol; i < totalCols; i++){
				headerArray[i] = getCellData(excelSheetName, headerIndex, i);
			}
		} catch (Exception e) {
			Log.error("Can't get the hearder of the sheet: ["+ excelSheetName + "]" );
			throw e;
		} 
		return (headerArray);
	}

	/**
	 * This function is used to get data of a sheet then store as a list of dictionary
	 * array
	 * 
	 * @param excelSheetName
	 *            The Sheet name
	 * @return The 2D arrays and each element is a dictionary
	 * @throws Exception
	 *             Throw an Exception if can't get file data
	 */
	public ArrayList<ArrayList<HashMap<String, String>>> getDictionaryData(String excelSheetName)
			throws Exception {
		
		ArrayList<ArrayList<HashMap<String, String>>> arr = new ArrayList<ArrayList<HashMap<String, String>>>();
		Object[] headerArray = null;
		Object[][] dataTable = null;
		try {
			
			this.setExcelFile(excelPath, excelFileName);
			headerArray = getHeader(excelSheetName);
			dataTable = getTableArray(excelSheetName);

			for(int i=0; i< dataTable.length; i++) {
				
				ArrayList<HashMap<String, String>> prodArrayList = new ArrayList<HashMap<String, String>>();
				
				for(int j=0; j < dataTable[i].length; j++){
					
					HashMap<String, String> prodHashMap = new HashMap<String, String>();
					prodHashMap.put(headerArray[j].toString(), dataTable[i][j].toString());
					prodArrayList.add(prodHashMap);
					
				}
				arr.add(prodArrayList);
			}

		}
		catch(Exception e) {
			Log.error(e.getMessage());
			throw e;
		}
		
		return arr;
	}

	/**
	 * This function is used to return all data of selected sheet as a 2D Arrays
	 * 
	 * @param excelSheetName
	 *            The Sheet name
	 * @return The 2D object Array
	 * @throws Exception
	 *             Throw an Exception if can't get file data
	 */
	public Object[][] getDictionaryExcelData(String sheetName) throws Exception {

		ArrayList<ArrayList<HashMap<String, String>>> testObjArray = getDictionaryData(sheetName);

		Object[][] data = new Object[testObjArray.size()][1];
		for (int i = 0; i < testObjArray.size(); i++) {
			data[i][0] = testObjArray.get(i).toArray();
		}
		return data;
	}
	
}
