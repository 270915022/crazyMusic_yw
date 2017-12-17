<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
	<link href="../../../css/style.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="../../../js/jquery.js"></script>
	<script type="text/javascript" src="../../../js/jquery.form.js"></script>
	<script type="text/javascript" src="../../../js/ajaxfileupload.js"></script>
	<script type="text/javascript" src="../../../js/jquery.idTabs.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="../../../js/method.js"></script>
	<script type="text/javascript" charset="utf-8" src="../../../../assmbly/laydate/laydate.js"></script>
    <script type="text/javascript">
	  	//实例化编辑器
	    //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
	    $(document).ready(function(){
	    	//常规用法
			laydate.render({
			  elem: '#effective_time',
			  type: 'datetime'
			});
			laydate.render({
			  elem: '#lose_effective_time',
			  type: 'datetime'
			});
		    //商品回显
		    var id = method.getParamString("id");
			if(id){
				$("#banner_id").val(id);
				var data = {};
				data.id = id;
				$.ajax({
			         type : "POST",
			         url : method.getBaseUrl()+"/banner/bannerDetail",
			         dataType : "json",
			         data:data,
			         success : function (data) {
			        	 $("#name").val(data.data.name);
			        	 $("#img").attr("src",data.data.img);
			        	 $("#url").val(data.data.url);
			        	 $("#skip_type").val(data.data.skip_type);
			        	 $("#skip_type").val(data.data.skip_type);
			        	 $("#inside_type").val(data.data.inside_type);
			        	 $("#outside_type").val(data.data.outside_type);
			        	 $("#effective_time").val(data.data.effective_time);
			        	 $("#lose_effective_time").val(data.data.lose_effective_time);
			         }
			     });
			};
		    
		    
	    });
	  	
	  	
	  	function subMitFile(){
	  		method.subMitFile("fileUpload",function(result){
	  			$("#img").attr("src",result.data.src);
	  		});
	  	}
	  	
	  	//发布shang
	  	function save(){
	  		var data = {};
	  		var name = $("#name").val();
	  		var img = $("#img").attr("src");
	  		var url = $("#url").val();
	  		var skip_type = $("#skip_type").val();
	  		var inside_type = $("#inside_type").val();
	  		var outside_type = $("#outside_type").val();
	  		var effective_time = $("#effective_time").val();
	  		var lose_effective_time = $("#lose_effective_time").val();
	  		
	  		data.name = name;
	  		data.img = img;
	  		data.url = url;
	  		data.skip_type = skip_type;
	  		data.inside_type = inside_type;
	  		data.outside_type = outside_type;
	  		data.effective_time = effective_time;
	  		data.lose_effective_time = lose_effective_time;
	  		data.id = $("#banner_id").val();
	  		 $.ajax({
		         type : "POST",
		         url : method.getBaseUrl()+"/banner/save",
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
    <input type="hidden"  id="banner_id" value="" /> 
    <div class="formbody">
    <ul class="forminfo">
	    <div class="formtitle"><span>banner管理</span></div>
	    <li><label>banner名称<b>*</b></label><input id="name" type="text" class="dfinput" /><i>不能超过50个字符</i></li>
	    <li><label>类型图<b>*</b></label>
	    	<img id="img"></img>
	    	<form id="addAttaForm" enctype="multipart/form-data" method="post">  
			    <div class="row">  
			        <label for="fileUpload">请选择图片：</label>  
			        <input type="file" name="fileUpload" id="fileUpload"/>  
			        <input type="button" value="点击上传" name="点击上传" onclick="subMitFile()"/>
			    </div>  
			</form>  
	    </li>
		<li><label>跳转地址<b>*</b></label><input id="url" type="text" class="dfinput" /></li>
		<li><label>跳转方式<b>*</b></label>
			<select id="skip_type">
				<option value="1" selected="selected">APP内部跳转</option>
				<option value="2">APP外部跳转</option>
			</select>
		</li>
		<li><label>内部跳转类别（跳转方式外部请忽略）</label>
			<select id="inside_type">
				<option value="-1" selected="selected">请选择</option>
				<option value="1">获取优惠券页面跳转</option>
			</select>
		</li>
		<li><label>外部跳转类别（跳转方式内部请忽略）</label>
			<select  id="outside_type">
				<option value="-1" selected="selected">请选择</option>
				<option value="1">网页跳转</option>
				<option value="2">外链APP跳转</option>
			</select>
		</li>
		<li><label>生效时间<b>*</b></label><input id="effective_time" type="text" class="dfinput" /></li>
		<li><label>失效时间<b>*</b></label><input id="lose_effective_time" type="text" class="dfinput" /></li>
	</ul>
	    <label>&nbsp;</label><input onclick="save()" type="button" class="btn" value="保存"/>
    </div>

</body>
</html>
