package com.crazyMusic.common.oss;

import java.io.InputStream;

import com.aliyun.openservices.oss.OSSClient;
import com.crazyMusic.common.constant.Const;
import com.qikemi.packages.alibaba.aliyun.oss.ObjectService;

/**
 * 阿里云oss操作
 * @author Administrator
 *
 */
public class OSSClientUtil {
	
	private static OSSClient instance;
    private OSSClientUtil (){
    }   
    public static OSSClient getInstance(){    //对获取实例的方法进行同步
      if (instance == null){
          synchronized(OSSClientUtil.class){
              if (instance == null)
                  instance = new OSSClient("http://"+Const.ENDPOINT,Const.ACCESSKEYID,Const.ACCESSKEYSECRT); 
          }
      }
      return instance;
    }
    
    /**
     * 上传流
     * @param key
     * @param ins
     * @throws Exception http://crazymusic-bucket.oss-cn-shenzhen.aliyuncs.com/u=594559231,2167829292&fm=27&gp=0.jpg
     */
    public static String uploadByStream(String key,InputStream ins) throws Exception {
    	if(ins == null)
    		throw new Exception("输入流不能为空！");
    	ObjectService.putObject(getInstance(), Const.BUCKETNAME, key,ins);
    	String url = "http://" + Const.BUCKETNAME + "." + Const.ENDPOINT + "/" + key;
    	return url;
    }
    
}
