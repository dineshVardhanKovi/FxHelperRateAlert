/*
 * 
 */
package com.fxHelper.general.common;

import java.io.File;
import java.util.ArrayList;

import com.fxHelper.general.common.ReadProperties;

import jxl.Sheet;
import jxl.Workbook;

public class XlRead {

	/**
	 * This Function returns the complete data from specified sheet by including
	 * columns record
	 * 
	 * @param fileLocation
	 * @param sheetname
	 * @return
	 */

	public static String[][] fetchData(String fileLocation, String sheetname) {

		Workbook wb = null;
		try {
			File f = new File(ReadProperties.projectLocation + fileLocation);
			wb = Workbook.getWorkbook(f);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TO get the access to the sheet
		Sheet sh = wb.getSheet(sheetname);

		// To get the number of rows present in sheet
		int totalNoOfRows = sh.getRows();

		// To get the number of columns present in sheet
		int totalNoOfCols = sh.getColumns();

		String data[][] = new String[totalNoOfRows][totalNoOfCols];

		for (int j = 0; j < totalNoOfRows; j++) {

			for (int j2 = 0; j2 < totalNoOfCols; j2++) {
				data[j][j2] = sh.getCell(j2, j).getContents().trim();
			}

		}
		return data;
	}

	/**
	 * Function return data object excluding first row of the sheet
	 * 
	 * @param fileLocation
	 * @param sheetname
	 * @return
	 */
	public static String[][] fetchDataExcludingFirstRow(String fileLocation, String sheetname) {

		Workbook wb = null;
		try {
			File f = new File(ReadProperties.projectLocation + fileLocation);
			wb = Workbook.getWorkbook(f);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TO get the access to the sheet
		Sheet sh = wb.getSheet(sheetname);

		// To get the number of rows present in sheet
		int totalNoOfRows = sh.getRows();

		// To get the number of columns present in sheet
		int totalNoOfCols = sh.getColumns();

		String data[][] = new String[totalNoOfRows - 1][totalNoOfCols];

		for (int j = 1; j < totalNoOfRows; j++) {

			for (int j2 = 0; j2 < totalNoOfCols; j2++) {
				data[j - 1][j2] = sh.getCell(j2, j).getContents().trim();
			}

		}
		return data;
	}

	/**
	 * Function return data object excluding first row of the sheet
	 * 
	 * @param fileLocation
	 * @param sheetname
	 * @return
	 */
	public static String[][] fetchDataBasedOnColumnType(String fileLocation, String sheetname, String columName,
			String columnData) {

		Workbook wb = null;
		try {
			File f = new File(ReadProperties.projectLocation + fileLocation);
			wb = Workbook.getWorkbook(f);
		} catch (Exception e) {

			e.printStackTrace();
		}

		// TO get the access to the sheet
		Sheet sh = wb.getSheet(sheetname);
		System.out.println(sheetname);
		// To get the number of rows present in sheet
		int totalNoOfRows = sh.getRows();

		// To get the number of columns present in sheet
		int totalNoOfCols = sh.getColumns();

		// Finding column index of required column
		int requuiredColumnIndex = 0;

		for (int i = 0; i < totalNoOfCols; i++) {
			requuiredColumnIndex = i;
			boolean b = sh.getCell(i, 0).getContents().trim().equalsIgnoreCase(columName);
			if (b) {
				break;
			}
		}
		// Find no of row with given content type
		ArrayList<Integer> rowNo = new ArrayList<>();
		for (int i = 0; i < totalNoOfRows; i++) {

			boolean b = sh.getCell(requuiredColumnIndex, i).getContents().trim().equalsIgnoreCase(columnData.trim());
			if (b) {
				rowNo.add(i);
			}
		}

		String data[][] = new String[rowNo.size()][totalNoOfCols];

		for (int j = 0; j < rowNo.size(); j++) {

			for (int j2 = 0; j2 < totalNoOfCols; j2++) {
				data[j][j2] = sh.getCell(j2, rowNo.get(j)).getContents().trim();
			}

		}
		return data;
	}

	// sample usage code
	public static void main(String[] args) {
		fetchDataBasedOnColumnType("TestInputs/viewarticle/viewarticle_testdata.xls", "NewsArtilceContent",
				"ArticleFeature", "NewsToolBar");
	}
}