package com.develop.influx;

import com.develop.influx.base.Delete;
import com.influxdb.client.DeleteApi;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.OffsetDateTime;

@SpringBootTest
@Configuration
@RunWith(SpringRunner.class)
class CInfluxApplicationTests {
    @Value("${influx2.org}")
    private String org;


    public InfluxDBClient influxDBClient(){
        InfluxDBClient client = InfluxDBClientFactory.create();
        client.enableGzip();
        return client;
    }

    public void delete(String bucket, OffsetDateTime start, OffsetDateTime stop, String predicate){
        DeleteApi deleteApi = this.influxDBClient().getDeleteApi();
        deleteApi.delete(start,stop,predicate,bucket,org);
    }

    @Test
    void del01() {
        delete("temp_realtime_calculate_result",OffsetDateTime.parse("2022-04-27T13:57:00.000Z"),OffsetDateTime.parse("2022-04-27T13:57:00.001Z"),"_measurement=\"realtime_calculate_result\"");
        delete("temp_realtime_calculate_result",OffsetDateTime.parse("2022-04-27T13:57:00.000Z"),OffsetDateTime.parse("2022-04-27T13:57:00.001Z"),"_measurement=\"temp_equipment_gas\"");

        delete("temp_growth_calculate_result",OffsetDateTime.parse("2022-04-27T13:57:00.000Z"),OffsetDateTime.parse("2022-04-27T13:57:00.001Z"),"_measurement=\"growth_calculate_result\"");
        delete("temp_growth_calculate_result",OffsetDateTime.parse("2022-04-27T13:57:00.000Z"),OffsetDateTime.parse("2022-04-27T13:57:00.001Z"),"_measurement=\"temp_equipment_gas_growth\"");
        delete("temp_growth_calculate_result",OffsetDateTime.parse("2022-04-28T06:16:00.000Z"),OffsetDateTime.parse("2022-04-28T06:16:00.001Z"),"_measurement=\"temp_equipment_gas_growth\"");

    }

}
