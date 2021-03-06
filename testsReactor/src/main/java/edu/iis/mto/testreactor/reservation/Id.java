/*
 * Copyright 2011-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package edu.iis.mto.testreactor.reservation;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.Validate;

public class Id implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;

    public Id(String id) {
        Validate.notNull(id);
        this.id = id;
    }

    protected Id() {}

    public static Id generate() {
        return new Id(UUID.randomUUID()
                          .toString());
    }

    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Id other = (Id) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return id;
    }
}
