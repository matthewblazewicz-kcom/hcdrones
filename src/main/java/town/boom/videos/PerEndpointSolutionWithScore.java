package town.boom.videos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PerEndpointSolutionWithScore {

    private StreamingService service;

    public List<CacheServerStatus> solveIt() {
        List<CacheServerStatus> solution = new ArrayList<>();
        String cachedVideos = "";
        int usedCapacity = 0;
        boolean firstVideo = true;

        int[] videoRequests = new int[service.getNumberOfVideos()];
        int[][] videoRequestsByEndpoint = new int[service.getNumberOfEndpoints()][service.getNumberOfVideos()];
        for (int requestId = 0; requestId < service.getNumberOfRequests(); requestId++) {
            Request r = service.getRequests()[requestId];
            videoRequests[r.getVideoId()] += r.getNumberOfRequests();
            videoRequestsByEndpoint[r.getEndpointId()][r.getVideoId()] += r.getNumberOfRequests();
        }

        int[][] videoRequestsByCacheServer = new int[service.getNumberOfServers()][service.getNumberOfVideos()];
        for (int endpointId = 0; endpointId < service.getNumberOfEndpoints(); endpointId++) {
            for (int serverId : service.getEndpoints()[endpointId].getConnectedServersIds()) {
                for (int videoId = 0; videoId < service.getNumberOfVideos(); videoId++) {
                    videoRequestsByCacheServer[serverId][videoId] += videoRequestsByEndpoint[endpointId][videoId];
                }
            }
        }

        int[][] videoScoresByCacheServer = new int[service.getNumberOfServers()][service.getNumberOfVideos()];
        for (int endpointId = 0; endpointId < service.getNumberOfEndpoints(); endpointId++) {
            for (int serverId : service.getEndpoints()[endpointId].getConnectedServersIds()) {
                for (int videoId = 0; videoId < service.getNumberOfVideos(); videoId++) {
                    videoScoresByCacheServer[serverId][videoId] += videoRequestsByEndpoint[endpointId][videoId] *
                            (service.getEndpoints()[endpointId].getLatency()
                            - service.getEndpoints()[endpointId].getLatencyByServerId()[serverId]);
                }
            }
        }

        // for each server sort by the number of requests
        for (int serverId = 0; serverId < service.getNumberOfServers(); serverId++) {
            TreeSet<VideoWithScore> sortedVideosForEndpoint = new TreeSet<>();
            for (int videoIndex = 0; videoIndex < service.getNumberOfVideos(); videoIndex++) {
                sortedVideosForEndpoint.add(new VideoWithScore(videoIndex,
                        service.getVideoSize()[videoIndex],
                        videoRequestsByCacheServer[serverId][videoIndex],
                        videoScoresByCacheServer[serverId][videoIndex]));
            }

            cachedVideos = "";
            usedCapacity = 0;
            for (VideoWithScore vid : sortedVideosForEndpoint) {
                if (usedCapacity + vid.getVideoSize() <= service.getCapacityOfCacheServer()) {
                    if (!firstVideo) {
                        cachedVideos += " ";
                    }
                    firstVideo = false;
                    cachedVideos += vid.getVideoId();
                    usedCapacity += vid.getVideoSize();
                }
            }
//            solution.add(new CacheServerStatus(serverId, cachedVideos.split(" ")));
        }

        return solution;
    }
}
