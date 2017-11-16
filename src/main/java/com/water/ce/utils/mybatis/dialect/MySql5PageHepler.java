package com.water.ce.utils.mybatis.dialect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MySql5PageHepler {
	/**
	 * 瀵版鍩岄弻銉嚄閹粯鏆熼惃鍓唓l
	 */
	public static String getCountString(String querySelect) {
		
		querySelect		= getLineSql(querySelect);
		int orderIndex  = getLastOrderInsertPoint(querySelect);
		
		int formIndex   = getAfterFormInsertPoint(querySelect);
		String select   = querySelect.substring(0, formIndex);
		
		//婵″倹鐏塖ELECT 娑擃厼瀵橀崥锟紻ISTINCT 閸欘亣鍏橀崷銊ヮ檱鐏炲倸瀵橀崥鐛礝UNT
		if (select.toLowerCase().indexOf("select distinct") != -1 || querySelect.toLowerCase().indexOf("group by")!=-1) {
			return new StringBuffer(querySelect.length()).append(
					"select count(1) count from (").append(
					querySelect.substring(0, orderIndex)).append(" ) t")
					.toString();
		} else {
			return new StringBuffer(querySelect.length()).append(
					"select count(1) count ").append(
					querySelect.substring(formIndex, orderIndex)).toString();
		}
	}
	
	/**
	 * 瀵版鍩岄張锟芥倵娑擄拷閲淥rder By閻ㄥ嫭褰冮崗銉у仯娴ｅ秶鐤�
	 * @return 鏉╂柨娲栭張锟芥倵娑擄拷閲淥rder By閹绘帒鍙嗛悙鍦畱娴ｅ秶鐤�
	 */
	private static int getLastOrderInsertPoint(String querySelect){
		int orderIndex = querySelect.toLowerCase().lastIndexOf("order by");
		if (orderIndex == -1
				|| !isBracketCanPartnership(querySelect.substring(orderIndex,querySelect.length()))) {
			throw new RuntimeException("My SQL 閸掑棝銆夎箛鍛淬�鐟曚焦婀丱rder by 鐠囶厼褰�");
		}
		return orderIndex;
	}
	/**
	 * 瀵版鍩岄崚鍡涖�閻ㄥ嚪QL
	 * @param offset 	閸嬪繒些闁诧拷
	 * @param limit		娴ｅ秶鐤�
	 * @return	閸掑棝銆塖QL
	 */
	public static String getLimitString(String querySelect,int offset, int limit) {
		querySelect	= getLineSql(querySelect);
		//String sql =  querySelect.replaceAll("[^\\s,]+\\.", "") +" limit "+ offset +" ,"+ limit;
		String sql =  querySelect +" limit "+ offset +" ,"+ limit;
		return sql;
		
	}

	/**
	 * 鐏忓摖QL鐠囶厼褰為崣妯诲灇娑擄拷娼拠顓炲綖閿涘苯鑻熸稉鏃�槨娑擃亜宕熺拠宥囨畱闂傛挳娈ч柈鑺ユЦ1娑擃亞鈹栭弽锟�	 * 
	 * @param sql SQL鐠囶厼褰�
	 * @return 婵″倹鐏塻ql閺勭枽ULL鏉╂柨娲栫粚鐚寸礉閸氾箑鍨潻鏂挎礀鏉烆剙瀵查崥搴ｆ畱SQL
	 */
	private static String getLineSql(String sql) {
		return sql.replaceAll("[\r\n]", " ").replaceAll("\\s{2,}", " ");
	}

	/**
	 * 瀵版鍩孲QL缁楊兛绔存稉顏咁劀绾喚娈慒ROM閻ㄥ嫮娈戦幓鎺戝弳閻愶拷
	 */
	private static int getAfterFormInsertPoint(String querySelect) {
		String regex = "\\s+FROM\\s+";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(querySelect);
		while (matcher.find()) {
			int fromStartIndex = matcher.start(0);
			String text = querySelect.substring(0, fromStartIndex);
			if (isBracketCanPartnership(text)) {
				return fromStartIndex;
			}
		}
		return 0;
	}

	/**
	 * 閸掋倖鏌囬幏顒�娇"()"閺勵垰鎯侀崠褰掑帳,楠炴湹绗夋导姘灲閺傤厽甯撻崚妤呫�鎼村繑妲搁崥锔筋劀绾拷
	 * 
	 * @param text
	 *            鐟曚礁鍨介弬顓犳畱閺傚洦婀�
	 * @return 婵″倹鐏夐崠褰掑帳鏉╂柨娲朤RUE,閸氾箑鍨潻鏂挎礀FALSE
	 */
	private static boolean isBracketCanPartnership(String text) {
		if (text == null
				|| (getIndexOfCount(text, '(') != getIndexOfCount(text, ')'))) {
			return false;
		}
		return true;
	}

	/**
	 * 瀵版鍩屾稉锟介嚋鐎涙顑侀崷銊ュ綗娑擄拷閲滅�妤冾儊娑撹弓鑵戦崙铏瑰箛閻ㄥ嫭顐奸弫锟�	 * @param text	閺傚洦婀�
	 * @param ch    鐎涙顑�
	 */
	private static int getIndexOfCount(String text, char ch) {
		int count = 0;
		for (int i = 0; i < text.length(); i++) {
			count = (text.charAt(i) == ch) ? count + 1 : count;
		}
		return count;
	}
}
