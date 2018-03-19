package com.nashtech.utils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.nashtech.common.Common;
import com.nashtech.common.Constant;
import com.nashtech.utils.report.*;

public class ExcelUtility {

	public Workbook workBook;
	public FormulaEvaluator objFormulaEvaluator;
	public DataFormatter objDefaultFormater;
	
	public String excelPath;
	public String excelFileName;


	public ExcelUtility(String excelPath, String excelFileName) {
		
		this.excelPath = excelPath;
		this.excelFileName = excelFileName;
		
	}
	
	/**
	 * This function is used to setup an excel file and read data of an excel
	 * sheet to prepare for reading or writing a cell data.
	 * 
	 * @author Hanoi Automation team
	 * @param excelPath
	 *            The path of data folder
	 * @param excelFileName
	 *            The name of excel file
	 * @throws Exception
	 *             Throw an Exception if reading excel file unsuccessfully
	 */
	public void setExcelFile(String excelPath, String excelFileName) throws Exception {

		String strExcelDataPath = Common.correctPath(excelPath + "\\" + excelFileName);
		FileInputStream in = new FileInputStream(new File(strExcelDataPath));
		try {
			
			if(workBook == null) {
				
				objDefaultFormater = new DataFormatter();
				// Get type of excel file to prepare the reader
				String extension = excelFileName.substring(excelFileName
						.indexOf("."));
				
				if (extension.equalsIgnoreCase(Constant.XSSF_EXTENSION)) {
//					workBook = new XSSFWorkbook(in);
					workBook = new XSSFWorkbook(in);
					objFormulaEvaluator = new XSSFFormulaEvaluator(
							(XSSFWorkbook) workBook);
				} else if (extension.equalsIgnoreCase(Constant.HSSF_EXTENSION)) {
					
					workBook = new HSSFWorkbook(in);
					objFormulaEvaluator = new HSSFFormulaEvaluator(
							(HSSFWorkbook) workBook);
				} else {
					throw new Exception("The excel file is invalid!!!!");
				}
				
			}

		} catch (Exception e) {
			Log.error(e.getMessage());
			throw (e);
		} 
		finally {
			in.close();
		}
	}
	
	/**
	 * This function is used to get an excel sheet
	 * 
	 * @author Hanoi Automation team
	 * @param strSheetName
	 *            The excel sheet name
	 * @return a Sheet object
	 * @throws Exception
	 *             Throw an Exception if can't get the sheet
	 */
	public Sheet getSheet(String strSheetName) throws Exception {
		try {
			if(workBook != null) {
				return workBook.getSheet(strSheetName);
			}
			else {
				throw new Exception("workbook is null, set the excel file first");
			}
		}
		catch(Exception e) {
			Log.error(e.getMessage());
			throw e;
		}
	}
	
	/**
	 * This function is used to read the value of an excel Cell, this Cell is
	 * located by the row number and the column header.
	 * 
	 * @author Hanoi Automation team
	 * @param strSheetName
	 *            The excel sheet name
	 * @param iRow
	 *            The row number
	 * @param strColumn
	 *            The column header
	 * @return The string value of cell
	 * @throws Exception
	 *             Throw an Exception if can't get cell data
	 */
	public String getCellData(String strSheetName, int iRow, String strColumn) throws Exception {

		String strCellValue = "";
		Cell cellTmp = null;
		// Get column index
		try {
			Sheet sheet = getSheet(strSheetName);
			Row theFirstRow = sheet.getRow(0);
			Iterator<Cell> cellIter = theFirstRow.cellIterator();
			int iColumnIndex = -1;
			while (cellIter.hasNext()) {
				cellTmp = cellIter.next();
				int cellType = cellTmp.getCellType();

				if ((cellType == Cell.CELL_TYPE_STRING)
						&& (cellTmp.getStringCellValue()
								.equalsIgnoreCase(strColumn))) {
					iColumnIndex = cellTmp.getColumnIndex();
					break;
				}

			}
			if (iColumnIndex == -1) {
				throw new Exception("The column " + strColumn + " is not exist");
			}

			// Get the value of cell
			Cell cell = sheet.getRow(iRow - 1).getCell(iColumnIndex);
			objFormulaEvaluator.evaluate(cell);
			strCellValue = objDefaultFormater.formatCellValue(cell,
					objFormulaEvaluator);

		} catch (Exception e) {

			throw e;

		}

		return strCellValue.trim();
	}

