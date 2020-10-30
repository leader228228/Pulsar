package ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.services;

import java.util.UUID;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.dto.CreateClientHostDTO;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.entities.User;
import ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.entities.client.ClientHost;

public interface ClientHostService {

    /**
     * Creates a {@link ClientHost} based on a user request.
     * <p>
     * In this business case the requester will be an owner
     * of a new {@link ClientHost}
     *
     * @param     request   a POJO representation of a user request
     *                      for creating a new {@link ClientHost}
     * @param     requester a requester and an owner of new {@link ClientHost}
     * @return              created {@link ClientHost}
     * @exception ua.edu.sumdu.elit.in71.tss2020t3.pulsar.core.exceptions.AlreadyExistsException
     *                      if there already exists a host with such private key
     * @exception javax.validation.ConstraintViolationException
     *                      if passed {@code request} is not valid
     * @exception IllegalArgumentException
     *                      if {@code owner} does not exist
     *                      or passed object is not valid
     * */
    ClientHost createForUserRequest(
        CreateClientHostDTO request, User requester
    );

    /**
     * Searches en entity of {@link ClientHost} by its public key
     *
     * @param  publicKey a client's host public key
     * @return           a {@link ClientHost} associated with the
     *                   {@code publicKey} or {@code null} if it does not exist
     * */
    ClientHost getByPublicKey(String publicKey);

    /**
     * Searches en entity of {@link ClientHost} by its private key
     *
     * @param  privateKey a client's host private key
     * @return            a {@link ClientHost} associated with the
     *                    {@code privateKey} or {@code null} if it
     *                    does not exist
     * */
    ClientHost getByPrivateKey(UUID privateKey);
}