<!doctype html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>兑换码生成</title>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="/webjars/bootstrap/3.3.7/css/bootstrap.min.css" />
    <link rel="stylesheet" href="/webjars/Eonasdan-bootstrap-datetimepicker/4.17.47/css/bootstrap-datetimepicker.min.css" />

    <!-- HTML5 shim 和 Respond.js 是为了让 IE8 支持 HTML5 元素和媒体查询（media queries）功能 -->
    <!-- 警告：通过 file:// 协议（就是直接将 html 页面拖拽到浏览器中）访问页面时 Respond.js 不起作用 -->
    <!--[if lt IE 9]>
    <script src="/webjars/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="/webjars/respond/1.4.2/dest/respond.min.js"></script>
    <![endif]-->
    <!-- jQuery (Bootstrap 的所有 JavaScript 插件都依赖 jQuery，所以必须放在前边) -->
    <script src="/webjars/jquery/1.12.4/jquery.min.js"></script>
    <!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->
    <script src="/webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <!-- 日期控件 -->
    <script src="/webjars/momentjs/2.10.3/min/moment-with-locales.min.js"></script>
    <script src="/webjars/Eonasdan-bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>
    <script src="/webjars/layer/dist/layer.js"></script>
</head>
<body>
<div class="container">
    <form class="form-horizontal" style="margin-top: 60px;">
        <div class="form-group">
            <label for="account" class="col-sm-4 control-label"><span style="color: red">*</span>生成数量（单位：个）</label>
            <div class="col-sm-8">
                <input type="number" required class="form-control" id="count" placeholder="必填项，例如：1，5">
            </div>
        </div>
        <div class="form-group">
            <label for="account" class="col-sm-4 control-label"><span style="color: red">*</span>会员天数（单位：天）</label>
            <div class="col-sm-8">
                <input type="number" required class="form-control" id="validDays" placeholder="必填项，例如：31，180">
            </div>
        </div>
        <div class="form-group">
            <label for="account" class="col-sm-4 control-label">截止兑换日期</label>
            <div class="col-sm-8">
                <div class='input-group date' id='datetimepicker1'>
                    <input type="text" class="form-control" id="exchangeDeadline" placeholder="选填项：兑换截止日期">
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-4 col-sm-8">
                <button type="button" class="btn btn-success" onclick="mysubmit()">确 定</button>
            </div>
        </div>
        <div class="form-group" id="resultShow" style="display: none">
            <label for="account" class="col-sm-4 control-label">生成的兑换码：</label>
            <div class="col-sm-8">
                <div id="result" style="padding-top: 5px;"></div>
            </div>
        </div>
    </form>


</div>
<script type="text/javascript">
    var options = {
        format: 'YYYY-MM-DD',
        showClear: true,  //清除按钮
        showTodayButton: true,  // 今天
        locale: moment.locale('zh-CN'),
        tooltips: {
            today: '今天',
            clear: '清除'
        }
    };
    $(function() {
        var picker1 = $('#datetimepicker1').datetimepicker(options);
    });
    function mysubmit() {
        // let count = $.trim($("#count").val());
        let count = $("#count").val();
        if (count === "") {
            layer.msg("会员天数不能为空", function(){});
            return false;
        }
        let validDays = $("#validDays").val();
        if (validDays === "") {
            layer.msg("生成数量不能为空", function(){});
            return false;
        }
        let data = {
            count: count,
            validDays: validDays
        }
        if ($("#exchangeDeadline").val()) {
            data.exchangeDeadline = $("#exchangeDeadline").val();
        }
        layer.load();
        $("#result").empty();
        $.ajax({
            url: "/grammarly/exchangeCode/gen",
            type: "post",
            contentType: 'application/json',
            cache: false,
            dataType: "json",
            data: JSON.stringify(data),
            success: function(res){
                if (res.code == 200) {
                    let nums = res.data;
                    let innerHtml = "";
                    for (var i=0; i<nums.length; i++) {
                        innerHtml += "<div style='color: red; font-weight: 600'>" + nums[i] + "</div>";
                    }
                    $("#result").html(innerHtml);
                    $("#resultShow").show();
                    layer.msg('操作成功!', {icon: 1, time: 1000}, function(){
                    });
                } else {
                    layer.alert(res.msg, {icon: 5});
                }
            },
            error:function(res) {
                layer.alert(res.responseJSON.message, {icon: 5});
            },
            complete: function () {
                layer.closeAll('loading');
            }
        });
    }
</script>
</body>
</html>