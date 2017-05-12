//package com.example.util;
//
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class GetConnect {
//    public static Connection getConn() {
//    	
//        String driver = "com.mysql.jdbc.Driver";
//        String url = "jdbc:mysql://localhost:3306/BeautyProduct";
//        String username = "devep";
//        String password = "development@hufu";
//        Connection conn = null;
//        try {
//            Class.forName(driver); 
//            conn = (Connection) DriverManager.getConnection(url, username, password);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return conn;
//
//    }
//    
//    public static int insertProduct(BeautyProduct beautyProduct) {
//    	System.out.println("正在保存"+beautyProduct.getName()+"信息.....");
//        Connection conn = getConn();
//        int i = 0;
//        String sql = "insert into BeautyProduct values(?,?,?,?,?,?,?,?,?)";
//        PreparedStatement ps;
//        try {
//				ps = (PreparedStatement) conn.prepareStatement(sql);
//				ps.setString(1, beautyProduct.getName());
//				ps.setFloat(2,  beautyProduct.getPrice());
//				ps.setInt(3,    beautyProduct.getExpertRating());
//				ps.setInt(4,    beautyProduct.getCommunityRating());
//				ps.setString(5, beautyProduct.getEXPERTREVIEWS());
//				ps.setString(6, beautyProduct.getCOMMUNITYREVIEWS());
//				ps.setString(7, beautyProduct.getCLAIMS());
//				ps.setString(8, beautyProduct.getINGREDIENTS());
//				ps.setString(9, beautyProduct.getBRANDOVERVIEW());
//            i = ps.executeUpdate();
//            ps.close();
//            conn.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return i;
//    }
//    /**
//     * 保存目录对像
//     * @param beautyCategory
//     * @return
//     */
//    public static int insertCategory(BeautyCategory beautyCategory) {
//    	System.out.println("正在保存"+beautyCategory.getCategoryName()+"信息.....");
//        Connection conn = getConn();
//        int i = 0;
//        String sql = "insert into BeautyCategory values(?,?,?,?)";
//        PreparedStatement ps;
//        try {
//				ps = (PreparedStatement) conn.prepareStatement(sql);
//				ps.setInt(1,  0);
//				ps.setString(2, beautyCategory.getCategoryName());
//				ps.setString(3, beautyCategory.getUrl());
//				ps.setInt(4, beautyCategory.getType().ordinal());
//            i = ps.executeUpdate();
//            ps.close();
//            conn.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return i;
//    }
//    /**
//     * 通过id查
//     * @param CategoryId
//     */
//    public static String GetCategoryByName(int  CategoryId) {
//    	System.out.println("正在查询"+CategoryId+"信息.....");
//        Connection conn = getConn();
//        String url = "";
//        String sql = "select * from  BeautyCategory where CategoryId = ?";
//        PreparedStatement ps;
//        try {
//				ps = (PreparedStatement) conn.prepareStatement(sql);
//				ps.setInt(1, CategoryId);
//		        ResultSet rs = ps.executeQuery();
//		        while (rs.next()) {
//		            url = rs.getString("url");    
//		           }
//            ps.close();
//            conn.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return url;
//    }
//}