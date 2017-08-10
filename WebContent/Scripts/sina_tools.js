/*
*同步加载页面使用的JS
*/


/*
*返回yyyyMMddHHmmss字符串
*/
function getNowFormatDate() {
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
function get_notify_url() {
    return "http://" + window.location.host + "/JavaDemo/Notify";
}
/*
*返回同步地址字符串
*/
function get_return_url() {
    return "http://" + window.location.host + "/JavaDemo/index.html";
}
/*
*按钮的禁止函数
*/
function updateTimeLabel(time) {
    var btn = $(".form-actions :button");
    btn.attr({ "disabled": "disabled" });
    var hander = setInterval(function () {
        if (time <= 0) {
        clearInterval(hander);
            btn.removeAttr("disabled");
        }
        else {
            time--;
        }
    }, 1000);

}

$(document).ready(function () {
    $("#content-header").append(
         "<div class='widget-content'><div class='alert'><button class='close' data-dismiss='alert'>×</button><strong>注意!</strong>  " 
         + "DEMO任何文件代码只做示例，不得用于商户程序生产使用" +
         "</div></div>"
    );
});
