package com.taskmanager.modules.team.repository;

import com.taskmanager.modules.team.entity.TeamMember;
import com.taskmanager.modules.team.entity.Team;
import com.taskmanager.modules.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    Optional<TeamMember> findByTeamAndUser(Team team, User user);
}