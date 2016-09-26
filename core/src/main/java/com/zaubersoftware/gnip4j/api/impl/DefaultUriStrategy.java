/**
 * Copyright (c) 2011-2012 Zauber S.A. <http://www.zaubersoftware.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zaubersoftware.gnip4j.api.impl;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.zaubersoftware.gnip4j.api.UriStrategy;

/**
 * The default implementation of {@link UriStrategy} that creates {@link URI}s to connect
 * to the real Gnip endpoint.
 *
 * <p>
 * The base URI format for stream is {@link DefaultUriStrategy#BASE_GNIP_URI_FMT}
 * <p>
 *
 * <p>
 * The base URI format for rules is {@link DefaultUriStrategy#BASE_GNIP_RULES_URI_FMT}
 * <p>
 *
 * @author Guido Marucci Blas
 * @since 11/11/2011
 */
public final class DefaultUriStrategy implements UriStrategy {
    public static final String BASE_GNIP_STREAM_URI = "https://stream.gnip.com:443/accounts/%s/publishers/twitter/streams/%s/%s.json";
    public static final String BASE_GNIP_RULES_URI = "https://api.gnip.com:443/accounts/%s/publishers/twitter/streams/%s/%s/rules.json";
    
    // Replay Date Format
    DateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
    
    @Override
    public URI createStreamUri(String streamType, final String account, String streamName) {
        validate(account,streamName);
        return URI.create(String.format(BASE_GNIP_STREAM_URI, account.trim(), streamType.trim(), streamName.trim()));
    }


    @Override
    public URI createRulesUri(String streamType, final String account, String streamName) {
        validate(account,streamName);
        return URI.create(String.format(BASE_GNIP_RULES_URI, account.trim(), streamType.trim(), streamName.trim()));
    }
    
    
    private void validate(String account, String streamName) {
        if (account == null || account.trim().isEmpty()) {
            throw new IllegalArgumentException("The account cannot be null or empty");
        }
        if (streamName == null || streamName.trim().isEmpty()) {
            throw new IllegalArgumentException("The streamName cannot be null or empty");
        }
    }
}
