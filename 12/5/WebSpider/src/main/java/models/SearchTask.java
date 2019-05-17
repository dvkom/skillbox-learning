package models;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class SearchTask implements Callable<Set<String>> {
  private static final Logger log = Logger.getLogger(SearchTask.class);
  private String siteUrl;
  private URL validUrl;
  private static long pause = 200; // default value

  SearchTask(String siteUrl) {
    this.siteUrl = siteUrl;
  }

  private boolean isValidUrl() {
    try {
      validUrl = new URL(siteUrl);
      return true;
    } catch (MalformedURLException e) {
      return false;
    }
  }

  @Override
  public Set<String> call() {
    log.debug(Thread.currentThread().getName() + " started processing the URL: " + siteUrl);
    if (!isValidUrl()) {
      log.warn("Invalid URL: " + siteUrl);
      return null;
    }
    try {
      TimeUnit.MILLISECONDS.sleep(pause);
    } catch (InterruptedException e) {
      log.warn(Thread.currentThread().getName() + " was terminated, because: " + e.getMessage());
      return null;
    }
    Document doc;
    try {
      doc = Jsoup.connect(siteUrl).get();
    } catch (IOException e) {
      log.error("Error getting a web-page ", e);
      return null;
    }
    Set<String> foundUrls = doc.getElementsByTag("a")
        .stream()
        .map(element -> element.attr("href"))
        .filter(s -> !s.equals(""))
        .flatMap(this::buildUrl)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
    log.debug(Thread.currentThread().getName() + " finished processing the URL: " + siteUrl);
    return foundUrls;
  }

  private Stream<String> buildUrl(String url) {
    url = url.trim().toLowerCase();
    if (url.contains("mailto:") || url.contains("tel:") || url.contains("skype:")
        || url.contains("callto:") || url.contains("wtai:") || url.contains("sms:")
        || url.contains("market:") || url.contains("geopoint:") || url.contains("ymsgr:")
        || url.contains("msnim:") || url.contains("gtalk:") || url.contains("sip:")
        || url.contains("whatsapp:") || url.contains("javascript:")) {
      return Stream.empty();
    }
    StringBuilder urlBuilder = new StringBuilder();
    if (!url.contains("://")) {
      if (url.startsWith("//")) {
        urlBuilder.append(validUrl.getProtocol()).append(":").append(url);
      } else if (url.charAt(0) == '/') {
        urlBuilder.append(validUrl.getProtocol()).append("://").append(validUrl.getHost())
            .append(url);
      } else {
        String file = validUrl.getFile();
        if (!file.contains("/")) {
          urlBuilder.append(validUrl.getProtocol()).append("://").append(validUrl.getHost())
              .append("/").append(url);
        } else {
          String path = file.substring(0, file.lastIndexOf('/') + 1);
          urlBuilder.append(validUrl.getProtocol()).append("://").append(validUrl.getHost())
              .append(path).append(url);
        }
      }
    }
    url = urlBuilder.toString();
    if (url.contains("#")) {
      url = url.substring(0, url.indexOf("#"));
    }
    if (url.contains("?")) {
      url = url.substring(0, url.indexOf("?"));
    }
    if (url.contains("." + validUrl.getHost() + "/")
        || url.contains("/" + validUrl.getHost() + "/")) {
      return Stream.of(url);
    }
    return Stream.empty();
  }

  static void setPause(long pause) {
    SearchTask.pause = pause;
  }
}
