package com.ft.api.util.buildinfo;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.junit.Test;

public class BuildInfoResourceTest {

  @Test
  public void shouldReturn500ErrorWhenBuildInfoPropertiesFileIsNotAvailable() {
    try {
      new BuildInfoResource().getBuildInfo();
      fail(
          "Expected a WebApplicationException to be thrown when build-info.properties not available.");
    } catch (WebApplicationException ex) {
      Response response = ex.getResponse();

      assertThat(
          "Response body: ",
          response,
          hasProperty(
              "entity",
              hasEntry(
                  "message", "Unable to load resource from classpath:/build-info.properties.")));
      assertThat("Response status code: ", response.getStatus(), equalTo(500));
      assertThat(
          "Response header \"Content-Type\": ",
          (MediaType) response.getMetadata().getFirst("Content-Type"),
          equalTo(MediaType.APPLICATION_JSON_TYPE));
    }
  }

  @Test
  public void shouldReturnBuildInfoWhenBuildInfoPropertiesFileIsAvailable() {
    BuildInfo buildInfo = new BuildInfoResource("/test-build-info.properties").getBuildInfo();
    assertThat(buildInfo.getProperty("testProperty"), equalTo("TEST"));
  }
}
