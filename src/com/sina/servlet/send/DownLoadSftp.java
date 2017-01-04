package com.sina.servlet.send;

import com.sina.tools.ZipUtil;
import com.sina.tools.sftp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * SFTP下载对账单示例
 */
public class DownLoadSftp {
	
	public static void main(String[] args) {
		
		
		/*
		 * 生产环境下，下载文件时候获取文件名的样子（经供参考）
		 
		Date nowTime = new Date();//当天时间
		Date dBefore = new Date();//前一天时间
		String datetime = null;//获取文件所用到的时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		
		
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(nowTime);//把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
		dBefore = calendar.getTime();   //得到前一天的时间
		datetime = dateFormat.format(dBefore);
		
				// 交易明细
				String jymxzjtg = datetime + "_jymx-zjtg.zip";
				// 存钱罐账户收益汇怿		
				String zhsyhzyhcqg = datetime + "_zhsyhz-yh-cqg.zip";
				// zhye-yh-cqg存钱罐账户余额及收益
				String zhyeyhcqg = datetime + "_zhye-yh-cqg.zip";
				// 账务明细-用户-存钱罐（zwmx-yh-cqg＿		
				String zwmxyhcqg = datetime + "_zwmx-yh-cqg.zip";
				// 账务明细-平台中间户（zwmx-pt-zj-rmb＿		
				String zwmxptzjrmb = datetime + "_zwmx-pt-zj-rmb.zip";
				// 账务明细-平台（zwmx-pt-rmb＿		
				String zwmxptrmb = datetime + "_wmx-pt-rmb.zip";
		*/
		
		/*
		 * 联调环境下获取的样例
		 */
		String directory = "/upload/busiexport/";//下载目录
		String downloadFile = "20150623_zhye-yh-cqg.zip";//下载的文件名
		String saveFile = "D:/test/";//存在本地的路径
		sftp s=new sftp();
		s.download(directory, downloadFile, saveFile, s.connectSFTP());
		
		ZipUtil unzip = new ZipUtil();
		unzip.unZip(saveFile+downloadFile);
		    
		
		/*
		 * 读取csv文件数据示例
		 */
		 File file = new File(saveFile+"20150623_zhye-yh-cqg_0.csv");
		 DataInputStream in;
		try {
			in = new DataInputStream(new FileInputStream(file));
		
		    BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(in,"gbk"));
		    String stemp;   
		    String str;// 一个单元格
		    List listFile = new ArrayList();
		    //如果当前行不为空
		       while((stemp = bufferedreader.readLine()) != null){  
		           Pattern pCells = Pattern
		        		      .compile("(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*,");
		            	    Matcher mCells = pCells.matcher(stemp + ",");
		        		    List cells = new ArrayList();// 每行记录一个list
		        		    // 读取每个单元格
		        		    while (mCells.find()) {
		        		     str = mCells.group();
		        		     str = str.replaceAll(
		        		       "(?sm)\"?([^\"]*(\"{2})*[^\"]*)\"?.*,", "$1");
		        		     str = str.replaceAll("(?sm)(\"(\"))", "$2");
		        		     cells.add(str);
		        		    }
		        		    listFile.add(cells);
		        		    
		        		    
		       }
		       for(int i=0;i<listFile.size();i++)
		       {
		    	   System.out.println(listFile.get(i));
		       }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}		
}
		
		
		
		
	      
		
		
		
	
	
	
	

