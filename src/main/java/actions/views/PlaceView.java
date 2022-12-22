package actions.views;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 場所情報について画面の表示値を扱うViewモデル
 *
 */
@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
public class PlaceView {
    /**
     * 場所id
     */
    private String id;

    /**
     * 場所名
     */
    private String name;

    /**
     * 緯度
     */
    private String lat;

    /**
     * 経度
     */
    private String lng;

    /**
     * 付近（住所）
     */
    private String vicinity;

}
