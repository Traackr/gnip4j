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
package com.zaubersoftware.gnip4j.api.model;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;

     @JsonAutoDetect
    public  class Activities implements Serializable  {
        /**
       * 
       */
      private static final long serialVersionUID = -7796043418440450933L;
        List<Activity> activities;
        /**
         * Creates the JSONDeserializationTest.Activities.
         *
         */
        public Activities() {
        }
        
        
        /**
         * Sets the activities. 
         *
         * @param activities <code>List<Activity></code> with the activities.
         */
        public void setActivities(List<Activity> activities) {
            this.activities = activities;
        }
        
        /**
         * Returns the activities.
         * 
         * @return <code>List<Activity></code> with the activities.
         */
        public List<Activity> getActivities() {
            return activities;
        }
        
    }