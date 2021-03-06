package ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.entities;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A Hibernate POJO for a DB record that represents a {@link User} activation
 * (by default via email)
 *
 * @see ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.services.MailService
 * @see User
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserRegistrationConfirmation implements Serializable {

    private ID id;

    private ZonedDateTime registrationDate;

    private ZonedDateTime confirmationDate;

    private UUID key;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static class ID implements Serializable {
        private User user;
    }
}
