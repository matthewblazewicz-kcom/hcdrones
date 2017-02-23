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
public class AnythingAnywhereSolution {

    private StreamingService service;

    public List<CacheServerStatus> solveIt() {
        List<CacheServerStatus> solution = new ArrayList<>();
        String cachedVideos = "";
        int usedCapacity = 0;
        boolean firstVideo = true;
//        int[] vids = Arrays.copyOf(service.getVideoSize(), service.getVideoSize().length);
//        Arrays.sort(vids);

//        Request[] requests = service.getRequests();
//        requests[0].g


        int[] videoRequests = new int[service.getNumberOfVideos()];
        for (int requestId = 0; requestId < service.getNumberOfRequests(); requestId++) {
            Request r = service.getRequests()[requestId];
            videoRequests[r.getVideoId()] += r.getNumberOfRequests();
        }

        TreeSet<VideoWithIndex> sortedVideos = new TreeSet<>();
        for (int videoIndex = 0; videoIndex < service.getNumberOfVideos(); videoIndex++) {
            sortedVideos.add(new VideoWithIndex(videoIndex,
                    service.getVideoSize()[videoIndex],
                    videoRequests[videoIndex]));
        }
        for (VideoWithIndex vid : sortedVideos) {
            if (usedCapacity + vid.getVideoSize() <= service.getCapacityOfCacheServer()) {
                if (!firstVideo) {
                    cachedVideos += " ";
                }
                firstVideo = false;
                cachedVideos += vid.getVideoId();
                usedCapacity += vid.getVideoSize();
            }
        }

//        for (int videoIndex = 0; videoIndex < service.getNumberOfVideos(); videoIndex++) {
//            if (usedCapacity + vids[videoIndex] <= service.getCapacityOfCacheServer()) {
//                if (!firstVideo) {
//                    cachedVideos += " ";
//                }
//                firstVideo = false;
//                cachedVideos += videoIndex;
//                usedCapacity += vids[videoIndex];
//            }
//        }
        for (int serverId = 0; serverId < service.getNumberOfServers(); serverId++) {
            solution.add(new CacheServerStatus(serverId, cachedVideos.split(" ")));
        }
        return solution;
    }
}
