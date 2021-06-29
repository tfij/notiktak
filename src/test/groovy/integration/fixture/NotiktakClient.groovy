package integration.fixture

import groovy.transform.SelfType
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity

@SelfType(BaseIntegrationSpec)
trait NotiktakClient {
    private final TestRestTemplate restTemplate = new TestRestTemplate()

    ResponseEntity<Map> createUser() {
        return restTemplate.exchange(
                localUrl("/users/create"),
                HttpMethod.POST,
                new HttpEntity<Object>(new HttpHeaders()),
                Map)
    }

    ResponseEntity<Map> getUser(String userId) {
        return restTemplate.exchange(
                localUrl("/users/$userId"),
                HttpMethod.GET,
                new HttpEntity<Object>(new HttpHeaders()),
                Map)
    }
}
