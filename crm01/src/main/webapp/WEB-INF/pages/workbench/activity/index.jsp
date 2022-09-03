<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <% String path = "" + request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";%>
    ;
    <base href="<%=path%>"/>
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
          rel="stylesheet"/>
    <link href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>
    <script type="text/javascript">
        $(function () {
            $("#creatform")[0].reset();
            $("#create").click(function () {
                $("#createActivityModal").modal("show")
            })
            $("#checkedAll").click(function () {
                $("#htmlstr input[type='checkbox']").prop("checked", this.checked)
            })
            $("#htmlstr").on("click", "input[type='checkbox']", function () {
                if ($("#htmlstr input[type='checkbox']").size() == $("#htmlstr input[type='checkbox']:checked").size()) {
                    $("#checkedAll").prop("checked", true);
                } else {
                    $("#checkedAll").prop("checked", false);
                }
            })
            $("#exportActivityAllBtn").click(function () {
                window.location.href = "workbench/activity/exportActivitAll.do";
            })
            $("#exportActivityXzBtn").click(function () {
                var ids = $("#htmlstr input[type='checkbox']:checked");
                if (ids == null || ids.size() == 0) {
                    alert("请至少选择一条市场活动")
                    return
                }
                var id = "";
                $.each(ids, function () {
                    id += "id=" + this.value + "&";
                })
                window.location.href = "workbench/activity/exportActivitById.do" + "?" + id;
            })
            $("#importActivityBtn").click(function () {
                // *只支持.xls
                // 	*文件大小不超过5MB
                // 	*导入成功之后,提示成功导入记录条数,关闭模态窗口,刷新市场活动列表,显示第一页数据,保持每页显示条数不变
                // 	*导入失败,提示信息,模态窗口不关闭,列表也不刷新
                var activityFileName = $("#activityFile").val();
                var activityFile = $("#activityFile")[0].files[0];
                var extendName = activityFileName.substr(activityFileName.lastIndexOf("."));
                alert(activityFile.size);
                if (extendName != ".xls") {
                    alert("仅支持xls文件");
                    return;
                }
                if (activityFile.size > 5 * 1024 * 1024) {
                    alert("文件大小不超过5MB");
                    return;
                }
                var formData = new FormData();
                formData.append("multipartFile", activityFile)
                $.ajax({
                    url: "workbench/activity/importActivit.do",
                    processData: false,
                    contentType: false,
                    data: formData,
                    type: "post",
                    async: false,
                    dataType: "json",
                    success: function (date) {
                        alert(1);
                        if (date.code == "1") {
                            alert("成功导入" + date.data + "条数据")
                            $("#importActivityModal").modal("hide")
                            queryActivity(1, $("#pagination").bs_pagination('getOption', 'rowsPerPage'));
                        } else {
                            alert(date.message)
                        }
                    }
                })
            })
            $("#save").click(function () {
                var reg = /^(([1-9]\d*)|0)$/;
                var Owner = $.trim($("#create-marketActivityOwner").val());
                var Activity = $.trim($("#create-marketActivityName").val());
                var startTime = $.trim($("#create-startTime").val());
                var endTime = $.trim($("#create-endTime").val());
                var cost = $.trim($("#create-cost").val());
                var describe = $.trim($("#create-describe").val());
                if (Owner == "") {
                    alert("所有者不能为空")
                    return;
                }
                if (Activity == "") {
                    alert("市场活动名称不能为空")
                    return;
                }
                if (startTime != "" && endTime != "") {
                    if (endTime < startTime) {
                        alert("结束日期不能小于开始日期")
                        return;
                    }
                }
                if (!reg.test(cost)) {
                    alert("成本只能为非负整数")
                    return;
                }
                $.ajax({
                    url: "workbench/activity/save.do",
                    data: {
                        owner: Owner,
                        name: Activity,
                        startDate: startTime,
                        endDate: endTime,
                        cost: cost,
                        description: describe
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.code == "1") {
                            // 创建成功之后,关闭模态窗口,
                            $("#createActivityModal").modal("hide")
                            //刷新市场活动列，显示第一页数据，保持每页显示条数不变
                            queryActivity(1, $("#pagination").bs_pagination('getOption', 'rowsPerPage'))
                        } else {
                            // *创建失败,提示信息创建失败,模态窗口不关闭,市场活动列表也不刷新
                            alert(data.message)
                        }
                    },
                })
            })
            $(".mydate").datetimepicker({
                language: "zh-CN",
                format: "yyyy-mm-dd",
                minView: "month",
                initialDate: new Date(),
                autoclose: true,
                todayBtn: true,
                clearBtn: true
            })
            queryActivity(1, 10);
            $("#query").click(function () {
                queryActivity(1, $("#pagination").bs_pagination('getOption', 'rowsPerPage'));
            })
            $("#delete").click(function () {
                var ids = $("#htmlstr input[type='checkbox']:checked");
                var id = "";
                if (ids.size() == 0) {
                    alert("请选择要删除市场活动")
                    return;
                }
                if (window.confirm("确认删除么？")) {
                    $.each(ids, function () {
                        id += "id=" + this.value + "&";
                    })
                }
                $.ajax({
                    url: "workbench/activity/deleteActivity.do",
                    data: id,
                    type: "post",
                    dataType: "json",
                    success: function (date) {
                        if (date.code == 1) {
                            queryActivity(1, $("#pagination").bs_pagination('getOption', 'rowsPerPage'));
                        } else {
                            alert(date.message)
                        }
                    }
                })
            })

            $("#alter").click(function () {
                var val = $("#htmlstr input[type='checkbox']:checked");
                if (val.size() == 0) {
                    alert("请选择要修改的市场活动")
                    return;
                }
                if (val.size() > 1) {
                    alert("只能选择一个市场活动")
                    return;
                }
                var value = val[0].value;
                $.ajax({
                    url: "workbench/activity/queryActivityById.do",
                    data: {
                        id: value
                    },
                    type: "post",
                    dataType: "json",
                    success: function (date) {
                        $("#edit-marketActivityName").val(date.data.name)
                        $("#edit-marketActivityOwner").val(date.data.owner)
                        $("#edit-startTime").val(date.data.startDate)
                        $("#edit-endTime").val(date.data.endDate)
                        $("#edit-cost").val(date.data.cost)
                        $("#edit-describe").val(date.data.description)
                        $("#editActivityModal").modal("show");
                        $("#Activityid").val(date.data.id);
                    }
                })
            })

            $("#edit").click(function () {
                var id = $("#Activityid").val();
                var reg = /^(([1-9]\d*)|0)$/;
                var Owner = $.trim($("#edit-marketActivityOwner").val());
                var Activity = $.trim($("#edit-marketActivityName").val());
                var startTime = $.trim($("#edit-startTime").val());
                var endTime = $.trim($("#edit-endTime").val());
                var cost = $.trim($("#edit-cost").val());
                var describe = $.trim($("#edit-describe").val());
                if (Owner == "") {
                    alert("所有者不能为空")
                    return;
                }
                if (Activity == "") {
                    alert("市场活动名称不能为空")
                    return;
                }
                if (startTime != "" && endTime != "") {
                    if (endTime < startTime) {
                        alert("结束日期不能小于开始日期")
                        return;
                    }
                }
                if (!reg.test(cost)) {
                    alert("成本只能为非负整数")
                    return;
                }
                $.ajax({
                    url: "workbench/activity/UpdateActivityById.do",
                    data: {
                        id: id,
                        owner: Owner,
                        name: Activity,
                        startDate: startTime,
                        endDate: endTime,
                        cost: cost,
                        description: describe
                    },
                    type: "post",
                    dataType: "json",
                    success: function (date) {
                        if (date.code == 1) {
                            $("#editActivityModal").modal("hide")
                            //刷新市场活动列，显示第一页数据，保持每页显示条数不变
                            queryActivity($("#pagination").bs_pagination('getOption', 'currentPage'), $("#pagination").bs_pagination('getOption', 'rowsPerPage'))
                        } else {
                            alert(date.message)
                        }
                    }
                })
            })
        });

        function queryActivity(pagenum, pagesize) {
            var name = $("#name").val();
            var owner = $("#owner").val();
            var startDate = $("#startDate").val();
            var endDate = $("#endDate").val();
            $.ajax({
                url: "workbench/activity/queryActivity.do",
                data: {
                    name: name,
                    owner: owner,
                    start_date: startDate,
                    end_date: endDate,
                    pagenum: pagenum,
                    pagesize: pagesize
                },
                type: "post",
                dataType: "json",
                success: function (data) {
                    $("#totalnum").text(data.num);
                    var text = "";
                    $.each(data.list, function (index, obj) {
                        text += "<tr class=\"active\">"
                        text += "<td><input type=\"checkbox\" value=" + obj.id + " /></td>"
                        text += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/activity/queryActivityForDetail.do?id=" + obj.id + "'\">" + obj.name + "</a></td>"
                        text += "<td>" + obj.owner + "</td>"
                        text += "<td>" + obj.startDate + "</td>"
                        text += "<td>" + obj.endDate + "</td>"
                        text += "</tr>"
                    })
                    $("#htmlstr").html(text);
                    $("#checkedAll").prop("checked", false);
                    var totalPage = data.num % pagesize == 0 ? data.num / pagesize : parseInt(data.num / pagesize + 1);
                    $("#pagination").bs_pagination({
                        currentPage: pagenum, //当前从第几行开始查，相当于pageNO
                        rowsPerPage: pagesize, //每页显示几行数据，相当于pageSize
                        totalPages: totalPage, //总页数，不能自己赋值，要根据数据库查询到的结果计算后赋值变量
                        totalRows: data.num,//总行数，也是变量
                        visiblePageLinks: 5,//最多可以显示的卡片数
                        onChangePage: function (event, pageobj) {
                            queryActivity(pageobj.currentPage, pageobj.rowsPerPage);
                        }
                    })
                }
            })
        }
    </script>
