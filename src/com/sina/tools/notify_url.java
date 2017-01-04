package com.sina.tools;

import java.io.IOException;


import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sina.tools.SignUtil;
import com.sina.tools.Tools;

/**
 * Servlet implementation class GetSignMsg
 */
public class notify_url extends HttpServlet {
	Log log = LogFactory.getLog(this.getClass());
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public notify_url() {
		super();
		// TODO Auto-generated constructor stub
	}

	SignUtil signutil = new SignUtil();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @return 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
		String sign=request.getParameter("sign");
		String sign_type = request.getParameter("sign_type");//
		String like_result = Tools.createLinkString(Tools.getParameterMap(request,true), false);//获取异步回调参数
		String _input_charset=request.getParameter("_input_charset");
		String UUID = Tools.getUUID();//日志跟踪号
		String signKey = "";
		log .info("------------------------------------------------------");
		log .info("["+UUID+"]"+"异步回调验签参数："+like_result.toString());
		log.info("["+UUID+"]"+"异步验签sign："+sign);
		
			if ("RSA".equalsIgnoreCase(sign_type.toString())) {
				//验签公钥
				signKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDv0rdsn5FYPn0EjsCPqDyIsYRawNWGJDRHJBcdCldodjM5bpve+XYb4Rgm36F6iDjxDbEQbp/HhVPj0XgGlCRKpbluyJJt8ga5qkqIhWoOd/Cma1fCtviMUep21hIlg1ZFcWKgHQoGoNX7xMT8/0bEsldaKdwxOlv3qGxWfqNV5QIDAQAB";
			}
			try {
				//对异步参数做验签
				if (SignUtil.Check_sign(like_result.toString(),sign,sign_type,signKey,_input_charset )) 
					
				{
					log .info("["+UUID+"]"+"异步验签成功验签成功，并返回success");
					response.setContentType("text/html;charset=UTF-8");
					/*
					 * 验签成功后做数据落地，数据落地成功后返回success告知sina收到通知，以免sina重复通知，浪费双方资源
					 */
					response.getWriter().print("success");
					/*
					 * 返回success后，根据产品需求以及落地相关数据信息做相应业务处理
					 */
				} else {
					response.setContentType("text/html;charset=UTF-8");
					response.getWriter().print("sign error!");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("非法请求！！！");
		}
	}
}
