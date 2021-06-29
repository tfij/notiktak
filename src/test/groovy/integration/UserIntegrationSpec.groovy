package integration

import integration.fixture.BaseIntegrationSpec
import integration.fixture.NotiktakClient
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Ignore

@Ignore // require run axonserver in test spring context
class UserIntegrationSpec extends BaseIntegrationSpec implements NotiktakClient {

    def "Should create user"() {
        when: "send user creation request"
        ResponseEntity<Map> response = createUser()

        then: "response is 200 and contains user id"
        response.statusCode == HttpStatus.OK
        isValidUUID(response.body.userId as String)
    }

    def "Should fetch created user"() {
        given: "created user"
        String createdUserId = createUser().body.userId

        when: "I fetch the user"
        ResponseEntity<Map> response = getUser(createdUserId)

        then: "response is 200 and contains user data"
        response.statusCode == HttpStatus.OK
        response.body == [:]
    }

    private static boolean isValidUUID(String uuidCandidate) {
        try {
            UUID.fromString(uuidCandidate)
            return true
        } catch(Exception ignored) {
            return false
        }
    }

}
