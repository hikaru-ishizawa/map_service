<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actSamp" value="${ForwardConst.ACT_SAMP.getValue()}" />
<c:set var="commSearch" value="${ForwardConst.CMD_SEARCH_OCHANOMIZU.getValue()}" />

<c:import url="../layout/app.jsp">
    <c:param name="content">
        <h2>現在位置のGoogleマップ</h2>
        <!-- Googleマップを表示するエリア -->
        <div id="current-map" style="width: 620px; height: 400px"></div>

        <input type="hidden" id="lat" name="lat" value="" />
        <input type="hidden" id="lng" name="lng" value="" />

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
        <br>
        <a href="<c:url value='?action=${actSamp}&command=${commSearch}' />">御茶ノ水付近の施設を検索する</a>
    </c:param>
</c:import>