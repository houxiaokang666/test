<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <% String path = "" + request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";%>
    ;
    <base href="<%=path%>"/>
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

    <script type="text/javascript">

        //默认情况下取消和保存按钮是隐藏的
        var cancelAndSaveBtnDefault = true;

        $(function () {
            $("#father").on("click", "a[name='editA']", function () {
                var remarkId = $(this).attr("remarkId");
                $("#remarkId").val(remarkId);
                var h5 = $("#h5_" + remarkId).text();
                $("#noteContent").val(h5);
                $("#editRemarkModal").modal("show");
            });
            $("#updateRemarkBtn").click(function () {
                var remarkId = $("#remarkId").val();
                var noteContent = $("#noteContent").val();
                if (noteContent == "") {
                    alert("备注内容不能为空");
                    return;
                }
                ;
                $.ajax({
                    url: "workbench/activity/saveEditRemark.do",
                    data: {
                        id: remarkId,
                        noteContent: noteContent
                    },
                    type: "post",
                    dataType: "json",
                    success: function (date) {
                        if (date.code == "1") {
                            $("#editRemarkModal").modal("hide");
                            $("#h5_" + remarkId).text(date.data.noteContent);
                            $("#small_" + remarkId).text(date.data.editTime + "由${sessionScope.user.name}修改");
                        } else {
                            alert(date.message);
                        }
                        ;
                    }
                })
            });
            // 用户在市场活动明细页面,点击"删除"市场活动备注的图标,完成删除市场活动备注的功能.
            // *删除成功之后,刷新备注列表
            // *删除失败,提示信息,备注列表不刷新
            $("#father").on("click", "a[name='deleteA']", function () {
                var remarksId = $(this).attr("remarkId");
                $.ajax({
                    url: "workbench/activity/delRemark.do",
                    data: {
                        remarksId: remarksId,
                    },
                    type: "post",
                    dataType: "json",
                    success: function (date) {
                        if (date.code == "1") {
                            $("#div_" + remarksId).remove();
                        } else {
                            alert(date.message);
                        }
                    }
                })
            });

            $("#save").click(function () {
                var noteContent = $.trim($("#remark").val());
                var activityId = '${requestScope.activity.id}';
                if (noteContent == "") {
                    alert("备注信息不能为空");
                    return
                }
                $.ajax({
                    url: "workbench/activity/saveRemark.do",
                    data: {
                        noteContent: noteContent,
                        activityId: activityId
                    },
                    type: "post",
                    dataType: "json",
                    success: function (date) {
                        if (date.code == "1") {
                            alert("保存成功");
                            var appendhtml = "";
                            appendhtml += "<div class=\"remarkDiv\" id=\"div_" + date.data.id + "\" style=\"height: 60px;\">";
                            appendhtml += "<img title=\"${sessionScope.user.name}\" src=\"image/user-thumbnail.png\" style=\"width: 30px; height:30px;\">";
                            appendhtml += "<div style=\"position: relative; top: -40px; left: 40px;\" >";
                            appendhtml += "<h5 id=\"h5_" + date.data.id + "\">" + date.data.noteContent + "</h5>";
                            appendhtml += "<font color=\"gray\">市场活动</font> <font color=\"gray\">-</font> <b>${requestScope.activity.name}</b> <small id=\"small_" + date.data.id + "\"  style=\"color: gray;\"> " + date.data.createTime + " 由${sessionScope.user.name}:\"创建\"</small>";
                            appendhtml += "<div style=\"position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;\">";
                            appendhtml += "<a class=\"myHref\" name=\"editA\" remarkId=\"" + date.data.id + "\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-edit\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
                            appendhtml += "&nbsp;&nbsp;&nbsp;&nbsp;";
                            appendhtml += "<a class=\"myHref\" name=\"deleteA\" remarkId=\"" + date.data.id + "\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-remove\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
                            appendhtml += "</div>";
                            appendhtml += "</div>";
                            appendhtml += "</div>";
                            $("#remark").val("");
                            $("#remarkDiv").before(appendhtml);
                        } else {
                            alert(date.message);
                        }
                    }
                })
            });
            $("#remark").focus(function () {
                if (cancelAndSaveBtnDefault) {
                    //设置remarkDiv的高度为130px
                    $("#remarkDiv").css("height", "130px");
                    //显示
                    $("#cancelAndSaveBtn").show("2000");
                    cancelAndSaveBtnDefault = false;
                }
            });

            $("#cancelBtn").click(function () {
                //显示
                $("#cancelAndSaveBtn").hide();
                //设置remarkDiv的高度为130px
                $("#remarkDiv").css("height", "90px");
                cancelAndSaveBtnDefault = true;
            });
            $("#father").on("mouseover", ".remarkDiv", function () {
                $(this).children("div").children("div").show();
            });
            // $(".remarkDiv").mouseover(function(){
            // 	$(this).children("div").children("div").show();
            // });

            $("#father").on("mouseout", ".remarkDiv", function () {
                $(this).children("div").children("div").hide();
            });
            // $(".remarkDiv").mouseout(function(){
            // 	$(this).children("div").children("div").hide();
            // });

            $("#father").on("mouseover", ".myHref", function () {
                $(this).children("span").css("color", "red");
            });
            // $(".myHref").mouseover(function(){
            // 	$(this).children("span").css("color","red");
            // });

            $("#father").on("mouseout", ".myHref", function () {
                $(this).children("span").css("color", "#E6E6E6");
            });
            // $(".myHref").mouseout(function(){
            // 	$(this).children("span").css("color","#E6E6E6");
            // });
        });

    </script>

