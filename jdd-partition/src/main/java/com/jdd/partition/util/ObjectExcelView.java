package com.jdd.partition.util;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

/**
* 导入到EXCEL
* 类名称：ObjectExcelView.java
* 类描述： 
* @author fh
* 作者单位： 
* 联系方式：
* @version 1.0
 */
public class ObjectExcelView extends AbstractXlsxView {

	protected void buildExcelDocument(Map<String, Object> model,Workbook workbook, HttpServletRequest request,
									  HttpServletResponse response) {
		String filename = DateUtil.convertDateToString(new Date(),"yyyy-MM-dd HH:mm:ss");
		Sheet sheet;
		Cell cell;
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename="+filename+".xls");
		sheet = workbook.createSheet("sheet1");
		
		List<String> titles = (List<String>) model.get("titles");
		int len = titles.size();
		CellStyle headerStyle = workbook.createCellStyle(); //标题样式
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		Font headerFont = workbook.createFont();	//标题字体
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short)11);
		headerStyle.setFont(headerFont);
		short width = 20,height=25*20;
		sheet.setDefaultColumnWidth(width);
		for(int i=0; i<len; i++){ //设置标题
			String title = titles.get(i);
			cell = getCell(sheet, 0, i);
			cell.setCellStyle(headerStyle);
			cell.setCellValue(title);
		}
		sheet.getRow(0).setHeight(height);
		
		CellStyle contentStyle = workbook.createCellStyle(); //内容样式
		contentStyle.setAlignment(HorizontalAlignment.CENTER);
		List<PageData> varList = (List<PageData>) model.get("varList");
		int varCount = varList.size();
		for(int i=0; i<varCount; i++){
			PageData vpd = varList.get(i);
			for(int j=0;j<len;j++){
				String varstr = vpd.getString("var"+(j+1)) != null ? vpd.getString("var"+(j+1)) : "";
				cell = getCell(sheet, i+1, j);
				cell.setCellStyle(contentStyle);
				cell.setCellValue(varstr);
			}
			
		}
		
	}

	private Cell getCell(Sheet sheet, int i, int j){
		Row row = sheet.getRow(i);
		if (row == null) {
			row = sheet.createRow(i);
		}
		Cell cell = row.getCell(j);
		if (cell == null) {
			cell = row.createCell(j);
		}
		return cell;
	}

}
