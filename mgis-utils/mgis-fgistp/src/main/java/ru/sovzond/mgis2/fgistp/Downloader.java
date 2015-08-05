package ru.sovzond.mgis2.fgistp;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader {
	public InputStream doGet(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection cn = (HttpURLConnection) url.openConnection();
		cn.setRequestMethod("GET");
		cn.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		cn.setDoOutput(true);
		cn.setUseCaches(false);
		return cn.getInputStream();
	}
}
