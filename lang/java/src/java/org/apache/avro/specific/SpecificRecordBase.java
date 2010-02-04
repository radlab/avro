/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.avro.specific;

import org.apache.avro.Schema;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.BinaryDecoder;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

/** Base class for generated record classes. */
public abstract class SpecificRecordBase
  implements SpecificRecord, Comparable<SpecificRecord> {

  public abstract Schema getSchema();
  public abstract Object get(int field);
  public abstract void put(int field, Object value);

  @SuppressWarnings(value="unchecked")
  public byte[] toBytes() throws java.io.IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    BinaryEncoder enc = new BinaryEncoder(out);
    SpecificDatumWriter writer = new SpecificDatumWriter(getSchema());
    writer.write(this, enc);
    return out.toByteArray();
  }

  @SuppressWarnings(value="unchecked")
	public void parse(java.nio.ByteBuffer buff) throws java.io.IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(buff.array(), buff.position(), buff.limit() - buff.position());
		BinaryDecoder dec = new BinaryDecoder(in);
		SpecificDatumReader reader = new SpecificDatumReader(getSchema());
		reader.read(this, dec);
	}

  @SuppressWarnings(value="unchecked")
	public void parse(byte[] bytes) throws java.io.IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		BinaryDecoder dec = new BinaryDecoder(in);
		SpecificDatumReader reader = new SpecificDatumReader(getSchema());
		reader.read(this, dec);
	}

  @Override
  public boolean equals(Object that) {
    if (that == this) return true;                        // identical object
    if (!(that instanceof SpecificRecord)) return false;  // not a record
    if (this.getClass() != that.getClass()) return false; // not same schema
    return this.compareTo((SpecificRecord)that) == 0;
  }
    
  @Override
  public int hashCode() {
    return SpecificData.get().hashCode(this, this.getSchema());
  }

  @Override
  public int compareTo(SpecificRecord that) {
    return SpecificData.get().compare(this, that, this.getSchema());
  }

  @Override
  public String toString() {
    return SpecificData.get().toString(this);
  }

}

