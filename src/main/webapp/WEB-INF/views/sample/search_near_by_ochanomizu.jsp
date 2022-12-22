<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actSamp" value="${ForwardConst.ACT_SAMP.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW_PRICE.getValue()}" />

<c:import url="../layout/app.jsp">
    <c:param name="content">
        <h2>御茶ノ水付近の施設一覧</h2>
        <!-- 御茶ノ水付近の施設一覧 -->
        <c:if test="${errors != null}">
            <div id="flush_error">
                施設の検索中にエラーが発生しました。<br />
                <c:forEach var="error" items="${errors}">
                    ・<c:out value="${error}" /><br />
                </c:forEach>

            </div>
        </c:if>
        <table>
            <tbody>
                <tr>
                    <th>場所id</th>
                    <th>場所名</th>
                    <th>緯度</th>
                    <th>経度</th>
                    <th>付近（住所）</th>
                </tr>
                <c:forEach var="place" items="${places}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td>
                            <a href="<c:url value='?action=${actSamp}&command=${commShow}&placeId=${place.id}' />"><c:out value="${place.id}" /></a>
                        </td>
                        <td><c:out value="${place.name}" /></td>
                        <td><c:out value="${place.lat}" /></td>
                        <td><c:out value="${place.lng}" /></td>
                        <td><c:out value="${place.vicinity}" /></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:param>
</c:import>