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

import actions.views.PlaceDetailView;
import actions.views.PlaceView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.MessageConst;
import constants.PropertyConst;


public class MapAction extends ActionBase {


    @Override
    public void process() throws ServletException, IOException {

        //メソッドを実行
        invoke();

    }

    public void index() throws ServletException, IOException {

        //リクエストスコープに半径100を設定　サークル描画用
        //putRequestScope(AttributeConst.RADIUS, 100);

        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }
        forward(ForwardConst.FW_MAP_INDEX);

    }

    public void search() throws ServletException, IOException {

        List<String> errors = null;
        List<PlaceView> places = new ArrayList<PlaceView>();

        // APIキー
        String apiKey = getContextScope(PropertyConst.API_KEY);

        // ロケーション（緯度、経度）
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
        putRequestScope(AttributeConst.RADIUS, radius);//サークル描画用

        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
        forward(ForwardConst.FW_MAP_INDEX);
    }

    /**
     * 施設を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void showPlace() throws ServletException, IOException {

        List<String> errors = null;
        PlaceDetailView place = null;

        // 場所ID
        String placeId  = getRequestParam(AttributeConst.PLACE_ID);

        // APIキー
        String apiKey = getContextScope(PropertyConst.API_KEY);

        String urlString = "https://maps.googleapis.com/maps/api/place/details/json?key="
                + apiKey
                + "&place_id="
                + placeId;

        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            //  // リクエストメソッドの設定
            con.setRequestMethod("GET");
            // リクエストヘッダーに日本語を設定
            con.setRequestProperty("Accept-Language", "ja,en-US;q=0.8,en;q=0.6");
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
            JsonNode result = root.get("result");
            if (root.has("error_message")) {
                // WebサービスAPIの実行結果にエラーメッセージが存在する場合
                errors = new ArrayList<String>();
                errors.add(root.get("error_message").asText());

            } else if (result == null){
                // 該当データが見つからなかった場合
                errors = new ArrayList<String>();
                errors.add(MessageConst.E_NODATA.getMessage());
            }else {
                // WebサービスAPIの実行結果にエラーメッセージが存在しない場合

                JsonNode geometryLocation = result.get("geometry").get("location");

                place = new PlaceDetailView(
                        result.get("place_id").asText(),
                        result.get("name").asText(),
                        geometryLocation.get("lat").asText(),
                        geometryLocation.get("lng").asText(),
                        result.get("formatted_address").asText(),
                        result.get("url").asText()
                        );

            }

            con.disconnect();
         }catch(Exception e) {

            e.printStackTrace();
            errors = new ArrayList<String>();
            errors.add(e.getMessage());
         }

        putRequestScope(AttributeConst.ERR, errors);
        putRequestScope(AttributeConst.PLACE, place);

        forward(ForwardConst.FW_SHOW_PLACE);
    }

}