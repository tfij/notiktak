package pl.tfij.notiktak.users.command

import org.axonframework.commandhandling.GenericCommandResultMessage
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pl.tfij.notiktak.users.coreapi.ConfirmUserEmailCommand
import pl.tfij.notiktak.users.coreapi.UserCreatedEvent
import pl.tfij.notiktak.users.coreapi.UserEmailConfirmedEvent

class UserSpec {
    private FixtureConfiguration<User> fixture

    @BeforeEach
    void setup() {
        fixture = new AggregateTestFixture<>(User.class)
    }

    @Test
    void "Should confirmed user email"() {
        UUID userId = UUID.fromString("75879c2e-4d27-4fcf-9a99-bc4b3a5a7839")
        String emailConfirmationToken = "cf838fb1e6c644269aa5eee65f507ddf"
        fixture.given(new UserCreatedEvent(userId, "andrew@example.com", emailConfirmationToken))
                .when(new ConfirmUserEmailCommand(userId, emailConfirmationToken))
                .expectSuccessfulHandlerExecution()
                .expectResultMessage(new GenericCommandResultMessage<String>("abc"))
                .expectEvents(new UserEmailConfirmedEvent(userId))
    }

    @Test
    void "Should not confirmed user email if email validation confirmation is invalid"() {
        UUID userId = UUID.fromString("75879c2e-4d27-4fcf-9a99-bc4b3a5a7839")
        String emailConfirmationToken = "cf838fb1e6c644269aa5eee65f507ddf"
        String invalidEmailConfirmationToken = "337503b0c3f64e898a82f605301374b7"
        fixture.given(new UserCreatedEvent(userId, "andrew@example.com", emailConfirmationToken))
                .when(new ConfirmUserEmailCommand(userId, invalidEmailConfirmationToken))
                .expectNoEvents()
                .expectException(RuntimeException)
    }
}
