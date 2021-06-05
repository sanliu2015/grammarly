<!doctype html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>兑换码兑换</title>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="/webjars/bootstrap/3.3.7/css/bootstrap.min.css" />

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
    <script src="/webjars/layer/dist/layer.js"></script>
</head>
<body>
<div class="container">
    <form class="form-horizontal" style="margin-top: 60px;">
        <div class="form-group">
            <label for="account" class="col-sm-4 control-label"><span style="color: red">*</span>兑换码</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="number" placeholder="必填项">
            </div>
        </div>
        <div class="form-group">
            <label for="account" class="col-sm-4 control-label"><span style="color: red">*</span>邮箱</label>
            <div class="col-sm-8">
                <input type="email" class="form-control" id="email" placeholder="必填项，接收邮箱">
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-4 col-sm-8">
                <button type="button" class="btn btn-success" onclick="submitForm()">确 定</button>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">
    function submitForm() {
        let number = $.trim($("#number").val());
        if (number === "") {
            layer.msg("兑换码不能为空", function(){});
            return false;
        }
        let email = $("#email").val();
        if (email === "") {
            layer.msg("邮箱不能为空", function(){});
            return false;
        }
        let data = {
            number: number,
            email: email
        }
        layer.load();
        $.ajax({
            url: "/grammarly/exchangeCode/exchange",
            type: "post",
            contentType: 'application/json',
            cache: false,
            dataType: "json",
            data: JSON.stringify(data),
            success: function(res){
                if (res.code == 200) {
                    layer.msg(res.msg, {icon: 1, time: 3000}, function(){
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