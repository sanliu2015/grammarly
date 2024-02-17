<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>兑换码兑换</title>
    <link rel="stylesheet" href="https://cdn.staticfile.org/bootstrap/5.3.2/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

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

    <style>
        body {
            font-family: 'Segoe UI', Arial, sans-serif;
            background-color: #f4f4f7;
            color: #333;
            margin: 0;
            padding: 0;
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .container {
            background-color: #fff;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
            max-width: 500px; /* 调整为更合适的宽度 */
        }

        h1, h2 {
            color: #d9534f; /* 鲜艳的红色 */
            text-align: center;
            margin-bottom: 20px;
        }

        .form-horizontal {
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .form-group {
            width: 100%; /* 设置宽度以便于居中 */
            margin-bottom: 20px;
        }

        .form-group label {
            text-align: center;
            display: block;
            font-weight: bold;
        }

        .btn-primary, .link-button {
            width: 100%;
            padding: 10px 0;
            margin-bottom: 15px;
        }

        .btn-primary {
            background-color: #d9534f;
            border-color: #d9534f;
        }

        .btn-primary:hover {
            background-color: #c9302c;
            border-color: #c9302c;
        }

        .link-button {
            display: block;
            background-color: #d9534f;
            color: #fff;
            text-align: center;
            text-decoration: none;
            padding: 10px 0;
            border-radius: 5px;
            font-size: 16px;
        }

        .link-button:hover {
            background-color: #c9302c;
            color: #fff;
            text-decoration: none;
        }
    </style>
</head>
<body>
<div class="container">
    <h1><i class="fas fa-exchange-alt"></i> Grammarly会员激活</h1>
    <form class="form-horizontal" style="margin-top: 30px;">
        <div class="form-group">
            <label for="number">兑换码</label>
            <input type="text" class="form-control" id="number" placeholder="兑换码/激活码">
        </div>
        <div class="form-group">
            <label for="email">邮箱</label>
            <input type="email" class="form-control" id="email" placeholder="需要激活的会员邮箱">
        </div>
        <div class="form-group">
            <div class="col-sm-12">
                <button type="button" class="btn btn-primary" onclick="submitForm()">第一步：兑换会员</button>
            </div>
        </div>
        <div class="form-group" >
            <div class="col-sm-12">
                <a href="https://www.grammarly.com/business/activate" class="link-button" ><span
                            style="color:#ffffff;font-weight: bolder">第二步:激活会员</span></a>
            </div>
        </div>
        <div class="form-group" style="accent-color: #007bff">
            <div class="col-sm-12">
                <a href="https://note.youdao.com/s/D3A7f0O0" class="link-button">问题1：收不到6位数字验证码请点我</a>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-12">
                <a href="https://note.youdao.com/s/3p7U2Ono" class="link-button">问题2：其他问题请点我</a>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-12">
                <p class="link-button">售后请联系淘宝客服/微信客服:leke123123</p>
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
        };
        layer.confirm('确定激活账号邮箱是 <strong><span style="color:red">' + email + '</span></strong><br>请仔细确认邮箱！<br>兑换后不可更换！更换邮箱需要收 <strong><span style="color:red">20元手续费</span></strong> ', function (index) {
            layer.close(index);
            layer.load();
            $.ajax({
                url: "${ctx.contextPath}/exchangeCode/exchange",
                type: "post",
                contentType: 'application/json',
                cache: false,
                dataType: "json",
                data: JSON.stringify(data),
                success: function (res) {
                    if (res.code == 200) {
                        layer.open({
                            content: '<span style="font-size: 16px; font-weight: bold;">恭喜您，兑换成功！' +
                                '<br>请点击邮箱内的激活邮件' +
                                '<br>' +
                                '或者点击<span style="color: #c9302c;font-weight: bolder" >下方按钮跳转到到第二步激活</span></span>',
                            btn: ['OK'],
                            yes: function (index) {
                                layer.close(index);
                                redirectToConfirmation(email);
                            },
                            time: 5000, // 5秒后自动关闭
                            end: function () {
                                redirectToConfirmation(email); // 修改这里
                            }
                        });
                    } else {
                        layer.alert(res.msg, {icon: 5});
                    }
                },
                error: function (res) {
                    layer.alert(res.responseJSON.message, {icon: 5});
                },
                complete: function () {
                    layer.closeAll('loading');
                }
            });
        });

    }
</script>
</body>
</html>
