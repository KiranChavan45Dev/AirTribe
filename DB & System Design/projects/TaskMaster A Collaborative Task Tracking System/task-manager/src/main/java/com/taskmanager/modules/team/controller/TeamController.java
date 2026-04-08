package com.taskmanager.modules.team.controller;

import com.taskmanager.modules.team.dto.AddUserToTeamRequest;
import com.taskmanager.modules.team.dto.TeamRequest;
import com.taskmanager.modules.team.dto.TeamResponse;
import com.taskmanager.modules.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@Valid @RequestBody TeamRequest request) {
        return ResponseEntity.ok(teamService.createTeam(request));
    }

    @PostMapping("/{teamId}/add-user")
    public ResponseEntity<TeamResponse> addUserToTeam(@PathVariable Long teamId,
                                                      @Valid @RequestBody AddUserToTeamRequest request) {
        return ResponseEntity.ok(teamService.addUserToTeam(teamId, request));
    }
}