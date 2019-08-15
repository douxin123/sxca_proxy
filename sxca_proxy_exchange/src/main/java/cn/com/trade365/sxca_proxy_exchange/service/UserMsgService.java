package cn.com.trade365.sxca_proxy_exchange.service;

import cn.com.trade365.sxca_proxy_exchange.core.BaseService;
import cn.com.trade365.platform.project.dto.UsOrganizationDto;
import cn.com.trade365.platform.project.dto.UsUserDto;

public interface UserMsgService extends BaseService {

    /**
     * 查询用户信息
     * @param userId 用户ID
     * @return UsUser
     */
    public UsUserDto getUser(String userId);

    /**
     * 查询机构信息
     * @param orgId 机构ID
     * @return UsOrganization>
     */
    public UsOrganizationDto getOrg(String orgId);

    /**
     * 更新内容的用户和数据中心交换关联
     * @param userId 平台用户ID
     * @param dataId  数据中心ID
     * @return int 1 success 0 fail
     */
    public int updateUserData(String userId, Long dataId);

    /**
     *关联机构ID及数据中心ID
     * @param jgbh 机构编号
     * @param dataId
     * @return int 1 success 0 fail
     */
    public int updateOrgData(String jgbh, Long dataId);
}
