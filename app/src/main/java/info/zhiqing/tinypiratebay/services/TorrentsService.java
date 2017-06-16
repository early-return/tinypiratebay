package info.zhiqing.tinypiratebay.services;

import info.zhiqing.tinypiratebay.entities.Torrent;
import info.zhiqing.tinypiratebay.entities.TorrentsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by zhiqing on 17-6-15.
 */

public interface TorrentsService {
    @GET("{page}")
    Call<TorrentsResponse> getTorrents(@Path("page") int page);
}
