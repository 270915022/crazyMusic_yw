package com.crazyMusic.controller.system;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.crazyMusic.common.util.ResponseUtils;
import com.crazyMusic.controller.base.BaseController;

/**
 * banner管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/banner")
public class BannerController extends BaseController{
	
	
	/**
	 * banner列表
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/list")
    @ResponseBody
    public void listProduct(HttpServletRequest req,HttpServletResponse resp){
		List<JSONObject> list = queryForJsonList("select * from banner");
		ResponseUtils.putJsonResponse(resp, list);
    }
	
	
	/**
	 * banner详情
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/bannerDetail")
	@ResponseBody
	public void bannerDetail(HttpServletRequest req,HttpServletResponse resp){
		String id = req.getParameter("id");
		JSONObject data = queryForJsonObject("select * from banner where id = ?",id);
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(data));
	}
	/**
	 * banner删除
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/bannerDelete")
	@ResponseBody
	public void bannerDelete(HttpServletRequest req,HttpServletResponse resp){
		String id = req.getParameter("id");
		int execute = execute("delete from banner where id = ?",id);
		if(execute <= 0) {
			ResponseUtils.putJsonResponse(resp, getFailJSON("删除失败"));
			return;
		}
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(null));
	}
	
	/**
	 * banner 保存
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/save")
	@ResponseBody
	public void save(HttpServletRequest req,HttpServletResponse resp){
		String id = req.getParameter("id");
		String name = req.getParameter("name");
		String img = req.getParameter("img");
		String url = req.getParameter("url");
		String skip_type = req.getParameter("skip_type");
		String inside_type = req.getParameter("inside_type");
		String outside_type = req.getParameter("outside_type");
		String effective_time = req.getParameter("effective_time");
		String lose_effective_time = req.getParameter("lose_effective_time");
		int execute = 0;
		if(StringUtils.isEmpty(id)) {
			execute = execute("insert into banner (id,name,img,url,skip_type,inside_type,outside_type,create_date,effective_time,lose_effective_time) "
					+ "values (?,?,?,?,?,?,?,?,?,?)",generateKey(),name,img,url,skip_type,inside_type,outside_type,new Date(),effective_time,lose_effective_time);
		}else {
			execute = execute("update banner set name=?,img=?,url=?,skip_type=?,inside_type=?,outside_type=?,effective_time=?,lose_effective_time=? where id = ?",
					name,img,url,skip_type,inside_type,outside_type,effective_time,lose_effective_time,id);
		}
		if(execute <= 0) {
			ResponseUtils.putJsonResponse(resp, getFailJSON("添加失败"));
			return;
		}
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(null));
	}
}
