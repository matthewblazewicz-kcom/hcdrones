package town.boom.videos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Scanner;
import java.util.stream.IntStream;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Endpoint {

    private int latency;
    private int numberOfConnectedServers;
    private int[] connectedServersIds;
    private int[] connectedServersLatency;
    private int[] latencyByServerId; // -1 == no connection
    private int totalNumberOfServers;

    public static Endpoint readFromScanner(Scanner scanner, int totalNumberOfServers) {
        Endpoint endpoint = new Endpoint();
        endpoint.totalNumberOfServers = totalNumberOfServers;

        // the latency of serving a video request from the data center to this D ≤ 4 endpoint, in milliseconds
        endpoint.latency = scanner.nextInt();

        // the number of cache servers that this endpoint is connected to
        endpoint.numberOfConnectedServers = scanner.nextInt();
        scanner.nextLine();

        // K lines describing the connections from the endpoint to each of the K connected cache servers
        endpoint.connectedServersIds = new int[endpoint.numberOfConnectedServers];
        endpoint.connectedServersLatency = new int[endpoint.numberOfConnectedServers];
        IntStream.range(0, endpoint.numberOfConnectedServers).forEach(serverDescriptionNumber -> {
            // the ID of the cache server
            endpoint.connectedServersIds[serverDescriptionNumber] = scanner.nextInt();

            // the latency of serving a video request from this cache server to this endpoint, in milliseconds
            endpoint.connectedServersLatency[serverDescriptionNumber] = scanner.nextInt();
            scanner.nextLine();
        });

        endpoint.latencyByServerId = new int[totalNumberOfServers];
        for (int i = 0; i < totalNumberOfServers; i++) {
            endpoint.latencyByServerId[i] = -1;
        }
        for (int i = 0; i < endpoint.numberOfConnectedServers; i++) {
            endpoint.latencyByServerId[endpoint.connectedServersIds[i]] = endpoint.connectedServersLatency[i];
        }
        return endpoint;
    }
}
