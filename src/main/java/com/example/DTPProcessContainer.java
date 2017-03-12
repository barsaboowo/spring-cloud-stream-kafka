package com.example;

import org.apache.avro.specific.SpecificRecord;

/**
 * Created by b on 12/3/17.
 */
public interface DTPProcessContainer<Request extends SpecificRecord, Response extends SpecificRecord> {
     Response process(Request payload, Response response);
}
