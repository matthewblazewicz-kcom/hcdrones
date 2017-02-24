package town.boom.videos;

import lombok.Getter;
import town.boom.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

@Getter
public class StreamingService extends Problem {

    private int numberOfVideos;
    private int numberOfEndpoints;
    private int numberOfRequests;
    private int numberOfServers;
    private int capacityOfCacheServer;

    private int[] videoSize;
    private Endpoint[] endpoints;
    private Request[] requests;

    @Override
    public void process(final Scanner scanner) {
        numberOfVideos = scanner.nextInt();
        numberOfEndpoints = scanner.nextInt();
        numberOfRequests = scanner.nextInt();
        numberOfServers = scanner.nextInt();
        capacityOfCacheServer = scanner.nextInt();
        scanner.nextLine();

        // V​ numbers describing the sizes of individual videos in megabytes: S , , .. S . 0 S1 . V −1
        // S is the size of video i in megabytes .
        videoSize = new int[numberOfVideos];
        IntStream.range(0, numberOfVideos).forEach(videoNumber -> {
            videoSize[videoNumber] = scanner.nextInt();
        });
        scanner.nextLine();

        // The next section describes each of the endpoints one after another, from endpoint 0 to endpoint E − 1
        endpoints = new Endpoint[numberOfEndpoints];
        IntStream.range(0, numberOfEndpoints).forEach(endpointNumber -> {
            endpoints[endpointNumber] = Endpoint.readFromScanner(scanner, numberOfServers);
        });

        // Finally, the last section contains R ​request descriptions in separate lines.
        requests = new Request[numberOfRequests];
        IntStream.range(0, numberOfRequests).forEach(requestNumber -> {
            requests[requestNumber] = Request.readFromScanner(scanner);
        });
    }

    @Override
    public List solve() {

        StreamingService service = this;
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
                    videoScoresByCacheServer[serverId][videoId] += 1.00 * videoRequestsByEndpoint[endpointId][videoId]
                            * (service.getEndpoints()[endpointId].getLatency()
                            - service.getEndpoints()[endpointId].getLatencyByServerId()[serverId])
                    ;
                }
            }
        }

        double[][] videoScoresWeightByCacheServer = new double[service.getNumberOfServers()][service.getNumberOfVideos()];
        for (int endpointId = 0; endpointId < service.getNumberOfEndpoints(); endpointId++) {
            for (int serverId : service.getEndpoints()[endpointId].getConnectedServersIds()) {
                for (int videoId = 0; videoId < service.getNumberOfVideos(); videoId++) {
                    videoScoresByCacheServer[serverId][videoId] += 1.00 * videoRequestsByEndpoint[endpointId][videoId]
                            * (service.getEndpoints()[endpointId].getLatency()
                            - service.getEndpoints()[endpointId].getLatencyByServerId()[serverId])
                    / service.getVideoSize()[videoId];
                }
            }
        }

        double[] videoScores = new double[service.getNumberOfVideos()];
        for (int videoId = 0; videoId < service.getNumberOfVideos(); videoId++) {
            for (int serverId = 0; serverId < service.getNumberOfServers(); serverId++) {
                videoScores[videoId] += videoScoresByCacheServer[serverId][videoId];
            }
        }

        double[] videoScoresWeight = new double[service.getNumberOfVideos()];
        for (int videoId = 0; videoId < service.getNumberOfVideos(); videoId++) {
            for (int serverId = 0; serverId < service.getNumberOfServers(); serverId++) {
                videoScoresWeight[videoId] += videoScoresWeightByCacheServer[serverId][videoId];
            }
        }

        CacheServerStatus[] servers = new CacheServerStatus[numberOfServers];
        Arrays.stream(requests)
                .sorted((o1, o2) -> {
                    double fs1 = videoScoresWeight[o1.getVideoId()];
                    double fs2 = videoScoresWeight[o2.getVideoId()];
                    return (int) (fs2 - fs1);
                })
                .skip(service.getNumberOfRequests() / 5)
                .sorted((o1, o2) -> {
//                    int fs1 = videoSize[o1.getVideoId()];
//                    int fs2 = videoSize[o2.getVideoId()];
                    double fs1 = videoScores[o1.getVideoId()];
                    double fs2 = videoScores[o2.getVideoId()];
                    return (int) (fs2 - fs1);
                })
//                .skip(service.getNumberOfRequests() / 10)
                .forEach(request -> {
                    int endpointId = request.getEndpointId();
                    Endpoint endpoint = endpoints[endpointId];
                    boolean cached = false;
                    for (Integer serverid : endpoint.getServerIdsInOrderOfLatency()) {
                        if (servers[serverid] != null && servers[serverid].cached(request.getVideoId())) {
                            cached = true;
                        }
                    }
                    if (!cached) {
                        for (Integer serverid : endpoint.getServerIdsInOrderOfLatency()) {
                            if (servers[serverid] == null ) {
                                servers[serverid] = new CacheServerStatus(serverid, new String[numberOfVideos], new ArrayList<>());
                            }
                            if (servers[serverid].fitsIn(request.getVideoId(),videoSize, capacityOfCacheServer)) {
                                servers[serverid].cache(request.getVideoId());
                                break;
                            }
                        }
                    }
                 });
        List result  = new ArrayList();
        for (CacheServerStatus server : servers) {
            if (server != null && !server.getCachedVideoList().isEmpty()) {
                result.add(server);
            }
        }
        return result;
    }
}
