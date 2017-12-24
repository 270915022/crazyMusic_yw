package com.crazyMusic.controller.user;

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
 * 用户
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController{
	
	/**
	 * 用户列表
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/list")
    @ResponseBody
    public void listProduct(HttpServletRequest req,HttpServletResponse resp){
		List<JSONObject> list = queryForJsonList("select * from user");
		ResponseUtils.putJsonResponse(resp, list);
    }
	
	/**
	 * 用户详情
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/userDetail")
	@ResponseBody
	public void userDetail(HttpServletRequest req,HttpServletResponse resp){
		String id = req.getParameter("id");
		if(StringUtils.isEmpty(id)) {
			ResponseUtils.putJsonResponse(resp, getFailJSON("id 不能为空"));
		}
		JSONObject jb = queryForJsonObject("select * from user where id = ?", id);
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(jb));
	}
	
	
	/**
	 * 用户保存
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/userSave")
	@ResponseBody
	public void tagSave(HttpServletRequest req,HttpServletResponse resp){
		String username = req.getParameter("username");
		String phone = req.getParameter("phone");
		String id = req.getParameter("id");
		if(StringUtils.isEmpty(username) || StringUtils.isEmpty(phone)) {
			ResponseUtils.putJsonResponse(resp, getFailJSON("用户名或手机号不能为空！"));
			return ;
		}
		JSONObject usernameJSON = queryForJsonObject("select * from user where username = ?", username);
		JSONObject phoneJSON = queryForJsonObject("select * from user where phone = ?", phone);
		if(StringUtils.isEmpty(id)) {
			if(usernameJSON != null) {
				ResponseUtils.putJsonResponse(resp, getFailJSON("用户名已存在！"));
				return ;
			}
			if(phoneJSON != null) {
				ResponseUtils.putJsonResponse(resp, getFailJSON("手机号已存在！"));
				return ;
			}
		}
		
		String password = req.getParameter("password");
		String nick_name = req.getParameter("nick_name");
		String role_state = req.getParameter("role_state");
		String regist_way = req.getParameter("regist_way");
		String headImg = req.getParameter("headImg");
		String status = req.getParameter("status");
		String sign = req.getParameter("sign");
		
		if(StringUtils.isEmpty(id)) {
			execute("insert into user (id,username,password,phone,status,regist_date,role_state,regist_way,head_img,sign,nick_name) values (?,?,?,?,?,?,?,?,?,?,?) ", generateKey(),username,password,phone
					,status,new Date(),role_state,regist_way,headImg,sign,nick_name);
		}else {
			execute("update user set username=?,password=?,phone=?,status=?,role_state=?,regist_way=?,head_img=?,sign=?,nick_name=? where id = ? ",username,password,phone
					,status,role_state,regist_way,headImg,sign,nick_name,id);
		}
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(null));
	}
	
	/**
	 * 用户删除
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/userDelete")
	@ResponseBody
	public void tagDelete(HttpServletRequest req,HttpServletResponse resp){
		String id = req.getParameter("id");
		if(StringUtils.isEmpty(id)) {
			ResponseUtils.putJsonResponse(resp, getFailJSON("id 不能为空"));
		}
		execute("update user set del_flag = 0 where id = ? ", id);
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(null));
	}
	
	
	
	/**
	 * 审核列表
	 */
	@RequestMapping("/teacherExamine")
	@ResponseBody
	public void teacherExamine(HttpServletRequest req,HttpServletResponse resp){
		List<JSONObject> list = queryForJsonList("select * from user_teacher_longevity order by FIELD('state',0),create_date");
		ResponseUtils.putJsonResponse(resp, list);
	}
	/**
	 * 审核详情
	 */
	@RequestMapping("/teacherExamineDetail")
	@ResponseBody
	public void teacherExamineDetail(HttpServletRequest req,HttpServletResponse resp){
		String id = req.getParameter("id");
		JSONObject jb = queryForJsonObject("select * from user_teacher_longevity where id = ?", id);
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(jb));
	}
	/**
	 * 审核
	 */
	@RequestMapping("/examine")
	@ResponseBody
	public void examine(HttpServletRequest req,HttpServletResponse resp){
		String id = req.getParameter("id");
		String state = req.getParameter("state");
		String error_msg = req.getParameter("error_msg");
		int execute = execute("update user_teacher_longevity set state=?,error_msg=? where id = ?", state,error_msg,id);
		if(execute <= 0) {
			ResponseUtils.putJsonResponse(resp, getFailJSON("审核失败！"));
			return;
		}
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(null));
	}
	
}
