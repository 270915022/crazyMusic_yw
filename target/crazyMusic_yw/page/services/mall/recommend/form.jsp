<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
	<link href="../../../css/style.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="../../../js/jquery.js"></script>
	<script type="text/javascript" src="../../../js/ajaxfileupload.js"></script>
	<script type="text/javascript" src="../../../js/jquery.idTabs.min.js"></script>
	<script type="text/javascript" src="../../../js/select-ui.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="../../../js/method.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			var id = method.getParamString("id");
			if(id){
				$("#recommendId").val(id);
				var data = {};
				data.id = id;
				$.ajax({
			         type : "POST",
			         url : method.getBaseUrl()+"/mall/recommendDetail",
			         dataType : "json",
			         data:data,
			         success : function (data) {
			        	 if(data.code != "0"){
			        		 alert(data.msg);
			        	 }else{
			        		 $("#product_id").val(data.data.product_id);
			        		 $("#recommend_img").attr("src",data.data.product_img);
			        		 $("#recommend_star").val(data.data.recommend_star);
			        		 $("#recommend_sort").val(data.data.sort);
			        		 $("#recommend_desc").val(data.data.recommend_desc);
			        		 $("input[name=isRecommend][value="+data.data.benable+"]").attr("checked","true");
			        	 }
			         }
			     });
			};
		})
	
		function subMitFile(){
	  		method.subMitFile("fileUpload",function(result){
	  			$("#recommend_img").attr("src",result.data.src);
	  		});
	  	}
		
		function save(){
			var data = {};
			data.product_id = $("#product_id").val();
			data.recommend_star = $("#recommend_star").val();
			data.sort = $("#recommend_sort").val();
			data.recommend_desc = $("#recommend_desc").val();
			data.isRecommend = $("input[name=isRecommend]:checked").val();
			data.id = $("#recommendId").val();
			data.product_img = $("#recommend_img").attr("src");
			$.ajax({
		         type : "POST",
		         url : method.getBaseUrl()+"/mall/recommendSave",
		         dataType : "json",
		         data:data,
		         success : function (data) {
		        	if(data.code == "0"){
		        		alert("保存成功");
		        	}else{
		        		alert(data.msg);
		        	}
		         }
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
	<input type="hidden"   id="recommendId" value="" />    
    <div class="formbody">
	    <ul class="forminfo">
		    <div class="formtitle"><span>推荐信息</span></div>
		    <li><label>商品编号<b>*</b></label><input id="product_id" type="text" class="dfinput" /></li>
		    <li><label>推荐星级<b>*</b></label><input id="recommend_star" type="text" class="dfinput" /></li>
		    <li><label>推荐排序<b>*</b></label><input id="recommend_sort" type="text" class="dfinput" /></li>
		    <li><label>推荐理由<b>*</b></label><textarea id="recommend_desc" cols="" rows="" class="textinput"></textarea></li>
		     <li><label>类型图<b>*</b></label>
	    	<img id="recommend_img"></img>
	    	<form id="addAttaForm" enctype="multipart/form-data" method="post">  
			    <div class="row">  
			        <label for="fileUpload">请选择图片：</label>  
			        <input type="file" name="fileUpload" id="fileUpload"/>  
			        <input type="button" value="点击上传" name="点击上传" onclick="subMitFile()"/>
			    </div>  
			</form>  
	    </li>
		    <li><label>是否推荐<b>*</b></label><input type="radio" name="isRecommend" value="1"/>是&emsp;&emsp;<input  type="radio" value="0" name="isRecommend"  />否</li>
	    </ul>
	    <label>&nbsp;</label><input onclick="save()" type="button" class="btn" value="保存"/>
    </div>
</body>
</html>
