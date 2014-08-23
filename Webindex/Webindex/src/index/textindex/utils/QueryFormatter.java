package index.textindex.utils;

public class QueryFormatter {

	public static String format(String query, boolean isIntersected) {
		String formatted = "";
		String[] parts = query.split(" ");
		for(int i = 0; i < parts.length-1;++i){
			formatted += "<theme>"+parts[i] + "</theme>"+(isIntersected?"AND":"OR")+ " ";
		}
		if(parts.length > 0){
			formatted += "<theme>"+parts[parts.length-1]+"<theme>";
		}
		return formatted.trim();
	}

}
