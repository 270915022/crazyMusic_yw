package com.crazyMusic.controller.conmmunity;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.crazyMusic.common.util.ResponseUtils;
import com.crazyMusic.controller.base.BaseController;
@Controller
@RequestMapping("/community")
public class CommunityController extends BaseController{
	
	/**
	 * 帖子列表
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/list")
    @ResponseBody
    public void listProduct(HttpServletRequest req,HttpServletResponse resp){
		List<JSONObject> queryForJsonList = queryForJsonList("select * from community order by create_date desc");
		ResponseUtils.putJsonResponse(resp, queryForJsonList);
    }
	
	/**
	 * 帖子详情
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/communityDetail")
	@ResponseBody
	public void communityDetail(HttpServletRequest req,HttpServletResponse resp){
		JSONObject comm = queryForJsonObject("select * from community where id = ?",req.getParameter("id"));
		if(comm != null &&!comm.isEmpty()) {
			List<JSONObject> imgList = queryForJsonList("select * from community_file where community_id = ?",comm.getString("id"));
			comm.put("imgList", imgList);
		}
		ResponseUtils.putJsonResponse(resp, comm);
	}
	
	/**
	 * 商品列表
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/updataStatus")
	@ResponseBody
	public void updataStatus(HttpServletRequest req,HttpServletResponse resp){
		String commId = req.getParameter("id");
		String status = req.getParameter("status");
		execute("update community set community_status = ? where id = ?", status,commId);
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(null));
	}
}
