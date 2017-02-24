package town.boom.videos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VideoWithScore implements Comparable<VideoWithScore> {

    private int videoId;
    private int videoSize;
    private int requests;
    private int score;

    @Override
    public int compareTo(VideoWithScore o) {
        return - score + o.score;
    }
}
