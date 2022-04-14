//package com.oxo.ball.utils;
//
//import org.apache.poi.hssf.usermodel.*;
//import org.apache.poi.ss.usermodel.CellStyle;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.OutputStream;
//import java.util.Date;
//import java.util.List;
//
//public class PoiUtil {
//
//    public static void export(String time, List<WxAccountDAO> datas, HttpServletResponse response) {
//        try {
//            HSSFWorkbook wb = new HSSFWorkbook();
//            // 根据页面index 获取sheet页
//            HSSFSheet sheet = wb.createSheet(time);
//            sheet.setDefaultColumnWidth(12);
//            int i=0;
//            HSSFRow row = sheet.createRow(0);
//            row.createCell(0).setCellValue("分组名");
//            row.createCell(1).setCellValue("昵称");
//            row.createCell(2).setCellValue("好友数量");
//            row.createCell(3).setCellValue("群数量");
//            for (WxAccountDAO data:datas) {
//                // 创建HSSFRow对象
//                row = sheet.createRow(++i);
//                // 创建HSSFCell对象 设置单元格的值
//                row.createCell(0).setCellValue(data.getGroupName());
//                row.createCell(1).setCellValue(data.getNickname());
//                row.createCell(2).setCellValue(data.getBuddyCount());
//                row.createCell(3).setCellValue(data.getChatroomCount());
//            }
//            // 输出Excel文件
//            OutputStream output = response.getOutputStream();
//            response.reset();
//            // 设置文件头
//            response.setHeader("Content-Disposition",
//                    "attchement;filename=" + new String((time + ".xls").getBytes("gb2312"), "ISO8859-1"));
//            response.setContentType("application/msexcel");
//            wb.write(output);
//            wb.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public static void exportBuddyData(String time, WxBuddyDataService wxBuddyDataService,Long groupId,String sex,Integer status, HttpServletResponse response) {
//        try {
//            HSSFWorkbook wb = new HSSFWorkbook();
//            // 根据页面index 获取sheet页
//            HSSFSheet sheet = wb.createSheet(time);
//            sheet.setDefaultColumnWidth(12);
//            int i=0;
//            HSSFRow row = sheet.createRow(0);
//            row.createCell(0).setCellValue("手机号");
//            row.createCell(1).setCellValue("是否使用");
//            row.createCell(2).setCellValue("执行微信");
//            row.createCell(3).setCellValue("使用状态");
//            HSSFCell cell = row.createCell(4);
//            cell.setCellValue("使用时间");
//            row.createCell(5).setCellValue("昵称");
//            row.createCell(6).setCellValue("性别");
//            row.createCell(7).setCellValue("标签");
//            row.createCell(8).setCellValue("验证状态");
//            int pageNo = 1;
//            while(true){
//                SearchResponse<WxBuddyDataDAO> search = wxBuddyDataService.search(WxBuddyDataDAO.builder().groupId(groupId).sex(sex).status(status).build(), pageNo++, 100);
//                if(search.getResults()==null||search.getResults().isEmpty()){
//                    break;
//                }
//                for (WxBuddyDataDAO data:search.getResults()) {
//                    // 创建HSSFRow对象
//                    row = sheet.createRow(++i);
//                    // 创建HSSFCell对象 设置单元格的值
//                    row.createCell(0).setCellValue(data.getMobile());
//                    row.createCell(1).setCellValue(data.getStatus());
//                    row.createCell(2).setCellValue(data.getWxid());
//                    row.createCell(3).setCellValue(data.getErrorMsg());
//                    row.createCell(4).setCellValue(data.getTimeString());
//                    row.createCell(5).setCellValue(data.getNickname());
//                    row.createCell(6).setCellValue(data.getSex());
//                    row.createCell(7).setCellValue(data.getRemark());
//                    row.createCell(8).setCellValue(data.getValidStatusStr());
//                }
//            }
//            for(int j=0;j<7;j++){
//                sheet.autoSizeColumn(j);
//            }
//            // 输出Excel文件
//            OutputStream output = response.getOutputStream();
//            response.reset();
//            // 设置文件头
//            response.setHeader("Content-Disposition",
//                    "attchement;filename=" + new String((time + ".xls").getBytes("gb2312"), "ISO8859-1"));
//            response.setContentType("application/msexcel");
//            wb.write(output);
//            wb.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//
//    }
//}
