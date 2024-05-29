package com.rparnp.gist_tool.service;

import com.rparnp.gist_tool.client.GitHubClient;
import com.rparnp.gist_tool.client.PipedriveClient;
import com.rparnp.gist_tool.config.ToolConfig;
import com.rparnp.gist_tool.exceptions.NetworkException;
import com.rparnp.gist_tool.model.github.Gist;
import com.rparnp.gist_tool.model.pipedrive.Deal;
import com.rparnp.gist_tool.model.pipedrive.DealsResponse;
import com.rparnp.gist_tool.model.pipedrive.StagesResponse;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GistService {

    @Resource
    private GitHubClient gitHubClient;
    @Resource
    private PipedriveClient pipedriveClient;
    @Autowired
    private ToolConfig toolConfig;

    public List<String> getScannedUsers() {
        List<String> scannedUsers = new ArrayList<>();
        try {
            List<StagesResponse.Data> stages = pipedriveClient.getAllStages();
            stages.forEach(s -> scannedUsers.add(s.getName()));
        } catch (IOException | InterruptedException e) {
            throw new NetworkException();
        }
        return scannedUsers;
    }

    public List<Gist> getGists(String username) {
        List<Gist> response;
        try {
            response = gitHubClient.getGists(username);
        } catch (IOException | URISyntaxException | InterruptedException e) {
            throw new NetworkException();
        }
        return response;
    }

    public void uploadGists(String username) {
        Integer stageId;
        Set<String> existingDealsTitles;

        // Create Stage for user if it does not exist
        try {
            Optional<StagesResponse.Data> stage = pipedriveClient.getAllStages().stream()
                    .filter(s -> s.getName().equals(username)).findFirst();
            stageId = stage.isEmpty() ? pipedriveClient.createStage(username) : stage.get().getId();

            existingDealsTitles = pipedriveClient.getAllDeals().stream().map(DealsResponse.Data::getTitle)
                    .collect(Collectors.toSet());
        } catch (IOException | InterruptedException e) {
            throw new NetworkException();
        }

        List<Gist> gists = getGists(username);
        List<Deal> deals = gists.stream().map(gist ->
                new Deal(gist.getDescription(), "Placeholder", toolConfig.getPipedrivePipelineId(), stageId)).toList();

        // Create deals if they don't exist
        deals.forEach(deal -> {
            if (!existingDealsTitles.contains(deal.getTitle())) {
                try {
                    pipedriveClient.createDeal(deal);
                } catch (IOException | InterruptedException e) {
                    throw new NetworkException();
                }
            }
        });
    }
}
