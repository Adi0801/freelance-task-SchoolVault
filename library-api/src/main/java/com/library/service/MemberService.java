package com.library.service;

import com.library.dto.mapper.MemberMapper;
import com.library.dto.request.MemberRequest;
import com.library.dto.response.MemberResponse;
import com.library.entity.Member;
import com.library.entity.enums.MemberStatus;
import com.library.exception.DuplicateResourceException;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for managing library members.
 * Handles CRUD operations with email uniqueness enforcement and member deactivation.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * Creates a new member after validating email uniqueness.
     *
     * @param request the member creation request
     * @return the created member response
     * @throws DuplicateResourceException if a member with the same email already exists
     */
    public MemberResponse createMember(MemberRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Member", "email", request.getEmail());
        }

        Member member = MemberMapper.toEntity(request);
        Member savedMember = memberRepository.save(member);
        return MemberMapper.toResponse(savedMember);
    }

    /**
     * Updates an existing member identified by its ID.
     * Validates email uniqueness if the email has changed.
     *
     * @param id      the member ID
     * @param request the member update request
     * @return the updated member response
     * @throws ResourceNotFoundException  if the member is not found
     * @throws DuplicateResourceException if the new email conflicts with another member
     */
    public MemberResponse updateMember(Long id, MemberRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", id));

        // Check email uniqueness only if it has changed
        if (!member.getEmail().equals(request.getEmail()) && memberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Member", "email", request.getEmail());
        }

        MemberMapper.updateEntity(member, request);
        Member updatedMember = memberRepository.save(member);
        return MemberMapper.toResponse(updatedMember);
    }

    /**
     * Deactivates a member by setting their status to {@link MemberStatus#INACTIVE}.
     *
     * @param id the member ID
     * @return the deactivated member response
     * @throws ResourceNotFoundException if the member is not found
     */
    public MemberResponse deactivateMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", id));

        member.setStatus(MemberStatus.INACTIVE);
        Member savedMember = memberRepository.save(member);
        return MemberMapper.toResponse(savedMember);
    }

    /**
     * Retrieves a single member by its ID.
     *
     * @param id the member ID
     * @return the member response
     * @throws ResourceNotFoundException if the member is not found
     */
    @Transactional(readOnly = true)
    public MemberResponse getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", id));

        return MemberMapper.toResponse(member);
    }

    /**
     * Retrieves all members.
     *
     * @return the list of member responses
     */
    @Transactional(readOnly = true)
    public List<MemberResponse> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        return MemberMapper.toResponseList(members);
    }

    /**
     * Returns the count of active members.
     *
     * @return the active member count
     */
    @Transactional(readOnly = true)
    public long getActiveMemberCount() {
        return memberRepository.countByStatus(MemberStatus.ACTIVE);
    }
}
