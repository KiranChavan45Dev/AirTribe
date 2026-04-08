package com.taskmanager.modules.team.service;

import com.taskmanager.modules.auth.entity.User;
import com.taskmanager.modules.auth.repository.UserRepository;
import com.taskmanager.modules.team.dto.AddUserToTeamRequest;
import com.taskmanager.modules.team.dto.TeamRequest;
import com.taskmanager.modules.team.dto.TeamResponse;
import com.taskmanager.modules.team.entity.Team;
import com.taskmanager.modules.team.entity.TeamMember;
import com.taskmanager.modules.team.repository.TeamMemberRepository;
import com.taskmanager.modules.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserRepository userRepository;

    private TeamResponse mapToResponse(Team team) {
        Set<TeamMember> members = team.getMembers() != null ? team.getMembers() : Set.of();
        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getDescription(),
                team.getCreatedAt(),
                members.stream()
                        .map(m -> new TeamResponse.UserSummary(
                                m.getUser().getId(),
                                m.getUser().getUsername(),
                                m.getUser().getEmail()))
                        .collect(Collectors.toSet())
        );
    }

    @Transactional
    public TeamResponse createTeam(TeamRequest request) {
        if (teamRepository.findByName(request.getName()).isPresent()) {
            throw new IllegalArgumentException("Team name already exists");
        }
        Team team = Team.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return mapToResponse(teamRepository.save(team));
    }

    @Transactional
    public TeamResponse addUserToTeam(Long teamId, AddUserToTeamRequest request) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (teamMemberRepository.findByTeamAndUser(team, user).isPresent()) {
            throw new IllegalArgumentException("User already in team");
        }

        TeamMember member = TeamMember.builder()
                .team(team)
                .user(user)
                .build();

        team.getMembers().add(member);
        teamMemberRepository.save(member);

        return mapToResponse(team);
    }


}