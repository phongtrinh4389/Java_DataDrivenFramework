package com.nashtech.utils.excel;

import java.io.FileOutputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.nashtech.common.Common;
import com.nashtech.utils.report.*;

public class ExcelWriter extends ExcelUtility{

	public static ExcelWriter instance;
	
	public ExcelWriter(String excelPath, String excelFileName) {
		super(excelPath, excelFileName);
	}
	
	public static ExcelWriter getInstance(String excelPath, String excelFileName) {
		if(instance == null) {
			instance = new ExcelWriter(excelPath, excelFileName);
		}
		
		return instance;
	}

	
	/**
	 * This function is used to write the value into a cell of an excel Cell,
	 * this Cell is located by the number of row and the column string header.
	 * 
	 * @author HaNoi Automation Team
	 * @param strSheetName
	 *            The excel sheet name
	 * @param iRow
	 *            The number of row , start from index 1
	 * @param strColumn
	 *            The column header
	 * @param strData
	 *            The data to write
	 * @throws Exception
	 *             Throw an Exception if writing to Cell unsuccessfully
	 */
	public void setCellData(String strSheetName, int iRow, String strColumn, String strData)
			throws Exception {
		Row row = null;
		Cell cell = null;
		Cell cellTmp = null;

		try {
			setExcelFile(excelPath, excelFileName);
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

			// Initial Row if it's null
			if ((iRow - 1) < sheet.getFirstRowNum()
					|| ((iRow - 1) > sheet.getLastRowNum())) {
				throw new Exception("The row " + iRow + " is not exist");
			} else {
				if (sheet.getRow(iRow - 1) == null) {
					sheet.createRow(iRow - 1);
				}
				row = sheet.getRow(iRow - 1);
			}

			// Initial Cell if null
			if (row.getCell(iColumnIndex) == null) {
				row.createCell(iColumnIndex);
			}
			cell = row.getCell(iColumnIndex);
			// Set the strData to this Cell
			cell.setCellValue(strData);

		} catch (Exception e) {

			throw (e);

		}
	}

	/**
	 * This function is used to write the value into a cell of an excel Cell,
	 * this Cell is located by the number of row and the column string header.
	 * 
	 * @author HaNoi Automation Team
	 * @param strSheetName
	 *            The excel sheet name
	 * @param iRow
	 *            The index of row
	 * @param iColumn
	 *            The index of column
	 * @param strData
	 *            The data to write
	 * @throws Exception
	 *             Throw an Exception if writing to Cell unsuccessfully
	 */
	public void setCellData(String strSheetName, int iRow, int iColumn, String strData)
			throws Exception {
		
		try {
			Sheet sheet = getSheet(strSheetName);
			Row row = null;
			Cell cell = null;

			// Init Row if it's null
			if ((iRow - 1) < sheet.getFirstRowNum()
					|| ((iRow - 1) > sheet.getLastRowNum())) {
				throw new Exception("The row " + iRow + " is not exist");
			} else {
				if (sheet.getRow(iRow - 1) == null) {
					sheet.createRow(iRow - 1);
				}
			}
			row = sheet.getRow(iRow - 1);

			// Init cell if it's null
			if ((iColumn - 1) < row.getFirstCellNum()) {
				throw new Exception("The cell (" + iRow + ", " + iColumn
						+ ") is not exist");
			} else {
				if (row.getCell(iColumn - 1) == null) {
					row.createCell(iColumn - 1);
				}
				cell = row.getCell(iColumn - 1);
			}
			// Set the strData to this Cell
			cell.setCellValue(strData);

		} catch (Exception e) {

			throw (e);

		}
	}