</head>
<body>

<!-- 创建市场活动的模态窗口 -->
<div class="modal fade" id="createActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
            </div>
            <div class="modal-body">

                <form id="creatform" class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-marketActivityOwner">
                                <c:forEach items="${requestScope.userall}" var="u">
                                    <option value="${u.id}">${u.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-marketActivityName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-startTime" class="col-sm-2 control-label ">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control mydate" readonly id="create-startTime">
                        </div>
                        <label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control mydate" id="create-endTime" readonly>
                        </div>
                    </div>
                    <div class="form-group">

                        <label for="create-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-cost">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-describe"></textarea>
                        </div>
                    </div>
                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="save">保存</button>
            </div>
        </div>
    </div>
</div>

<!-- 修改市场活动的模态窗口 -->
<div class="modal fade" id="editActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form">
                    <input type="hidden" id="Activityid">
                    <div class="form-group">
                        <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-marketActivityOwner">
                                <c:forEach items="${requestScope.userall}" var="u">
                                    <option value="${u.id}">${u.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-startTime" value="2020-10-10">
                        </div>
                        <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-endTime" value="2020-10-20">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-cost" value="5,000">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="edit">更新</button>
            </div>
        </div>
    </div>
</div>

<!-- 导入市场活动的模态窗口 -->
<div class="modal fade" id="importActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
            </div>
            <div class="modal-body" style="height: 350px;">
                <div style="position: relative;top: 20px; left: 50px;">
                    请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                </div>
                <div style="position: relative;top: 40px; left: 50px;">
                    <input type="file" id="activityFile">
                </div>
                <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;">
                    <h3>重要提示</h3>
                    <ul>
                        <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                        <li>给定文件的第一行将视为字段名。</li>
                        <li>请确认您的文件大小不超过5MB。</li>
                        <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                        <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                        <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                        <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                    </ul>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
            </div>
        </div>
    </div>
</div>


<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>市场活动列表</h3>
        </div>
    </div>
</div>
<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input id="name" class="form-control" type="text">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input id="owner" class="form-control" type="text">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">开始日期</div>
                        <input class="form-control" type="text" id="startDate"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">结束日期</div>
                        <input class="form-control" type="text" id="endDate">
                    </div>
                </div>

                <button type="button" class="btn btn-default" id="query">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" id="create" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span>
                    创建
                </button>
                <button type="button" class="btn btn-default" id="alter"><span
                        class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button type="button" class="btn btn-danger" id="delete"><span class="glyphicon glyphicon-minus"></span>
                    删除
                </button>
            </div>
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal">
                    <span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）
                </button>
                <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span
                        class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）
                </button>
                <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span
                        class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）
                </button>
            </div>
        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="checkedAll"/></td>
                    <td>名称</td>
                    <td>所有者</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                </tr>
                </thead>
                <tbody id="htmlstr">
                <%--						<tr class="active">--%>
                <%--							<td><input type="checkbox" /></td>--%>
                <%--							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>--%>
                <%--                            <td>zhangsan</td>--%>
                <%--							<td>2020-10-10</td>--%>
                <%--							<td>2020-10-20</td>--%>
                <%--						</tr>--%>
                <%--                        <tr class="active">--%>
                <%--                            <td><input type="checkbox" /></td>--%>
                <%--                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>--%>
                <%--                            <td>zhangsan</td>--%>
                <%--                            <td>2020-10-10</td>--%>
                <%--                            <td>2020-10-20</td>--%>
                <%--                        </tr>--%>
                </tbody>
            </table>
        </div>

        <div id="pagination"></div>
        <%--			<div style="height: 50px; position: relative;top: 30px;">--%>
        <%--				<div>--%>
        <%--					<button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalnum">50</b>条记录</button>--%>
        <%--				</div>--%>
        <%--				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">--%>
        <%--					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>--%>
        <%--					<div class="btn-group">--%>
        <%--						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">--%>
        <%--							10--%>
        <%--							<span class="caret"></span>--%>
        <%--						</button>--%>
        <%--						<ul class="dropdown-menu" role="menu">--%>
        <%--							<li><a href="#">20</a></li>--%>
        <%--							<li><a href="#">30</a></li>--%>
        <%--						</ul>--%>
        <%--					</div>--%>
        <%--					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>--%>
        <%--				</div>--%>
        <%--				<div style="position: relative;top: -88px; left: 285px;">--%>
        <%--					<nav>--%>
        <%--						<ul class="pagination">--%>
        <%--							<li class="disabled"><a href="#">首页</a></li>--%>
        <%--							<li class="disabled"><a href="#">上一页</a></li>--%>
        <%--							<li class="active"><a href="#">1</a></li>--%>
        <%--							<li><a href="#">2</a></li>--%>
        <%--							<li><a href="#">3</a></li>--%>
        <%--							<li><a href="#">4</a></li>--%>
        <%--							<li><a href="#">5</a></li>--%>
        <%--							<li><a href="#">下一页</a></li>--%>
        <%--							<li class="disabled"><a href="#">末页</a></li>--%>
        <%--						</ul>--%>
        <%--					</nav>--%>
        <%--				</div>--%>
        <%--			</div>--%>

    </div>

</div>
</body>
</html>