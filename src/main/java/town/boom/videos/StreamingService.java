package town.boom.videos;

import town.boom.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

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
        CacheServerStatus[] servers = new CacheServerStatus[numberOfServers];
        Arrays.stream(requests)
                .sorted((o1, o2) -> {
                    
                    int fs1 = videoSize[o1.getVideoId()];
                    int fs2 = videoSize[o2.getVideoId()];
                    return fs1 - fs2;
                })
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
