<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="../layout/app.jsp">
    <c:param name="content">
        <h2>施設の詳細ページ</h2>
        <!-- 施設の詳細ページ -->
        <c:if test="${errors != null}">
            <div id="flush_error">
                施設の取得処理中にエラーが発生しました。
                <br />
                <c:forEach var="error" items="${errors}">
                    ・<c:out value="${error}" />
                    <br />
                </c:forEach>

            </div>
        </c:if>
        <c:if test="${errors == null}">
            <table>
                <tbody>
                    <tr>
                        <th>場所id</th>
                        <td><c:out value="${place.id}" /></td>
                    </tr>
                    <tr>
                        <th>場所名</th>
                        <td><c:out value="${place.name}" /></td>
                    </tr>
                    <tr>
                        <th>緯度</th>
                        <td><c:out value="${place.lat}" /></td>
                    </tr>
                    <tr>
                        <th>経度</th>
                        <td><c:out value="${place.lng}" /></td>
                    </tr>
                    <tr>
                        <th>付近（住所）</th>
                        <td><c:out value="${place.formattedAddress}" /></td>
                    </tr>
                    <tr>
                        <th>URL</th>
                        <td><c:out value="${place.url}" /></td>
                    </tr>
                </tbody>
            </table>
        </c:if>
    </c:param>
</c:import>