</head>
<body>
<!-- 修改市场活动备注的模态窗口 -->
<div class="modal fade" id="editRemarkModal" role="dialog">
    <%-- 备注的id --%>
    <input type="hidden" id="remarkId">
    <div class="modal-dialog" role="document" style="width: 40%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">修改备注</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="noteContent" class="col-sm-2 control-label">内容</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="noteContent"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
            </div>
        </div>
    </div>
</div>


<!-- 返回按钮 -->
<div style="position: relative; top: 35px; left: 10px;">
    <a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left"
                                                                         style="font-size: 20px; color: #DDDDDD"></span></a>
</div>

<!-- 大标题 -->
<div style="position: relative; left: 40px; top: -30px;">
    <div class="page-header">
        <h3>市场活动-${requestScope.activity.name} <small>${requestScope.activity.startDate}
            ~ ${requestScope.activity.endDate}</small></h3>
    </div>

</div>

<br/>
<br/>
<br/>

<!-- 详细信息 -->
<div style="position: relative; top: -70px;">
    <div style="position: relative; left: 40px; height: 30px;">
        <div style="width: 300px; color: gray;">所有者</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${requestScope.activity.owner}</b>
        </div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${requestScope.activity.name}</b>
        </div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>

    <div style="position: relative; left: 40px; height: 30px; top: 10px;">
        <div style="width: 300px; color: gray;">开始日期</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${requestScope.activity.startDate}</b>
        </div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${requestScope.activity.endDate}</b>
        </div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 20px;">
        <div style="width: 300px; color: gray;">成本</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${requestScope.activity.cost}</b>
        </div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 30px;">
        <div style="width: 300px; color: gray;">创建者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${requestScope.activity.createBy}&nbsp;&nbsp;</b><small
                style="font-size: 10px; color: gray;">${requestScope.activity.createTime}</small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 40px;">
        <div style="width: 300px; color: gray;">修改者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${requestScope.activity.editBy}&nbsp;&nbsp;</b><small
                style="font-size: 10px; color: gray;">${requestScope.activity.editTime}</small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 50px;">
        <div style="width: 300px; color: gray;">描述</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b>${requestScope.activity.description} </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
</div>

<!-- 备注 -->
<div id="father" style="position: relative; top: 30px; left: 40px;">
    <div class="page-header">
        <h4>备注</h4>
    </div>

    <!-- 备注1 -->
    <c:forEach items="${requestScope.remarks}" var="u">
        <div class="remarkDiv" id="div_${u.id}" style="height: 60px;">
            <img title="${u.createBy}" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
            <div style="position: relative; top: -40px; left: 40px;">
                <h5 id="h5_${u.id}">${u.noteContent}</h5>
                <font color="gray">市场活动</font> <font color="gray">-</font> <b>${requestScope.activity.name}</b> <small
                <small id="small_${u.id}" style="color: gray;"> ${u.editFlag=="1"?u.editTime:u.createTime}
                    由${u.editFlag=="1"?u.editBy:u.createBy}${u.editFlag=="1"?"修改":"创建"}</small>
                <div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
                    <a class="myHref" name="editA" remarkId="${u.id}" href="javascript:void(0);"><span
                            class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <a class="myHref" name="deleteA" remarkId="${u.id}" href="javascript:void(0);"><span
                            class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
                </div>
            </div>
        </div>
    </c:forEach>

    <div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
        <form role="form" style="position: relative;top: 10px; left: 10px;">
            <textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"
                      placeholder="添加备注..."></textarea>
            <p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
                <button id="cancelBtn" type="button" class="btn btn-default">取消</button>
                <button type="button" class="btn btn-primary" ID="save">保存</button>
            </p>
        </form>
    </div>
</div>
<div style="height: 200px;"></div>
</body>
</html>