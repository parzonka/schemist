package com.github.parzonka.schemist.web;

import java.net.InetAddress;

import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Bean that provides the port number of this application at runtime.
 */
@Slf4j
@Component
public class WebContext implements ApplicationListener<ServletWebServerInitializedEvent> {

  @Getter
  private int port;

  @Getter
  private String hostname;

  @SneakyThrows
  @Override
  public void onApplicationEvent(final ServletWebServerInitializedEvent servletWebServerInitializedEvent) {
    hostname = InetAddress.getLoopbackAddress()
        .getHostAddress();
    port = servletWebServerInitializedEvent.getWebServer()
        .getPort();
    log.debug("Server started at " + getHttp());
  }

  public String getHttp() {
    return "http://" + hostname + ":" + port;
  }

  public String getHttps() {
    return "https://" + hostname + ":" + port;
  }
}
