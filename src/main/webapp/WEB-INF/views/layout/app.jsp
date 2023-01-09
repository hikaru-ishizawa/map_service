<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="constants.AttributeConst"%>
<%@ page import="constants.ForwardConst"%>
<c:set var="actMap" value="${ForwardConst.ACT_MAP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="actAuth" value="${ForwardConst.ACT_AUTH.getValue()}" />
<c:set var="commOut" value="${ForwardConst.CMD_LOGOUT.getValue()}" />
<c:set var="commNew" value="${ForwardConst.CMD_NEW.getValue()}" />
<c:set var="actUser" value="${ForwardConst.ACT_USER.getValue()}" />
<c:set var="commShowLogin" value="${ForwardConst.CMD_SHOW_LOGIN.getValue()}" />
<c:set var="actBookMark" value="${ForwardConst.ACT_BOOKMARK.getValue()}" />

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
    <title><c:out value="map_service" /></title>
    <link rel="stylesheet" href="<c:url value='/css/reset.css' />">
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
</head>
<body>
    <div id="wrapper">
        <div id="header">
            <div id="header_menu">
                <h1><a href="<c:url value='?action=${actMap}&command=${commIdx}' />">map_service</a></h1>&nbsp;

                <!-- ログインした従業員の権限によって表示メニューを変える -->
                <c:if test="${sessionScope.login_user != null}">
                    <c:if test="${sessionScope.login_user.adminFlag == AttributeConst.ROLE_ADMIN.getIntegerValue()}">
                        <a href="<c:url value='?action=${actUser}&command=${commIdx}' />">&nbsp;&nbsp;ユーザー管理</a>&nbsp;
                        <a href="<c:url value='?action=${actBookMark}&command=${commIdx}' />">&nbsp;&nbsp;ブックマーク</a>&nbsp;
                    </c:if>
                </c:if>
            </div>
            <c:if test="${sessionScope.login_user != null}">
                <div id="user_name">
                    <c:out value="${sessionScope.login_user.name}" />
                    さん&nbsp;&nbsp;&nbsp;
                    <a href="<c:url value='?action=${actAuth}&command=${commOut}' />">ログアウト</a>
                </div>
            </c:if>
            <c:if test="${sessionScope.login_user == null}">
                <div id="user_name">

                    ゲストさん&nbsp;&nbsp;&nbsp;
                    <a href="<c:url value='?action=${actAuth}&command=${commShowLogin}' />">ログイン</a>
                </div>
            </c:if>
        </div>
        <div id="content">${param.content}</div>
        <div id="footer"> </div>

    </div>
</body>
</html>