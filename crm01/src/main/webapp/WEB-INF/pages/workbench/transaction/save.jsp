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

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>

    <script>
        $(function () {
            $("#saveCreated").click(function () {
                var owner = $("#created-owner").val();
                var money = $("#created-money").val();
                var name = $("#created-name").val();
                var expectedDate = $("#created-expectedDate").val();
                var customerId = $("#created-customerId").val();
                var stage = $("#created-stage").val();
                alert(stage);
                var type = $("#created-type").val();
                alert(type)
                var source = $("#created-source").val();
                var activityId = $("#created-activityId").val();
                var contactsId = $("#created-contactsId").val();
                var description = $("#created-description").val();
                var contactSummary = $("#created-contactSummary").val();
                var nextContactTime = $("#created-nextContactTime").val();
                $.ajax({
                    url: "workbench/transaction/saveTransaction.do",
                    data: {
                        owner: owner,
                        money: money,
                        name: name,
                        expectedDate: expectedDate,
                        customerId: customerId,
                        stage: stage,
                        type: type,
                        source: source,
                        activityId: activityId,
                        contactsId: contactsId,
                        description: description,
                        contactSummary: contactSummary,
                        nextContactTime: nextContactTime
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.code == "1") {
                            window.location.href = "workbench/transaction/index.do";
                        } else {
                            alert(data.message)
                        }
                    }
                })
            })
            $("#created-nextContactTime").datetimepicker({
                language: "zh-CN",
                format: "yyyy-mm-dd",
                minView: "month",
                initialDate: new Date(),
                autoclose: true,
                todayBtn: true,
                clearBtn: true
            })
            $("#created-customerId").typeahead({
                source: function (query, process) {
                    $.ajax({
                        url: "workbench/transaction/getaccountName.do",
                        data: {
                            name: query
                        },
                        type: "post",
                        dataType: "json",
                        success: function (data) {
                            return process(data);
                        }
                    })
                }
            })
            $("#created-stage").change(function () {
                var selected = $("#created-stage option:selected").text();
                $.ajax({
                    url: "workbench/transaction/getPossibility.do",
                    data: {
                        possibility: selected
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        $("#create-possibility").val(data)
                    }
                })
            })
            $("#created-stage").change();
        })
    </script>
</head>
<body>

<!-- ?????????????????? -->
<div class="modal fade" id="findMarketActivity" role="dialog">
    <div class="modal-dialog" role="document" style="width: 80%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">??</span>
                </button>
                <h4 class="modal-title">??????????????????</h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                    <form class="form-inline" role="form">
                        <div class="form-group has-feedback">
                            <input type="text" class="form-control" style="width: 300px;"
                                   placeholder="????????????????????????????????????????????????">
                            <span class="glyphicon glyphicon-search form-control-feedback"></span>
                        </div>
                    </form>
                </div>
                <table id="activityTable3" class="table table-hover"
                       style="width: 900px; position: relative;top: 10px;">
                    <thead>
                    <tr style="color: #B3B3B3;">
                        <td></td>
                        <td>??????</td>
                        <td>????????????</td>
                        <td>????????????</td>
                        <td>?????????</td>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td><input type="radio" name="activity"/></td>
                        <td>?????????</td>
                        <td>2020-10-10</td>
                        <td>2020-10-20</td>
                        <td>zhangsan</td>
                    </tr>
                    <tr>
                        <td><input type="radio" name="activity"/></td>
                        <td>?????????</td>
                        <td>2020-10-10</td>
                        <td>2020-10-20</td>
                        <td>zhangsan</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- ??????????????? -->
