<#assign  sec=JspTaglibs["http://www.springframework.org/security/tags"] />
<!doctype html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>兑换码生成</title>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="${ctx.contextPath}/webjars/bootstrap/3.3.7/css/bootstrap.min.css" />
    <link rel="stylesheet" href="${ctx.contextPath}/webjars/Eonasdan-bootstrap-datetimepicker/4.17.47/css/bootstrap-datetimepicker.min.css" />
    <link rel="stylesheet" href="//unpkg.com/layui@2.6.8/dist/css/layui.css">

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
    <script src="${ctx.contextPath}/webjars/momentjs/2.10.3/min/moment-with-locales.min.js"></script>
    <script src="${ctx.contextPath}/webjars/Eonasdan-bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>
    <script src="${ctx.contextPath}/webjars/js-cookie/2.2.1/js.cookie.min.js"></script>
    <script src="//unpkg.com/layui@2.6.8/dist/layui.js"></script>
</head>
<body>
<div class="container">
    <div style="text-align: right;">
        <a class="btn btn-link" href="${ctx.contextPath}/grammarlyAccounts">账户管理</a>
        <button type="button" class="btn btn-link" onclick="logout()">退出登录</button>
    </div>
    <div style="margin-top: 60px;"></div>
    <div class="demoTable">
        兑换码：
        <div class="layui-inline">
            <input class="layui-input" placeholder="精确匹配" name="number" id="code" autocomplete="off">
        </div>
        接收邮箱：
        <div class="layui-inline">
            <input class="layui-input" placeholder="模糊匹配" name="receiveEmail" id="receiveEmail" autocomplete="off">
        </div>
        兑换状态：
        <div class="layui-input-inline">
            <select name="status" id="status">
                <option value=""></option>
                <option value="0">未兑换</option>
                <option value="1">兑换成功</option>
                <option value="2">兑换出错</option>
                <option value="3">兑换过期</option>
            </select>
        </div>
        <button class="layui-btn" data-type="reload">搜索</button>
    </div>
    <script type="text/html" id="toolbarDemo">
        <div class="layui-btn-container">
            <@sec.authorize access="hasRole('ROLE_ADMIN')">
            <button class="layui-btn layui-btn-sm" onclick="gen()">产生兑换码</button>
            </@sec.authorize>
        </div>
    </script>
    <table class="layui-hide" id="test" lay-filter="test"></table>

    <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="exampleModalLabel">产生兑换码</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label for="account" class="col-sm-4 control-label"><span style="color: red">*</span>生成数量（单位：个）</label>
                            <div class="col-sm-8">
                                <input type="number" required class="form-control" id="count" placeholder="必填项，例如：1，5">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="account" class="col-sm-4 control-label">截止兑换日期</label>
                            <div class="col-sm-8">
                                <div class='input-group date' id='datetimepicker1'>
                                    <input type="text" class="form-control" id="deadline" placeholder="选填项：兑换截止日期">
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
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
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </div>
    </div>


</div>
<script type="text/javascript">
    layui.use(['layer', 'table', 'form'], function(){
        var layer = layui.layer
            ,table = layui.table;
        var $ = layui.$, active = {
            reload: function(){
                var whereObj = {};
                var code = $.trim($('#code').val());
                var receiveEmail = $.trim($('#receiveEmail').val());
                if (code != "") {
                    whereObj.code = code;
                }
                if (receiveEmail != "") {
                    whereObj.receiveEmail = receiveEmail;
                }
                whereObj.status = $("#status").val();
                // 执行重载,重新从第 1 页开始
                table.reload('test', {
                    page: {
                        curr: 1,
                        limit: ins1.config.limit
                    }
                    ,where: whereObj
                });
            }
        };

       var ins1 = table.render({
            elem: '#test'
            ,toolbar: '#toolbarDemo' //开启头部工具栏，并为其绑定左侧模板
            ,url:'${ctx.contextPath}/questionExchangeCodes'
            ,cols: [[
                 // {type: 'checkbox', fixed: 'left'}
                ,{field:'id', width:80, title: 'ID', hide: true, fixed: 'left' }
                ,{type: 'numbers', title: '序号', width: 40, fixed: 'left' }//序号列
                ,{field:'code', width:300, title: '兑换码', fixed: 'left' }
                ,{field:'createTime', width:180, title: '生成时间'}
                ,{field:'deadline', width:120, title: '截止兑换日'}
                ,{field:'receiveEmail', minWidth: 200, title: '接收邮箱'}
                ,{field:'status', width:90, title: '兑换状态'
                   , templet: function(d){
                    if (d.status == '0') {
                        return '未兑换'
                    } else if (d.status == '1') {
                        return '兑换成功'
                    } else if (d.status == '2') {
                        return '兑换出错'
                    } else if (d.status == '3') {
                        return '兑换过期'
                    }
                   }}
                ,{field:'updateTime', width:180, title: '兑换时间'}
                ,{field:'errmsg', minWidth: 200, title: '出错详情'}
                // ,{field:'account', width:200, title: '解锁账号'}
            ]]
            ,page: true
            ,limits: [10,20,50,100]
        });

        $('.demoTable .layui-btn').on('click', function(){
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });

        window.gen = function() {
            $("#count").val("");
            $("#deadline").val("");
            $("#result").empty();
            $('#exampleModal').modal('show');
        }

        window.remove = function() {
            var checkStatus = table.checkStatus('test');
            if (checkStatus.data.length === 0) {
                layer.msg('请选择一行');
                return;
            }
            var data = checkStatus.data; //获取选中的数据
            var ids = [];
            for (let row of data) {
                ids.push(row.id);
            }
            layer.prompt(function(val,index){
                layer.load();
                $.ajax({
                    url: "${ctx.contextPath}/exchangeCode?reason=" + val,
                    type: "delete",
                    contentType: 'application/json',
                    cache: false,
                    dataType: "json",
                    data: JSON.stringify(ids),
                    success: function(res){
                        if (res.code == 200) {
                            if (res.data) {
                                layer.msg('删除成功!', {icon: 1, time: 1000}, function(){
                                    $('.demoTable .layui-btn').trigger("click");
                                });
                            } else {
                                layer.alert('删除失败，详见后台日志', {icon: 5, time: 5000});
                            }
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
                layer.close(index);
            });
        }

        window.mysubmit = function() {
            let count = $("#count").val();
            if (count === "") {
                layer.msg("生成数量不能为空", function(){});
                return false;
            }
            let deadline = $("#deadline").val();
            // if (deadline === "") {
            //     layer.msg("截止兑换日期不能为空", function(){});
            //     return false;
            // }
            let data = {
                count: count,
                deadline: deadline
            }
            layer.load();
            $("#result").empty();
            $.ajax({
                url: "${ctx.contextPath}/py/questionExchangeCode/gen",
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
                            $('.demoTable .layui-btn').trigger("click");
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
        $('#datetimepicker1').datetimepicker(options);

    });

    function logout() {
        Cookies.remove("gp-token");
        Cookies.remove("gp-username");
        location.href = "${ctx.contextPath}/login";
    }
</script>
</body>
</html>