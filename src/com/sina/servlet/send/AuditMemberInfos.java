package com.sina.servlet.send;
 
 import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.sina.tools.Tools;
import com.sina.tools.ZipUtil;
import com.sina.tools.sftp;

 public class AuditMemberInfos extends HttpServlet {
 
     public void doGet(HttpServletRequest request, HttpServletResponse response)
             throws ServletException, IOException {
                 //得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
                 
                 Date now = new Date(); 
             	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");//可以方便地修改日期格式
                 String hehe = dateFormat.format( now );//获取的当前时间
                 String savePath = hehe;//资质图片所放置的文件夹路径
                 System.out.println(savePath);
                 String zipPath = savePath+".zip";//压缩包路劲
                     /*
                      * 创建目录
                      */
                 File file = new File(savePath);
                 if (!file.exists() && !file.isDirectory()) {
                	                      System.out.println(savePath+"目录不存在，需要创建");
                	                      //创建目录
                	                      file.mkdir();
                	                  }
                 
                 String filename = null;
                 try{
                     //使用Apache文件上传组件处理文件上传步骤：
                     //1、创建一个DiskFileItemFactory工厂
                     DiskFileItemFactory factory = new DiskFileItemFactory();
                     //2、创建一个文件上传解析器
                     ServletFileUpload upload = new ServletFileUpload(factory);
                      //解决上传文件名的中文乱码
                     upload.setHeaderEncoding("UTF-8"); 
                     //3、判断提交上来的数据是否是上传表单的数据
                     if(!ServletFileUpload.isMultipartContent(request)){
                         //按照传统方式获取数据
                         return;
                     }
                     //4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
                     List<FileItem> list = upload.parseRequest(request);
                     for(FileItem item : list){
                         //如果fileitem中封装的是普通输入项的数据
                         if(item.isFormField()){
                             String name = item.getFieldName();
                             //解决普通输入项的数据的中文乱码问题
                             String value = item.getString("UTF-8");
                         }else{//如果fileitem中封装的是上传文件
                             //得到上传的文件名称，
                             filename = item.getName();
                             System.out.println("filename:"+filename);
                             if(filename==null || filename.trim().equals("")){
                                 continue;
                             }
                             //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
                             //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
                             filename = filename.substring(filename.lastIndexOf("\\")+1);
                             //获取item中的上传文件的输入流
                             InputStream in = item.getInputStream();
                             //创建一个文件输出流
                             FileOutputStream out = new FileOutputStream(savePath + "\\" + filename);
                             //创建一个缓冲区
                             byte buffer[] = new byte[1024];
                             //判断输入流中的数据是否已经读完的标识
                             int len = 0;
                             //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
                             while((len=in.read(buffer))>0){
                                 //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
                                 out.write(buffer, 0, len);
                             }
                             //关闭输入流
                             in.close();
                             //关闭输出流
                             out.close();
                             //删除处理文件上传时生成的临时文件
                             item.delete();
                         }
                     }
                 }catch (Exception e) {
                     e.printStackTrace();
                     
                 }
                 /*
                  * 上传到sftp
                  */
                 StringBuilder sb = new StringBuilder();//拼接提交的参数
                 sb.append("{");
                 
                 
                 //压缩文件夹
                 ZipUtil zip = new ZipUtil();
                 zip.doZip(savePath);
                 
                 
                 
                
                 File File = new File(zipPath);
         		String digest=Tools.getFileMD5(File);//文件摘要计算
         		
         		
         		String fileName = hehe+".zip";//指定需要上传的压缩包
         		System.out.println("获得压缩包文件："+fileName);
        		System.out.println("-------------------------start------------------------------");
        		System.out.println("开始连接sftp");
            	sftp s=new sftp();
        		s.upload("/upload/",zipPath,s.connectSFTP());
        			sb.append("\"upresult\":\"" + "success" + "\",");
                    sb.append("\"fileName\":\"" + fileName + "\",");
                    sb.append("\"digest\":\"" + digest + "\""); 
                    
                    System.out.println("sftp上传完成");
            		System.out.println("-------------------------end------------------------------");
        		
        		sb.append("}");
        		System.out.println(sb);
        		response.setContentType("text/html;charset=UTF-8");
    			response.getWriter().print(sb);    
                 
                 
     }
 
     public void doPost(HttpServletRequest request, HttpServletResponse response)
             throws ServletException, IOException {
 
         doGet(request, response);
     }
     
     
     
     
    
     
     
 }