package com.example.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;

/**
 * Created by 陈成 on 2017/5/12.
 * 路径分隔符使用“/”
 */
public final class ReadAndWritePoiUtil {
    private static volatile ReadAndWritePoiUtil instance=null;
    private  File file;
    private  Workbook  wb;
    private int lineNum;
    private Properties properties;


    private ReadAndWritePoiUtil(String  filePath) throws FileNotFoundException {
        File temp =   new File(filePath.substring(0,filePath.lastIndexOf("/")));
        if(!temp.exists()){
            temp.mkdirs();
        }
        this.file = new File(filePath);
        getWorkbookType();
        properties = new Properties();
        try {
            properties.load(new InputStreamReader(ReadAndWritePoiUtil.class.getResourceAsStream("/title_name.properties"),"utf-8"));

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public static ReadAndWritePoiUtil getInstance(String  filePath) throws FileNotFoundException {
        filePath = filePath == null ? "d:/data/temp.xls":filePath;
        if(instance == null){
            instance = new ReadAndWritePoiUtil(filePath);
        }else if(!filePath.equals(instance.getFilePath())){
            instance = new ReadAndWritePoiUtil(filePath);
        }
        return instance ;
    }

    private boolean writeTitleName(Object o )  {
        try (FileOutputStream fos = new FileOutputStream(file)){
            Field[] fields =  o.getClass().getDeclaredFields();
            Sheet sheet =  wb.getSheetAt(0);
            Row row =  sheet.createRow(0);
            Cell cell = null;
            for(int i = 0 ;i <fields.length; i ++){
                cell = row.createCell(i);
                cell.setCellValue(properties.getProperty(fields[i].getName(),fields[i].getName()));
           }

            wb.write(fos);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
    }

    /**
     * 将对象持久化成excel文件
     * @param objs
     * @return
     */
    public boolean writeProuctInfo(List<?> objs){
        try( BufferedOutputStream fos =new BufferedOutputStream( new FileOutputStream(file))) {

            writeObj(objs,fos);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return  false;
        }

    }



    private void writeObj(List<?>objs,OutputStream fos) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        if (lineNum <= 0) {
            if (objs.size() > 0)
                writeTitleName(objs.get(0));
        }
        lineNum = lineNum + 1;
        StringBuffer sb = null;
        String name = null;
        Object val = null;
        Sheet sheet = wb.getSheetAt(0);
        Row row = null;
        Cell cell = null;
        Cell cell1 = null;
        Field[] fields = null;
        for (Object o : objs) {
           row =  sheet.createRow(lineNum++);
            fields = o.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                cell = row.createCell(i);
                name = fields[i].getName();
                sb = new StringBuffer();
                sb.append("get").append(name.substring(0, 1).toUpperCase()).append(name.substring(1, name.length()));
                Method method = o.getClass().getMethod(sb.toString());
                val = method.invoke(o);
                if (val != null) {
                    String str = val.toString();
                    if ("infoMations".equals(name)) {
                        String[] strs = str.split("<:>");
                        for (int j = 1; j < strs.length; j++) {
                            cell1 = row.createCell(i + j);
                            cell1.setCellValue(strs[j]);
                        }
                        str = strs[0];
                    }
                    cell.setCellValue(str);
                } else {
                    cell.setCellValue("");
                }
            }
        }
        wb.write(fos);
    }
    private void setLineNum() throws IOException {
        Sheet sheet =  wb.getSheetAt(0);
        lineNum = sheet.getLastRowNum();
    }

    private void  getWorkbookType() {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            if (file.exists()) {
                String fileName = file.getName();
                fis = new FileInputStream(file);
                if (fileName.endsWith(".xls")) {
                    wb = new HSSFWorkbook(fis);
                } else if (fileName.endsWith(".xlsx")) {
                    wb = new XSSFWorkbook(fis);
                } else {
                    System.out.println("非法的文件");
                }
            } else {
                if (file.getPath().endsWith(".xls")) {
                    wb = new HSSFWorkbook();
                } else {
                    wb = new XSSFWorkbook();
                }
                fos = new FileOutputStream(file);

               wb.createSheet("sheet1");
                wb.write(fos);

            }
            setLineNum();
        } catch (Exception e) {
            e.printStackTrace();

        }finally {
           try {
               if(fis != null)
                    fis.close();
               if(fos != null)
                   fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    private String getFilePath(){
        if(file != null)
             return file.getAbsolutePath();
        return null;
    }

}
