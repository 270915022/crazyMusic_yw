package com.crazyMusic.common.listener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.crazyMusic.common.constant.Const;
import com.crazyMusic.common.util.PropertiesUtil;

/**
 * 初始化config
 * @author Administrator
 *
 */
public class InitConfig {
	private static Logger logger = Logger.getLogger(InitConfig.class);
	
	public static boolean initConf() {
		InputStream ins = InitConfig.class.getClassLoader().getResourceAsStream("config.properties");
		Properties props = new Properties();
		try {
			props.load(ins);
			String aesKey = props.getProperty("aeskey");
			Const.AESKEY = aesKey;
			Const.Key = props.getProperty("Key");
			Const.Uid = props.getProperty("Uid");
			Const.msgUrl = props.getProperty("msgUrl");
			Const.ENDPOINT = props.getProperty("endpoint");
			Const.ACCESSKEYID = props.getProperty("accessKeyId");
			Const.ACCESSKEYSECRT = props.getProperty("accessKeySecret");
			Const.BUCKETNAME = props.getProperty("bucketName");
			Const.LOGISTICAL_NAME = props.getProperty("logistics_name");
			logger.info("--------初始化配置完成");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
