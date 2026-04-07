package Utilities;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.*;

public class ExcelFileWriter {
	public XSSFRow row;
	public XSSFCell cell;
	public XSSFSheet sheet;
	public String sheetName;
	public XSSFWorkbook wb;
	public int rownum=1;
	
	public ExcelFileWriter(XSSFWorkbook wb, String sheetName) {
		this.wb=wb;
		sheet = wb.getSheet(sheetName);
	}
	public void setCellValue(int col, String cellValue) throws IOException {
		try {
			row = sheet.getRow(rownum);
			cell = row.getCell(col);
		}
		catch(NullPointerException e){
			row = sheet.createRow(rownum);
			cell = row.createCell(col);
		}
		if(cell==null) {
			cell = row.createCell(col);
		}
		cell.setCellValue(cellValue);
	}
	
	public void rowInc() {
		this.rownum++;
	}
}
