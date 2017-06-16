package info.zhiqing.tinypiratebay.services;

import info.zhiqing.tinypiratebay.entities.TorrentDetailResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by zhiqing on 17-6-15.
 */

public interface TorrentDetailService {
    @GET("{code}")
    Call<TorrentDetailResponse> getDetail(@Path("code") String code);
}
