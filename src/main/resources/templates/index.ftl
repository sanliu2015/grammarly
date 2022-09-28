<!doctype html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>兑换码兑换</title>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="${ctx.contextPath}/webjars/bootstrap/3.3.7/css/bootstrap.min.css"/>

    <!-- HTML5 shim 和 Respond.js 是为了让 IE8 支持 HTML5 元素和媒体查询（media queries）功能 -->
    <!-- 警告：通过 file:// 协议（就是直接将 html 页面拖拽到浏览器中）访问页面时 Respond.js 不起作用 -->
    <!--[if lt IE 9]>
    <script src="${ctx.contextPath}/webjars/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="${ctx.contextPath}/webjars/respond/1.4.2/dest/respond.min.js"></script>
    <![endif]-->
    <!-- jQuery (Bootstrap 的所有 JavaScript 插件都依赖 jQuery，所以必须放在前边) -->
    <script src="${ctx.contextPath}/webjars/jquery/1.12.4/jquery.min.js"></script>
    <!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->
    <script src="${ctx.contextPath}/webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <!-- 日期控件 -->
    <script src="${ctx.contextPath}/webjars/layer/dist/layer.js"></script>
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
        <div class="form-group">
            <div class="mb-5">
                <br class="btn btn-primary btn-lg px-3 col-sm-offset-4 col-sm-6">
                <text class="btn btn-primary btn-lg px-3 col-sm-offset-4 col-sm-6" overflow="hidden">请先输入兑换码与兑换邮箱
                plz enter activate code and email first</text>
            </div>
        </div>
        <div class="form-group">
            <div class="mb-5">
                <a href="https://www.grammarly.com/business/join-existing?sid=S1noCBafdksruA8hiPcd" class="btn btn-primary btn-lg px-4 col-sm-offset-4 col-sm-6">收不到激活邮件请点我手动激活，
                    sign in后join team即可激活</a>
            </div>
        </div>
        <div class="form-group">
            <div class="mb-5">
                <a href="https://note.youdao.com/s/D3A7f0O0" class="btn btn-primary btn-lg px-4 col-sm-offset-4 col-sm-6">收不到6位数字验证码请点我</a>
            </div>
        </div>
        <div class="form-group">
            <div class="mb-5">
                <a href="https://note.youdao.com/s/3p7U2Ono" class="btn btn-primary btn-lg px-4 col-sm-offset-4 col-sm-6">其他问题请点我</a>
            </div>
        </div>
        <div class="form-group">
            <div class="mb-5">
                <text class="btn btn-primary btn-lg px-3 col-sm-offset-4 col-sm-6">售后请联系淘宝客服或者微信客服:leke123123(不定时有福利))</text>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">
    function submitForm() {
        let number = $.trim($("#number").val());
        if (number === "") {
            layer.msg("兑换码不能为空", function () {
            });
            return false;
        }
        let email = $("#email").val();
        if (email === "") {
            layer.msg("邮箱不能为空", function () {
            });
            return false;
        }
        let data = {
            number: number,
            email: email
        }
        // layer.load();
        layer.confirm('确定激活账号邮箱是' + email, function(index){
            $.ajax({
                url: "${ctx.contextPath}/exchangeCode/exchange",
                type: "post",
                contentType: 'application/json',
                cache: false,
                dataType: "json",
                data: JSON.stringify(data),
                success: function (res) {
                    if (res.code == 200) {
                        // 兑换成功提示
                        layer.msg("恭喜您，兑换成功，请注意查收来自grammarly的确认邮件" +
                            "如果没收到请点击下面的->收不到激活邮件请点我手动激活", {icon: 1, time: 3000}, function () {
                        });
                    } else {
                        layer.alert(res.msg, {icon: 5});
                    }
                },
                error: function (res) {
                    layer.alert(res.responseJSON.message, {icon: 5});
                },
                complete: function () {
                    // layer.closeAll('loading');
                }
            });
        });

    }
</script>
</body>
</html>