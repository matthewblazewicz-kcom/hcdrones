package town.boom.videos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CacheServerStatus {

    private int serverId;
    private String[] cachedVideos;

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(serverId + " ");
        stringBuilder.append(String.join(" ", cachedVideos));
        return stringBuilder.toString();
    }
}
