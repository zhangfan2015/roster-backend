package java.com.example.demo.modules.user.vo;

import com.example.demo.modules.user.entity.User;
import lombok.Data;

@Data
public class LoginVO {
    //
    private String accessToken;
    private String refreshToken;
    private User userInfo;

    public static LoginVO build(String accessToken, String refreshToken, User userInfo) {
        LoginVO vo = new LoginVO();
        vo.setAccessToken(accessToken);
        vo.setRefreshToken(refreshToken);
        vo.setUserInfo(userInfo);
        return vo;
    }
} 