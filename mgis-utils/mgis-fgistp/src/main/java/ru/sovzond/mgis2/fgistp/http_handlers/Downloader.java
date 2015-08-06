package ru.sovzond.mgis2.fgistp.http_handlers;

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

	public InputStream download(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection cn = (HttpURLConnection) url.openConnection();
		cn.setRequestMethod("GET");
		cn.addRequestProperty("Accept", "*/*");
		cn.addRequestProperty("Accept-Language", "ru");
		cn.addRequestProperty("Referer", "http://fgis.economy.gov.ru/fgis/ClientBin/Strategis.FGIS.xap");
		cn.addRequestProperty("Accept-Encoding", "gzip, deflate");
		cn.addRequestProperty("Host", "fgis.economy.gov.ru");

		cn.setDoOutput(true);
		cn.setUseCaches(false);
		return cn.getInputStream();
	}
}
