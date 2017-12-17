package com.crazyMusic.common.constant;

/**
 * 常量类
 * @author hsq
 *
 */
public class Const {
	
	//商品详情开始节点html
	public static String PRODUCT_STARTHTML = "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body>";
	
	//商品详情结束节点html
	public static String PRODUCT_ENDHTML = "</body></html>";
	
	 /** 通用字符集编码 */
    public static final String CHARSET_UTF8 = "UTF-8";

    /** 中文字符集编码 */
    public static final String CHARSET_CHINESE = "GBK";

    /** 英文字符集编码 */
    public static final String CHARSET_LATIN = "ISO-8859-1";
    
    /** 日期格式 */
    public static final String FORMAT_DATE = "yyyy-MM-dd";

    /** 日期时间格式 */
    public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

    /** 时间戳格式 */
    public static final String FORMAT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss.SSS";
    
    /**
     * 返回结果code约定
     */
    public static final int SUCCESS_CODE = 0;
    
    public static final int FAIL_CODE = -1;
    
    public static final String SUCCESS_MSG = "成功";
    
    public static final String FAIL_MSG = "失败";
    
  //#########阿里云oss配置开始#####################
    public static String ENDPOINT;
    
    public static String ACCESSKEYID;
    
    public static String ACCESSKEYSECRT;
    
    public static String BUCKETNAME;
    
    //#########阿里云oss配置结束#####################
    
    public static String AESKEY = "";
    
    //###########短信配置常量开始#########################
     public static String msgUrl = "";
     
     public static String Uid = "";
     
     public static String Key = "";
     
   //###########短信配置常量结束#########################
     
   //###########商品图片类别开始#########################
     
     public static String PRODUCT_IMG_MAIN = "1";//商品展示图
     
   //###########商品图片类别结束#########################
     
     /**
      * 商城配置
      */
     //购物车失效时间 分钟
     public static int CARD_EXPIRE_TIMEOUT = 1000;
     
     //物流名称
     public static String LOGISTICAL_NAME = "";
     
     //订单状态
     public static int ORDER_STATE_NOTPAY = 1;//未付款
     public static int ORDER_STATE_PAY = 2;//已付款
     public static int ORDER_STATE_NOTSEND = 3;//未发货
     public static int ORDER_STATE_SEND = 4;//已发货
     public static int ORDER_STATE_CLOSE = 5;//交易关闭
     public static int ORDER_STATE_COMPLETE = 6;//交易完成
     
     
}
