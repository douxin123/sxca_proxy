package cn.com.trade365.sxca_proxy_exchange.service.impl;

import cn.com.trade365.platform.project.dto.UsOrganizationDto;
import cn.com.trade365.platform.project.dto.UsUserDto;
import cn.com.trade365.sxca_proxy_exchange.service.UserMsgService;
import cn.com.trade365.sxca_proxy_exchange.utils.ConvertUtils;
import cn.com.trade365.sxca_proxy_exchange.utils.DateUtil;
import cn.com.trade365.store.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户相关
 * @Author weichunjie
 */
@Service("userMsgService")
@Transactional
public class UserMsgServiceImpl implements UserMsgService {
    @Autowired
    private  Client fileClient;
    @Autowired
    private NamedParameterJdbcTemplate  userDao;
    /**
     * 查询用户信息
     * @param userId 用户ID
     * @return UsUser
     */
    @Override
    public UsUserDto getUser(String userId){
        Map<String,Object> param=new HashMap<>();
        UsUserDto user=new UsUserDto();
        try {
        String sql="SELECT  id,dlh,mc,mm,MOBILE,TEL,SJBD,EMAIL,YXBD,FAX,ZCSJ,SCZTLX,CAID,SCDLSJ,SYBZ,SFZH,DATA_ID  from gg_czyb  where id=:id";
        param.put("id",userId);
        Map<String,Object> czy=this.userDao.queryForMap(sql,param);
        if(czy.get("DATA_ID")!=null){
            user.setId(ConvertUtils.convert2Long(czy.get("DATA_ID")));
        }
        user.setUserName(ConvertUtils.convert2str(czy.get("dlh")));
        user.setUserNickname(ConvertUtils.convert2str(czy.get("mc")));
        user.setPassword(ConvertUtils.convert2str(czy.get("mm")));
        user.setCodeType("01");
        user.setCode(ConvertUtils.convert2str(czy.get("SFZH")));
        user.setPhone(ConvertUtils.convert2str(czy.get("TEL")));
        user.setMobile(ConvertUtils.convert2str(czy.get("MOBILE")));
        user.setIsMobileUsed(ConvertUtils.convert2Int(czy.get("SJBD")));
        user.setEmail(ConvertUtils.convert2str(czy.get("EMAIL")));
        user.setIsEmailUsed(ConvertUtils.convert2Int(czy.get("YXBD")));
        user.setFax(ConvertUtils.convert2str(czy.get("TEL")));
        user.setRegisterTime(DateUtil.getCurrTime());
        user.setOrganizationId(0L);
        user.setAttachmentCode("");
        user.setApproveTime(DateUtil.getCurrTime());
        user.setLoginTime(DateUtil.getCurrTime());
        user.setLoginAttemp(0);
        user.setStatus(ConvertUtils.convert2Int(czy.get("SYBZ")));
        user.setIsLocked(0);
        user.setPassChangedTime(DateUtil.getCurrTime());
        user.setUpdateTime(DateUtil.getCurrTime());
        user.setOpenId("");
        //KEYINFO 表查询KEY 信息
        sql="select  keyid,cert,zksj from gg_keyinfo where czybh=:id and zt=1";
        Map<String,Object> keyinfo=this.userDao.queryForMap(sql,param);
        if(keyinfo!=null){
          user.setCaId(ConvertUtils.convert2str(keyinfo.get("keyid")));
          user.setCaDn(ConvertUtils.convert2str(keyinfo.get("cert")));
          user.setCaExpiredTime(ConvertUtils.convert2Date(keyinfo.get("zksj")));
         }
         }catch (Exception e){
            user.setCaId("");
            user.setCaDn("");
            user.setCaExpiredTime(DateUtil.getCurrTime());
        }
        return  user;
    }

