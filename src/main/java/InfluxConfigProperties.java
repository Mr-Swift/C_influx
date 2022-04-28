import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxConfigProperties {

    @Bean
    public InfluxDBClient influxDBClient(){
        InfluxDBClient client = InfluxDBClientFactory.create();
        client.enableGzip();
        return client;
    }
}
