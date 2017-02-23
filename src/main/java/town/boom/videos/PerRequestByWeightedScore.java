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
public class PerRequestByWeightedScore {

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

        double[][] videoScoresByCacheServer = new double[service.getNumberOfServers()][service.getNumberOfVideos()];
        for (int endpointId = 0; endpointId < service.getNumberOfEndpoints(); endpointId++) {
            for (int serverId : service.getEndpoints()[endpointId].getConnectedServersIds()) {
                for (int videoId = 0; videoId < service.getNumberOfVideos(); videoId++) {
                    videoScoresByCacheServer[serverId][videoId] += 1.00 * videoRequestsByEndpoint[endpointId][videoId] *
                            (service.getEndpoints()[endpointId].getLatency()
                                    - service.getEndpoints()[endpointId].getLatencyByServerId()[serverId])
                            / service.getVideoSize()[videoId];
                }
            }
        }

        double[][] videoScoresOtherByCacheServer = new double[service.getNumberOfServers()][service.getNumberOfVideos()];
        for (int endpointId = 0; endpointId < service.getNumberOfEndpoints(); endpointId++) {
            for (int serverId : service.getEndpoints()[endpointId].getConnectedServersIds()) {
                for (int videoId = 0; videoId < service.getNumberOfVideos(); videoId++) {
                    videoScoresByCacheServer[serverId][videoId] += 1.00 * videoRequestsByEndpoint[endpointId][videoId] *
                            (service.getEndpoints()[endpointId].getLatency()
                                    - service.getEndpoints()[endpointId].getLatencyByServerId()[serverId]);
                }
            }
        }

        // for each server sort by the number of requests
        for (int serverId = 0; serverId < service.getNumberOfServers(); serverId++) {
            TreeSet<VideoWithScoreAndSize> sortedVideosForEndpoint = new TreeSet<>();
            TreeSet<VideoWithScoreAndSize> sortedVideosWithSizeForEndpoint = new TreeSet<>();
            for (int videoIndex = 0; videoIndex < service.getNumberOfVideos(); videoIndex++) {
                sortedVideosForEndpoint.add(new VideoWithScoreAndSize(videoIndex,
                        service.getVideoSize()[videoIndex],
                        videoRequestsByCacheServer[serverId][videoIndex],
                        0,
                        videoScoresOtherByCacheServer[serverId][videoIndex]));
                sortedVideosWithSizeForEndpoint.add(new VideoWithScoreAndSize(videoIndex,
                        service.getVideoSize()[videoIndex],
                        videoRequestsByCacheServer[serverId][videoIndex],
                        0,
                        videoScoresByCacheServer[serverId][videoIndex]));
            }

            cachedVideos = "";
            usedCapacity = 0;
            VideoWithScoreAndSize[] ones = sortedVideosForEndpoint.toArray(new VideoWithScoreAndSize[0]);
            VideoWithScoreAndSize[] twos = sortedVideosWithSizeForEndpoint.toArray(new VideoWithScoreAndSize[0]);

            for (int i = 0; i < sortedVideosForEndpoint.size(); i++) {
                VideoWithScoreAndSize vid = ones[i];
                if (usedCapacity + vid.getVideoSize() <= service.getCapacityOfCacheServer()) {
                    if (!firstVideo) {
                        cachedVideos += " ";
                    }
                    firstVideo = false;
                    cachedVideos += vid.getVideoId();
                    usedCapacity += vid.getVideoSize();
                }
                vid = twos[i];
                if (usedCapacity + vid.getVideoSize() <= service.getCapacityOfCacheServer()) {
                    if (!firstVideo) {
                        cachedVideos += " ";
                    }
                    firstVideo = false;
                    cachedVideos += vid.getVideoId();
                    usedCapacity += vid.getVideoSize();
                }
            }

            solution.add(new CacheServerStatus(serverId, cachedVideos.split(" ")));
        }

        return solution;
    }
}
