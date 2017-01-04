package com.sina.servlet.send;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sina.tools.CallServiceUtil;
import com.sina.tools.GsonUtil;
import com.sina.tools.SignUtil;
import com.sina.tools.Tools;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *Servlet implementation class Send
 */
public class Send extends HttpServlet {
	Log log = LogFactory.getLog(this.getClass());//日志记录在tomcat下logs文件内
	
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Send() {
		super();
		// TODO Auto-generated constructor stub
	}

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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log .info("-----------------------------------------------------------------");
		// TODO Auto-generated method stub
		String _input_charset = request.getParameter("_input_charset");//字符编码集
		String sign_type = request.getParameter("sign_type");//签名类型
		String service = request.getParameter("service");//接口名称
		String url = Tools.get_url(service);//遍历接口名，选取对应的请求网关地址
		String UUID = Tools.getUUID();//日志跟踪号
		//加密密钥
		String encrypt = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBpueNweMbYdb+CMl8dUNv5g5THYLD9Z33cAMA4GNjmPYsbcNQLyO5QSlLNjpbCwopt7b5lFP8TGLUus4x0Ed6S4Wd9KmNw6NLbszNEmppP9HXlT9sT4/ShL0CpVF4ofFS8O/gXwCTJjYZJ0HvK3GBTSP2C9WlipTpWQ+9QJugewIDAQAB";
//		String encrypt=Tools.getKey("rsa_public.pem"); 另一种是直接取文件，路径要传对
		Map<String, String> param1 = null;
		param1 = Tools.getParameterMap(request,true);//获取前端数据
		
		//去除map中为空参数
		 Iterator<Map.Entry<String, String>> it = param1.entrySet().iterator();  
	        while(it.hasNext()){  
	            Map.Entry<String, String> entry=it.next();  
	            String value=entry.getValue(); 
	            String key=entry.getKey();
	            try {
	            	//value不能是null，否则equals会抛错
	            	if(value==null||value==""||value.equals("")){  
	            		//如果value是空值则删除这个key
		                System.out.println("delete this: "+key+" = "+value);  
		                
		                it.remove();        
		            }
				} catch (Exception e) {
					e.printStackTrace();
				}
	              
	        } 
	        
	      //加密部分
	        param1 = Tools.toBeEncrypt(request, param1);

	        
	        //加签
		String content = Tools.trimInnerSpaceStr(Tools.createLinkString(param1, false));
		log .info("["+UUID+"]"+"加签参数："+content);
		String signKey = null;
		String sign=null;
		if("RSA".equalsIgnoreCase(sign_type)){
			//加签密钥
//			signKey = Tools.getKey("rsa_sign_private.pem");
			signKey="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAO/6rPCvyCC+IMalLzTy3cVBz/+wamCFNiq9qKEilEBDTttP7Rd/GAS51lsfCrsISbg5td/w25+wulDfuMbjjlW9Afh0p7Jscmbo1skqIOIUPYfVQEL687B0EmJufMlljfu52b2efVAyWZF9QBG1vx/AJz1EVyfskMaYVqPiTesZAgMBAAECgYEAtVnkk0bjoArOTg/KquLWQRlJDFrPKP3CP25wHsU4749t6kJuU5FSH1Ao81d0Dn9m5neGQCOOdRFi23cV9gdFKYMhwPE6+nTAloxI3vb8K9NNMe0zcFksva9c9bUaMGH2p40szMoOpO6TrSHO9Hx4GJ6UfsUUqkFFlN76XprwE+ECQQD9rXwfbr9GKh9QMNvnwo9xxyVl4kI88iq0X6G4qVXo1Tv6/DBDJNkX1mbXKFYL5NOW1waZzR+Z/XcKWAmUT8J9AkEA8i0WT/ieNsF3IuFvrIYG4WUadbUqObcYP4Y7Vt836zggRbu0qvYiqAv92Leruaq3ZN1khxp6gZKl/OJHXc5xzQJACqr1AU1i9cxnrLOhS8m+xoYdaH9vUajNavBqmJ1mY3g0IYXhcbFm/72gbYPgundQ/pLkUCt0HMGv89tn67i+8QJBALV6UgkVnsIbkkKCOyRGv2syT3S7kOv1J+eamGcOGSJcSdrXwZiHoArcCZrYcIhOxOWB/m47ymfE1Dw/+QjzxlUCQCmnGFUO9zN862mKYjEkjDN65n1IUB9Fmc1msHkIZAQaQknmxmCIOHC75u4W0PGRyVzq8KkxpNBq62ICl7xmsPM=";
			
			}
			try {
				//计算签名
				sign = SignUtil.sign(content, sign_type, signKey,
						_input_charset);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			log .info("["+UUID+"]"+"算出的签名sign是："+sign);
		param1.put("sign", sign);
		param1.put("sign_type", sign_type);
		String param = Tools.createLinkString(param1,true);
		log .info("["+UUID+"]"+"提交sina的请求参数："+param);
		//获取同步响应
		String result = URLDecoder.decode(
				CallServiceUtil.sendPost(url, param), _input_charset);
		log .info("["+UUID+"]"+"同步响应："+result);
		try {
			
			Map<String, String> content2 = GsonUtil.fronJson2Map(result);
			if("RSA".equalsIgnoreCase(content2.get("sign_type").toString())){
				//验签公钥
//				signKey = Tools.getKey("rsa_sign_public.pem");
				signKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDv0rdsn5FYPn0EjsCPqDyIsYRawNWGJDRHJBcdCldodjM5bpve+XYb4Rgm36F6iDjxDbEQbp/HhVPj0XgGlCRKpbluyJJt8ga5qkqIhWoOd/Cma1fCtviMUep21hIlg1ZFcWKgHQoGoNX7xMT8/0bEsldaKdwxOlv3qGxWfqNV5QIDAQAB";
			}
			String sign_result = content2.get("sign").toString();
			String sign_type_result = content2.get("sign_type").toString();
			String _input_charset_result = content2.get("_input_charset")
					.toString();
			content2.remove("sign");
			content2.remove("sign_type");
			content2.remove("sign_version");
			
			//去除map中为空参数
			 Iterator<Map.Entry<String, String>> it2 = content2.entrySet().iterator();  
		        while(it2.hasNext()){  
		            Map.Entry<String, String> entry2=it2.next();  
		            String value2=entry2.getValue(); 
		            String key=entry2.getKey();
		            //value不能是null，否则equals会抛错
		            if(value2==null||value2==""||value2.equals("")){  
		                System.out.println("delete this: "+key+" = "+value2);  
		                
		                it2.remove();        
		            }  
		        } 
			String like_result = Tools.trimInnerSpaceStr(Tools.createLinkString(content2,false));
			//对同步响应做验签
			log .info("["+UUID+"]"+"同步验签参数："+like_result);
			log .info("["+UUID+"]"+"同步响应sign："+sign_result);
			if (SignUtil.Check_sign(like_result.toString(), sign_result,
					sign_type_result, signKey, _input_charset_result)) {
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().print(result);
				log .info("["+UUID+"]"+"同步验签成功");
			} else {
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().print("sign error!");
			}
		} catch (Exception e) {
			//获取返回的表单
			// TODO Auto-generated catch block
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().print(result);
		}
	}
	
}
