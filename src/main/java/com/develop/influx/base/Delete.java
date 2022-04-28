package com.develop.influx.base;

import com.influxdb.client.DeleteApi;
import com.influxdb.client.InfluxDBClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.OffsetDateTime;

public class Delete {
    @Autowired
    private InfluxDBClient influxDBClient;

    @Value("${influx2.org}")
    private String org;

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public void delete(String bucket, OffsetDateTime start, OffsetDateTime stop, String predicate){
        DeleteApi deleteApi = influxDBClient.getDeleteApi();
        deleteApi.delete(start,stop,predicate,bucket,org);
    }
}
