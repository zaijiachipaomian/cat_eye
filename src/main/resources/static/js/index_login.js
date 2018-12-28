var countdown=60; 
function sendemail(){
    var obj = $("#getKey");
    settime(obj);
    
    }
function settime(obj) { //发送验证码倒计时
    if (countdown == 0) { 
        obj.attr('disabled',false); 
        //obj.removeattr("disabled"); 
        obj.html("短信验证码");
        countdown = 60; 
        return;
    } else { 
        obj.attr('disabled',true);

        obj.html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+countdown+"s");
        countdown--; 
    } 
setTimeout(function() { 
    settime(obj) }
    ,1000) 
}

var countdown1=60; 
function sendemail1(){
    var obj = $("#getKey1");
    settime1(obj);
    
    }
function settime1(obj) { //发送验证码倒计时
    if (countdown1 == 0) { 
        obj.attr('disabled',false); 
        //obj.removeattr("disabled"); 
        obj.html("短信验证码");
        countdown1 = 60; 
        return;
    } else { 
        obj.attr('disabled',true);

        obj.html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+countdown1+"s");
        countdown1--; 
    } 
setTimeout(function() { 
    settime1(obj) }
    ,1000) 
}

$(function(){
	var pho=0,pwd=0,pwd1=0,usern=0;
	//页面切换初始化
	$(".number2").click(function(){
		$(".mainForm1").show();
		$(".mainForm2").hide();
		$(".error").hide();
		$(".normalInput").removeClass("errorC");
		$(".normalInput").removeClass("checkedN");
		$(".mainForm input").val("");
	});
	$(".number1").click(function(){
		$(".mainForm2").show();
		$(".mainForm1").hide();
		$(".error").hide();
		$(".normalInput").removeClass("errorC");
		$(".normalInput").removeClass("checkedN");
		$(".mainForm input").val("");
	});

	//mainform1
	//密码是否可见(mainform1)
	$(".pwdBtnShow").click(function(){
		if($(".pwdBtnShow").attr("isshow")=="false")
		{
			$(".pwdBtnShow i").css("background-position","-60px -93px");
			$(".password").hide();
			$(".password1").val($(".password").val());
			$(".password1").show();
			$(".pwdBtnShow").attr("isshow","true");
		}
		else
		{
			$(".pwdBtnShow i").css("background-position","-30px -93px");
			$(".password1").hide();
			$(".password").val($(".password1").val());
			$(".password").show();
			$(".pwdBtnShow").attr("isshow","false");
		}
		
	});
	
	//手机号栏失去焦点
	$("#phone").blur(function(){
		reg=/^1[3|4|5|8][0-9]\d{4,8}$/i;//验证手机正则(输入前7位至11位)
		pho=0;
		if( $("#phone").val()=="")
		{ 
			$("#phone").parent().addClass("errorC");
			$(".error1").html("请输入手机号");
			$(".error1").css("display","block");
		}
		else if($("#phone").val().length<11)
        {   
        	$("#phone").parent().addClass("errorC");
            $(".error1").html("手机号长度有误！");
            $(".error1").css("display","block");
        }
        else if(!reg.test($("#phone").val()))
        {   
        	$("#phone").parent().addClass("errorC");
            $(".error1").html("请输入正确的手机号!!");
            $(".error1").css("display","block");
        }
        else
        {
        	$("#getKey1").css("pointer-events","auto");
        	pho=1;
        	$("#phone").parent().addClass("checkedN");
        }
	});

	//用户名栏失去焦点
	$("#username").blur(function(){
		usern=0;
		if( $("#username").val()=="")
		{ 
			
			$("#username").parent().addClass("errorC");
			$(".error4").html("请输入用户名");
			$(".error4").css("display","block");
		}
        else
        {
        	usern=1;
        	$("#username").parent().addClass("checkedN");
        }		
	});

	//验证码栏失去焦点
	$(".kapkey").blur(function(){
		reg=/^.*[\u4e00-\u9fa5]+.*$/;
		if( $(".kapkey").val()=="")
		{
			$(".kapkey").parent().addClass("errorC");
			$(".error2").html("请输入验证码");
			$(".error2").css("display","block");
		}
        else if($(".kapkey").val().length<6)
        {   
        	$(".kapkey").parent().addClass("errorC");
            $(".error2").html("验证码长度有误！");
            $(".error2").css("display","block");
        }
        else if(reg.test($(".kapkey").val()))
        {
        	$(".kapkey").parent().addClass("errorC");
            $(".error2").html("验证码里无中文！");
            $(".error2").css("display","block");
        }
        else 
        {
        	$(".kapkey").parent().addClass("checkedN");
        }
	});

	//密码栏失去焦点(mainform1)
	$(".password,.password1").blur(function(){
		pwd=0;
		pwd1=1;
		reg1=/^.*[\d]+.*$/;
		reg2=/^.*[A-Za-z]+.*$/;
		reg3=/^.*[_@#%&^+-/*\/\\]+.*$/;//验证密码
		if($(".pwdBtnShow").attr("isshow")=="false")
		{
			var Pval = $("#password").val();
		}
		else
		{
			var Pval = $(".password1").val();
		}
		
		if( Pval ==""&&($(".password").attr("id")=="password"))
		{
			$(".password").parent().addClass("errorC");
			$(".error3").html("请输入密码");
			$(".error3").css("display","block");
		}
        else if(Pval.length>16 || Pval.length<8)
        {   
        	$(".password").parent().addClass("errorC");
            $(".error3").html("密码应为8-16个字符，区分大小写");
            $(".error3").css("display","block");
        }
        else
        {
        	pwd=1;
        	pwd1=1;
        	$(".password").parent().addClass("checkedN");
        }
	});

	//手机号栏获得焦点
	$(".phone").focus(function(){
		$(".phone").parent().removeClass("errorC");
		$(".phone").parent().removeClass("checkedN");
		$(".error1").hide();
		$("#mz_Float").css("top","232px");
		//$("#mz_Float").find(".bRadius2").html("输入11位手机号码，可用于登录和找回密码");
	});

	//用户名栏获得焦点
	$(".username").focus(function(){
		$(".username").parent().removeClass("errorC");
		$(".username").parent().removeClass("checkedN");
		$(".error4").hide();
		$("#mz_Float").css("top","232px");
	});

	//验证码栏获得焦点
	$(".kapkey").focus(function(){
		$(".kapkey").parent().removeClass("errorC");
		$(".kapkey").parent().removeClass("checkedN");
		$(".error2").hide();
		if($(".error1").css("display")=="block")
		{
			$("#mz_Float").css("top","334px");
		}
		else
		{
			$("#mz_Float").css("top","304px");
		}
		
		// $("#mz_Float").find(".bRadius2").html("请输入手机收到的验证码");
	});
	//密码栏获得焦点(mainform1)
	$(".password,.password1").focus(function(){
		$(".password").parent().removeClass("errorC");
		$(this).parent().removeClass("checkedN");
		$(".error3").hide();
		if($(".error1").css("display")=="block" && $(".error2").css("display")=="block")
		{
			$("#mz_Float").css("top","436px");
		}
		else if($(".error1").css("display")=="block" || $(".error2").css("display")=="block")
		{
			$("#mz_Float").css("top","406px");
		}
		else
		{
			$("#mz_Float").css("top","376px");
		}
		
		//$("#mz_Float").find(".bRadius2").html("长度为8-16个字符，区分大小写，至少包含两种类型");
	});

	$(".phone,.username,.password,.password1").blur(function(){
		if(usern==1&&pho==1&&pwd==1&&pwd1==1){
			$("#getKey").css("pointer-events","auto");
		}

		else{
			$("#getKey").css("pointer-events","none");
		}
	});


});

