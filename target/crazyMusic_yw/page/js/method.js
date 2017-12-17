var method = {
	getBaseUrl:function(){
		return window.location.protocol + '//' + window.location.host + "/crazyMusic_yw";
	},
	subMitFile:function(inputId,callback){
		try {
		$.ajaxFileUpload({
            url:method.getBaseUrl()+"/base/upload",             //需要链接到服务器地址  
            secureuri:false,  
            fileElementId:"fileUpload",                     //文件选择框的id属性
            dataType: 'text',                                     //服务器返回的格式，可以是json  
            success: function (data, status){
            	var resultStr = $(data)[0].innerHTML;
            	var rtnData = $.parseJSON(resultStr);
            	if(callback){
            		callback(rtnData);
            	}
            },  
            error: function (data, status, e)             //相当于java中catch语句块的用法  
            {  
                alert('上传图片失败');  
            }  
          });
		} catch (e) {
	}
  },
  getParamString:function(name) { 
	  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
	  var r = window.location.search.substr(1).match(reg); 
	  if (r != null) return unescape(r[2]); return null;
  }
};