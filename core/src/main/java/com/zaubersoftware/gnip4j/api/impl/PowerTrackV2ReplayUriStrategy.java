/**
 * Copyright (c) 2011-2016 Zauber S.A. <http://flowics.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zaubersoftware.gnip4j.api.impl;

import com.zaubersoftware.gnip4j.api.UriStrategy;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Implementation of {@link UriStrategy} that creates {@link URI}s to connect
 * to PowerTrack V2 Gnip Replay endpoints which are different than those used in DefaultUriStrategy
 *
 * https://stream-data-api.twitter.com/replay/powertrack/accounts/<account>/publishers/twitter
 * /<stream>.json
 * https://data-api.twitter.com/rules/powertrack-replay/accounts/<account>/publishers/twitter
 * /<stream>.json"
 *
 * <p>
 * The base URI format for stream is {@link PowerTrackV2UriStrategy#BASE_GNIP_URI_FMT}
 * <p>
 *
 * <p>
 * The base URI format for rules is {@link PowerTrackV2UriStrategy#BASE_GNIP_RULES_URI_FMT}
 * <p>
 *
 * @author jmmk
 * @since 09/29/16
 */
public class PowerTrackV2ReplayUriStrategy implements UriStrategy {
  public static final String DEFAULT_REPLAY_STREAM_URL_BASE = "https://gnip-stream.gnip.com";
  public static final String DEFAULT_REPLAY_RULE_URL_BASE
      = PowerTrackV2UriStrategy.DEFAULT_RULE_URL_BASE;
  public static final String PATH_GNIP_REPLAY_STREAM_URI
      = "/replay/powertrack/accounts/%s/publishers/%s/%s.json?fromDate=%s&toDate=%s";
  public static final String PATH_GNIP_REPLAY_RULES_URI
      = "/rules/powertrack-replay/accounts/%s/publishers/%s/%s.json";
  public static final String PATH_GNIP_REPLAY_RULES_VALIDATION_URI
      = "/rules/powertrack-replay/accounts/%s/publishers/%s/%s/validation.json";

  private String streamUrlBase = DEFAULT_REPLAY_STREAM_URL_BASE;
  private String ruleUrlBase = DEFAULT_REPLAY_RULE_URL_BASE;
  private DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm", Locale.ENGLISH);

  private final String publisher;
  private Date from;
  private Date to;

  /** Creates the DefaultUriStrategy. */
  public PowerTrackV2ReplayUriStrategy() {
    this("twitter", null, null);
  }

  public PowerTrackV2ReplayUriStrategy(final Date from, final Date to) {
    this("twitter", from, to);
  }

  /** Creates the DefaultUriStrategy. */
  public PowerTrackV2ReplayUriStrategy(final String publisher, final Date from, final Date to) {
    if (publisher == null) {
      throw new IllegalArgumentException("The publisher cannot be null or empty");
    }
    this.publisher = publisher;
    this.from = from;
    this.to = to;
  }

  @Override
  public URI createStreamUri(String account, String streamName, Integer backFillMinutes) {
    if (account == null || account.trim().isEmpty()) {
      throw new IllegalArgumentException("The account cannot be null or empty");
    }
    if (streamName == null || streamName.trim().isEmpty()) {
      throw new IllegalArgumentException("The streamName cannot be null or empty");
    }

    if (this.from == null || this.to == null) {
      throw new IllegalStateException(
          "Both 'from' and 'to' must be specified to start a replay stream");
    }

    final String fromString = this.formatter.format(this.from);
    final String toString = this.formatter.format(this.to);

    return URI.create(String.format(Locale.ENGLISH,
        streamUrlBase + PATH_GNIP_REPLAY_STREAM_URI, account.trim(),
        publisher, streamName.trim(), fromString, toString));
  }

  @Override
  public URI createRulesUri(String account, String streamName) {
    return URI.create(createRulesBaseUrl(account, streamName));

  }

  @Override
  public URI createRulesDeleteUri(String account, String streamName) {
    return URI.create(createRulesBaseUrl(account, streamName) + "?_method=delete");
  }

  @Override
  public URI createRulesValidationUri(final String account, final String streamName) {
    return URI.create(String.format(Locale.ENGLISH,
        DEFAULT_REPLAY_RULE_URL_BASE + PATH_GNIP_REPLAY_RULES_VALIDATION_URI, account.trim(),
        publisher, streamName.trim()));
  }

  @Override
  public String getHttpMethodForRulesDelete() {
    return UriStrategy.HTTP_POST;
  }

  private String createRulesBaseUrl(final String account, final String streamName) {
    if (account == null || account.trim().isEmpty()) {
      throw new IllegalArgumentException("The account cannot be null or empty");
    }
    if (streamName == null || streamName.trim().isEmpty()) {
      throw new IllegalArgumentException("The streamName cannot be null or empty");
    }

    return String.format(Locale.ENGLISH,
        this.ruleUrlBase + PATH_GNIP_REPLAY_RULES_URI, account.trim(), this.publisher.trim(),
        streamName.trim());
  }

  public final String getStreamUrlBase() {
    return this.streamUrlBase;
  }

  public final void setStreamUrlBase(final String streamUrlBase) {
    if (streamUrlBase == null) {
      throw new IllegalArgumentException("streamUrlBase can't be null");
    }
    this.streamUrlBase = streamUrlBase;
  }

  public final String getRuleUrlBase() {
    return this.ruleUrlBase;
  }

  public final void setRuleUrlBase(final String ruleUrlBase) {
    if (ruleUrlBase == null) {
      throw new IllegalArgumentException("streamUrlBase can't be null");
    }
    this.ruleUrlBase = ruleUrlBase;
  }

  public void setFrom(final Date from) {
    this.from = from;
  }

  public void setTo(final Date to) {
    this.to = to;
  }

  public Date getFrom() {
    return this.from;
  }

  public Date getTo() {
    return this.to;
  }
}
