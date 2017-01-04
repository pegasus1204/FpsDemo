/*
*异步加载页面使用的JS
*/

$(document).ready(function () {
    $("#service").val(window.location.pathname.split("/")[window.location.pathname.split("/").length - 1].split(".")[0]);
    $("#request_time").val(getrequest_time());
    $("#partner_id").val("200008363134");
    $("#_input_charset").val("utf-8");
    $("#sign_type").val("RSA");
    $("#notify_url").val(basicparame_get_notify_url());
    $("#return_url").val(basicparame_get_return_url());
});

/*
*返回yyyyMMddHHmmss字符串
*/
function getrequest_time() {
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    var strHour = date.getHours();
    var strMinute = date.getMinutes();
    var strSeconds = date.getSeconds();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    if (strHour >= 0 && strHour <= 9) {
        strHour = "0" + strHour;
    }
    if (strMinute >= 0 && strMinute <= 9) {
        strMinute = "0" + strMinute;
    }
    if (strSeconds >= 0 && strSeconds <= 9) {
        strSeconds = "0" + strSeconds;
    }
    var currentdate = year.toString() + month.toString() + strDate.toString()
            + strHour.toString() + strMinute.toString()
            + strSeconds.toString();
    return currentdate;
}
/*
*返回异步地址字符串
*/
function basicparame_get_notify_url() {
    return "http://" + window.location.host + "/JavaDemo/Notify";
}
/*
*返回同步地址字符串
*/
function basicparame_get_return_url() {
    return "http://" + window.location.host + "/JavaDemo/index.html";
}
