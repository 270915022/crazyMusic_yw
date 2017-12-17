<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
	<link href="../../../css/style.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="../../../js/jquery.js"></script>
	<script type="text/javascript" src="../../../js/jquery.form.js"></script>
	<script type="text/javascript" src="../../../js/ajaxfileupload.js"></script>
	<script type="text/javascript" src="../../../js/jquery.idTabs.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="../../../js/method.js"></script>
    <script type="text/javascript">
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
		        			 if(data[i].parent_id == null){
			        			 $("#product_type").append("<option value='"+data[i].id+"'>"+data[i].type_name+"</option>");
		        			 }
		        		 }
		        	 }
		         }
		     });
		    //填充商品属性
		    $.ajax({
		         type : "GET",
		         url : method.getBaseUrl()+"/mall/attrList",
		         dataType : "json",
		         data:{isGroupBy:"true"},
		         async:false,
		         success : function (data) {
		        	 if(data != null && data.length > 0){
		        		 for(var i = 0;i < data.length;i++){
		        			 $("#attr_cite").append(data[i].name + ": <input name='attr_check' type='checkbox' value='"+data[i].type+"'/> &emsp;&emsp;");
		        		 }
		        	 }
		         }
		     });
		    //商品回显
		    var id = method.getParamString("id");
			if(id){
				$("#type_id").val(id);
				var data = {};
				data.id = id;
				$.ajax({
			         type : "POST",
			         url : method.getBaseUrl()+"/mall/typeDetail",
			         dataType : "json",
			         data:data,
			         success : function (data) {
			        	 if(data.code != "0"){
			        		 alert(data.msg);
			        	 }else{
			        		 $("#type_name").val(data.data.type_name);
			        		 $("#type_img").attr("src",data.data.type_pic);
			        		 $("#product_type").val(data.data.parent_id);
			        		 $("#type_sort").val(data.data.sort);
			        		 var typeAttrs = data.data.product_attr_type;
			        		 if(typeAttrs != null && typeAttrs != ""){
			        			 var typeAttrArr = typeAttrs.split(",");
			        			 for(var i = 0;i < typeAttrArr.length;i++){
			        				 $("input[name=attr_check][value="+typeAttrArr[i]+"]").attr("checked","checked");
			        			 }
			        		 }
			        	 }
			         }
			     });
			};
		    
		    
	    });
	  	
	  	
	  	function subMitFile(){
	  		method.subMitFile("fileUpload",function(result){
	  			$("#type_img").attr("src",result.data.src);
	  		});
	  	}
	  	
	  	//发布shang
	  	function save(){
	  		var data = {};
	  		var name = $("#type_name").val();
	  		var imgUrl = $("#type_img").attr("src");
	  		var parent_id = $("#product_type").val();
	  		var sort = $("#type_sort").val();
	  		var chk_value =[]; 
	  		$('input[name="attr_check"]:checked').each(function(){ 
	  			chk_value.push($(this).val()); 
	  		});
	  		var attrTypes = chk_value.join(",");
	  		data.name = name;
	  		data.imgUrl = imgUrl;
	  		data.parent_id = parent_id;
	  		data.attr_types = attrTypes;
	  		data.sort = sort;
	  		data.id = $("#type_id").val();
	  		 $.ajax({
		         type : "POST",
		         url : method.getBaseUrl()+"/mall/typeSave",
		         dataType : "json",
		         data:data,
		         success : function (data) {
		        	if(data.code == "0"){
		        		alert("保存成功");
		        	}else{
		        		alert("保存失败");
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
    <input type="text"  id="type_id" value="" /> 
    <div class="formbody">
    <ul class="forminfo">
	    <div class="formtitle"><span>商品类型信息</span></div>
	    <li><label>类型名称<b>*</b></label><input id="type_name" type="text" class="dfinput" /><i>不能超过50个字符</i></li>
	    <li><label>类型图<b>*</b></label>
	    	<img id="type_img"></img>
	    	<form id="addAttaForm" enctype="multipart/form-data" method="post">  
			    <div class="row">  
			        <label for="fileUpload">请选择图片：</label>  
			        <input type="file" name="fileUpload" id="fileUpload"/>  
			        <input type="button" value="点击上传" name="点击上传" onclick="subMitFile()"/>
			    </div>  
			</form>  
	    </li>
	    <li><label>上级类型<b>*</b></label>  
			    <select id="product_type" >
			    	<option value="" >请选择</option>
			    </select>
		</li>
		<li><label>类型排序<b>*</b></label><input id="type_sort" type="text" class="dfinput" /></li>
		<li>
			<label>类型所搭配：</label>
			<cite id="attr_cite">
			</cite>
			
		</li>
	</ul>
	    <label>&nbsp;</label><input onclick="save()" type="button" class="btn" value="保存"/>
    </div>

</body>
</html>
