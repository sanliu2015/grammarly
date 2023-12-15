<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>404</title>
    <style>
        body{
            background-color:#BEBEBE;
            font-size:14px;
        }
        h3{
            font-size:60px;
            color:#eee;
            text-align:center;
            padding-top:30px;
            font-weight:normal;
        }
    </style>
    <meta http-equiv="Refresh" content="3;url=${ctx.contextPath}/index">
</head>

<body>
<h3>404，您请求的网址不存在!</h3>
<p style="text-align: center">由于网站版本更新，您访问的地址可能不存在， <a href="${ctx.contextPath}/index"> 点我立刻前往首页 </a></p>
<p style="text-align: center">
    如果在3秒钟后网页没有自动跳转，请点击上面的链接！</p>
</body>
</html>