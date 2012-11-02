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
package com.zaubersoftware.gnip4j.server;

import java.io.InputStream;

import com.zaubersoftware.gnip4j.server.netty.MockServer;

/**
 * Main entry point to start the {@link GnipServer}
 *
 * @author Guido Marucci Blas
 * @since 11/11/2011
 */
public final class Main {

    private static final InputStream ACTIVITIES = Main.class.getClassLoader().getResourceAsStream(
            //"com/zaubersoftware/gnip4j/server/activity/activities.json");
            "com/zaubersoftware/gnip4j/server/activity/unlimitedActivity.json");

    public static void main(final String[] args) throws InterruptedException  {
      boolean startNormal = true;
      Integer port = 8080;

      if (args.length >= 1) {
        port = Integer.valueOf(args[0]);             
      } 
      
      
      
               final MockServer mockServer = new MockServer();
               if (null != args && args.length > 1) {
                 if (args.length != 3) {
                   System.err.println("ERROR: Wrong number of parameters: " + args.length);
                   System.err.println("Usage: Main <port> [slow <interval in ms>] or [chunk <chunk size>]");
                   System.exit(-1);               
                 } 
                 if (args[1].equals("slow")) {
                   startNormal = false;
                  int interval = Integer.valueOf(args[2]);
                  mockServer.startSlowly(ACTIVITIES, interval, port);
                 } else if (args[1].equals("chunk")) {
                   startNormal = false;
                   int chunkSize = Integer.valueOf(args[2]);
                   mockServer.startLimited(ACTIVITIES, chunkSize, port);
                 } 
               } 
               if (startNormal) {
                 mockServer.start(ACTIVITIES, port);
               }
    }

}
