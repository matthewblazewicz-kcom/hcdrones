package town.boom.videos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VideoWithIndex implements Comparable<VideoWithIndex> {

    private int videoId;
    private int videoSize;
    private int requests;

    @Override
    public int compareTo(VideoWithIndex o) {
        return - requests + o.requests;
    }
}
