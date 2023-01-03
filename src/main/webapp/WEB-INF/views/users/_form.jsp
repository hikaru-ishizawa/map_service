<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="action" value="${ForwardConst.ACT_USER.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />

<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br />
        </c:forEach>

    </div>
</c:if>
<label for="${AttributeConst.USER_NAME.getValue()}">ユーザー名</label><br />
<input type="text" name="${AttributeConst.USER_NAME.getValue()}" id="${AttributeConst.USER_NAME.getValue()}" value="${user.name}" />
<br /><br />

<label for="${AttributeConst.USER_PASS.getValue()}">パスワード</label><br />
<input type="password" name="${AttributeConst.USER_PASS.getValue()}" id="${AttributeConst.USER_PASS.getValue()}" />
<br /><br />

<c:if test="${sessionScope.login_user != null}">
    <c:if
        test="${sessionScope.login_user.adminFlag == AttributeConst.ROLE_ADMIN.getIntegerValue()}">
        <label for="${AttributeConst.USER_ADMIN_FLG.getValue()}">権限</label>
        <br />
        <select name="${AttributeConst.USER_ADMIN_FLG.getValue()}"
            id="${AttributeConst.USER_ADMIN_FLG.getValue()}">
            <option value="${AttributeConst.ROLE_GENERAL.getIntegerValue()}"
                <c:if test="${user.adminFlag == AttributeConst.ROLE_GENERAL.getIntegerValue()}"> selected</c:if>>一般</option>
            <option value="${AttributeConst.ROLE_ADMIN.getIntegerValue()}"
                <c:if test="${user.adminFlag == AttributeConst.ROLE_ADMIN.getIntegerValue()}"> selected</c:if>>管理者</option>
        </select>
        <br />
        <br />
    </c:if>
</c:if>

<input type="hidden" name="${AttributeConst.USER_NAME.getValue()}" value="${user.name}" />
<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
<button type="submit">登録</button>