<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
	<link href="../../css/style.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="../../js/jquery.js"></script>
	<script type="text/javascript" src="../../js/jquery.form.js"></script>
	<script type="text/javascript" src="../../js/ajaxfileupload.js"></script>
	<script type="text/javascript" src="../../js/jquery.idTabs.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="../../js/method.js"></script>
    <script type="text/javascript">
	  	//实例化编辑器
	    //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
	    $(document).ready(function(){
		    //商品回显
		    var id = method.getParamString("id");
			if(id){
				$("#user_id").val(id);
				var data = {};
				data.id = id;
				$.ajax({
			         type : "POST",
			         url : method.getBaseUrl()+"/user/userDetail",
			         dataType : "json",
			         data:data,
			         success : function (data) {
			        	 if(data.code != "0"){
			        		 alert(data.msg);
			        	 }else{
			        		 $("#username").val(data.data.username);
			        		 $("#password").val(data.data.password);
			        		 $("#headImg").attr("src",data.data.head_img);
			        		 $("#phone").val(data.data.phone);
			        		 $("#role_state").val(data.data.role_state);
			        		 $("#regist_way").val(data.data.regist_way);
			        		 $("#nick_name").val(data.data.nick_name);
			        		 $("#sign").val(data.data.sign);
			        		 $("input[name=status][value="+data.data.status+"]").attr("checked","true");
			        	 }
			         }
			     });
			};
		    
		    
	    });
	  	
	  	
	  	function subMitFile(){
	  		method.subMitFile("fileUpload",function(result){
	  			$("#headImg").attr("src",result.data.src);
	  		});
	  	}
	  	
	  	//发布shang
	  	function save(){
	  		var data = {};
	  		var username = $("#username").val();
	  		var password = $("#password").val();
	  		var nick_name = $("#nick_name").val();
	  		var phone = $("#phone").val();
	  		var role_state = $("#role_state").val();
	  		var regist_way = $("#regist_way").val();
	  		var sign = $("#sign").val();
	  		var headImg = $("#headImg").attr("src");
	  		var status =  $("input[name=status]:checked").val();
	  		
	  		data.username = username;
	  		data.password = password;
	  		data.nick_name = nick_name;
	  		data.phone = phone;
	  		data.role_state = role_state;
	  		data.regist_way = regist_way;
	  		data.headImg = headImg;
	  		data.status = status;
	  		data.sign = sign;
	  		data.id = $("#user_id").val();
	  		 $.ajax({
		         type : "POST",
		         url : method.getBaseUrl()+"/user/userSave",
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
    <input type="hidden"  id="user_id" value="" /> 
    <div class="formbody">
    <ul class="forminfo">
	    <div class="formtitle"><span>商品类型信息</span></div>
	    <li><label>用户名<b>*</b></label><input id="username" type="text" class="dfinput" /><i>不能超过50个字符</i></li>
	    <li><label>密码<b>*</b></label><input id="password" type="text" class="dfinput" /><i>不能超过50个字符</i></li>
	    <li><label>昵称<b>*</b></label><input id="nick_name" type="text" class="dfinput" /><i>不能超过50个字符</i></li>
	    <li><label>用户头像<b>*</b></label>
	    	<img id="headImg"></img>
	    	<form id="addAttaForm" enctype="multipart/form-data" method="post">  
			    <div class="row">  
			        <label for="fileUpload">请选择图片：</label>  
			        <input type="file" name="fileUpload" id="fileUpload"/>  
			        <input type="button" value="点击上传" name="点击上传" onclick="subMitFile()"/>
			    </div>  
			</form>  
	    </li>
	      <li><label>手机号<b>*</b></label><input id="phone" type="text" class="dfinput" /></li>
	      <li><label>用户状态<b>*</b></label><input type="radio" name="status" value="1"/>可用 &emsp;&emsp;<input  type="radio" value="0" name="status"  />限制登录</li>
	      <li><label>角色状态<b>*</b></label>  
			    <select id="role_state" >
			    	<option value="5" >平台自动生成用户</option>
			    	<option value="6" >平台自动生成老师</option>
			    	<option value="0" >普通用户</option>
			    	<option value="1" >审核中用户</option>
			    	<option value="2" >普通老师</option>
			    </select>
		</li>
	      <li><label>注册方式<b>*</b></label>  
			    <select id="regist_way" >
			    	<option value="5" >平台注册</option>
			    	<option value="0" >微信注册</option>
			    	<option value="1" >手机号注册</option>
			    </select>
		</li>
	    <li><label>个性签名<b>*</b></label><input id="sign" type="text" class="dfinput" /><i>不能超过50个字符</i></li>
	</ul>
	    <label>&nbsp;</label><input onclick="save()" type="button" class="btn" value="保存"/>
    </div>

</body>
</html>
