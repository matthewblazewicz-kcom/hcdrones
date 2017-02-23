package town.boom.videos;

import town.boom.Problem;

import java.util.ArrayList;
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
        ArrayList<CacheServerStatus> solutions = new ArrayList<>();
        return solutions;
    }
}
