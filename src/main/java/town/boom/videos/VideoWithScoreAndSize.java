package town.boom.videos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VideoWithScoreAndSize implements Comparable<VideoWithScoreAndSize> {

    private int videoId;
    private int videoSize;
    private int requests;
    private int score;
    private double scoreWithSize;

    @Override
    public int compareTo(VideoWithScoreAndSize o) {
        return (int) (- scoreWithSize + o.scoreWithSize);
    }
}
