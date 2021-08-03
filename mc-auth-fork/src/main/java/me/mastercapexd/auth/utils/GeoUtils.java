package me.mastercapexd.auth.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import me.mastercapexd.auth.objects.IPInfoResponse;

public class GeoUtils {
	private static final String API_SITE = "http://ip-api.com/json/";

	public IPInfoResponse getIPInfo(String ip) {
		if (ip == null)
			return new IPInfoResponse();
		JsonObject json;
		try {
			json = new JsonParser().parse(getURLContent(API_SITE + ip)).getAsJsonObject();
		} catch (JsonSyntaxException | IOException e) {
			e.printStackTrace();
			return new IPInfoResponse();
		}
		if (json.get("status").getAsString().equals("fail"))
			return new IPInfoResponse();
		String countryCode = json.get("countryCode").getAsString();
		String town = json.get("city").getAsString();
		String emoji = convertCountryCodeToFlag(countryCode);
		return new IPInfoResponse(countryCode, town, emoji);

	}

	private String getURLContent(String url) throws IOException {
		URL website = new URL(url);
		URLConnection connection = website.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		StringBuilder response = new StringBuilder();
		String inputLine;

		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);

		in.close();

		return response.toString();
	}

	private String convertCountryCodeToFlag(String country) {
		int flagOffset = 0x1F1E6;
		int asciiOffset = 0x41;
		int firstChar = Character.codePointAt(country, 0) - asciiOffset + flagOffset;
		int secondChar = Character.codePointAt(country, 1) - asciiOffset + flagOffset;

		return new String(Character.toChars(firstChar)) + new String(Character.toChars(secondChar));
	}
}