	/**
	 * This function is used to write the value into a cell of an excel Cell,
	 * this Cell is located by the name of row and the column string header.
	 * 
	 * @author HaNoi Automation Team
	 * @param strSheetName
	 *            The excel sheet name
	 * @param strRow
	 *            The name of row
	 * @param strColumn
	 *            The column header
	 * @param strData
	 *            The data to write
	 * @throws Exception
	 *             Throw an Exception if writing to Cell unsuccessfully
	 */
	public void setCellData(String strSheetName, String strRow, String strColumn, String strData)
			throws Exception {
		Row row = null;
		Cell cell = null;
		Cell cellTmp = null;
		Row rowTmp = null;

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

			int firstRowIndex = sheet.getFirstRowNum();
			int lastRowIndex = sheet.getLastRowNum();
			int iRowIndex = -1;
			// Get the row index that matches with the strRow
			for (int i = firstRowIndex; i <= lastRowIndex; i++) {

				if (sheet.getRow(i) == null) {
					sheet.createRow(i);
				}
				rowTmp = sheet.getRow(i);

				if (rowTmp.getCell(0) == null) {
					rowTmp.createCell(0);
				}
				cellTmp = rowTmp.getCell(0);

				if ((cellTmp.getCellType() == Cell.CELL_TYPE_STRING)
						&& (cellTmp.getStringCellValue()
								.equalsIgnoreCase(strRow))) {
					iRowIndex = i;
					break;
				}
			}

			if (iRowIndex == -1) {
				throw new Exception("The row " + strRow + " is not exist");
			}

			row = sheet.getRow(iRowIndex);
			// Initial Cell if null
			if (row.getCell(iColumnIndex) == null) {
				row.createCell(iColumnIndex);
			}
			cell = row.getCell(iColumnIndex);

			// Set the strData to this Cell
			cell.setCellValue(strData);

		} catch (Exception e) {

			throw (e);

		}
	}

	/**
	 * This function is used to write data into an excel Cell, this Cell is
	 * located by the id of test case and the column header.
	 * 
	 * @author HaNoi Automation Team
	 * @param strSheetName
	 *            The excel sheet name
	 * @param strColname
	 *            The column header
	 * @param strValue
	 *            The data to write
	 * @throws Exception
	 *             Throw an Exception if can't set the cell data
	 */
	public void setCellData(String strSheetName, String strColname, String strValue)
			throws Exception {
		Cell frcell = null;
		int fcolindex = -1;

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
						fcolindex = frcell.getColumnIndex();

					}
				}
			}

			if (fcolindex != -1) {

				Row Row = sheet.getRow(SuiteListener.globalitv);
				Cell cell = Row.getCell(fcolindex);
				if (cell == null) {
					throw new Exception("The invalid cell :("
							+ SuiteListener.globalitv + "," + strColname + ")");
				}
				cell.setCellValue(strValue);

			} else {
				throw new Exception("The column " + strColname
						+ " is not exist");
			}
		} catch (Exception e) {
			throw (e);
		}

	}

	/**
	 * This function is used to write down test result
	 * 
	 * @author HaNoi Automation Team
	 * @param strSheetName
	 *            The excel sheet name
	 * @param sTestCaseName
	 *            test case that going to write result
	 * @param value
	 *            test result that going to write
	 * @throws Exception
	 *             Throw an Exception if can't write result to file
	 */
	public void writeResultToExcel(String strSheetName, String sTestCaseName, String value)
			throws Exception {
		
		String filePath = Common.correctPath(excelPath + excelFileName);
		
		try {
			setExcelFile(excelPath, excelFileName);
			Sheet sheet = getSheet(strSheetName);
			
			int i = getRowContains(strSheetName, sTestCaseName, 0);
			// Update the value of cell
			Cell cell = sheet.getRow(i).getCell(1);
			cell.setCellValue(value);

			FileOutputStream outFile = new FileOutputStream(filePath);
			workBook.write(outFile);
			outFile.close();
			
		} catch (Exception e) {
			Log.error(e.getMessage());
		}
	}

	/**
	 * This function is used to write down test result
	 * 
	 * @author HaNoi Automation Team
	 * @param strSheetName
	 *            The excel sheet name
	 * @param sTestCaseName
	 *            test case name that going to write result
	 * @param strColumn
	 *            Column name that going to write result
	 * @param value
	 *            test result that going to write
	 * @throws Exception
	 *             Throw an Exception if can't write result to file
	 */
	public void writeResultToExcel(String strSheetName, String sTestCaseName, String strColumn,
			String value) throws Exception {
		String filePath = Common.correctPath(excelPath + excelFileName);
		try {
			
			setExcelFile(excelPath, excelFileName);

			int i = getRowContains(strSheetName, sTestCaseName, 0);
			// Update the value of cell
			setCellData(strSheetName, i, strColumn, value);

			FileOutputStream outFile = new FileOutputStream(filePath);
			workBook.write(outFile);
			outFile.close();

		} catch (Exception e) {
			Log.error(e.getMessage());
		}
	}

	/**
	 * This function is used to write down test result
	 * 
	 * @author HaNoi Automation Team
	 * @param strSheetName
	 *            The excel sheet name
	 * @param sTestCaseName
	 *            test case name that going to write result
	 * @param columnIndex
	 *            Column index that going to write result
	 * @param value
	 *            test result that going to write
	 * @throws Exception
	 *             Throw an Exception if can't write result to file
	 */
	public void writeResultToExcel(String strSheetName, String sTestCaseName, int columnIndex,
			String value) throws Exception {
		String filePath = Common.correctPath(excelPath + excelFileName);
		try {
			
			setExcelFile(excelPath, excelFileName);

			int i = getRowContains(strSheetName, sTestCaseName, 0) + 1;
			// Update the value of cell
			setCellData(strSheetName, i, columnIndex, value);

			FileOutputStream outFile = new FileOutputStream(filePath);
			workBook.write(outFile);
			outFile.close();
			
		} catch (Exception e) {
			Log.error( e.getMessage());
		}
	}

	/**
	 * This function is used to write down test result
	 * 
	 * @author HaNoi Automation Team
	 * @param strSheetName
	 *            The excel sheet name
	 * @param testCaseIndex
	 *            test case index that going to write result, start from 1
	 * @param strColumn
	 *            Column name that going to write result
	 * @param value
	 *            test result that going to write
	 * @throws Exception
	 *             Throw an Exception if can't write result to file
	 */
	public void writeResultToExcel(String strSheetName, int testCaseIndex, String strColumn, String value)
			throws Exception {
		
		String filePath = Common.correctPath(excelPath + excelFileName);
		
		try {
			
			setExcelFile(excelPath, excelFileName);
			
			// Update the value of cell
			setCellData(strSheetName, testCaseIndex, strColumn, value);

			FileOutputStream outFile = new FileOutputStream(filePath);
			workBook.write(outFile);
			outFile.close();
			
		} catch (Exception e) {
			Log.error(e.getMessage());
		}
	}
	
	/**
	 * This function is used to write down test result into the excel file
	 * 
	 * @author HaNoi Automation Team
	 * @param strDest
	 *            The Test Result file path
	 * @throws Exception
	 *             Throw an Exception if can't write result to file
	 */
	public void writeToExcel(String strDest)
			throws Exception {
		
		try {
			
			FileOutputStream outFile = new FileOutputStream(strDest);
			workBook.write(outFile);
			outFile.close();
			
		} catch (Exception e) {
			Log.error(e.getMessage());
		}
	}
}
