<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="constants.AttributeConst"%>
<%@ page import="constants.ForwardConst"%>
<c:set var="actMap" value="${ForwardConst.ACT_MAP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commSearch" value="${ForwardConst.CMD_SEARCH.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW_PLACE.getValue()}" />
<c:set var="actBookMark" value="${ForwardConst.ACT_BOOKMARK.getValue()}" />
<c:set var="commCreate" value="${ForwardConst.CMD_CREATE.getValue()}" />

<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>

        <!-- Googleマップを表示するエリア -->
        <div id="current-map" style="width: 600px; height: 400px; margin:0 auto;"></div>

            <script type="text/javascript">
                // 位置情報取得が成功したときに実行される関数
                function success(pos) {
                    let crd = pos.coords;

                    // マップオプションを変数に格納
                    let mapOptions = {
                        zoom: 14, // 拡大率
                        center: { // 中心座標を指定
                            lat:crd.latitude, // 緯度
                            lng: crd.longitude // 経度
                        }
                    };
                    let map;

                    // マップにマーカーを表示する
                    // https://www.javadrive.jp/google-maps-javascript/gmarker/index4.html
                    let myLocation = new google.maps.Marker({
                        map: map, // mapに対して指定（マップオブジェクト作成したやつ）
                        position: mapOptions.center, // mapOptionsから座標を指定
                        title: '現在地' // アイコンにマウスホバーすると出てくる文言
                    });

                    // マップオブジェクト作成
                    map = new google.maps.Map(
                        document.getElementById('current-map'),
                        mapOptions
                    );
                    myLocation.setMap(map);

                    // hiddenタグに緯度と経度を設定
                    document.getElementById('lat').value = mapOptions.center.lat;
                    document.getElementById('lng').value = mapOptions.center.lng;

                    //テーブルデータを配列化
                    var mytable = document.getElementById("tbl");
                    //console.log(mytable);
                    //console.log(mytable.rows);

                    let markerData=[];

                    for (let row of mytable.rows) {
                      //console.log(row.innerText);
                      markerData.push(row.innerText.split('\t'))
                    }
                    //console.log(markerData);

                    var marker = [];
                    for (var i = 1; i < markerData.length; i++) {
                        markerLatLng = new google.maps.LatLng(markerData[i][2], markerData[i][3]); // 緯度経度のデータ作成
                        marker[i] = new google.maps.Marker({ // マーカーの追加
                         position: markerLatLng, // マーカーを立てる位置を指定
                         map: map // マーカーを立てる地図を指定
                        });
                        marker[i].setMap(map);
                    }
                    //現在地の緯度経度を中心にマップに円を描く
                       var rad = parseInt(document.getElementById('radius').value);
                       //rad.addEventListener('input', initMap);//イベントリスナー使わなくてもサークル描画できた・・・ */
                        var circleOptions = {
                        map: map,
                        //center: new google.maps.LatLng(mapOptions.center), ググった内容のまま
                        center: mapOptions.center,
                        radius: rad, //ここを変数にして検索範囲力入力値を取得すればサークルの大きさを可変にできるはず
                        strokeColor: "#009933",
                        strokeOpacity: 1,
                        strokeWeight: 1,
                        fillColor: "#00ffcc",
                        fillOpacity: 0.2
                    };
                    circle = new google.maps.Circle(circleOptions);
                    /* console.log(rad); */

                }
                // 位置情報取得が失敗したときに実行される関数
                function error(err) {
                    alert('エラーが発生しました: ' + err);
                }

                function initMap() {

                    // getCurrentPositionのオプション
                    let options = {
                        enableHighAccuracy: false,
                        timeout: 5000,
                        maximumAge: 0
                    }
                    /**
                     * 現在位置を取得する
                     * APIの詳細は＞https://developer.mozilla.org/ja/docs/Web/API/Geolocation/getCurrentPosition
                     */
                    navigator.geolocation.getCurrentPosition(success, error, options);

                }
            </script>
            <script async defer
                src="https://maps.googleapis.com/maps/api/js?key=<c:out value="${applicationScope.apikey}" />&callback=initMap">
            </script>

        <form method="POST"
            action="<c:url value='?action=${actMap}&command=${commSearch}' />">
            <input type="hidden" id="lat" name="lat" value="" />
            <input type="hidden" id="lng" name="lng" value="" />

            <br>
            <div>
                検索ワード： <input type="text" name="keyword">&nbsp;&nbsp;&nbsp;&nbsp;

                検索範囲：半径 <input type="text" id="radius" name="radius"
                    style="width: 100px; "value="${radius}">
                メートル以内&nbsp;&nbsp;&nbsp;&nbsp;
                <button type="submit">検索</button>
            </div>

        </form>

        <div id="java">
            <%
            String result_keyword = request.getParameter("keyword");
            String result_radius = request.getParameter("radius");

            if (result_keyword == null || result_keyword == "") {
                out.println("検索値：検索ワードを入力してください。");
            } else {
                out.println("検索値：「半径" + result_radius + "m以内の" + result_keyword + "」");
            }
            %>
        </div>


    <p>検索結果一覧：
        <c:if test="${errors != null}">
            <div id="flush_error">
                施設の検索中にエラーが発生しました。<br />
                <c:forEach var="error" items="${errors}">
                    ・<c:out value="${error}" /><br />
                </c:forEach>

            </div>
        </c:if>
        <c:if test="${sessionScope.login_user != null}">
            <table>
                <thead>
                    <tr>
                        <th>ブックマーク</th>
                        <th>場所名</th>
                        <th>緯度</th>
                        <th>経度</th>
                        <th>付近（住所）</th>
                    </tr>
                </thead>
                <tbody id="tbl">
                    <c:forEach var="place" items="${places}" varStatus="status">
                        <tr class="row${status.count % 2}">

                            <td>
                                <form method="POST" action="<c:url value='?action=${actBookMark}&command=${commCreate}' />">
                                <input type="hidden" id="placeId" name="placeId" value="${place.id}" />
                                <input type="hidden" id="placeName" name="placeName" value="${place.name}" />
                                <input type="hidden" id="placeLat" name="placeLat" value="${place.lat}" />
                                <input type="hidden" id="placeLng" name="placeLng" value="${place.lng}" />
                                <input type="hidden" id="placeAddress" name="placeAddress" value="${place.vicinity}" />
                                <input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />

                                <button>登録</button>
                                </form>
                            </td>

                            <td><a
                                href="<c:url value='?action=${actMap}&command=${commShow}&placeId=${place.id}' />"><c:out
                                        value="${place.name}" /></a></td>
                            <td><c:out value="${place.lat}" /></td>
                            <td><c:out value="${place.lng}" /></td>
                            <td><c:out value="${place.vicinity}" /></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
        <c:if test="${sessionScope.login_user == null}">
            <table>
                <thead>
                    <tr>
                        <th>No.</th>
                        <th>場所名</th>
                        <th>緯度</th>
                        <th>経度</th>
                        <th>付近（住所）</th>
                    </tr>
                </thead>
                <tbody id="tbl">
                    <c:forEach var="place" items="${places}" varStatus="status">
                        <tr class="row${status.count % 2}">
                            <td><c:out value="${place.id}" /></td>
                            <td><a
                                href="<c:url value='?action=${actMap}&command=${commShow}&placeId=${place.id}' />"><c:out
                                        value="${place.name}" /></a></td>
                            <td><c:out value="${place.lat}" /></td>
                            <td><c:out value="${place.lng}" /></td>
                            <td><c:out value="${place.vicinity}" /></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>

    </c:param>

</c:import>