<div class="modal fade" id="findContacts" role="dialog">
    <div class="modal-dialog" role="document" style="width: 80%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">??</span>
                </button>
                <h4 class="modal-title">???????????????</h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                    <form class="form-inline" role="form">
                        <div class="form-group has-feedback">
                            <input type="text" class="form-control" style="width: 300px;" placeholder="?????????????????????????????????????????????">
                            <span class="glyphicon glyphicon-search form-control-feedback"></span>
                        </div>
                    </form>
                </div>
                <table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
                    <thead>
                    <tr style="color: #B3B3B3;">
                        <td></td>
                        <td>??????</td>
                        <td>??????</td>
                        <td>??????</td>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td><input type="radio" name="activity"/></td>
                        <td>??????</td>
                        <td>lisi@bjpowernode.com</td>
                        <td>12345678901</td>
                    </tr>
                    <tr>
                        <td><input type="radio" name="activity"/></td>
                        <td>??????</td>
                        <td>lisi@bjpowernode.com</td>
                        <td>12345678901</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>


<div style="position:  relative; left: 30px;">
    <h3>????????????</h3>
    <div style="position: relative; top: -40px; left: 70%;">
        <button type="button" id="saveCreated" class="btn btn-primary">??????</button>
        <button type="button" class="btn btn-default">??????</button>
    </div>
    <hr style="position: relative; top: -40px;">
</div>
<form class="form-horizontal" role="form" style="position: relative; top: -30px;">
    <div class="form-group">
        <label for="created-owner" class="col-sm-2 control-label">?????????<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="created-owner">
                <c:forEach items="${requestScope.users}" var="u">
                    <option value="${u.id}">${u.name}</option>
                </c:forEach>
            </select>
        </div>
        <label for="created-money" class="col-sm-2 control-label">??????</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="created-money">
        </div>
    </div>

    <div class="form-group">
        <label for="created-name" class="col-sm-2 control-label">??????<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="created-name">
        </div>
        <label for="created-expectedDate" class="col-sm-2 control-label">??????????????????<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="created-expectedDate">
        </div>
    </div>

    <div class="form-group">
        <label for="created-customerId" class="col-sm-2 control-label">????????????<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="created-customerId" placeholder="???????????????????????????????????????????????????">
        </div>
        <label for="created-stage" class="col-sm-2 control-label">??????<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="created-stage">
                <c:forEach items="${requestScope.stage}" var="s">
                    <option id="option_${s.id}" value="${s.id}">${s.value}</option>
                </c:forEach>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label for="created-type" class="col-sm-2 control-label">??????</label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="created-type">
                <c:forEach items="${requestScope.transactionType}" var="t">
                    <option value="${t.id}">${t.value}</option>
                </c:forEach>
            </select>
        </div>
        <label for="create-possibility" class="col-sm-2 control-label">?????????</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-possibility">
        </div>
    </div>

    <div class="form-group">
        <label for="created-source" class="col-sm-2 control-label">??????</label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="created-source">
                <c:forEach items="${requestScope.source}" var="s">
                    <option value="${s.id}">${s.value}</option>
                </c:forEach>
            </select>
        </div>
        <label for="create-activitySrc" class="col-sm-2 control-label">???????????????&nbsp;&nbsp;<a href="javascript:void(0);"
                                                                                           data-toggle="modal"
                                                                                           data-target="#findMarketActivity"><span
                class="glyphicon glyphicon-search"></span></a></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-activitySrc">
        </div>
    </div>

    <div class="form-group">
        <label for="created-contactsId" class="col-sm-2 control-label">???????????????&nbsp;&nbsp;<a href="javascript:void(0);"
                                                                                           data-toggle="modal"
                                                                                           data-target="#findContacts"><span
                class="glyphicon glyphicon-search"></span></a></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="created-contactsId">
        </div>
    </div>

    <div class="form-group">
        <label for="created-description" class="col-sm-2 control-label">??????</label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea class="form-control" rows="3" id="created-description"></textarea>
        </div>
    </div>

    <div class="form-group">
        <label for="created-contactSummary" class="col-sm-2 control-label">????????????</label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea class="form-control" rows="3" id="created-contactSummary"></textarea>
        </div>
    </div>

    <div class="form-group">
        <label for="created-nextContactTime" class="col-sm-2 control-label">??????????????????</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="created-nextContactTime">
        </div>
    </div>

</form>
</body>
</html>