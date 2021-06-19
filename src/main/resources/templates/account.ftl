<!doctype html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>grammarly账号管理</title>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="${ctx.contextPath}/webjars/bootstrap/3.3.7/css/bootstrap.min.css" />

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
    <script src="${ctx.contextPath}/webjars/layer/dist/layer.js"></script>
    <script src="${ctx.contextPath}/webjars/js-cookie/2.2.1/js.cookie.min.js"></script>
</head>
<body>
<div class="container">
    <div style="text-align: right;">
        <a class="btn btn-link" href="${ctx.contextPath}/exchangeCode">兑换码管理</a>
        <button type="button" class="btn btn-link" onclick="modifyPwd()">修改登录密码</button>
        <button type="button" class="btn btn-link" onclick="logout()">退出登录</button>
    </div>
    <div style="margin-top: 50px">
        <button type="button" class="btn btn-primary" onclick="add()">新增</button>
    </div>

    <table class="table table-bordered">
        <thead>
            <tr>
                <th>#</th>
                <th>账号</th>
                <th>账号类别</th>
                <th>身份凭证</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
        <#list accounts as item>
            <tr>
                <th>${item_index?if_exists+1}</th>
                <th>${item.account}</th>
                <th>${item.typeName}</th>
                <th>${item.curlIsSet}</th>
                <th>
                    <div>
                        <button type="button" class="btn btn-link" onclick="add()">添加</button>
                        <button type="button" class="btn btn-link" onclick="edit('${item.id}')">编辑</button>
                    </div>
                </th>
            </tr>
        </#list>
        </tbody>
    </table>

    <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="exampleModalLabel">grammarly账户信息</h4>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <label for="number" class="control-label">grammarly账号:</label>
                            <input type="text" class="form-control" id="account">
                        </div>
                        <div class="form-group">
                            <label for="name" class="control-label">账号类别:</label>
                            <select id="accountType" class="form-control">
                                <option value="0">适用30天以下</option>
                                <option value="1">适用30天及以上</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="number" class="control-label">curl字符穿:</label>
                            <textarea class="form-control" rows="20" id="curlStr">
                            </textarea>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关 闭</button>
                    <button type="button" class="btn btn-primary" onclick="saveAccount()">保 存</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    var record = {};
    function edit(id) {
        layer.load();
        $.ajax({
            url: "${ctx.contextPath}/grammarlyAccount/" + id,
            type: "get",
            cache: false,
            dataType: "json",
            success: function(res){
                if (res.code == 200) {
                    record = res.data;
                    $("#account").val(record.account);
                    $("#curlStr").val(record.curlStr);
                    $("#accountType").val(record.accountType);
                    $('#exampleModal').modal('show');
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
    function add() {
        $("#account").val("");
        $("#curlStr").val("");
        $("#accountType").val("0");
        $('#exampleModal').modal('show');
        record = {};
    }
    function saveAccount() {
        let account = $.trim($("#account").val());
        if (account === "") {
            layer.msg("grammarly账号不能为空", function(){});
            return false;
        }
        let accountType = $("#accountType").val();
        if (accountType === "") {
            layer.msg("账号类别不能为空", function(){});
            return false;
        }
        let curlStr = $("#curlStr").val();
        if (curlStr === "") {
            layer.msg("curl字符穿不能为空", function(){});
            return false;
        }
        record.account = account;
        record.accountType = accountType;
        record.curlStr = curlStr;

        layer.load();
        $.ajax({
            url: "${ctx.contextPath}/grammarlyAccount",
            type: "post",
            contentType: 'application/json',
            cache: false,
            dataType: "json",
            data: JSON.stringify(record),
            success: function(res){
                if (res.code == 200) {
                    layer.msg('操作成功!', {icon: 1, time: 1000}, function(){
                    });
                    $('#exampleModal').modal('hide');
                    location.reload();
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
    function logout() {
        Cookies.remove("gp-token");
        Cookies.remove("gp-username");
        location.href = "${ctx.contextPath}/login";
    }
    function modifyPwd() {
        layer.prompt({
            formType: 0,
            title: '修改密码',
        },function(val, index){
            $.ajax({
                url: "${ctx.contextPath}/grammarlyAccount/modifyPwd",
                type: "put",
                contentType: 'application/json',
                cache: false,
                dataType: "json",
                data: JSON.stringify({
                    username: Cookies.get("username"),
                    password: val
                }),
                success: function(res){
                    if (res.code == 200) {
                        layer.msg('修改密码成功，请您退出重登!', {icon: 1, time: 2000}, function(){
                        });
                        layer.close(index);
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
        });
    }
</script>
</body>
</html>