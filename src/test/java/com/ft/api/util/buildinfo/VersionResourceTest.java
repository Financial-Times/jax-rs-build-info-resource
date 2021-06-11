package com.ft.api.util.buildinfo;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.junit.Test;

public class VersionResourceTest {

  @Test
  public void shouldReturn500ErrorWhenBuildInfoPropertiesFileIsNotAvailable() {
    try {
      new VersionResource().getVersion();
      fail(
          "Expected a WebApplicationException to be thrown when build-info.properties not available.");
    } catch (WebApplicationException ex) {
      Response response = ex.getResponse();

      assertThat(
          "Response body: ",
          response,
          hasProperty(
              "entity", equalTo("Unable to load resource from classpath:/build-info.properties.")));
      assertThat("Response status code: ", response.getStatus(), equalTo(500));
      assertThat(
          "Response header \"Content-Type\": ",
          (MediaType) response.getMetadata().getFirst("Content-Type"),
          equalTo(MediaType.TEXT_PLAIN_TYPE));
    }
  }

  @Test
  public void shouldReturnVersionWhenBuildInfoPropertiesFileIsAvailable() {
    String version = new VersionResource("/test-build-info.properties").getVersion();
    assertThat(version, equalTo("99.99.99"));
  }
}
