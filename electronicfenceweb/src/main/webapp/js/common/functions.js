var timeout = 1000 * 60 * 2;

function commit(url, data, type, fun) {
    $.ajax({
        type: type,
        url: url,
        //traditional:true,
        data: data,
        dataType: "json",
        contentType: 'application/json;charset=utf-8',
        timeout: timeout,
        success: function (data) {
            fun(data);
            hideModel();
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("发送请求失败");
            hideModel();
        }
    });
}

Array.prototype.serializeObject = function (lName) {
    var o = {};
    $t = this;

    for (var i = 0; i < $t.length; i++) {
        for (var item in $t[i]) {
            o[lName + '[' + i + '].' + item.toString()] = $t[i][item].toString();
        }
    }
    return o;
};


function showModel() {
    var _LoadingHtml = '<div id="loadingDiv"></div>';
//呈现loading效果
    document.write(_LoadingHtml);

}

//加载状态为complete时移除loading效果
function hideModel() {
}
