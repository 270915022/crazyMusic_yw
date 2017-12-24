package com.crazyMusic.controller.mall;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.crazyMusic.common.util.ResponseUtils;
import com.crazyMusic.controller.base.BaseController;
/**
 * 优惠券
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/coupon")
public class CouponController extends BaseController{
	/**
	 * 优惠券列表
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/list")
    @ResponseBody
    public void listProduct(HttpServletRequest req,HttpServletResponse resp){
		List<JSONObject> list = queryForJsonList("select * from coupon");
		ResponseUtils.putJsonResponse(resp, list);
    }
	
	/**
	 * 发布优惠券
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/publishCoupon")
	@ResponseBody
	public void publishCoupon(HttpServletRequest req,HttpServletResponse resp){
		String name = req.getParameter("name");
		String payment = req.getParameter("payment");
		String expireTime = req.getParameter("expireTime");
		String publishTime = req.getParameter("createTime");
		String publish_stop_date = req.getParameter("publish_stop_date");
		String number = req.getParameter("number");
		String chance = req.getParameter("chance");
		String img = req.getParameter("img");
		int execute = execute("insert into coupon (id,name,payment,publish_date,expire_date,number,chance,img,create_date,publish_stop_date) values (?,?,?,?,?,?,?,?,?,?)",generateKey(),name,payment,publishTime,expireTime,number,chance,img,new Date(),publish_stop_date);
		if(execute <= 0) {
			ResponseUtils.putJsonResponse(resp, getFailJSON("添加失败"));
			return;
		}
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(null));
	}
	
	
	/**
	 * 详情
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/couponDetail")
	@ResponseBody
	public void couponDetail(HttpServletRequest req,HttpServletResponse resp){
	}
}
