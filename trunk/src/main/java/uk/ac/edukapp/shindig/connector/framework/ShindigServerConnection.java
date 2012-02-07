package uk.ac.edukapp.shindig.connector.framework;

public class ShindigServerConnection {

  private String url;

  public ShindigServerConnection(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
