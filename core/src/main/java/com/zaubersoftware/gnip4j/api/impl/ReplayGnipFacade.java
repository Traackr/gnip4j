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

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;

import com.zaubersoftware.gnip4j.api.GnipFacade;
import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.RemoteResourceProvider;
import com.zaubersoftware.gnip4j.api.StreamNotification;
import com.zaubersoftware.gnip4j.api.UriStrategy;
import com.zaubersoftware.gnip4j.api.exception.GnipException;
import com.zaubersoftware.gnip4j.api.model.Rules;
import com.zaubersoftware.gnip4j.api.stats.StreamStats;
import com.zaubersoftware.gnip4j.api.support.jmx.JMXProvider;
/**
 * Http implementation for the {@link GnipFacade}
 *
 * @author Guido Marucci Blas
 * @since Apr 29, 2011
 */
public class ReplayGnipFacade extends DefaultGnipFacade {

    private static final UriStrategy DEFAULT_BASE_URI_STRATEGY = new DefaultUriStrategy();

    /** Creates the HttpGnipFacade. */
    public ReplayGnipFacade(final RemoteResourceProvider facade, final UriStrategy baseUriStrategy) {
      super(facade, baseUriStrategy);
    }

    /** Creates the HttpGnipFacade. */
    public ReplayGnipFacade(final RemoteResourceProvider facade) {
        this(facade, DEFAULT_BASE_URI_STRATEGY);
    }


    public final GnipStream createStream(
            final String account,
            final String streamName, final String streamType,
            final StreamNotification observer, Date from, Date to) {
        final ExecutorService executor = Executors.newFixedThreadPool(getStreamDefaultWorkers());
        final GnipStream target = createStream(account, streamName, streamType, observer, executor, from, to);
        return new GnipStream() {
            @Override
            public void close() {
                try {
                    target.close();
                } finally {
                    executor.shutdown();
                }
            }

            @Override
            public void await() throws InterruptedException {
                target.await();
            }

            @Override
            public final String getStreamName() {
                return target.getStreamName();
            }

            @Override
            public StreamStats getStreamStats() {
                return target.getStreamStats();
            }
        };
    }

    public final GnipStream createStream(final String account, final String streamName, final String streamType,
            final StreamNotification observer, final ExecutorService executor, Date from, Date to) {
        final DefaultGnipStream stream = createStream(account, streamName, streamType,executor, from, to);
        stream.open(observer);
        GnipStream ret = stream;
        if(super.isUseJMX()) {
            ret = new GnipStream() {
                @Override
                public String getStreamName() {
                    return stream.getStreamName();
                }

                @Override
                public void close() {
                    try {
                        stream.close();
                    } finally {
                        JMXProvider.getProvider().unregister(stream);
                    }
                }

                @Override
                public void await() throws InterruptedException {
                    stream.await();
                }

                @Override
                public StreamStats getStreamStats() {
                    return stream.getStreamStats();
                }
            };
            JMXProvider.getProvider().registerBean(stream, stream.getStreamStats());
        }
        return ret;
    }

   
    @Override
    public final Rules getRules(final String account, final String streamName, final String streamType) {
        try {
            final InputStream gnipRestResponseStream = getFacade().getResource(getBaseUriStrategy().createRulesUri(streamType, account, streamName));
            final JsonParser parser =  ReplayGnipStream.getObjectMapper()
                    .getJsonFactory().createJsonParser(gnipRestResponseStream);
            final Rules rules = parser.readValueAs(Rules.class);
            gnipRestResponseStream.close();
            return rules;
        } catch (final JsonProcessingException e) {
            throw new GnipException("Unexpected response from Gnip REST API", e);
        } catch (final IOException e) {
            throw new GnipException(e);
        }
    }

    @Override
    public final void addRule(final String account, final String streamName, final String streamType, final Rules rules) {
        getFacade().postResource(getBaseUriStrategy().createRulesUri(streamType, account, streamName), rules);
    }
    
    @Override
    public final void deleteRule(final String account, final String streamName, final String streamType, final Rules rules) {
      getFacade().deleteResource(getBaseUriStrategy().createRulesUri(streamType, account, streamName), rules);
    }
    

    /**
     * Creates a new {@link DefaultGnipStream}
     *
     * @param domain
     * @param dataCollectorId
     * @param executor
     * @return
     */
    private DefaultGnipStream createStream(
            final String account,
            final String streamName, final String streamType,
            final ExecutorService executor, final Date from, final Date to) {
            return new ReplayGnipStream(getFacade(), account, streamName, streamType, executor, getBaseUriStrategy(), from, to);
    }
}
