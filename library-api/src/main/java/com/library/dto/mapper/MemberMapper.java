package com.library.dto.mapper;

import com.library.dto.request.MemberRequest;
import com.library.dto.response.MemberResponse;
import com.library.entity.Member;
import com.library.entity.enums.MemberStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between {@link Member} entities and Member DTOs.
 */
@Component
public class MemberMapper {

    /**
     * Converts a {@link Member} entity to a {@link MemberResponse} DTO.
     * The status enum is serialized as its name string.
     *
     * @param member the member entity
     * @return the member response DTO
     */
    public static MemberResponse toResponse(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .status(member.getStatus().name())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }

    /**
     * Converts a {@link MemberRequest} DTO to a new {@link Member} entity.
     * Sets the initial status to {@link MemberStatus#ACTIVE}.
     *
     * @param request the member request DTO
     * @return a new member entity
     */
    public static Member toEntity(MemberRequest request) {
        return Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .status(MemberStatus.ACTIVE)
                .build();
    }

    /**
     * Updates an existing {@link Member} entity's fields from a {@link MemberRequest} DTO.
     *
     * @param member  the existing member entity to update
     * @param request the member request containing updated values
     */
    public static void updateEntity(Member member, MemberRequest request) {
        member.setName(request.getName());
        member.setEmail(request.getEmail());
    }

    /**
     * Converts a list of {@link Member} entities to a list of {@link MemberResponse} DTOs.
     *
     * @param members the list of member entities
     * @return the list of member response DTOs
     */
    public static List<MemberResponse> toResponseList(List<Member> members) {
        return members.stream()
                .map(MemberMapper::toResponse)
                .collect(Collectors.toList());
    }
}
