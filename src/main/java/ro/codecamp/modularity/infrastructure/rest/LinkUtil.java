package ro.codecamp.modularity.infrastructure.rest;

public class LinkUtil {

	private LinkUtil() {
	}

	public static Long getIdFromLink(String uri) {
		String[] parts = uri.split("/");
		return Long.valueOf(parts[parts.length - 1]);
	}

}
