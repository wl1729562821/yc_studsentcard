package cc.manbu.schoolinfocommunication.tools;

public class StringUtil {
	/** 
     * ASCII表中可见字符从!开始，偏移位值为33(Decimal) 
     */  
    static final char DBC_CHAR_START = 33; // 半角!  
  
    /** 
     * ASCII表中可见字符到~结束，偏移位值为126(Decimal) 
     */  
    static final char DBC_CHAR_END = 126; // 半角~  
  
    /** 
     * 全角对应于ASCII表的可见字符从！开始，偏移值为65281 
     */  
    static final char SBC_CHAR_START = 65281; // 全角！  
  
    /** 
     * 全角对应于ASCII表的可见字符到～结束，偏移值为65374 
     */  
    static final char SBC_CHAR_END = 65374; // 全角～  
  
    /** 
     * ASCII表中除空格外的可见字符与对应的全角字符的相对偏移 
     */  
    static final int CONVERT_STEP = 65248; // 全角半角转换间隔  
  
    /** 
     * 全角空格的值，它没有遵从与ASCII的相对偏移，必须单独处理 
     */  
    static final char SBC_SPACE = 12288; // 全角空格 12288  
  
    /** 
     * 半角空格的值，在ASCII中为32(Decimal) 
     */  
    static final char DBC_SPACE = ' '; // 半角空格  
  
    /** 
     * <PRE> 
     * 半角字符->全角字符转换   
     * 只处理空格，!到&tilde;之间的字符，忽略其他 
     * </PRE> 
     */  
    public static String toDBC(String src) {  
        if (src == null) {  
            return src;  
        }  
        StringBuilder buf = new StringBuilder(src.length());  
        char[] ca = src.toCharArray();  
        for (int i = 0; i < ca.length; i++) {  
            if (ca[i] == DBC_SPACE) { // 如果是半角空格，直接用全角空格替代  
                buf.append(SBC_SPACE);  
            } else if ((ca[i] >= DBC_CHAR_START) && (ca[i] <= DBC_CHAR_END)) { // 字符是!到~之间的可见字符  
                buf.append((char) (ca[i] + CONVERT_STEP));  
            } else { // 不对空格以及ascii表中其他可见字符之外的字符做任何处理  
                buf.append(ca[i]);  
            }  
        }  
        return buf.toString();  
    }  
  
    /** 
     * <PRE> 
     * 全角字符->半角字符转换   
     * 只处理全角的空格，全角！到全角～之间的字符，忽略其他 
     * </PRE> 
     */  
    public static String qj2bj(String src) {  
        if (src == null) {  
            return src;  
        }  
        StringBuilder buf = new StringBuilder(src.length());  
        char[] ca = src.toCharArray();  
        for (int i = 0; i < src.length(); i++) {  
            if (ca[i] >= SBC_CHAR_START && ca[i] <= SBC_CHAR_END) { // 如果位于全角！到全角～区间内  
                buf.append((char) (ca[i] - CONVERT_STEP));  
            } else if (ca[i] == SBC_SPACE) { // 如果是全角空格  
                buf.append(DBC_SPACE);  
            } else { // 不处理全角空格，全角！到全角～区间外的字符  
                buf.append(ca[i]);  
            }  
        }  
        return buf.toString();  
    }  
	/**
	 * 替换字符串"null"为""
	 * @param str
	 * @return
	 */
	public static String getNoNullStr(String str){
		if("null".equals(str)){
			str = "";
		}
		return str;
	}
	
//	/**
//	 * 半角转为全角
//	 * @param input
//	 * @toolbar_back
//	 */
//	public static String toDBC(String input) {
//		if(null==input){
//			toolbar_back "";
//		}
//		   char[] c = input.toCharArray();
//		   for (int i = 0; i< c.length; i++) {
//		       if (c[i] == 12288) {
//		         c[i] = (char) 32;
//		         continue;
//		       }if (c[i]> 65280&& c[i]< 65375)
//		          c[i] = (char) (c[i] - 65248);
//		       }
//		   toolbar_back new String(c);
//		}
	
	/**
	 * 将value值转换成sql语句中string类型的参数值
	 * @param value
	 * @return
	 */
	public static String getSqlColumnValueStr(Object value){
		if(value!=null){
			if(!value.toString().startsWith("\'") && !value.toString().endsWith("\'")){
				return "'"+value.toString()+"'";
			}
			return value.toString();
		}
		return "";
	}
	
	/**
	 * 将html文本转换成textview支持的文本
	 * @param source
	 * @return
	 */
	public static String formatToTextViewSupportHtml(String source){
		if(source.startsWith("<p>") && source.startsWith("</p>") || source.startsWith("<P>") && source.startsWith("</P>")){
			
		}
		return source;
	}
	
	public static String subStr(String value, int len) {
		if (value != null && value.length() > len) {
			value = value.substring(0, len);
		}
		return value;
	}
	
	public static String formatToTwoNumberStr(int i){
		if(i<10){
			return "0"+i;
		}else{
			return ""+i;
		}
	}
	
	public static int convertTwoNumberToInt(String s){
		int i = 0;
		if(s.length()==2){
			if(s.startsWith("0")){
				i = Integer.valueOf(s.substring(1, 2));
			}else{
				i = Integer.valueOf(s);
			}
		}
		return i;
	}

	
}
