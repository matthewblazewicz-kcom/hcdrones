package town.boom.videos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Scanner;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    private int videoId;
    private int endpointId;
    private int numberOfRequests;

    public static Request readFromScanner(Scanner scanner) {
        Request request = new Request();

        // - the ID of the requested video
        request.videoId = scanner.nextInt();

        // - the ID of the endpoint from which the requests are coming from
        request.endpointId = scanner.nextInt();

        // - the number of requests
        request.numberOfRequests = scanner.nextInt();
        scanner.nextLine();

        return request;
    }
}
