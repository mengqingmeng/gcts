package com.example.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

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

    public boolean writeProuctInfo(Object o){
        try {
            Field[] fields =  o.getClass().getDeclaredFields();
            if(getRowCount()<=0){
                writeTitleName(o);
            }
            int startLine = getRowCount()+1;
            StringBuffer sb = null;
            String name = null;
            Object val = null;
            wb = getWorkbookType();
            Sheet sheet =  wb.getSheetAt(0);
            Row row =  sheet.createRow(startLine);
            Cell cell = null;
            Cell cell1 = null;
            for(int i = 0 ;i <fields.length; i ++){
                cell = row.createCell(i);
                name =   fields[i].getName();
                sb  = new StringBuffer();
                sb.append("get").append(name.substring(0,1).toUpperCase()).append(name.substring(1,name.length()));
                Method method = o.getClass().getMethod(sb.toString());
                val = method.invoke(o);
               if(val != null){
                   String str = val.toString();
                  if("amazonProductInfoMations".equals(name)){
                      String[] strs = str.split(":");
                      for(int j = 1 ;j<strs.length;j++){
                         cell1 = row.createCell(i+j);
                         cell1.setCellValue(strs[j]);
                      }
                      str = strs[0];
                  }
                cell.setCellValue(str);
            }else {
                   cell.setCellValue("");
               }
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
            if(file.getPath().endsWith(".xls")){
                wb = new HSSFWorkbook();
            }else {
                wb = new XSSFWorkbook();
            }
            FileOutputStream fos = new FileOutputStream(file);

            wb.createSheet("sheet1");

            wb.write(fos);
        }
        return wb;
    }


}
