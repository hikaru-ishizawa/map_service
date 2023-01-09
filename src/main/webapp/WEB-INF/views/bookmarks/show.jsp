<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actBookMark" value="${ForwardConst.ACT_BOOKMARK.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commDestroy" value="${ForwardConst.CMD_DESTROY.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">

        <h2>ブックマーク 詳細ページ</h2>

        <table>
            <tbody>
                <tr>
                    <th>場所ID</th>
                    <td><c:out value="${bookmark.placeId}" /></td>
                </tr>
                                <tr>
                    <th>施設名</th>
                    <td><c:out value="${bookmark.placeName}" /></td>
                </tr>
                                <tr>
                    <th>住所</th>
                    <td><c:out value="${bookmark.address}" /></td>
                </tr>
                <tr>
                    <th>登録日時</th>
                    <fmt:parseDate value="${bookmark.createdAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="createDay" type="date" />
                    <td><fmt:formatDate value="${createDay}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                </tr>
            </tbody>
        </table>

        <c:if test="${sessionScope.login_user.id == bookmark.userName.id}">

            <p>
                <a href="#" onclick="confirmDestroy();">このブックマークを削除する</a>
            </p>

            <form method="POST"
                action="<c:url value='?action=${actBookMark}&command=${commDestroy}' />">
                <input type="hidden" name="${AttributeConst.BOOKMARK_ID.getValue()}" value="${bookmark.id}" />
                <input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
            </form>

            <script>
                function confirmDestroy() {
                    if(confirm("本当に削除してよろしいですか？")) {
                        document.forms[0].submit();
                    }
                }
            </script>
        </c:if>

        <p><a href="<c:url value='?action=${actBookMark}&command=${commIdx}' />">一覧に戻る</a></p>

    </c:param>
</c:import>