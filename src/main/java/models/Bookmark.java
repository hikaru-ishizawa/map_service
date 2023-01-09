package models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ブックマークデータのDTOモデル
 *
 */
@Table(name = JpaConst.TABLE_BOOKMARK)
@NamedQueries({
    @NamedQuery(
            name = JpaConst.Q_BOOKMARK_GET_ALL,
            query = JpaConst.Q_BOOKMARK_GET_ALL_DEF),
    @NamedQuery(
            name = JpaConst.Q_BOOKMARK_COUNT_REGISTERED_BY_ID,
            query = JpaConst.Q_BOOKMARK_COUNT_REGISTERED_BY_ID_DEF),
    @NamedQuery(
            name = JpaConst.Q_BOOKMARK_GET_ALL_MINE,
            query = JpaConst.Q_BOOKMARK_GET_ALL_MINE_DEF),
    @NamedQuery(
            name = JpaConst.Q_BOOKMARK_COUNT_ALL_MINE,
            query = JpaConst.Q_BOOKMARK_COUNT_ALL_MINE_DEF)

})

@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
@Entity
public class Bookmark {

    /**
     * id
     */
    @Id
    @Column(name = JpaConst.BOOKMARK_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Place id
     */
    @Column(name = JpaConst.BOOKMARK_COL_PLACE_ID, nullable = false, unique = true)

    private String placeId;
    /**
     *場所名
     */
    @Column(name = JpaConst.BOOKMARK_COL_PLACE_NAME, nullable = false)
    private String placeName;

    /**
     *緯度
     */
    @Column(name = JpaConst.BOOKMARK_COL_LAT, nullable = false)
    private String lat;

    /**
     *緯度
     */
    @Column(name = JpaConst.BOOKMARK_COL_LNG, nullable = false)
    private String lng;

    /**
     *付近（住所）
     */
    @Column(name = JpaConst.BOOKMARK_COL_ADDRESS, nullable = false)
    private String address;

    /**
     * 登録したユーザ名
     */
    @ManyToOne
    @JoinColumn(name = JpaConst.BOOKMARK_COL_USER_NAME, nullable = false)
    private User userName;

    /**
     *登録日時
     */
    @Column(name = JpaConst.BOOKMARK_COL_CREATED_AT, nullable = false)
    private LocalDateTime createdAt;

}
