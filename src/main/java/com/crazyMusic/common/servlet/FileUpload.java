package com.crazyMusic.common.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.internal.OSSUtils;
import com.crazyMusic.common.oss.OSSClientUtil;

public class FileUpload extends HttpServlet {  
  
    public FileUpload() {  
        super();  
    }  
  
    public void destroy() {  
        super.destroy();   
    }  
    public void doGet(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {  
    	doPost(request, response);
  
    }  
    public void doPost(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {  
        response.setContentType("text/html");     
        response.setCharacterEncoding("UTF-8");  
        String realDir = request.getSession().getServletContext().getRealPath("");  
        try {  
        String filePath = "uploadfiles";  
        String realPath = realDir+"\\"+filePath;  
        //判断路径是否存在，不存在则创建  
        File dir = new File(realPath);  
        if(!dir.isDirectory())  
            dir.mkdir();  
  
        if(ServletFileUpload.isMultipartContent(request)){  
  
            DiskFileItemFactory dff = new DiskFileItemFactory();  
            dff.setRepository(dir);  
            dff.setSizeThreshold(1024000);  
            ServletFileUpload sfu = new ServletFileUpload(dff);  
            FileItemIterator fii = null;  
            fii = sfu.getItemIterator(request);  
            String url = "";    //图片地址 
            String fileName = null;
            while(fii.hasNext()){  
                FileItemStream fis = fii.next();
                fileName = UUID.randomUUID().toString().replaceAll("-", "").substring(2, 10)+fis.getName();
                try{  
                    if(!fis.isFormField() && fis.getName().length()>0){  
                        BufferedInputStream in = new BufferedInputStream(fis.openStream());//获得文件输入流 
                        FileOutputStream osm = new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\新建文件夹\\"+fileName));
                        InputStream is = in;

                        //FileOutputStream fos = new FileOutputStream("b.txt");

                        byte[] b = new byte[1024];

                        while((is.read(b)) != -1){

                        	osm.write(b);

                        }

                        is.close();
                        osm.close();
                        //url = OSSClientUtil.uploadByStream(fileName, in);;
                    }else{
                    	
                    }  
  
                }catch(Exception e){  
                    e.printStackTrace();  
                }  
            }
            JSONObject jb = new JSONObject();
            jb.put("src", url);
            jb.put("status", "success");
           // response.getWriter().write(retxt);
            response.getWriter().print(jb.toJSONString());
        }  
        }catch(Exception ee) {  
            ee.printStackTrace();  
        }  
          
    }  
    public void init() throws ServletException {  
        // Put your code here  
    }  
  
}  