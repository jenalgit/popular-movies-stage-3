package stanislav.volnjanskij.popularmovies.api;

import java.util.List;

/**
 * Created by Stas on 08.08.15.
 */
public class TrailersResponse {
    List<TrailerModel> results;

    public List<TrailerModel> getResults() {
        return results;
    }

    public void setResults(List<TrailerModel> results) {
        this.results = results;
    }
}
