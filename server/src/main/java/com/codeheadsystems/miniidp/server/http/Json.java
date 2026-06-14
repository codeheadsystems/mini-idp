package com.codeheadsystems.miniidp.server.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;

/**
 * Shared Jackson {@link ObjectMapper} plus tiny (de)serialization helpers for the HTTP layer.
 *
 * <p>One mapper is reused for every request/response body. It pretty-prints so responses are
 * readable when poked at with {@code curl}, and a malformed request body surfaces as a
 * {@link ApiException} (HTTP 400) rather than a stack trace.
 */
public final class Json {

  /** The shared mapper (thread-safe once configured). */
  public static final ObjectMapper MAPPER = new ObjectMapper()
      .enable(SerializationFeature.INDENT_OUTPUT);

  private Json() {
  }

  /** Serialize a value to JSON bytes. */
  public static byte[] toBytes(final Object value) {
    try {
      return MAPPER.writeValueAsBytes(value);
    } catch (final JsonProcessingException e) {
      throw new IllegalStateException("failed to serialize response", e);
    }
  }

  /**
   * Parse JSON bytes into the given type, mapping any parse failure to a 400.
   *
   * @throws ApiException (400 {@code invalid_request}) if the body is missing or malformed.
   */
  public static <T> T parse(final byte[] body, final Class<T> type) {
    if (body == null || body.length == 0) {
      throw ApiException.badRequest("request body is required");
    }
    try {
      return MAPPER.readValue(body, type);
    } catch (final IOException e) {
      throw ApiException.badRequest("malformed JSON request body");
    }
  }
}
