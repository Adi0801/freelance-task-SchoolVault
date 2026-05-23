package com.library.repository;

import com.library.entity.Member;
import com.library.entity.enums.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Member} entity operations.
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * Finds a member by their email address.
     *
     * @param email the email to search for
     * @return an Optional containing the member if found
     */
    Optional<Member> findByEmail(String email);

    /**
     * Checks whether a member with the given email exists.
     *
     * @param email the email to check
     * @return true if a member with the email exists
     */
    boolean existsByEmail(String email);

    /**
     * Retrieves all members with the given status.
     *
     * @param status the member status to filter by
     * @return list of members with the specified status
     */
    List<Member> findByStatus(MemberStatus status);

    /**
     * Counts all members with the given status.
     *
     * @param status the member status to count
     * @return the count of members with the specified status
     */
    long countByStatus(MemberStatus status);
}
