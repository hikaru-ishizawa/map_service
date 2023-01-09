<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actBookMark" value="${ForwardConst.ACT_BOOKMARK.getValue()}" />
<c:set var="actMap" value="${ForwardConst.ACT_MAP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />


<c:import url="../layout/app.jsp">
     <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>ブックマーク一覧</h2>
        <table id="bookmark_list">
            <tbody>
                <tr>
                    <th class="bookmark_placeName">施設名</th>
                    <th class="bookmark_address">住所</th>
                    <th class="bookmark_createdAt">登録日時</th>
                    <th class="bookmark_action">操作</th>


                </tr>
                <c:forEach var="bookmark" items="${bookmarks}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td class="bookmark_placeName">${bookmark.placeName}</td>
                        <td class="bookmark_address">${bookmark.address}</td>
                        <td class="bookmark_createdAt">${bookmark.createdAt}</td>
                        <td class="bookmark_action"><a href="<c:url value='?action=${actBookMark}&command=${commShow}&id=${bookmark.id}' />">詳細を見る</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${bookmarks_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((reports_count - 1) / maxRow) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='?action=${actBookMark}&command=${commIdx}&page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='?action=${actMap}&command=${commIdx}' />">検索画面へ戻る</a></p>
    </c:param>
</c:import>