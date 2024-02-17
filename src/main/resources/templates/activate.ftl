<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Grammarly会员激活系统</title>
    <link rel="stylesheet" href="https://cdn.staticfile.org/bootstrap/5.3.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <script src="https://cdn.staticfile.org/jquery/3.6.0/jquery.min.js"></script>
    <script src="https://cdn.staticfile.org/layui/2.9.1/layui.js"></script>
    <link rel="stylesheet" href="https://cdn.staticfile.org/layui/2.9.1/css/layui.css">
    <style>
        body {
            font-family: 'Segoe UI', Arial, sans-serif;
            background-color: #eef2f7;
            color: #333;
            margin: 0;
            padding: 20px;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .container {
            background-color: #fff;
            padding: 40px 50px;
            border-radius: 12px;
            box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
            max-width: 600px;
        }

        h1 {
            font-size: 28px;
            color: #007bff;
            margin-bottom: 20px;
            text-align: center;
        }

        h2 {
            font-size: 22px;
            color: #0056b3;
            margin-bottom: 15px;
            text-align: center;
        }

        .form-group {
            margin-bottom: 1.5rem;
        }

        .btn-submit {
            background-color: #007bff;
            border-color: #007bff;
            padding: 10px 20px;
            width: 100%;
            font-size: 16px;
            letter-spacing: 1px;
        }

        .btn-submit:hover {
            background-color: #0056b3;
            border-color: #0056b3;
        }

        .contact-info {
            margin-top: 40px;
            text-align: center;
        }

        .contact-info i {
            font-size: 1.3rem;
            margin-right: 8px;
        }

        .contact-info a, .contact-info button {
            color: #007bff;
            text-decoration: none;
            font-size: 16px;
        }

        .contact-info a:hover {
            text-decoration: underline;
        }

        @media (max-width: 768px) {
            .container {
                padding: 20px;
            }

            h1, h2 {
                font-size: 24px;
            }

            .form-group {
                margin-bottom: 1rem;
            }

            .btn-submit {
                padding: 10px 15px;
                font-size: 14px;
            }
        }
    </style>
</head>
<body>

<div class="container">
    <h1><i class="fas fa-key"></i> 欢迎使用 Grammarly 会员自助激活</h1>
    <h2><i class="fas fa-key"></i> 两步轻松激活会员</h2>
    <p class="text-center mb-4"><i class="fas fa-hand-point-right"></i> 第一步：输入兑换码和邮箱，点击“开通会员”</p>
    <p class="text-center mb-4"><i class="fas fa-hand-point-right"></i> 第二步：登录Grammarly+"join the team"完成会员同步”</p>
    <form>
        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" class="form-control" id="email" placeholder="输入要激活的邮箱">
        </div>
        <div class="form-group">
            <label for="number">兑换码</label>
            <input type="text" class="form-control" id="number" placeholder="兑换码/激活码">
        </div>
        <button type="button" class="btn btn-submit mt-2" onclick="activatePremium()"><span
                    style="color: #e8e8e8 ;font-weight: bolder">第一步:兑换会员</span></button>
    </form>

    <div class="contact-info">
        <p><i class="fa fa-comments"></i>售后请加微信 <span id="售后微信">GGRAMMARLYVIP</span></p>
        <button onclick="copyWeChat()" class="btn btn-outline-secondary"><i class="fa fa-copy"></i> 复制售后微信</button>
        <p id="copyMsg" style="display: none; margin-top: 10px;"><i class="fa fa-check"></i> 微信号复制成功，请添加。</p>
        <p><i class="fa fa-question-circle"></i> 查看问题及教程 <a href="https://note.youdao.com/s/3p7U2Ono"><i
                        class="fa fa-book"></i> 教程</a></p>
    </div>
</div>

<script>
    function copyWeChat() {
        const tempInput = document.createElement("input");
        tempInput.value = document.getElementById("售后微信").innerText;
        document.body.appendChild(tempInput);

        tempInput.select();
        tempInput.setSelectionRange(0, 99999);

        document.execCommand("copy");

        document.body.removeChild(tempInput);

        const copyMsg = document.getElementById("copyMsg");
        copyMsg.style.display = "block";

        setTimeout(function () {
            copyMsg.style.display = "none";
        }, 3000);
    }

    function activatePremium() {
        let number = $.trim($("#number").val());
        let email = $("#email").val();
        if (number === "" || email === "") {
            alert("兑换码和邮箱不能为空");
            return false;
        }
        // 电子邮件地址的简单正则表达式检查
        var emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
        if (!emailRegex.test(email)) {
            alert("亲！你的邮箱输错啦请");
            return false;
        }
        let data = {
            number: number,
            email: email
        }
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
            }
        )
        ;
    }

    function redirectToConfirmation(email) {
        localStorage.setItem('email', email);
        var currentURL = window.location.href;
        // 通过拼接实现打开新页面
        var newPageURL = currentURL.substring(0, currentURL.lastIndexOf('/')) + '/confirm';
        var newWindow = window.open(newPageURL, '_blank');
        console.log(newPageURL)
        if (newWindow) {
            newWindow.focus();
        } else {
            alert('请点击浏览器->网址栏的弹窗允许，完成激活');
        }
    }

</script>

</body>
</html>
