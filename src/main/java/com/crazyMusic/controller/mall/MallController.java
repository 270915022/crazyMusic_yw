package com.crazyMusic.controller.mall;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crazyMusic.common.constant.Const;
import com.crazyMusic.common.util.ResponseUtils;
import com.crazyMusic.controller.base.BaseController;
@Controller
@RequestMapping("/mall")
public class MallController extends BaseController{
	
	/**
	 * 商品列表
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/list")
    @ResponseBody
    public void listProduct(HttpServletRequest req,HttpServletResponse resp){
		List<JSONObject> list = queryForJsonList("select p.*,pt.tag_name,ptype.type_name from product p "
				+ "left join product_tag pt on p.product_tag_id = pt.id left join product_type ptype on p.type_id = ptype.id where p.del_flag = 1 ");
		ResponseUtils.putJsonResponse(resp, list);
    }
	
	/**
	 * 商品删除
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/productDelete")
	@ResponseBody
	public void productDelete(HttpServletRequest req,HttpServletResponse resp){
		String id = req.getParameter("id");
		if(StringUtils.isEmpty(id)) {
			ResponseUtils.putJsonResponse(resp, getFailJSON("id 不能为空"));
		}
		execute("update product set del_flag = 0 where id = ?", id);
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(null));
	}
	
	
	/**
	 * 商品详情
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/productDetail")
	@ResponseBody
	public void productDetail(HttpServletRequest req,HttpServletResponse resp){
		String id = req.getParameter("id");
		if(StringUtils.isEmpty(id)) {
			ResponseUtils.putJsonResponse(resp, getFailJSON("id 不能为空"));
		}
		JSONObject rtnJSON = new JSONObject();
		JSONObject jb = queryForJsonObject("select * from product where id = ?", id);
		if(jb != null) {
			rtnJSON.put("product", jb);
			List<JSONObject> attrList = queryForJsonList("select * from product_attr_val where product_id = ?", id);
			rtnJSON.put("attrList", attrList);
			List<JSONObject> imgList = queryForJsonList("select product_img from product_img where product_id = ?  order by sort asc", id);
			rtnJSON.put("imgList", imgList);
		}
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(rtnJSON));
	}
	

	/**
	 * 发布商品
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/productPublish")
	@ResponseBody
	public void productPublish(HttpServletRequest req,HttpServletResponse resp){
		String id = req.getParameter("id");
		if(StringUtils.isEmpty(id)) {
			ResponseUtils.putJsonResponse(resp, getFailJSON("id 不能为空"));
		}
		JSONObject rtnJSON = new JSONObject();
		execute("update product set publish_state = 1 where id = ?",id);
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(rtnJSON));
	}
	
	/**
	 * 取消商品
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/productUnPublish")
	@ResponseBody
	public void productUnPublish(HttpServletRequest req,HttpServletResponse resp){
		String id = req.getParameter("id");
		if(StringUtils.isEmpty(id)) {
			ResponseUtils.putJsonResponse(resp, getFailJSON("id 不能为空"));
		}
		JSONObject rtnJSON = new JSONObject();
		execute("update product set publish_state = 0 where id = ?",id);
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(rtnJSON));
	}
	
	/**
	 * 保存商品
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/save")
    @ResponseBody
    @Transactional
    public void saveProduct(HttpServletRequest req,HttpServletResponse resp){
		Map<String,Object> parameterMap = req.getParameterMap();
		Set<String> keySet = parameterMap.keySet();
		String id = req.getParameter("id");
		if(StringUtils.isNoneBlank(id)) {
			//删除商品图片
			execute("delete from product_img where product_id = ?", id);
			//删除商品类型
			execute("delete from product_attr_val where product_id = ?", id);
		}
		//商品Id
		String product_id = StringUtils.isBlank(id)?generateKey():id;
		for (String key : keySet) { 
			Integer sortNum = 1;
			if(key.contains("imgs[img")) {
				execute("insert into product_img (id,product_id,product_img,type,sort) values (?,?,?,?,?)  ", generateKey()
						,product_id, ((String[])parameterMap.get(key))[0],Const.PRODUCT_IMG_MAIN,sortNum);
				sortNum++;
			}
		}
		String htmlVal = Const.PRODUCT_STARTHTML+req.getParameter("product_htmlVal")+Const.PRODUCT_ENDHTML;
		if(StringUtils.isBlank(id)) {
			execute("insert into product (id,name,simple_name,type_id,now_price,market_price,product_tag_id,cost_price,simple_desc,html,post_fee) "
					+ " values (?,?,?,?,?,?,?,?,?,?,?)  ", product_id,req.getParameter("product_allTitleVal"),req.getParameter("product_simpleTitleVal")
					,req.getParameter("product_typeVal"),req.getParameter("product_now_priceVal"),req.getParameter("product_market_priceVal"),
					req.getParameter("product_tagVal"),req.getParameter("product_cost_priceVal"),req.getParameter("product_simple_descVal"),htmlVal,req.getParameter("post_fee"));
		}else {
			execute("update product set name=?,simple_name =?,type_id=?,now_price=?,market_price=?,product_tag_id=?,cost_price=?,simple_desc=?,html=?,post_fee=? where id = ?",req.getParameter("product_allTitleVal"),req.getParameter("product_simpleTitleVal")
					,req.getParameter("product_typeVal"),req.getParameter("product_now_priceVal"),req.getParameter("product_market_priceVal"),
					req.getParameter("product_tagVal"),req.getParameter("product_cost_priceVal"),req.getParameter("product_simple_descVal"),htmlVal,req.getParameter("post_fee"),product_id);
		}
		//商品属性保存
		if(!StringUtils.isEmpty(req.getParameter("attrs"))) {
			List<JSONObject> jsonArr = JSONArray.parseArray(req.getParameter("attrs"),JSONObject.class);
			if(!jsonArr.isEmpty()) {
				for (JSONObject jb : jsonArr) {
					execute("insert into product_attr_val (id,product_id,product_attr_id,increase_price,create_date,number) values (?,?,?,?,?,?) ", generateKey()
							,product_id,jb.getString("attrId"),jb.getInteger("price"),new Date(),jb.getInteger("number"));
				}
			}
		}
		ResponseUtils.putJsonSuccessResponse(resp);
	}
	
	
	/**
	 * 商品推荐管理
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/orderList")
	@ResponseBody
	public void orderList(HttpServletRequest req,HttpServletResponse resp){
		List<JSONObject> list = queryForJsonList("select tor.*,odi.product_id,odi.product_title,odi.num,odi.product_attrs from t_order tor left JOIN order_item odi on tor.id = odi.order_id ");
		if(CollectionUtils.isNotEmpty(list)) {
			for (JSONObject productData : list) {
				String orderId = productData.getString("id");
				JSONObject shippingData = queryForJsonObject("select shipping_name,shipping_code from order_shipping where order_id = ?", orderId);
				if(shippingData != null && !shippingData.isEmpty()) {
					productData.put("shipping_name",shippingData.getString("shipping_name"));
					productData.put("shipping_code",shippingData.getString("shipping_code"));
				}
				if(StringUtils.isNoneEmpty(productData.getString("product_attrs"))) {
					String attrs = productData.getString("product_attrs");
					String[] product_attrId = attrs.split(",");
					List<JSONObject> queryForJsonList = queryForJsonList("select pa.value from product_attr_val pav LEFT JOIN product_attr pa on pav.product_attr_id = pa.id where " + getIn("pav.id", product_attrId));
					if(CollectionUtils.isNotEmpty(queryForJsonList)) {
						String attrName = "";
						for (JSONObject attrJSON : queryForJsonList) {
							attrName += attrJSON.getString("value") + ",";
						}
						productData.put("attrName",attrName);
					}
				}
			}
		}
		ResponseUtils.putJsonResponse(resp, list);
	}
	
	
	
	
	/**
	 * 商品推荐管理
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/recommendList")
	@ResponseBody
	public void listProductRecommend(HttpServletRequest req,HttpServletResponse resp){
		List<JSONObject> list = queryForJsonList("select * from product_recommend where del_flag = 1");
		ResponseUtils.putJsonResponse(resp, list);
	}
	
	/**
	 * 商品推荐新增
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/recommendSave")
	@ResponseBody
	public void recommendSave(HttpServletRequest req,HttpServletResponse resp){
		String id = req.getParameter("id");
		if(!StringUtils.isBlank(req.getParameter("product_id"))) {
			JSONObject jb = queryForJsonObject("select * from product where id = ?", req.getParameter("product_id"));
			if(jb == null || jb.isEmpty()) {
				ResponseUtils.putJsonResponse(resp, getFailJSON("商品ID不存在"));
				return;
			}
		}
		if(StringUtils.isEmpty(id)) {
			execute("insert into product_recommend (id,product_id,recommend_star,sort,create_date,benable,recommend_desc,product_img) values (?,?,?,?,?,?,?,?) ", generateKey(),
					req.getParameter("product_id"),req.getParameter("recommend_star"),req.getParameter("sort"),new Date(),req.getParameter("isRecommend"),req.getParameter("recommend_desc"),req.getParameter("product_img"));
		}else {
			execute("update product_recommend set product_id=?,recommend_star=?,sort=?,benable=?,recommend_desc=?,product_img=? where id = ? ",req.getParameter("product_id"),req.getParameter("recommend_star"),req.getParameter("sort"),
					req.getParameter("isRecommend"),req.getParameter("recommend_desc"),req.getParameter("product_img"),id);
		}
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(null));
	}
	
	/**
	 * 发货
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/shippingSave")
	@ResponseBody
	@Transactional
	public void shippingSave(HttpServletRequest req,HttpServletResponse resp){
		String orderId = req.getParameter("orderId");
		String shipping_name = req.getParameter("shipping_name");
		String shipping_code = req.getParameter("shipping_code");
		if(StringUtils.isEmpty(shipping_code)) {
			ResponseUtils.putJsonResponse(resp, getFailJSON("物流单号不能为空！"));
			return;
		}
		if(StringUtils.isEmpty(shipping_name)) {
			shipping_name = Const.LOGISTICAL_NAME;
		}
		if(execute("update t_order set status=?,update_date=?,consign_time=? where id = ?", Const.ORDER_STATE_SEND,new Date(),new Date(),orderId) > 0) {
			if(execute("update order_shipping set update_date=?,shipping_name=?,shipping_code=? where order_id=?",new Date(),shipping_name,shipping_code,orderId) <= 0) {
				ResponseUtils.putJsonResponse(resp, getFailJSON("发货失败！"));
				return ;
			}else {
				ResponseUtils.putJsonResponse(resp, getSuccessJSON(null));
				return;
			}
		};
		ResponseUtils.putJsonResponse(resp, getFailJSON("发货失败！"));
	}
	
	/**
	 * 商品推荐详情
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/recommendDetail")
	@ResponseBody
	public void recommendDetail(HttpServletRequest req,HttpServletResponse resp){
		String id = req.getParameter("id");
		if(StringUtils.isEmpty(id)) {
			ResponseUtils.putJsonResponse(resp, getFailJSON("id 不能为空"));
		}
		JSONObject jb = queryForJsonObject("select * from product_recommend where id = ?", id);
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(jb));
	}
	

	/**
	 * 商品推荐删除
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/recommendDelete")
	@ResponseBody
	public void recommendDelete(HttpServletRequest req,HttpServletResponse resp){
		String id = req.getParameter("id");
		if(StringUtils.isEmpty(id)) {
			ResponseUtils.putJsonResponse(resp, getFailJSON("id 不能为空"));
		}
		execute("update product_recommend set del_flag=0 where id = ?", id);
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(null));
	}
	
	
	/**
	 * 促销标签管理
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/tagList")
    @ResponseBody
    public void listProductTag(HttpServletRequest req,HttpServletResponse resp){
		List<JSONObject> list = queryForJsonList("select * from product_tag");
		ResponseUtils.putJsonResponse(resp, list);
	}
	
	/**
	 * 标签详情
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/tagDetail")
	@ResponseBody
	public void tagDetail(HttpServletRequest req,HttpServletResponse resp){
		String id = req.getParameter("id");
		if(StringUtils.isEmpty(id)) {
			ResponseUtils.putJsonResponse(resp, getFailJSON("id 不能为空"));
		}
		JSONObject jb = queryForJsonObject("select * from product_tag where id = ?", id);
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(jb));
	}
	
	/**
	 * 标签保存
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/tagSave")
	@ResponseBody
	public void tagSave(HttpServletRequest req,HttpServletResponse resp){
		String tag_name = req.getParameter("tag_name");
		String id = req.getParameter("id");
		if(StringUtils.isEmpty(id)) {
			execute("insert into product_tag (id,tag_name,create_date) values (?,?,?) ", generateKey(),tag_name,new Date());
		}else {
			execute("update product_tag set tag_name=? where id = ? ",tag_name,id);
		}
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(null));
	}
	
	/**
	 * 标签删除
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/tagDelete")
	@ResponseBody
	public void tagDelete(HttpServletRequest req,HttpServletResponse resp){
		String id = req.getParameter("id");
		if(StringUtils.isEmpty(id)) {
			ResponseUtils.putJsonResponse(resp, getFailJSON("id 不能为空"));
		}
		execute("delete from product_tag where id = ?", id);
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(null));
	}
	
	/**
	 * 列出所有商品类型
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/typeList")
    @ResponseBody
    public void listProductType(HttpServletRequest req,HttpServletResponse resp){
		List<JSONObject> list = queryForJsonList("select * from product_type order by sort asc");
		ResponseUtils.putJsonResponse(resp, list);
    }
	
	/**
	 * 商品类型详情
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/typeDetail")
    @ResponseBody
    public void typeDetail(HttpServletRequest req,HttpServletResponse resp){
		String id = req.getParameter("id");
		if(StringUtils.isEmpty(id)) {
			ResponseUtils.putJsonResponse(resp, getFailJSON("id 不能为空"));
		}
		JSONObject jb = queryForJsonObject("select pt.*,pta.product_attr_type from product_type pt left join product_type_attr pta on pt.id = pta.product_type_id where pt.id = ?", id);
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(jb));
	}
	
	/**
	 * 商品类型保存
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/typeSave")
    @ResponseBody
    @Transactional
    public void typeSave(HttpServletRequest req,HttpServletResponse resp){
		String name = req.getParameter("name");
		String imgUrl = req.getParameter("imgUrl");
		String parent_id = req.getParameter("parent_id");
		String attr_types = req.getParameter("attr_types");
		String sort = req.getParameter("sort");
		String id = req.getParameter("id");
		if(StringUtils.isEmpty(id)) {
			String newKey = generateKey();
			execute("insert into product_type (id,type_name,parent_id,type_pic,sort,create_date) values (?,?,?,?,?,?) ", newKey,name,parent_id,imgUrl,sort,new Date());
			execute("insert into product_type_attr (id,product_type_id,product_attr_type) values (?,?,?) ", generateKey(),newKey,attr_types);
		}else {
			execute("update product_type set type_name=?,parent_id=?,type_pic=?,sort=? where id = ? ", name,parent_id,imgUrl,sort,id);
			execute("update product_type_attr set product_attr_type=? where product_type_id = ? ", attr_types,id);
		}
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(null));
    }
	
	
	/***
	 * 属性列表
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/attrList")
	@ResponseBody
	public void listProductAttr(HttpServletRequest req,HttpServletResponse resp) {
		String isGroupBy = req.getParameter("isGroupBy");
		List<JSONObject> attrList  = null;
		if("true".equals(isGroupBy)) {
			attrList = queryForJsonList("select type,name from product_attr where del_flag = 1 group by type,name asc");
		}else {
			attrList = queryForJsonList("select * from product_attr where del_flag = 1 order by type asc");
		}
		ResponseUtils.putJsonResponse(resp, attrList);
	}
	
	
	/**
	 * 属性保存
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/attrSave")
	@ResponseBody
	public void saveProductAttr(HttpServletRequest req,HttpServletResponse resp){
		String attr_type = req.getParameter("attr_type");
		String attr_name = req.getParameter("attr_name");
		String attr_val = req.getParameter("attr_val");
		String id = req.getParameter("id");
		if(StringUtils.isEmpty(id)) {
			execute("insert into product_attr (id,type,name,value) values (?,?,?,?) ", generateKey(),attr_type,attr_name,attr_val);
		}else {
			execute("update product_attr set type=?,name=?,value=? where id = ? ", attr_type,attr_name,attr_val,id);
		}
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(null));
	}
	
	/**
	 * 属性保存
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/attrDetail")
	@ResponseBody
	public void attrDetail(HttpServletRequest req,HttpServletResponse resp){
		String id = req.getParameter("id");
		if(StringUtils.isEmpty(id)) {
			ResponseUtils.putJsonResponse(resp, getFailJSON("id 不能为空"));
		}
		JSONObject jb = queryForJsonObject("select * from product_attr where id = ?", id);
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(jb));
	}
	
	/**
	 * 通过类型获取属性列表
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/attrByType")
	@ResponseBody
	public void attrByType(HttpServletRequest req,HttpServletResponse resp){
		String id = req.getParameter("type_id");
		if(StringUtils.isEmpty(id)) {
			ResponseUtils.putJsonResponse(resp, getFailJSON("id 不能为空"));
		}
		
		JSONObject jb = queryForJsonObject("select product_attr_type as attrIds from product_type_attr where product_type_id = ?", id);
		List<JSONObject> rtnList = new ArrayList<JSONObject>();
		if(jb != null) {
			String attrIds = jb.getString("attrIds");
			if(!StringUtils.isEmpty(attrIds)) {
				String[] attrIdArr = attrIds.split(",");
				for (String attrId : attrIdArr) {
					JSONObject attrJb = queryForJsonObject("select * from product_attr where id = ?", attrId);
					rtnList.add(attrJb);
				}
			}
		}
		JSONObject rtnJb = new JSONObject();
		rtnJb.put("dataList", rtnList);
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(rtnJb));
	}
	
	/**
	 * 属性删除
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/attrDelete")
	@ResponseBody
	public void attrDelete(HttpServletRequest req,HttpServletResponse resp){
		String id = req.getParameter("id");
		if(StringUtils.isEmpty(id)) {
			ResponseUtils.putJsonResponse(resp, getFailJSON("id 不能为空"));
		}
		execute("delete from product_attr where id = ?", id);
		ResponseUtils.putJsonResponse(resp, getSuccessJSON(null));
	}
	
	
	
}
