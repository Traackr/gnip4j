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
import java.util.Date;
import java.util.concurrent.ExecutorService;

import com.zaubersoftware.gnip4j.api.RemoteResourceProvider;
import com.zaubersoftware.gnip4j.api.UriStrategy;

/**
 * 
 * @author paulkist
 * Created on: Dec 1, 2012
 */
public class ReplayGnipStream extends DefaultGnipStream {

    /** Creates a Replay HttpGnipStream  */
    public ReplayGnipStream(
            final RemoteResourceProvider client,
            final String account,
            final String streamName,
            final String streamType,            
            final ExecutorService activityService,
            final UriStrategy baseUriStrategy, Date from, Date to) {
        super(client,account,streamName,streamType,activityService,baseUriStrategy);
        setStreamURI(baseUriStrategy.createStreamUri(streamType, account, streamName));
    }
}
