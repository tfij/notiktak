package integration.fixture

import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.web.server.LocalServerPort
import pl.tfij.notiktak.NotiktakApplication
import spock.lang.Specification

@AutoConfigureDataMongo
@SpringBootTest(
        classes = [NotiktakApplication],
//        properties = "application.environment=integration",
        webEnvironment = WebEnvironment.RANDOM_PORT)
abstract class BaseIntegrationSpec extends Specification {

    @LocalServerPort
    protected int port

    String localUrl(String endpoint) {
        return "http://localhost:$port$endpoint"
    }
}