    /**
     * 查询机构信息
     * @param orgId 机构ID
     * @return UsOrganization>
     */
    @Override
    public UsOrganizationDto getOrg(String orgId){
        Map<String,Object> param =new HashMap<>();
        UsOrganizationDto  org=new UsOrganizationDto();
        param.put("orgId",orgId);
        String sql="SELECT JGMC,ZTDM,FZR,GBDQ,DWXZ,XZQY,HYDM,ZXDJ,KHYH,JBZH,ZCZB,ZCDW,ZCBZ,SYBZ,ZCSJ,YZSJ,YZRY,EMAIL,LXDZ,LXDH,SBR,CABH,DMLX,DATA_ID,YZBM  FROM GG_JGBH where jgbh=:orgId";
        Map<String,Object> reslut=this.userDao.queryForMap(sql,param);
        if(reslut.get("DATA_ID")!=null){
            org.setId(ConvertUtils.convert2Long(reslut.get("DATA_ID")));
        }
        org.setName(ConvertUtils.convert2str(reslut.get("JGMC")));
        //没有
        org.setCodeType("001");
        org.setSubjectCode(ConvertUtils.convert2str(reslut.get("ZTDM")));
        org.setIsGroup(1);
        org.setAddress(ConvertUtils.convert2str(reslut.get("LXDZ")));
        //联系人
        org.setContact(ConvertUtils.convert2str(reslut.get("SBR")));
        org.setPhone(ConvertUtils.convert2str(reslut.get("LXDH")));
        org.setEmail(ConvertUtils.convert2str(reslut.get("EMAIL")));
        org.setFax(ConvertUtils.convert2str(reslut.get("LXDH")));
        //注册时间
        org.setRegisterTime(ConvertUtils.convert2Date(reslut.get("ZCSJ")));
        //注册材料附件

        org.setAttachmentCode("");

        org.setApproveTime(ConvertUtils.convert2Date(reslut.get("YZSJ")));
        org.setApproveBy(ConvertUtils.convert2str(reslut.get("YZRY")));
        org.setStatus(ConvertUtils.convert2Int(reslut.get("SYBZ")));
        //创建时间
        org.setCreateTime(ConvertUtils.convert2Date(reslut.get("ZCSJ")));
        org.setUpdateTime(DateUtil.getCurrTime());//没有
        //负责人&法人名称
        org.setArtificialPerson(ConvertUtils.convert2str(reslut.get("FZR")));
        org.setArtificialPersonCode("01");
        org.setCountryRegion(ConvertUtils.convert2str(reslut.get("GBDQ")));
        org.setUnitNature(ConvertUtils.convert2str(reslut.get("DWXZ")));
        org.setRegionCode(ConvertUtils.convert2str(reslut.get("XZQY")));
        //行业代码
        org.setIndustryCode(ConvertUtils.convert2str(reslut.get("HYDM")));

        org.setCaCode(ConvertUtils.convert2str(reslut.get("CABH")));
        org.setCreditRate(ConvertUtils.convert2str(reslut.get("ZXDJ")));
        org.setOpeningBank(ConvertUtils.convert2str(reslut.get("KHYH")));
        org.setBasicAccount(ConvertUtils.convert2str(reslut.get("JBZH")));
        //注册资本
        org.setRegCapital(new BigDecimal(ConvertUtils.convert2str((reslut.get("ZCZB")==null?"0":reslut.get("ZCZB")))));
        org.setRegCapCurrency(ConvertUtils.convert2str(reslut.get("ZCBZ")));
        org.setRegUnit(ConvertUtils.convert2str(reslut.get("ZCDW")));
        org.setZipCode(new BigDecimal(reslut.get("YZBM")==null?"000000":ConvertUtils.convert2str(reslut.get("YZBM"))));
        //无
        org.setSourceId("");
        //无
        org.setPlatformCode("");
        return org;
    }




    /**
     * 更新内容的用户和数据中心交换关联
     * @param userId 平台用户ID
     * @param dataId  数据中心ID
     * @return int 1 success 0 fail
     */
    @Override
    public int updateUserData(String userId,Long dataId){
        Map<String,Object>  param=new HashMap<>();
        String sql="update gg_czyb set data_id =:dataId where id=:id";
        param.put("dataId",dataId);
        param.put("id",userId);
        return  this.userDao.update(sql,param);
    }

    /**
     *关联机构ID及数据中心ID
     * @param jgbh 机构编号
     * @param dataId
     * @return int 1 success 0 fail
     */
    @Override
    public int updateOrgData(String jgbh,Long dataId){
        Map<String,Object>  param=new HashMap<>();
        String sql="update gg_jgbh set data_id =:dataId where jgbh=:id";
        param.put("dataId",dataId);
        param.put("id",jgbh);
        return  this.userDao.update(sql,param);
    }

}
