package utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ning.http.util.UTF8UrlEncoder;

public class URLBuilder {

	String path;
	Map<String, String> parameters = new HashMap<String, String>();

	public URLBuilder(String path) {
		this.path = path;
	}

	public URLBuilder addQueryParameter(String key, String value) {
		parameters.put(key, value);
		return this;
	}

	public String build() {

		StringBuilder builder = new StringBuilder(path);
		if (!parameters.isEmpty())
			builder.append("?");

		for (Iterator<Entry<String, String>> i = parameters.entrySet().iterator(); i.hasNext();) {
			Entry<String, String> param = i.next();
			String name = param.getKey();
			String value = param.getValue();
			
			UTF8UrlEncoder.appendEncoded(builder, name);
			if (value != null && !value.equals("")) {
				builder.append('=');
				UTF8UrlEncoder.appendEncoded(builder, value);
			}
			if (i.hasNext()) {
				builder.append('&');
			}
		}

		return builder.toString();
	}
}
