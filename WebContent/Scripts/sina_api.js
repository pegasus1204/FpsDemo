/*
*请求后端的业务JS
*/

/*
*调用Sign_and_send_toweb方法
*/
function send(Encryptionstr) {
        Sign_and_send_toweb();
        updateTimeLabel(4);
}
/*
*发送网关
*/
function Sign_and_send_toweb() {
    $.ajax({
        type: "post",
        url: "Send",
        //async: false,
        data: $("#request_form").serialize(),
        success: function (data) {
            success_function(data);
        },
        error: function (data) {
        }
    })
}

/*
*响应同步的报文
*/
function success_function(data) {
    try {
        testJson = $.parseJSON(data);
        if (typeof (testJson.redirect_url) != "undefined") {
            location.href = testJson.redirect_url;
        } else {
            result_temp = "";
            for (var i in testJson) {
                result_temp += i + ":" + testJson[i] + "\n";
            }
            $("#result").addClass("huanhang");
            $("#result").text(data);
            if (testJson.response_code == "APPLY_SUCCESS" && typeof ($("#identity_id").val()) != "undefined") {
                $.cookie("identity_id", $("#identity_id").val()); // 存储 cookie
            } else {

            }
        }
    }
    catch (err) {
        document.write(data);
    }
}


/*
*上传企业会员资质文件专用
*/
function SFTPupimg(Encryptionstr) {
    updateTimeLabel(30);
    var formData = new FormData($("#request_form")[0]);
    $.ajax({
        type: "post",
        url: "AuditMemberInfos",
        data: formData,
        //async: false,
        cache: false,
        contentType: false,
        processData: false,
        success: function (data) {
            testJson = $.parseJSON(data);
            if (testJson["upresult"] == "success") {
                $("#fileName").val(testJson["fileName"]);
                $("#digest").val(testJson["digest"]);
                $("#weibopya_send_img").remove();
                send(Encryptionstr);
                
            } else {
                $("#result").val(testJson["upresult"]);
            }
        },
        error: function (data) {
        }
    })
};

/*
*SFTP对账专用
*/
function SFTP_duizhang() {
    $.ajax({
        type: "post",
        url: "DownLoad",
        //async: false,
        data: $("#request_form").serialize(),
        success: function (data) {
            success_function(data);
        },
        error: function (data) {
        }
    })
}


$("#reset").click(function () {
    location.reload();
});