//package com.wanou.system.util;
//
//import com.wanou.system.administrator.adminstrator;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import java.io.FileInputStream;
//import java.io.InputStream;
//import java.util.HashMap;
//import java.util.Map;
//
//public class LoadUtils implements ServletContextListener {
//
//	//存放产品信息
//	private static Map<String, adminstrator> productsDB=new HashMap<String, adminstrator>();
//
//	//获取信息
//	public static adminstrator getProductTable(String pkey){
//		if (null==pkey || pkey.equals("")) {
//			return null;
//		}
//
//		return productsDB.get(pkey);
//	}
//
//	public void contextDestroyed(ServletContextEvent arg0) {
//
//	}
//
//	public void contextInitialized(ServletContextEvent arg0) {
//		 String webroot = arg0.getServletContext().getRealPath("products.xml");
//		 if (webroot!=null&&webroot.indexOf("products.xml")!=-1) {
//			 LoadData(webroot);
//		 }
//	}
//
//	private void LoadData(String path){
//		DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
//		   try {
//		    DocumentBuilder dombuilder = domfac.newDocumentBuilder();
//
//		    InputStream is = new FileInputStream(path);
//		    Document doc = dombuilder.parse(is);
//		    Element root = doc.getDocumentElement();
//		    NodeList products = root.getChildNodes();
//		    if (products != null) {
//		     for (int i = 0; i < products.getLength(); i++) {
//		    	 	Node product=products.item(i);
//
//		    	 	if (product.getNodeType()>0) {
//		    	 		String nodeName=product.getNodeName();
//		    	 		if (nodeName.equals("product")) {
//		    	 			String pname=product.getAttributes().getNamedItem("name").getNodeValue();
//		    	 			String pcode=product.getAttributes().getNamedItem("code").getNodeValue();
//		    	 			String pmodule=product.getAttributes().getNamedItem("module").getNodeValue();
//							adminstrator pt=new adminstrator(pname,pcode,pmodule);
//		    	 			this.productsDB.put(pcode, pt);
//						}
//
//					}
//		     	}
//		     }
//		   }catch (Exception e) {
//			   System.out.println(" 配置文件加载出错："+e);
//		}
//
//
//	}
//
//
//}
