package com.example.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;

/**
 * Created by 陈成 on 2017/5/12.
 */
public class ReadAndWritePoiUtil {

    private  File file;

    private  Workbook  wb;


    public ReadAndWritePoiUtil(String  filePath) {
        this.file = new File(filePath);
    }

    public boolean writeTitleName(Object o)  {
        try {
            Field[] fields =  o.getClass().getDeclaredFields();
             wb = getWorkbookType();
            Sheet sheet =  wb.getSheetAt(0);
            Row row =  sheet.createRow(0);
            Cell cell = null;
            for(int i = 0 ;i <fields.length; i ++){
                cell = row.createCell(i);
                cell.setCellValue(fields[i].getName());
           }

            wb.write(new FileOutputStream(file));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
    }

    public boolean writeProuctInfo(Object o,int startLine){
        Field[] fields =  o.getClass().getDeclaredFields();
        try {
            wb = getWorkbookType();
            Sheet sheet =  wb.getSheetAt(0);
            Row row =  sheet.createRow(startLine+1);
            Cell cell = null;
            for(int i = 0 ;i <fields.length; i ++){
                cell = row.createCell(i);
                String name  = fields[i].getName();
                cell.setCellValue( o.getClass().getMethod(name).invoke(o).toString());
            }
            wb.write(new FileOutputStream(file));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return  false;
        }

    }

    public int getRowCount() throws IOException {
        wb = getWorkbookType();
        Sheet sheet =  wb.getSheetAt(0);
        return sheet.getLastRowNum();
    }

    private Workbook getWorkbookType() throws IOException {
        if(file.exists()){
            String fileName = file.getName();
            FileInputStream fis = new FileInputStream(file);
            if(fileName.endsWith(".xls")){
                wb = new HSSFWorkbook(fis);
            }else if(fileName.endsWith(".xlsx")){
                wb = new XSSFWorkbook(fis);
            }else {
                System.out.println("非法的文件");
            }
        }else {
            FileOutputStream fos = new FileOutputStream(file);
            wb = new XSSFWorkbook();
            wb.createSheet("sheet1");
            wb.write(fos);
        }
        return wb;
    }


}
