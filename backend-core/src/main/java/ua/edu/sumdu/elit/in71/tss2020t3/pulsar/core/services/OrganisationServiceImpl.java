package ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.services;

import static ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.entities.UserStatus.USER_STATUS_PREMIUM_ACCOUNT;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.entities.Organisation;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.entities.User;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.entities.client.ClientHost;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.exceptions.businesslogic.AlreadyExistsException;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.exceptions.businesslogic.UserStatusException;

/**
 * This is a default implementation of {@link OrganisationService}
 * */
@AllArgsConstructor
public class OrganisationServiceImpl implements OrganisationService {

    private static final Logger LOGGER = Logger.getLogger(
        OrganisationServiceImpl.class
    );

    private final SessionFactory sessionFactory;

    private final UserService userService;

    @Override
    public void addToOrganisationMembers(
        User user,
        Organisation.ID organisationId
    ) throws AlreadyExistsException {
        Organisation organisation = findById(organisationId);
        if (organisation == null) {
            throw new NoSuchElementException(
                "The organisation specified by "
                    + organisationId
                    + " does not exist"
            );
        }
        try (Session session = sessionFactory.openSession()) {
            if (isMember(user, organisation)) {
                throw new AlreadyExistsException(
                    "The user (username = "
                        + user.getUsername()
                        + ") is a member of "
                        + organisation.getName()
                        + " (organisation id = "
                        + organisation.getId()
                        + ") already"
                );
            }
            if (!userService.isUserPremiumAccount(user)) {
                throw new UserStatusException(
                    "The user (username = "
                        + user.getUsername()
                        + ") does not have "
                        + USER_STATUS_PREMIUM_ACCOUNT
                        + " status"
                );
            }
            Transaction t = session.beginTransaction();
            organisation.getMembers().add(user);
            session.update(organisation);
            session.flush();
            t.commit();
        } catch (UserStatusException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Organisation create(String organisationName, User owner)
        throws UserStatusException {
        Organisation organisation;
        if (!userService.isUserPremiumAccount(owner)) {
            throw new UserStatusException(
                "The user does not have premium account status"
            );
        }
        try (Session session = sessionFactory.openSession()) {
            Transaction t = session.beginTransaction();
            organisation = new Organisation(
                null,
                organisationName,
                owner,
                new HashSet<>()
            );
            session.save(organisation);
            session.flush();
            t.commit();
        }
        return organisation;
    }

    @Override
    public Organisation findById(Organisation.ID organisationId) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(
                Organisation.class, organisationId.getOrganisationId()
            );
        }
    }

    @Override
    public boolean isMember(User user, Organisation organisation) {
        try (Session session = sessionFactory.openSession()) {
            return session
                .createQuery(
                    "from Organisation o where o.id = :organisationId "
                        + "and :member in elements(o.members)"
                )
                .setParameter(
                    "member",
                    user
                )
                .setParameter(
                    "organisationId",
                    organisation.getId().getOrganisationId()
                )
                .list().size() > 0;
        }
    }

    @Override
    public Set<ClientHost> getOrganisationClientHosts(
        Organisation organisation
    ) {
        try (Session session = sessionFactory.openSession()) {
            return new HashSet<>(
                (List<ClientHost>) session
                    .createQuery(
                        "select distinct ch "
                            + "from Organisation o "
                            + "left join User u on "
                            + "(u in elements(o.members) or u = o.owner) "
                            + "inner join ClientHost ch "
                            + "on ch.owner = u "
                            + "where o = :organisation"
                    )
                    .setParameter("organisation", organisation)
                    .list()
            );
        }
    }

    @Override
    public Set<Organisation> getByMember(User user) {
        try (Session session = sessionFactory.openSession()) {
            return new HashSet<>(
                (List<Organisation>) session
                    .createQuery(
                        "select distinct o "
                            + "from Organisation o "
                            + "where :user in elements(o.members) "
                            + "or :user = o.owner"
                    )
                    .setParameter("user", user)
                    .list()
            );
        }
    }

    @Override
    public Set<Organisation> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return new HashSet<>(
                (List<Organisation>) session
                    .createQuery(
                        "from Organisation"
                    )
                    .list()
            );
        }
    }
}
