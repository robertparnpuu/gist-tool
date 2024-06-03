package com.rparnp.gist_tool.service;

import com.rparnp.gist_tool.client.GitHubClient;
import com.rparnp.gist_tool.client.PipedriveClient;
import com.rparnp.gist_tool.config.ToolConfig;
import com.rparnp.gist_tool.exceptions.DatabaseConnectionException;
import com.rparnp.gist_tool.exceptions.NetworkException;
import com.rparnp.gist_tool.model.firestore.GistEntry;
import com.rparnp.gist_tool.model.github.File;
import com.rparnp.gist_tool.model.github.Gist;
import com.rparnp.gist_tool.model.pipedrive.Deal;
import com.rparnp.gist_tool.model.pipedrive.DealData;
import com.rparnp.gist_tool.model.pipedrive.DealResponse;
import com.rparnp.gist_tool.model.pipedrive.StagesResponse;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class GistService {

    Logger logger = LoggerFactory.getLogger(GistService.class);

    @Resource
    private GitHubClient gitHubClient;
    @Resource
    private PipedriveClient pipedriveClient;
    @Autowired
    private ToolConfig toolConfig;
    @Autowired
    private FirestoreService firestoreService;

    public List<String> getScannedUsers() {
        List<String> scannedUsers = new ArrayList<>();
        try {
            List<StagesResponse.Data> stages = pipedriveClient.getAllStages();
            stages.forEach(s -> scannedUsers.add(s.getName()));
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage(), e);
            throw new NetworkException();
        }
        return scannedUsers;
    }

    public List<Gist> getGists(String username) {
        List<Gist> response;
        try {
            response = gitHubClient.getGists(username);
        } catch (IOException | URISyntaxException | InterruptedException e) {
            logger.error(e.getMessage(), e);
            throw new NetworkException();
        }
        return response;
    }

    public List<GistEntry> getRecentlyAddedGists() {
        try {
            List<GistEntry> recentGists = firestoreService.getGists();
            firestoreService.removeRecentGists();
            return recentGists;
        } catch (ExecutionException | InterruptedException e) {
            logger.error(e.getMessage(), e);
            throw new NetworkException();
        }
    }

    public void uploadGists(String username) {
        Integer stageId;
        Set<String> existingDealsIds;

        // Create Stage for user if it does not exist
        try {
            Optional<StagesResponse.Data> stage = pipedriveClient.getAllStages().stream()
                    .filter(s -> s.getName().equals(username)).findFirst();
            stageId = stage.isEmpty() ? pipedriveClient.createStage(username) : stage.get().getId();

            existingDealsIds = pipedriveClient.getAllDeals().stream().map(DealData::getOriginId)
                    .collect(Collectors.toSet());
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage(), e);
            throw new NetworkException();
        }

        List<Gist> gists = getGists(username);
        List<Deal> deals = gists.stream()
                .map(gist -> new Deal(
                        StringUtils.isNotEmpty(gist.getDescription()) ? gist.getDescription() : "No name",
                        toolConfig.getPipedrivePipelineId(),
                        stageId,
                        gist.getId(),
                        gist.getFiles())
                ).toList();

        // Create deals if they don't exist
        deals.forEach(deal -> {
            if (!existingDealsIds.contains(deal.getOriginId())) {
                try {
                    DealResponse dealResponse = pipedriveClient.createDeal(deal);
                    for (File file : deal.getFiles().values()) {
                        pipedriveClient.createFile(dealResponse.getData().getId(), file);
                    }
                    firestoreService.addGist(new GistEntry(deal.getOriginId(), deal.getTitle(), username));

                } catch (IOException | InterruptedException | URISyntaxException e) {
                    logger.error(e.getMessage(), e);
                    throw new NetworkException();
                } catch (ExecutionException e) {
                    logger.error(e.getMessage(), e);
                    throw new DatabaseConnectionException();
                }
            }
        });

    }

    public void scanGists() {
        logger.info("Scanning all gists");
        getScannedUsers().forEach(this::uploadGists);
    }
}
