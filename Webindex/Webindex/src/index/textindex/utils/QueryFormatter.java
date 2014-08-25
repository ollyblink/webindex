package index.textindex.utils;

public class QueryFormatter {

	public static String format(String query, boolean isIntersected) {
		String formatted = "";
		String[] parts = query.split(" ");
		for (int i = 0; i < parts.length - 1; ++i) {
			formatted += parts[i] + " " + (isIntersected ? "AND" : "OR") + " ";
		}
		if (parts.length > 0) {
			formatted += parts[parts.length - 1];
		}
		return formatted.trim();
	}

}
