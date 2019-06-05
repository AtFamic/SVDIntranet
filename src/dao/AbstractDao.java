package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Daoが継承すべきクラスです。
 * SQLの基本コマンドに加え、ResultSetから目的のオブジェクトを生成できるメソッドなどを保持します。
 * @author atfam
 *
 */
public abstract class AbstractDao<E> {
	/** テーブル名 */
	protected String TABLE;

	/** SQLコマンド*/
	protected final String SELECT = "select";
	protected final String FROM = "from ";
	protected final String INSERT_INTO = "insert into ";
	protected final String VALUES = "values";
	protected final String UPDATE_TABLE_SET = "update ";
	protected final String SET = "set";
	protected final String WHERE = "where";
	protected final String AND = "and";
	protected final String OR = "or";
	protected final String EQ = "=";
	protected final String EQ_QE = "= ?";
	protected final String SPACE = " ";
	protected final String COMMA = ", ";
	protected final String LEFT = "(";
	protected final String RIGHT = ")";



	/**
	 * コンストラクタでテーブル名を初期化
	 * @param table
	 */
	public AbstractDao(String table) {
		setTableName(table);

	}

	protected void setTableName(String table) {
		TABLE = table;
	}

	/** SQLユーティリティメソッド */
	/**
	 * 与えられた文字列をスペース挟んで結合する。
	 * @param strings
	 * @return
	 */
	protected String concatCommandsBySpace(String... strings) {
		StringBuilder result = new StringBuilder();
		for (String str : strings) {
			result.append(str);
			result.append(SPACE);
		}
		return result.toString();
	}

	/**
	 * 与えられた文字列をカンマを挟んで結合する。
	 * 要素の最後にカンマが入らないように調整します。
	 * 例) 引数にA, B, Cを渡したとき、返り値はA,B,Cとなります。
	 * @param strings
	 * @return
	 */
	protected String concatCommandsByCOMMA(String... strings) {
		StringBuilder result = new StringBuilder();
		int size = strings.length;
		int i = 0;
		for (String str : strings) {
			i++;
			result.append(str);
			if(size != i) {
				result.append(COMMA);
			}
		}
		return result.toString();
	}

	/**
	 * 結果を渡すと、それに対応するオブジェクトを生成し、返却するメソッド。
	 * @param resultSet SQLの実行結果
	 * @return 結果のリスト
	 * @throws SQLException
	 */
	protected <E> List<E> getElements(ResultSet resultSet) throws SQLException {
		List<E> result = new ArrayList<>();
		while (resultSet.next()) {
			E obj = getModel(resultSet);
			result.add(obj);
		}
		return result;
	}

	/**
	 * DBでの結果からモデルを生成します。
	 * この処理に関しては各Daoで実装してください。
	 * @param resultSet SQL実行結果
	 * @return
	 */
	abstract protected <E> E getModel(ResultSet resultSet) throws SQLException;
}
