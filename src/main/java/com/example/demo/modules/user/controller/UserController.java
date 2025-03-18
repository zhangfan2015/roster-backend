package com.example.demo.modules.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.modules.role.entity.Role;
import com.example.demo.modules.role.service.RoleService;
import com.example.demo.modules.user.entity.User;
import com.example.demo.modules.user.entity.UserRole;
import com.example.demo.modules.user.service.UserRoleService;
import com.example.demo.modules.user.service.UserService;
import com.example.demo.common.util.JwtUtil;
import com.example.demo.modules.user.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import io.jsonwebtoken.Claims;

/**

 */

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

	@Autowired
	private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private JwtUtil jwtUtil;

	/**
	 *
	 * @param user
	 * @return
	 */

	@PostMapping("/login")
	public Result<LoginVO> login(@RequestBody User user) {

		if (user.getId()!=null) {
			User newUser = new User();
			newUser.setId(user.getId());
			newUser.setStatus(user.getStatus());
			newUser.setUpdateTime(LocalDateTime.now());
			userService.updateById(newUser);
			User byId = userService.getById(user.getId());
			if (byId!=null && byId.getStatus()==1) {
				User loginUser = userService.login(user.getUsername(), user.getPassword());
				if (loginUser != null) {
					// 生成访问token和刷新token
					String accessToken = jwtUtil.generateAccessToken(loginUser.getId(), loginUser.getUsername());
					String refreshToken = jwtUtil.generateRefreshToken(loginUser.getId(), loginUser.getUsername());
					// 清除密码
					loginUser.setPassword(null);
					return Result.OK(LoginVO.build(accessToken, refreshToken, loginUser));
				}
			}else {
				return Result.error("用户未验证邮箱！");
			}
		}else {
			User loginUser = userService.login(user.getUsername(), user.getPassword());
			if (loginUser != null) {
				if (loginUser.getStatus()!=1) {
					return Result.error("用户未验证邮箱！");
				}
				// 生成访问token和刷新token
				String accessToken = jwtUtil.generateAccessToken(loginUser.getId(), loginUser.getUsername());
				String refreshToken = jwtUtil.generateRefreshToken(loginUser.getId(), loginUser.getUsername());
				// 清除密码
				loginUser.setPassword(null);
				return Result.OK(LoginVO.build(accessToken, refreshToken, loginUser));
			}else {
				return Result.error("用户名或密码错误");
			}
		}

		return Result.error("用户名或密码错误");
	}

	@PostMapping("/refresh-token")
	public Result<LoginVO> refreshToken(@RequestHeader("Authorization") String authorization) {
		try {
			// 从请求头中获取刷新token
			String refreshToken = authorization.replace("Bearer ", "");
			
			// 验证刷新token
			if (!jwtUtil.validateToken(refreshToken)) {
				return Result.error("无效的刷新令牌");
			}
			
			// 解析token获取用户信息
			Claims claims = jwtUtil.parseToken(refreshToken);
			String userId = claims.getSubject();
			String username = claims.get("username", String.class);
			
			// 生成新的访问token
			String newAccessToken = jwtUtil.generateAccessToken(userId, username);
			
			// 如果刷新token即将过期，则同时生成新的刷新token
			String newRefreshToken = refreshToken;
			if (jwtUtil.isTokenNearExpiration(refreshToken)) {
				newRefreshToken = jwtUtil.generateRefreshToken(userId, username);
			}
			
			// 获取用户信息
			User user = userService.getById(userId);
			if (user != null) {
				user.setPassword(null);
				return Result.OK(LoginVO.build(newAccessToken, newRefreshToken, user));
			}
			
			return Result.error("用户不存在");
		} catch (Exception e) {
			return Result.error("刷新令牌失败");
		}
	}

	@PostMapping("/register")
	public Result<Boolean> register(@RequestBody User user) {
		if (userService.register(user)) {
			return Result.OK(true);
		}
		return Result.error("注册失败，用户名已存在!");
	}

	@RequestMapping(value = "/userRoleList", method = RequestMethod.GET)
	public Result<IPage<User>> userRoleList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
											   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
		Result<IPage<User>> result = new Result<IPage<User>>();
		Page<User> page = new Page<User>(pageNo, pageSize);
		String roleId = req.getParameter("roleId");
		String username = req.getParameter("username");
		IPage<User> pageList = userService.getUserByRoleId(page,roleId,username);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	@PostMapping("/addUsers")
	public Result<String> addUser(@RequestBody User user) {
		// 获取当前日期和时间
		user.setCreateTime(LocalDateTime.now());
		userService.save(user);
		return Result.OK("添加成功！");
	}

	@GetMapping("/getList")
	public List<User> getList(){
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		List<User> users = userService.list(queryWrapper);
		return users;
	}


	@GetMapping("/getUserByName")
	public Result<User> getUserByName(@RequestParam("username") String username){
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("username",username);
		User user = userService.getOne(queryWrapper);
		return Result.OK(user);
	}

	@PutMapping("/edit")
	public Result<String> updateUser(@RequestBody User user) {
		user.setUpdateTime(LocalDateTime.now());
		userService.updateById(user);
		return Result.OK("编辑成功！");
	}

	@DeleteMapping("/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		userService.removeById(id);
		return Result.OK("删除成功!");
	}

	@GetMapping("/getUsersByCompanyId")
	public List<User> getUsersByCompanyId(@RequestParam(name="companyId",required=true) String companyId) {
		LambdaQueryWrapper<User> queryWrapper = new QueryWrapper<User>().lambda();
		queryWrapper.eq(User::getCompanyId,companyId);
		List<User> users = userService.list(queryWrapper);
		return users;
	}


	@GetMapping("/getUsersByComAndDeparts")
	public List<User> getUsersByComAndDeparts(@RequestParam(name="companyId",required=true) String companyId,
											  @RequestParam(name="departName",required=true) String departName) {
		LambdaQueryWrapper<User> queryWrapper = new QueryWrapper<User>().lambda();
		queryWrapper.eq(User::getCompanyId,companyId);
		queryWrapper.like(User::getDepartmentName,departName);
		List<User> users = userService.list(queryWrapper);
		return users;
	}

	@PostMapping("/addUserRole")
	public Result<String> addUserRole(@RequestBody UserRole user) {
		// 获取当前日期和时间

		userRoleService.save(user);
		return Result.OK("添加成功！");
	}


	@GetMapping("/getUserByRole")
	public List<String> getUserByRole(@RequestParam(name="id",required=true) String id) {
		LambdaQueryWrapper<UserRole> queryWrapper = new QueryWrapper<UserRole>().lambda();
		queryWrapper.eq(UserRole::getUserId,id);
		List<UserRole> users = userRoleService.list(queryWrapper);

		List<String> list = new ArrayList<>();
		users.forEach(user -> {
			LambdaQueryWrapper<Role> queryWrapper2 = new QueryWrapper<Role>().lambda();
			queryWrapper2.eq(Role::getId,user.getRoleId());
			Role role = roleService.getOne(queryWrapper2);
			list.add(role.getRoleName());
		});

		return list;
	}

    @GetMapping("/currentUser")
    public Result<User> getCurrentUser(@RequestHeader("Authorization") String authorization) {
		//
        try {
            // 从请求头中获取访问token
            String accessToken = authorization.replace("Bearer ", "");
            
            // 验证访问token
            if (!jwtUtil.validateToken(accessToken)) {
                return Result.error("无效的访问令牌");
            }
            
            // 解析token获取用户信息
            Claims claims = jwtUtil.parseToken(accessToken);
            String userId = claims.getSubject();
            
            // 获取用户信息
            User user = userService.getById(userId);
            if (user != null) {
                user.setPassword(null);
                return Result.OK(user);
            }
            
            return Result.error("用户不存在");
        } catch (Exception e) {
            return Result.error("获取当前用户失败");
        }
    }

}
