/**
 * Copyright (c) 2011-2016 Zauber S.A. <http://flowics.com/>
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

/**
 * TODO: Description of the class, Comments in english by default
 *
 *
 * @author Juan F. Codagnone
 * @since Dec 13, 2012
 */
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;
    private String term;
    private String label;

    public final String getTerm() {
        return term;
    }

    public final String getLabel() {
        return label;
    }

    public final void setLabel(final String label) {
        this.label = label;
    }


    public final void setTerm(final String term) {
        this.term = term;
    }
}