	/**
	 * This function is used to read the value of an excel Cell, this Cell is
	 * located by the row string and the column string header.
	 * 
	 * @author Hanoi Automation team
	 * @param strSheetName
	 *            The excel sheet name
	 * @param strRow
	 *            The name of row
	 * @param strColumn
	 *            The column header
	 * @return The string value of Cell
	 * @throws Exception
	 *             Throw an Exception if can't get cell data
	 */
	public String getCellData(String strSheetName, String strRow, String strColumn) throws Exception {

		String strCellValue = "";
		Cell cellTmp = null;

		try {
			Sheet sheet = getSheet(strSheetName);
			// Get the index of the column
			Row theFirstRow = sheet.getRow(0);
			Iterator<Cell> cellIter = theFirstRow.cellIterator();
			int iColumnIndex = -1;
			while (cellIter.hasNext()) {
				cellTmp = cellIter.next();

				if ((cellTmp.getCellType() == Cell.CELL_TYPE_STRING)
						&& (cellTmp.getStringCellValue()
								.equalsIgnoreCase(strColumn))) {
					iColumnIndex = cellTmp.getColumnIndex();
					break;
				}

			}
			if (iColumnIndex == -1) {
				throw new Exception("The column " + strColumn
						+ " is not exist!!!");
			}

			// Get the index of Row
			Iterator<Row> rowIter = sheet.rowIterator();
			int iRowIndex = 0;
			boolean hasRow = false; // This flag will be set to True when data
									// matching.
			Row rowTmp = null;
			Cell theFirstCell = null;
			while (rowIter.hasNext()) {
				rowTmp = rowIter.next();
				theFirstCell = rowTmp.getCell(0);

				if ((theFirstCell.getCellType() == Cell.CELL_TYPE_STRING)
						&& (theFirstCell.getStringCellValue()
								.equalsIgnoreCase(strRow))) {
					hasRow = true;
					break;
				}
				iRowIndex++;

			}
			if (!hasRow) {
				throw new Exception("The row " + strRow + " is not exist");
			}
			// Get the value of Cell
			Cell cell = sheet.getRow(iRowIndex).getCell(iColumnIndex);
			objFormulaEvaluator.evaluate(cell);
			strCellValue = objDefaultFormater.formatCellValue(cell,
					objFormulaEvaluator);

		} catch (Exception e) {
			throw e;
		}

		return strCellValue.trim();

	}

	/**
	 * This function is used to read the value of an excel Cell, this Cell is
	 * located by the id of test case and the column header.
	 * 
	 * @author Hanoi Automation team
	 * @param strSheetName
	 *            The excel sheet name
	 * @param strColname
	 *            The column header
	 * @return The string data of Cell
	 * @throws Exception
	 *             Throw an Exception if can't get the cell data
	 */
	public String getCellData(String strSheetName, String strColname) throws Exception {

		String strValue = null;
		int iColumnIndex = -1;
		Cell frcell = null;

		try {
			Sheet sheet = getSheet(strSheetName);
			Row r1 = sheet.getRow(0);
			Iterator<Cell> celliter = r1.cellIterator();
			while (celliter.hasNext()) {
				frcell = celliter.next();
				int frcelltype = frcell.getCellType();
				if (frcelltype == Cell.CELL_TYPE_STRING) {
					if (frcell.getStringCellValue()
							.equalsIgnoreCase(strColname)) {
						iColumnIndex = frcell.getColumnIndex();
					}
				}
			}

			if (iColumnIndex != -1) {

				Row Row = sheet.getRow(SuiteListener.globalitv);
				strValue = Row.getCell(iColumnIndex).getStringCellValue();

			} else {
				throw new Exception("The column " + strColname
						+ " is not exist");
			}

		} catch (Exception e) {

			throw e;

		}
		return strValue.trim();
	}

