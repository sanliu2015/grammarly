<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>成功激活</title>
    <link rel="stylesheet" href="https://cdn.staticfile.org/bootstrap/5.3.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <style>
        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
            background: linear-gradient(135deg, #f8f8f7 60%, #e8e8e8);
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .container {
            background-color: #fff;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 4px 25px rgba(0, 0, 0, 0.1);
            max-width: 700px;
            text-align: center;
            position: relative;
        }

        h1 {
            color: #007bff;
            margin-bottom: 10px;
        }

        .lead {
            font-size: 1.1rem;
            color: #666;
            margin-bottom: 30px;
        }

        .step {
            display: flex;
            align-items: center;
            justify-content: start;
            margin-bottom: 20px;
            text-align: left;
        }

        .icon {
            color: #007bff;
            margin-right: 15px;
            font-size: 2rem;
        }

        .step-text {
            font-size: 1rem;
            color: #333;
        }

        .activation-link {
            font-size: 1.2rem;
            color: #28a745;
            font-weight: bold;
            margin: 20px 0;
            display: inline-block;
            text-decoration: none;
            border-radius: 5px;
            padding: 12px 20px;
            background-color: #e8f5e9;
            transition: background-color 0.3s, color 0.3s;
            position: relative; /* 新增 */
        }

        .activation-link:hover {
            background-color: #c8e6c9;
            color: #1b5e20;
        }

        .custom-divider {
            height: 2px;
            background-color: #eaeaea;
            margin: 30px 0;
        }

        .faq-section {
            text-align: left;
            margin-top: 20px;
        }

        .faq-link {
            color: #007bff;
            font-weight: bold;
        }

        .animated {
            animation: fadeIn 1s ease-in;
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
            }
            to {
                opacity: 1;
            }
        }

        .email-display {
            font-weight: bold;
            color: #007bff;
        }

        .pointing-hand {
            position: absolute;
            font-size: 24px;
            color: #007bff;
            transform: rotate(-90deg); /* 手指旋转 */
        }

        .top-hand {
            top: -30px;
            left: 50%;
            transform: translateX(-50%) rotate(180deg);
        }

        .bottom-hand {
            bottom: -30px;
            left: 50%;
            transform: translateX(-50%) rotate(0deg);
        }

        .left-hand {
            left: -30px;
            top: 50%;
            transform: translateY(-50%) rotate(0deg);
        }

        /* 左手指向右 */
        .right-hand {
            right: -30px;
            top: 50%;
            transform: translateY(-50%) rotate(180deg);
        }

        /* 右手指向左 */
    </style>
</head>
<body>

<div class="container animated">
    <h1>请点击第二步:激活会员</h1>
    <p class="lead">您的兑换邮箱：<span id="emailDisplay" class="email-display"></span></p>
    <p class="lead">点击激活会员完成会员资格。</p>
    <div class="step">
        <i class="fas fa-sign-in-alt icon"></i>
        <div class="step-text">点击第二步后，登录Grammarly，选择"join the team“即可激活会员</div>
    </div>
    <div class="custom-divider"></div>
    <div class="activation-link">

        <a href='https://www.grammarly.com/business/activate' style="font-size: 1.5rem;">
            <i class="fas fa-hand-point-up pointing-hand top-hand"></i>
            <i class="fas fa-hand-point-up pointing-hand bottom-hand"></i>
            <i class="fas fa-hand-point-right pointing-hand left-hand"></i> <!-- 修正左手方向 -->
            <i class="fas fa-hand-point-right pointing-hand right-hand"></i> <!-- 修正右手方向 -->
            <i class="fas fa-rocket"></i> 第二步:激活会员
        </a>
    </div>
    <div class="custom-divider"></div>
    <div class="faq-section">
        <p><span class="faq-link">问题1：</span>收不到6位数字验证码，请点击<a href='https://note.youdao.com/s/D3A7f0O0'
                                                              class="faq-link">这里解决</a>。</p>
        <p><span class="faq-link">问题2：</span>其他的问题，请点击<a href='https://note.youdao.com/s/3p7U2Ono'
                                                         class="faq-link">这个链接</a>解决。</p>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        let email = localStorage.getItem('email');
        document.getElementById("emailDisplay").textContent = email;
    });
</script>

</body>
</html>
