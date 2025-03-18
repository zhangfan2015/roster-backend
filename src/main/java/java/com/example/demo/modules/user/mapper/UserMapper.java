package java.com.example.demo.modules.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.modules.user.entity.User;
import com.example.demo.modules.user.vo.UserDepVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据角色Id查询用户信息
     * @param page
     * @param roleId 角色id
     * @param username 用户登录账户
     * @return
     */
    IPage<User> getUserByRoleId(Page page, @Param("roleId") String roleId, @Param("username") String username);
    /**
     *  根据用户Ids,查询用户所属部门名称信息bb
     * @param userIds
     * @return
     */
    List<UserDepVo> getDepNamesByUserIds(@Param("userIds")List<String> userIds);

}
