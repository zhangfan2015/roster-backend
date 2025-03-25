package com.example.demo.modules.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.modules.company.entity.Company;
import com.example.demo.modules.role.entity.Role;
import com.example.demo.modules.role.service.RoleService;
import com.example.demo.modules.user.entity.User;
import com.example.demo.modules.user.entity.UserRole;
import com.example.demo.modules.user.service.UserRoleService;
import com.example.demo.modules.user.service.UserService;
import com.example.demo.common.util.JwtUtil;
import com.example.demo.modules.user.vo.LoginVO;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.jsonwebtoken.Claims;
import org.springframework.web.multipart.MultipartFile;

/**

 */

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private JwtUtil jwtUtil;
	@Value("${file.upload.path}")
	private String uploadPath;

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
	public Result<Boolean> register(@RequestBody User user) throws Exception {
		if (userService.register(user)) {
			return Result.OK(true);
		}
		return Result.error("注册失败，用户名已存在!");
	}


	@PostMapping("/add")
	public Result<String> addUser(@RequestBody User user) {
		// 获取当前日期和时间
		user.setCreateTime(LocalDateTime.now());
		userService.save(user);
		return Result.OK("添加成功！");
	}

	@GetMapping("/getUserList")
	public Result<Page<User>> getUserList(User user,
												@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												@RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		Page<User> page = userService.getUserList(user,pageNo,pageSize);
		return Result.OK(page);
	}

	@GetMapping("/getAllUser")
	public Result<?> getAllUser() {
		return Result.OK(userService.list());
	}

	@GetMapping("/getYxUserList")
	public Result<Page<User>> getYxUserList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		Page<User> page = userService.getYxUserList(pageNo,pageSize);
		return Result.OK(page);
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

		if (user.getRoleId()!=null){
			LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
			queryWrapper.eq(UserRole::getUserId,user.getId());
			queryWrapper.eq(UserRole::getRoleId,user.getRoleId());
			userRoleService.remove(queryWrapper);
			//
			UserRole userRole = new UserRole();
			userRole.setUserId(user.getId());
			userRole.setRoleId(user.getRoleId());
			userRoleService.save(userRole);
			//
			Role byId = roleService.getById(user.getRoleId());
			user.setRoleName(byId.getRoleName());
		}
		user.setUpdateTime(LocalDateTime.now());
		userService.updateById(user);
		return Result.OK("编辑成功！");
	}

	/**
	 * 修改用户密码
	 * @param user
	 * @return
	 */
	@PutMapping("/editPs")
	public Result<?> editPs(@RequestBody User user) {
		User u = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, user.getUsername()));
		if (u == null) {
			return Result.error("用户不存在！");
		}
		user.setId(u.getId());
		return userService.changePassword(user);
	}

	@DeleteMapping("/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		User byId = userService.getById(id);
		//删除状态(0-正常,1-已删除)
		byId.setDelFlag(1);
		userService.updateById(byId);
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

	@PutMapping("/updateStatus")
	public Result<String> updateStatus(@RequestBody User user) {
		if (user.getStatus() ==0){
			return Result.error("用户未验证邮箱！");
		}
		if (user.getStatus() == 1){
			user.setStatus(2);
		}else if (user.getStatus() == 2){
			user.setStatus(1);
		}
		user.setUpdateTime(LocalDateTime.now());
		userService.updateById(user);
		return Result.OK("编辑成功！");
	}


	//用户角色-------------------------------------------------------------------------

//	@GetMapping("/getUserList")
//	public Result<Page<User>> getUserList(User user,
//										  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
//										  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
//		Page<User> page = userService.getUserList(user,pageNo,pageSize);
//		return Result.OK(page);
//	}

	@PostMapping("/addRole")
	public Result<String> addUserRole(@RequestBody UserRole role) {
		// 获取当前日期和时间

		userRoleService.save(role);
		return Result.OK("添加成功！");
	}

//	@PutMapping("/editRole")
//	public Result<String> editRole(@RequestBody UserRole role) {
//		role.setUpdateTime(LocalDateTime.now());
//		userRoleService.updateById(role);
//		return Result.OK("编辑成功！");
//	}

	@GetMapping("/getUserByRole")
	public Result<List<String>> getUserByRole(@RequestParam(name="id",required=true) String id) {
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

		return Result.OK(list);
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


	@PostMapping("/bindUsers")
	public Result<?> bindUsers(@RequestBody Map<String, Object> params) {
		List<String> userIds = (List<String>) params.get("list");
		String roleId = (String) params.get("roleId");
		userIds.forEach(item -> {
			UserRole role = new UserRole();
			role.setRoleId(roleId);
			role.setUserId(item);
			userRoleService.save(role);
		});

		return Result.ok("添加成功");
	}

	@PostMapping("/unbindUser")
	public Result<?> unbindUser(@RequestBody Map<String, Object> params) {
		String roleId = (String) params.get("roleId");
		String userId = (String) params.get("userId");
		if (StringUtils.isEmpty(roleId) || StringUtils.isEmpty(userId)){
			return Result.error("移除失败！");
		}
		LambdaQueryWrapper<UserRole> queryWrapper = new QueryWrapper<UserRole>().lambda();
		queryWrapper.eq(UserRole::getRoleId,roleId);
		queryWrapper.eq(UserRole::getUserId,userId);
		userRoleService.remove(queryWrapper);
		return Result.ok("移除成功");
	}

	@PostMapping("/uploadAvatar")
	public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id) {
		try {
			// 检查文件是否为空
			if (file.isEmpty()) {
				return Result.error("请选择要上传的文件");
			}

			// 获取文件名
			String fileName = file.getOriginalFilename();
			// 获取文件后缀
			String suffix = fileName.substring(fileName.lastIndexOf("."));
			// 生成新的文件名
			String newFileName = UUID.randomUUID().toString() + suffix;

			// 检查上传目录是否存在，不存在则创建
			File uploadDir = new File(uploadPath);
			if (!uploadDir.exists()) {
				uploadDir.mkdirs();
			}

			// 构建文件保存路径
			String filePath = uploadPath + File.separator + newFileName;
			File dest = new File(filePath);

			// 保存文件
			file.transferTo(dest);

			// 更新用户头像信息到数据库
			User user = userService.getById(id);
			if (user != null) {
				// 保存相对路径到数据库
				String avatarUrl = "/uploads/" + newFileName;  // 这里的路径要根据你的静态资源访问配置来定
				user.setImg(avatarUrl);
				userService.updateById(user);

				return Result.OK(avatarUrl);
			} else {
				return Result.error("用户不存在");
			}

		} catch (IOException e) {
			log.error("文件上传失败", e);
			return Result.error("文件上传失败：" + e.getMessage());
		}
	}


}
