package models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ユーザーデータのDTOモデル
 *
 */
@Table(name = JpaConst.TABLE_USER)
@NamedQueries({
    @NamedQuery(
            name = JpaConst.Q_USER_GET_ALL,
            query = JpaConst.Q_USER_GET_ALL_DEF),
    @NamedQuery(
            name = JpaConst.Q_USER_COUNT,
            query = JpaConst.Q_USER_COUNT_DEF),
    @NamedQuery(
            name = JpaConst.Q_USER_COUNT_REGISTERED_BY_NAME,
            query = JpaConst.Q_USER_COUNT_REGISTERED_BY_NAME_DEF),
    @NamedQuery(
            name = JpaConst.Q_USER_GET_BY_NAME_AND_PASS,
            query = JpaConst.Q_USER_GET_BY_NAME_AND_PASS_DEF)
})

@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
@Entity
public class User {

    /**
     * id
     */
    @Id
    @Column(name = JpaConst.USER_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * ユーザ名
     */
    @Column(name = JpaConst.USER_COL_NAME, nullable = false, unique = true)
    private String name;

    /**
     * パスワード
     */
    @Column(name = JpaConst.USER_COL_PASS, length = 64, nullable = false)
    private String password;

    /**
     * 管理者権限があるかどうか（一般：0、管理者：1）
     */
    @Column(name = JpaConst.USER_COL_ADMIN_FLAG, nullable = false)
    private Integer adminFlag;

    /**
     *登録日時
     */
    @Column(name = JpaConst.USER_COL_CREATED_AT, nullable = false)
    private LocalDateTime createdAt;

    /**
     * 更新日時
     */
    @Column(name = JpaConst.USER_COL_UPDATED_AT, nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 削除されたユーザーかどうか（現役：0、削除済み：1）
     */
    @Column(name = JpaConst.USER_COL_DELETE_FLAG, nullable = false)
    private Integer deleteFlag;

}
