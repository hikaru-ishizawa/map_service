package actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import actions.views.PlaceView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.PropertyConst;


public class MapAction extends ActionBase {


    @Override
    public void process() throws ServletException, IOException {

        //メソッドを実行
        invoke();

    }

    public void index() throws ServletException, IOException {

        forward(ForwardConst.FW_MAP_INDEX);
    }

    public void search() throws ServletException, IOException {

        List<String> errors = null;
        List<PlaceView> places = new ArrayList<PlaceView>();

        // APIキー
        String apiKey = getContextScope(PropertyConst.API_KEY);

        // ロケーション（緯度、経度）※"35.6987769,139.76471"はお茶の水周辺の座標
        String lat = request.getParameter("lat");
        String lng = request.getParameter("lng");
        String location = lat + "," + lng;

        // 場所の結果を返す距離（単位:メートル）
        String radius = request.getParameter("radius");

        // 検索キーワード（名前、種類、住所、カスタマーレビューなど）

        //String keyword ="学校"; //公園OR広場OR駅
        String input = request.getParameter("keyword");

        String urlString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key="
                + apiKey
                + "&location="
                + location
                + "&radius="
                + radius
                + "&language=ja&keyword="
                + input;

        try {
           URL url = new URL(urlString);
           HttpURLConnection con = (HttpURLConnection)url.openConnection();

           con.connect(); // URL接続

           // URLに接続した結果をBufferedReaderのインスタンスに代入する
           BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

           String jsonResult = "";
           String tmp = "";

           while ((tmp = in.readLine()) != null) {
               jsonResult += tmp;
           }

           in.close();

           ObjectMapper mapper = new ObjectMapper();
           JsonNode root = mapper.readTree(jsonResult);

           if (root.has("error_message")) {
               // WebサービスAPIの実行結果にエラーメッセージが存在する場合
               errors = new ArrayList<String>();
               errors.add(root.get("error_message").asText());

           } else {
               // WebサービスAPIの実行結果にエラーメッセージが存在しない場合
               JsonNode results = root.get("results");
               for(int i = 0;i<results.size();i++) {
                   JsonNode result = results.get(i);
                   JsonNode geometryLocation = result.get("geometry").get("location");

                   PlaceView place = new PlaceView(
                           result.get("place_id").asText(),
                           result.get("name").asText(),
                           geometryLocation.get("lat").asText(),
                           geometryLocation.get("lng").asText(),
                           result.get("vicinity").asText()
                           );

                   places.add(place);
               }
           }

           con.disconnect();
        }catch(Exception e) {

           e.printStackTrace();
           errors = new ArrayList<String>();
           errors.add(e.getMessage());
        }
        putRequestScope(AttributeConst.ERR, errors);
        putRequestScope(AttributeConst.PLACES, places);

        forward(ForwardConst.FW_MAP_INDEX);
    }


}