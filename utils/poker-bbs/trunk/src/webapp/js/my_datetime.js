

var g_timestamp;
var g_ONE_DAY = 24 * 60 * 60 * 1000;

Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

function prevDay() {
	g_timestamp -= g_ONE_DAY;
	var now = new Date();
	now.setTime( g_timestamp );
	$("#timeBegin").val( now.Format( "yyyy-MM-dd" ) );
	$("#timeEnd").val( now.Format( "yyyy-MM-dd" ) );
}

function prevDayWithTime() {
	var now = new Date();
	now.setTime( g_timestamp );
	$("#timeEnd").val( now.Format( "yyyy-MM-dd 00:00" ) );
	g_timestamp -= g_ONE_DAY;
	now.setTime( g_timestamp );
	$("#timeBegin").val( now.Format( "yyyy-MM-dd 00:00" ) );
}

function today() {
	var now = new Date();
	g_timestamp = now.getTime();
	$("#offsetTimeZone").val( now.getTimezoneOffset() );
	$("#timeBegin").val( now.Format( "yyyy-MM-dd" ) );
	$("#timeEnd").val( now.Format( "yyyy-MM-dd" ) );
}

function todayWithTime() {
	var now = new Date();
	g_timestamp = now.getTime();
	var tomorrow = new Date( g_timestamp + g_ONE_DAY );
	$("#offsetTimeZone").val( now.getTimezoneOffset() );
	$("#timeBegin").val( now.Format( "yyyy-MM-dd 00:00" ) );
	$("#timeEnd").val( tomorrow.Format( "yyyy-MM-dd 00:00" ) );
}

function nextDay() {
	g_timestamp += g_ONE_DAY;
	var now = new Date();
	now.setTime( g_timestamp );
	$("#timeBegin").val( now.Format( "yyyy-MM-dd" ) );
	$("#timeEnd").val( now.Format( "yyyy-MM-dd" ) );
}

function nextDayWithTime() {
	var now = new Date();
	g_timestamp += g_ONE_DAY;
	now.setTime( g_timestamp );
	$("#timeBegin").val( now.Format( "yyyy-MM-dd 00:00" ) );
	var tmp = g_timestamp + g_ONE_DAY;
	now.setTime( tmp );
	$("#timeEnd").val( now.Format( "yyyy-MM-dd 00:00" ) );
}
