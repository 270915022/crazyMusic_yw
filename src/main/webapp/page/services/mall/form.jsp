<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
	<link href="../../css/style.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="../../js/jquery.js"></script>
	<script type="text/javascript" src="../../js/jquery.form.js"></script>
	<script type="text/javascript" src="../../js/ajaxfileupload.js"></script>
	<script type="text/javascript" src="../../js/jquery.idTabs.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="../../js/method.js"></script>
 	<script type="text/javascript" charset="utf-8" src="../../../assmbly/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="../../../assmbly/ueditor/ueditor.all.min.js"> </script>
    <!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
    <!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
    <script type="text/javascript" charset="utf-8" src="../../../assmbly/ueditor/lang/zh-cn/zh-cn.js"></script>
    <script type="text/javascript">
    	var attrs = []; 
	  	//实例化编辑器
	    //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
	    $(document).ready(function(){
		    //填充商品类型
		    $.ajax({
		         type : "GET",
		         url : method.getBaseUrl()+"/mall/typeList",
		         dataType : "json",
		         async:false,
		         success : function (data) {
		        	 if(data != null && data.length > 0){
		        		 for(var i = 0;i < data.length;i++){
		        			 $("#product_type").append("<option value='"+data[i].id+"'>"+data[i].type_name+"</option>");
		        		 }
		        	 }
		         }
		     });
		    //填充商品推荐标签
		    $.ajax({
		         type : "GET",
		         url : method.getBaseUrl()+"/mall/tagList",
		         dataType : "json",
		         success : function (data) {
		        	 if(data != null && data.length > 0){
		        		 for(var i = 0;i < data.length;i++){
		        			 $("#product_tag").append("<option value='"+data[i].id+"'>"+data[i].tag_name+"</option>");
		        		 }
		        	 }
		         }
		     });
		    var id = method.getParamString("id");
			if(id){
				$("#productId").val(id);
				var data = {};
				data.id = id;
				$.ajax({
			         type : "POST",
			         url : method.getBaseUrl()+"/mall/productDetail",
			         dataType : "json",
			         data:data,
			         async: false,
			         success : function (data) {
			        	 if(data.code != "0"){
			        		 alert(data.msg);
			        	 }else{
			        		 var product = data.data.product;
			        		 $("#product_allTitle").val(product.name);
			        		 $("#product_simpleTitle").val(product.simple_name);
			        		 $("#product_type").val(product.type_id);
			        		 $("#product_tag").val(product.product_tag_id);
			        		 $("#product_now_price").val(product.now_price);
			        		 $("#product_cost_price").val(product.cost_price);
			        		 $("#product_market_price").val(product.market_price);
			        		 $("#product_simple_desc").val(product.simple_desc);
			        		 $("#post_fee").val(product.post_fee);
			        		 setContent(product.html);
			        		 attrs = data.data.attrList;
			        		 var imgs = data.data.imgList;
			        		 if(imgs != null && imgs.length > 0){
			        			 for(var i = 0; i<imgs.length;i++){
			        				 $("#imgs").append("<img width=200 height=200 style='margin-left:20px;' src='"+imgs[i].product_img+"' ></img> <button onclick='removeImg(this)' >删除</button> ");
			        			 }
			        		 }
			        	 }
			         }
			     });
			};
	    });
	  	
	  	function removeImg(button){
	  		$(button).prev().remove();
	  		$(button).remove();
	  	}
	  	
	  	function subMitFile(){
	  		method.subMitFile("fileUpload",function(result){
	  			$("#imgs").append("<img width=200 height=200 style='margin-left:20px;' src='"+result.data.src+"' ></img> <button onclick='removeImg(this)' >删除</button>");
	  		});
	  	}
	  	
	  	//发布shang
	  	function save(){
	  		var product_allTitleVal = $("#product_allTitle").val();//商品全标题
	  		var product_simpleTitleVal = $("#product_simpleTitle").val();//商品简标题
	  		var product_typeVal = $("#product_type").val();//商品类别
	  		var product_tagVal = $("#product_tag").val();//商品标签
	  		var product_now_priceVal = $("#product_now_price").val();//商品现价
	  		var product_market_priceVal = $("#product_market_price").val();//商品市场价
	  		var product_cost_priceVal = $("#product_cost_price").val();//商品原价
	  		var product_simple_descVal = $("#product_simple_desc").val();//商品简单描述
	  		var post_fee = $("#post_fee").val();//商品简单描述
	  		var product_htmlVal = getContent();
	  		var imgs = $("#imgs").children();
	  		var imgsData = {};
	  		if(imgs.length > 0){
	  			for(var i = 0;i < imgs.length;i++){
	  				imgsData["img"+(i+1)] = imgs[i].src;
	  			}
	  		}
	  		//商品属性
	  		var chk_value =[]; 
	  		var flag = true;
	  		$('input[name="product_attr"]:checked').each(function(){
	  			var tr = $(this).closest("tr");
	  			var price = $(tr).find("input[name=price]").val();
	  			var number = $(tr).find("input[name=number]").val();
	  			if(price == null || price == "" || number == null || number == ""){
	  				alert("所选属性价格或者库存不能为空！");
	  				flag = false;
	  			}
	  			var attrId = $(this).val();
	  			var attrs = {};
	  			attrs.attrId = attrId;
	  			attrs.price = price;
	  			attrs.number = number;
	  			chk_value.push(JSON.stringify(attrs));
	  		});
	  		if(!flag){
	  			return;
	  		}
	  		if(chk_value.length == 0){
	  			alert("商品类型对应属性至少一个！");
	  			return;
	  		}
	  		var data = {};
	  		data.attrs = "["+chk_value.join(",")+"]";
	  		data.imgs = imgsData;
	  		data.product_allTitleVal = product_allTitleVal;
	  		data.product_simpleTitleVal = product_simpleTitleVal;
	  		data.product_typeVal = product_typeVal;
	  		data.product_tagVal = product_tagVal;
	  		data.product_now_priceVal = product_now_priceVal;
	  		data.product_market_priceVal = product_market_priceVal;
	  		data.product_cost_priceVal = product_cost_priceVal;
	  		data.product_simple_descVal = product_simple_descVal;
	  		data.product_htmlVal = product_htmlVal;
	  		data.post_fee = post_fee;
	  		data.id = $("#productId").val();
	  		 $.ajax({
		         type : "POST",
		         url : method.getBaseUrl()+"/mall/save",
		         dataType : "json",
		         data:data,
		         success : function (data) {
		        	if(data.success == true){
		        		alert("保存成功");
		        	}else{
		        		alert("保存失败");
		        	}
		        	
		         }
		     });
	  	};
	  	//选择类型属性填充
	  	function type_attr(seleDom){
	  		$("tr[rmif]").remove();
	  		var typeId = $(seleDom).val();
	  		var data = {};
	  		data.type_id = typeId;
	  		 $.ajax({
		         type : "POST",
		         url : method.getBaseUrl()+"/mall/attrByType",
		         dataType : "json",
		         data:data,
		         success : function (data) {
		        	 var dataList = data.data.dataList;
		        	 if(dataList != null && dataList.length > 0){
		        		 for(var i = 0;i < dataList.length;i++){
							 $("#attrTable").append("<tr rmif> <td><span name='nameSpan' style='margin-left: 30px;'>"+dataList[i].name+"</span></td> <td><span name='valueSpan' style='margin-left: 30px;'>"+dataList[i].value+"</span></td>  "
							 +" <td><input name='price' type='text' style='width: 50px;margin-left: 30px;' class='dfinput' /></td> "
							 +" <td><input name='number' type='text' style='width: 50px;margin-left: 30px;' class='dfinput'/></td> "
							 +" <td><input type='checkbox' name='product_attr' value='"+dataList[i].id+"' style='width: 50px;margin-left: 30px;' /></td></tr>");   			 
		        		 }
		        		 if(attrs != null && attrs.length > 0){
		        			 for(var i = 0;i<attrs.length;i++){
		        				 var attr = attrs[i];
		        				 var tr = $("input[value="+attr.product_attr_id+"]").closest("tr");
		        				 $(tr).find("input[name=price]").val(attr.increase_price);
		        				 $(tr).find("input[name=number]").val(attr.number);
		        				 $(tr).find("input[name=product_attr]").attr("checked",true);
		        			 }
		        		 }
		        	 }
		         }
		     });
	  	}
	  	
	    var ue = UE.getEditor('editor');
	    function getAllHtml() {
	        console.log(UE.getEditor('editor').getAllHtml())
	    }
	    function getContent() {
	        return UE.getEditor('editor').getContent();
	    }
	    
	    function setContent(content){
	    	ue.ready(function() { 
	    			ue.setContent(content); 
	    		});
	    }
    </script>
