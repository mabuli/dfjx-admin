/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjx.module.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dfjx.common.util.R;
import io.dfjx.module.sys.entity.SysUserEntity;
import io.dfjx.module.sys.entity.SysUserTokenEntity;

/**
 * 用户Token
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysUserTokenService extends IService<SysUserTokenEntity> {

	/**
	 * 生成token
	 * @param userId  用户ID
	 */
	R createToken(SysUserEntity userId);

	/**
	 * 退出，修改token值
	 * @param userId  用户ID
	 */
	void logout(long userId);

}