	/**
	 * This function is used to read the value of an excel Cell, this Cell is
	 * located by the row number and column number.
	 * 
	 * @author Ha Noi Automation team
	 * @param strSheetName
	 *            The excel sheet name
	 * @param iRow
	 *            The row number
	 * @param iColumn
	 *            The column number
	 * @return The string value of cell
	 * @throws Exception
	 *             Throw an Exception if can't get cell data
	 */
	public String getCellData(String strSheetName, int iRow, int iColumn) throws Exception {

		try {
			
			Sheet sheet = getSheet(strSheetName);
			if (sheet.getRow(iRow) == null) {
				sheet.createRow(iRow);
			}
			if (sheet.getRow(iRow).getCell(iColumn) == null) {
				sheet.getRow(iRow).createCell(iColumn);
			}
			Cell cell = sheet.getRow(iRow).getCell(iColumn);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			String CellData = cell.getStringCellValue();
			return CellData.trim();
			
		} catch (Exception e) {
			
			throw (e);
			
		}
	}
	
	/**
	 * This function is used to return number of data row in file
	 * 
	 * @author HaNoi Automation Team
	 * @param strSheetName
	 *            The excel sheet name
	 * @return Number of data row
	 * @throws Exception
	 *             Throw an Exception if can't set the cell data
	 */
	public int getRowUsed(String strSheetName) throws Exception {
		
		try {
			
			Sheet sheet = getSheet(strSheetName);
			int RowCount = sheet.getLastRowNum();
			return RowCount;
			
		} catch (Exception e) {
			
			throw (e);
			
		}
	}

	/**
	 * This function is used to return number of data column in file
	 * 
	 * @author HaNoi Automation Team
	 * @param strSheetName
	 *            The excel sheet name
	 * @return Number of data column
	 * @throws Exception
	 *             Throw an Exception if can't set the cell data
	 */
	public int getColUsed(String strSheetName) throws Exception {
		try {
			
			Sheet sheet = getSheet(strSheetName);
			int ColCount = sheet.getRow(0).getLastCellNum();
			return ColCount;
			
		} catch (Exception e) {
			
			throw (e);
			
		}
	}

	/**
	 * This function is used to return test case name by removing unnecessary
	 * text (e.g: com.tutorial.)
	 * 
	 * @author
	 * @param sTestCase
	 *            The file path
	 * @return Test case name
	 * @throws Exception
	 *             Throw an Exception if can't get test case name
	 */
	public String getTestCaseName(String sTestCase) throws Exception {
		String value = sTestCase;
		try {
			int posi = value.indexOf("@");
			value = value.substring(0, posi);
			posi = value.lastIndexOf(".");
			value = value.substring(posi + 1);
			return value;
		} catch (Exception e) {
			throw (e);
		}
	}

	/**
	 * This function is used to return row that containing select text in select
	 * column
	 * 
	 * @author HaNoi Automation Team
	 * @param strSheetName
	 *            The excel sheet name
	 * @param text
	 *            text that want to find
	 * @param colNum
	 *            Column that want to find
	 * @return row that contain text in select column
	 * @throws Exception
	 *             Throw an Exception if can't get row
	 */
	public int getRowContains(String strSheetName, String text, int colNum) throws Exception {
		int i;
		try {
			int rowCount = this.getRowUsed(strSheetName);
			for (i = 0; i < rowCount; i++) {
				if (this.getCellData(strSheetName, i, colNum).equalsIgnoreCase(text)) {
					break;
				}
			}
			return i;
		} catch (Exception e) {
			throw (e);
		}
	}
}