</head>
<body>
	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">首页</a></li>
    <li><a href="#">商品管理</a></li>
    <li><a href="#">添加商品</a></li>
    </ul>
    </div>
    <input type="hidden"  id="productId"/>
    <div class="formbody">
    <ul class="forminfo">
	    <div class="formtitle"><span>商品信息</span></div>
	    <li><label>商品全标题<b>*</b></label><input id="product_allTitle" type="text" class="dfinput" /><i>不能超过50个字符</i></li>
	    <li><label>商品简标题<b>*</b></label><input id="product_simpleTitle" type="text" class="dfinput" /><i>不能超过20个字符</i></li>
	    <li><label>商品主副图<b>*</b></label>
	    	<imgs id="imgs">
	    	</imgs>
	    	<br>
	    	<br>
	    	<form id="addAttaForm" enctype="multipart/form-data" method="post">  
			    <div class="row">  
			        <label for="fileUpload">选择商品图片：</label>  
			        <input type="file" name="fileUpload" id="fileUpload"/>  
			        <input type="button" value="点击上传" name="点击上传" onclick="subMitFile()"/>
			    </div>  
			</form>  
	    </li>
	    <li><label>商品类别<b>*</b></label>  
		    <div class="vocation">
			    <select id="product_type" onchange="type_attr(this)" >
			    </select>
		    </div>
		</li>
		<li> <label >商品属性</label> 
			<div>
				<table cellspacing="10" id="attrTable">
						<tr>
							<th ><span style="margin-left: 30px;">属性</span></th>
							<th ><span style="margin-left: 30px;">属性名</span></th>
							<th ><span style="margin-left: 30px;">增量价格</span></th>
							<th ><span style="margin-left: 30px;">库存</span></th>
							<th ><span style="margin-left: 30px;">选择</span></th>
						</tr>
				</table>
			</div>
		</li>
	    <li> <label >商品推荐标签</label>  
		    <div  class="vocation">
			    <select id="product_tag" >
			    </select>
		    </div>
		</li>
		<li><label>出售价格<b>*</b></label><input id="product_now_price" type="text" class="dfinput" /></li>
		<li><label>市场价格<b>*</b></label><input id="product_market_price" type="text" class="dfinput" /></li>
		<li><label>商品原价<b>*</b></label><input id="product_cost_price" type="text" class="dfinput" /></li>
		<li><label>邮费<b>*</b></label><input id="post_fee" type="text" class="dfinput" /></li>
		<li><label>商品简单描述</label><textarea id="product_simple_desc" cols="" rows="" class="textinput"></textarea></li>
	</ul>
		<div class="formtitle"><span>商品详细描述</span></div>
	    <script id="editor" type="text/plain" style="width:1024px;height:500px;"></script>
	    <label>&nbsp;</label><input onclick="save()" type="button" class="btn" value="发布商品"/>
    <!-- <ul class="forminfo">
    <li><label>文章标题</label><input name="" type="text" class="dfinput" /><i>标题不能超过30个字符</i></li>
    <li><label>关键字</label><input name="" type="text" class="dfinput" /><i>多个关键字用,隔开</i></li>
    <li><label>是否审核</label><cite><input name="" type="radio" value="" checked="checked" />是&nbsp;&nbsp;&nbsp;&nbsp;<input name="" type="radio" value="" />否</cite></li>
    <li><label>引用地址</label><input name="" type="text" class="dfinput" value="http://www..com/html/uidesign/" /></li>
    <li><label>文章内容</label><textarea name="" cols="" rows="" class="textinput"></textarea></li>
    <li><label>&nbsp;</label><input name="" type="button" class="btn" value="确认保存"/></li>
    </ul> -->
    </div>

</body>
</html>
