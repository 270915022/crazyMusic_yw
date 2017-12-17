package com.baidu.ueditor.upload;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.model.Bucket;
import com.baidu.ueditor.define.State;
import com.qikemi.packages.alibaba.aliyun.oss.BucketService;
import com.qikemi.packages.alibaba.aliyun.oss.OSSClientFactory;
import com.qikemi.packages.alibaba.aliyun.oss.properties.OSSClientProperties;
import com.qikemi.packages.baidu.ueditor.upload.AsynUploaderThreader;
import com.qikemi.packages.baidu.ueditor.upload.SynUploader;
import com.qikemi.packages.utils.SystemUtil;

/**
 * 同步上传文件到阿里云OSS<br>
 * 
 * @author XieXianbin me@xiexianbin.cn
 */
public class Uploader {
	private static Logger logger = Logger.getLogger(Uploader.class);

	private HttpServletRequest request = null;
	private Map<String, Object> conf = null;

	public Uploader(HttpServletRequest request, Map<String, Object> conf) {
		this.request = request;
		this.conf = conf;
	}

	public final State doExec() {
		State state = null;
		state = BinaryUploader.save(this.request, this.conf);
		JSONObject stateJson = new JSONObject(state.toJSONString());
		String bucketName = OSSClientProperties.bucketName;
		OSSClient client = OSSClientFactory.createOSSClient();
		if (OSSClientProperties.autoCreateBucket) {
			Bucket bucket = BucketService.create(client, bucketName);
			logger.debug("Bucket 's " + bucket.getName() + " Created.");
		}
		// 判别云同步方式
		if (OSSClientProperties.useAsynUploader) {
			AsynUploaderThreader asynThreader = new AsynUploaderThreader();
			asynThreader.init(stateJson, client, this.request);
			Thread uploadThreader = new Thread(asynThreader);
			uploadThreader.start();
		}else {
			SynUploader synUploader = new SynUploader();
			synUploader.upload(stateJson, client, this.request);
		}
		// storage type
		if (false == OSSClientProperties.useLocalStorager) {
			String uploadFilePath = (String) this.conf.get("rootPath") + (String) stateJson.get("url");
			File uploadFile = new File(uploadFilePath);
			if (uploadFile.isFile() && uploadFile.exists()) {
				uploadFile.delete();
			}
		}
		String url = "http://"+OSSClientProperties.bucketName+"."+OSSClientProperties.preEndPoint + stateJson.getString("url");
		state.putInfo(
				"url",
				url);
		
		logger.debug(state.toJSONString());
		return state;
	}
}
