package town.boom.videos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CacheServerStatus {

    private int serverId;
    private String[] cachedVideos;

    private List<Integer> cachedVideoList = new ArrayList<>();

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(serverId);
        for (Integer id : cachedVideoList) {
            stringBuilder.append(" " + id);
        }
        return stringBuilder.toString();
    }

    public boolean fitsIn(int videoId, int[] videoSizes, int cacheCapacity) {
        int sum =0;
        for (int index : cachedVideoList) {
            sum += videoSizes[index];
        }
        return cacheCapacity - sum >= videoSizes[videoId];
    }

    public void cache(int videoId) {
        cachedVideoList.add(videoId);
    }

    public boolean cached(int videoId) {
        return cachedVideoList.contains(videoId);
    }
}
