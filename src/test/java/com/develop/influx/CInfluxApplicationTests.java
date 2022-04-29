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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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


    @org.junit.Test
    public void connect() {
        InfluxDBClient client = this.influxDBClient();
        System.out.println(client);
    }

    @Test
    void del01() {
        delete("temp_realtime_calculate_result",OffsetDateTime.parse("2022-04-27T13:57:00.000Z"),OffsetDateTime.parse("2022-04-27T13:57:00.001Z"),"_measurement=\"realtime_calculate_result\"");
        delete("temp_realtime_calculate_result",OffsetDateTime.parse("2022-04-27T13:57:00.000Z"),OffsetDateTime.parse("2022-04-27T13:57:00.001Z"),"_measurement=\"temp_equipment_gas\"");

        delete("temp_growth_calculate_result",OffsetDateTime.parse("2022-04-27T13:57:00.000Z"),OffsetDateTime.parse("2022-04-27T13:57:00.001Z"),"_measurement=\"growth_calculate_result\"");
        delete("temp_growth_calculate_result",OffsetDateTime.parse("2022-04-27T13:57:00.000Z"),OffsetDateTime.parse("2022-04-27T13:57:00.001Z"),"_measurement=\"temp_equipment_gas_growth\"");
        delete("temp_growth_calculate_result",OffsetDateTime.parse("2022-04-28T06:16:00.000Z"),OffsetDateTime.parse("2022-04-28T06:16:00.001Z"),"_measurement=\"temp_equipment_gas_growth\"");

    }


    @Test
    public void clearAll() {
        OffsetDateTime start = OffsetDateTime.parse("2022-04-01T00:00:00Z");
        OffsetDateTime stop = OffsetDateTime.parse("2022-05-01T00:00:00Z");

        List<String> bucketNames = new ArrayList<>();
        bucketNames.add("temp_realtime_calculate_result");
        bucketNames.add("temp_growth_calculate_result");
        bucketNames.add("temp_realtime_collection_data");
        bucketNames.add("forever_realtime_calculate_result");
        bucketNames.add("forever_growth_calculate_result");
        bucketNames.add("forever_realtime_collection_data");
        bucketNames.add("calculate_queue");

        ExecutorService executorService = Executors.newFixedThreadPool(7);
        final CountDownLatch latch = new CountDownLatch(7);

        bucketNames.forEach(item->{
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    delete(item, start, stop, "");
                    latch.countDown();
                }
            };

            executorService.execute(runnable);

        });

        try {
            latch.await();
            executorService.shutdown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
