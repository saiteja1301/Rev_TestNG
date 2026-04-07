package Utilities;

import java.util.HashMap;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFileReader {
	public XSSFWorkbook wb;
	public XSSFSheet sh;
	public XSSFRow headerRow;
	public XSSFRow currentRow;
	public XSSFCell currentCell;
	
	public ExcelFileReader(XSSFWorkbook wb,String sheetName) {
		this.wb = wb;
		sh = this.wb.getSheet(sheetName);
	}
	
	public HashMap<String,String> readFile(){
		HashMap<String,String> map = new HashMap<>();
		headerRow = sh.getRow(0);
		currentRow = sh.getRow(1);
		for(int i=0;i<6;i++) {
			try {
				currentCell = currentRow.getCell(i);
				map.put(headerRow.getCell(i).toString(), currentCell.toString());
			}
			catch(NullPointerException e) {
				map.put(headerRow.getCell(i).toString(), null);
			}
		}
		
		return map;
	}
}
