var code = ""; //在全局定义验证码   
$(function(){
		$('.nav li').on('mouseover',function(){
			if($(this).find('.sub-nav').length != 0 ) {
			$(this).addClass('li-current').children('a').addClass('current').end().find('.sub-nav').show();
			$('.sub-nav-bg').stop().show();
			}else{
				return false;
			}
		})
		
		$('.nav li').on('mouseout',function(){
			$(this).removeClass('li-current').children('a').removeClass('current').end().find('.sub-nav').hide();
			$('.sub-nav-bg').stop().hide();
		})
		
		$('#username').on('focus',function(){
			$("#usernameLabel").html("");
			$("#usernameLabel").hide();
		});
		$('#username').on('blur',function(){
			checkusername();
		});
		
		$('#password').on('focus',function(){
			$("#passwordLabel").html("");
			$("#passwordLabel").hide();
		});
		$('#password').on('blur',function(){
			checkpassword();
		});
		
		$('#password2').on('focus',function(){
			$("#password2Label").html("");
			$("#password2Label").hide();
		});
		$('#password2').on('blur',function(){
			checkpassword2();
		});
		
		$('#realname').on('focus',function(){
			$("#realnameLabel").html("");
			$("#realnameLabel").hide();
		});
		$('#realname').on('blur',function(){
			checkrealname();
		});
		
		$('#card').on('focus',function(){
			$("#cardLabel").html("");
			$("#cardLabel").hide();
		});
		$('#card').on('blur',function(){
			checkcard();
		});
		
		$('#mobile').on('focus',function(){
			$("#mobileLabel").html("");
			$("#mobileLabel").hide();
		});
		$('#mobile').on('blur',function(){
			checkmobile();
		});
		
		$('#code').on('focus',function(){
			$("#codeLabel").html("");
			$("#codeLabel").hide();
		});
		$('#code').on('blur',function(){
			checkcode();
		});
		
		$('#codeShow').on('click',function(){
			createCode();
		});
		
		$('.submit').on('click',function(){
			if(checkusername() && checkpassword() && checkpassword2() && checkrealname() && checkcard() && checkmobile() && checkcode()){
				alert("恭喜您，注册成功啦！");
				window.location.href="http://lpsh.com.cn/";
			}
		});
		
		function checkusername(){
			var usernameP = /^[a-zA-Z][a-zA-Z0-9]{4,16}$/g;
			var username =  $("#username").val();
			if(!(usernameP.test(username))){
				$("#usernameLabel").html("<font color='red'>用户名不符合规范。用户名必须以字母开头,并且4-16位的数字或字母.</font>");
				$("#usernameLabel").show();
				return false;
			}
			return true;
		}
		function checkpassword(){
			var passwordP = /^[a-zA-Z0-9]{6,20}$/g;
			var password =  $("#password").val();
			if(!(passwordP.test(password))){
				$("#passwordLabel").html("<font color='red'>密码不符合规范.密码为数字字母组合。6-20位。区分大小写</font>");
				$("#passwordLabel").show();
				return false;
			}
			return true;
		}
		function checkpassword2(){
			var password =  $("#password").val();
			var password2 =  $("#password2").val();
			if(!password2 || password2=='' || password!=password2){
				$("#password2Label").html("<font color='red'>确认密码不一致.请重新输入</font>");
				$("#password2Label").show();
				return false;
			}
			return true;
		}
		function checkrealname(){
			var realnameP = /^[\u4E00-\u9FA5]{2,4}$/;
			var realname =  $("#realname").val();
			if (!(realnameP.test(realname))){
				$("#realnameLabel").html("<font color='red'>真实姓名不符合规范。请认真填写.</font>");
				$("#realnameLabel").show();
				return false;
			}
			return true;
		}
		function checkcard(){
			var card =  $("#card").val();
			if (!IdentityCodeValid(card)){
				$("#cardLabel").html("<font color='red'>身份证号码不符合规范.请认真填写.</font>");
				$("#cardLabel").show();
				return false;
			}
			return true;
		}
		function checkmobile(){
			var mobileP = /^1[3|4|5|7|8][0-9]\d{8}$/;
			var mobile =  $("#mobile").val();
			if(mobile.length==0 || mobile.length!=11 || !mobileP.test(mobile)){
				$("#mobileLabel").html("<font color='red'>请填写正确的11位手机号码.只支持中国大陆地区</font>");
				$("#mobileLabel").show();
				return false;
			}
			return true;
		}
		
		function checkcode(){
			var inputCode =  $("#code").val().toUpperCase();
			if(!inputCode || inputCode.length <= 0 || inputCode != code) { //若输入的验证码长度为0  
				$("#codeLabel").html("<font color='red'>请填写正确的验证码</font>");
				$("#codeLabel").show();
				return false;
			}
			return true;
		}
		
		createCode();
		
		function createCode(){
			code = "";
			var codeLength = 4;// 验证码的长度
			var checkCode = document.getElementById("codeShow");   
			var random = new Array(0,1,2,3,4,5,6,7,8,9,'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R',  
			'S','T','U','V','W','X','Y','Z');// 随机数
			for(var i = 0; i < codeLength; i++) {// 循环操作
				var index = Math.floor(Math.random()*36);// 取得随机数的索引（0~35）
				code += random[index];// 根据索引取得随机数加到code上
			}  
			checkCode.value = code;// 把code值赋给验证码
		}
		
		function IdentityCodeValid(code) {
		var city = {
			11 : "北京",
			12 : "天津",
			13 : "河北",
			14 : "山西",
			15 : "内蒙古",
			21 : "辽宁",
			22 : "吉林",
			23 : "黑龙江 ",
			31 : "上海",
			32 : "江苏",
			33 : "浙江",
			34 : "安徽",
			35 : "福建",
			36 : "江西",
			37 : "山东",
			41 : "河南",
			42 : "湖北 ",
			43 : "湖南",
			44 : "广东",
			45 : "广西",
			46 : "海南",
			50 : "重庆",
			51 : "四川",
			52 : "贵州",
			53 : "云南",
			54 : "西藏 ",
			61 : "陕西",
			62 : "甘肃",
			63 : "青海",
			64 : "宁夏",
			65 : "新疆",
			71 : "台湾",
			81 : "香港",
			82 : "澳门",
			91 : "国外 "
		};
		var tip = "";
		var pass = true;

		if (!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)) {
			tip = "身份证号格式错误";
			pass = false;
		}
		else if (!city[code.substr(0, 2)]) {
			tip = "身份证地址编码错误";
			pass = false;
		} else {
			// 18位身份证需要验证最后一位校验位
			if (code.length == 18) {
				code = code.split('');
				// ∑(ai×Wi)(mod 11)
				// 加权因子
				var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8,
						4, 2 ];
				// 校验位
				var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
				var sum = 0;
				var ai = 0;
				var wi = 0;
				for (var i = 0; i < 17; i++) {
					ai = code[i];
					wi = factor[i];
					sum += ai * wi;
				}
				var last = parity[sum % 11];
				if (parity[sum % 11] != code[17]) {
					tip = "校验位错误";
					pass = false;
				}
			}
		}
		return pass;
	}
	
	function tabControl(titleNodeId,contentNodeId){
		$('.'+titleNodeId+' li').on('click',function(){
			var num = $(this).index('.'+titleNodeId+' li');
			$('.'+titleNodeId+'-current').removeClass(titleNodeId+'-current');
			$(this).children('a').addClass(titleNodeId+'-current');
			$('.'+contentNodeId+' .ordinary:visible').hide();
			$('.'+contentNodeId+' .ordinary').eq(num).show();
		})	
	}
	tabControl("sidebar","column-main");
	tabControl("family-content","family-main");
})