package com.crazyMusic.controller.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.crazyMusic.common.constant.Const;
import com.crazyMusic.common.dao.BaseJdbcDao;
import com.crazyMusic.common.oss.OSSClientUtil;
import com.crazyMusic.common.util.RequestUtils;
import com.crazyMusic.common.util.ResponseUtils;
@Controller
@RequestMapping("/base")
public class BaseController extends BaseJdbcDao{
	private static Logger logger = Logger.getLogger(BaseController.class);
	/**
	 * 成功返回模板JSON
	 * @param jsonObject
	 * @return
	 */
	public JSONObject getSuccessJSON(JSONObject jsonObject){
		JSONObject newJSON = new JSONObject();
		newJSON.put("code", Const.SUCCESS_CODE);
		newJSON.put("msg",Const.SUCCESS_MSG);
		newJSON.put("data", jsonObject==null?new JSONObject():jsonObject);
		return newJSON;
	}
	
	public JSONObject getFailJSON(String msg){
		JSONObject newJSON = new JSONObject();
		newJSON.put("msg",StringUtils.isEmpty(msg)?Const.SUCCESS_MSG:msg);
		newJSON.put("code", Const.FAIL_CODE);
		return newJSON;
	}
	
	public JSONObject putSucMapJSON(Map<String,Object> dataMap){
		JSONObject sucJson = getSuccessJSON(null);
		JSONObject dataJson = (JSONObject) sucJson.get("data");
		dataJson.putAll(dataMap);
		return sucJson;
	}
	
	public JSONObject putSucList(List<Map<String,Object>> dataList){
		JSONObject jsonObj = getSuccessJSON(null);
		jsonObj.put("data", dataList);
		return jsonObj;
	}
	
	public JSONObject putSucListJSON(List<JSONObject> dataList){
		JSONObject jsonObj = getSuccessJSON(null);
		jsonObj.put("data", dataList);
		return jsonObj;
	}
	
	
	public JSONObject getAesJsonParams(HttpServletRequest req) throws IOException{
		return RequestUtils.getAesParams(req);
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(HttpServletRequest request,HttpServletResponse response) {
		// 转型为MultipartHttpRequest(重点的所在)这个就是上面ajax中提到如果直接访问此接口而不加"/"，此转型就会报错
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        String fileName = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
        JSONObject jb = new JSONObject();
        //MultipartFile file = multipartRequest.getFiles("errPic");//获取文件集合  对应  jquery $("#imageFile").get(0).files
        // 获得第1张图片（根据前台的name名称得到上传的文件）
        MultipartFile file = multipartRequest.getFile("fileUpload"); //对应  jquery $("#imageFile").get(0).files[index]
        try {
        	String name = file.getOriginalFilename();
			InputStream is = file.getInputStream();
			String url = OSSClientUtil.uploadByStream(fileName+name, is);
			System.err.println(url);
			jb.put("src", url);    
		} catch (Exception e) {
			e.printStackTrace();
			jb = getFailJSON("上传文件失败！");
		}
        ResponseUtils.putJsonResponse(response, getSuccessJSON(jb));
    }
}
