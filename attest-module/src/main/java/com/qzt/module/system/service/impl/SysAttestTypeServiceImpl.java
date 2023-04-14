package com.qzt.module.system.service.impl;

import com.qzt.common.constant.CacheConstants;
import com.qzt.common.core.redis.RedisCache;
import com.qzt.common.utils.DateUtils;
import com.qzt.module.system.domain.SysAttestType;
import com.qzt.module.system.domain.vo.user.SysAttestTypeTreeVO;
import com.qzt.module.system.mapper.SysAttestTypeMapper;
import com.qzt.module.system.service.ISysAttestTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 认证服务类型Service业务层处理
 *
 * @author kf
 * @date 2023-04-11
 */
@Service
public class SysAttestTypeServiceImpl implements ISysAttestTypeService {
    @Autowired
    private SysAttestTypeMapper sysAttestTypeMapper;
    @Autowired
    private RedisCache redisCache;

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init() {
        loadingAttestTypeCache();
    }

    /**
     * 加载模板key到缓存
     */
    public void loadingAttestTypeCache() {
        List<SysAttestType> list = sysAttestTypeMapper.selectList(new SysAttestType());
        redisCache.setCacheObject(CacheConstants.ATTEST_TYPE, list);
    }


    /**
     * 查询认证服务类型
     *
     * @param id 认证服务类型主键
     * @return 认证服务类型
     */
    @Override
    public SysAttestType selectById(Long id) {
        return sysAttestTypeMapper.selectById(id);
    }

    /**
     * 查询认证服务类型列表
     *
     * @param sysAttestType 认证服务类型
     * @return 认证服务类型
     */
    @Override
    public List<SysAttestType> selectList(SysAttestType sysAttestType) {
        return sysAttestTypeMapper.selectList(sysAttestType);
    }

    /**
     * 新增认证服务类型
     *
     * @param sysAttestType 认证服务类型
     * @return 结果
     */
    @Override
    public int save(SysAttestType sysAttestType) {
        sysAttestType.setCreateTime(DateUtils.getNowDate());
        int rows = sysAttestTypeMapper.save(sysAttestType);
        if (rows > 0) {
            loadingAttestTypeCache();
        }
        return rows;
    }

    /**
     * 修改认证服务类型
     *
     * @param sysAttestType 认证服务类型
     * @return 结果
     */
    @Override
    public int update(SysAttestType sysAttestType) {
        sysAttestType.setUpdateTime(DateUtils.getNowDate());
        int rows = sysAttestTypeMapper.update(sysAttestType);
        if (rows > 0) {
            loadingAttestTypeCache();
        }
        return rows;
    }

    /**
     * 批量删除认证服务类型
     *
     * @param ids 需要删除的认证服务类型主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteByIds(List<Long> ids) {
        sysAttestTypeMapper.deleteByPids(ids);
        int rows = sysAttestTypeMapper.deleteByIds(ids);
        if (rows > 0) {
            loadingAttestTypeCache();
        }
        return rows;
    }

    /**
     * 删除认证服务类型信息
     *
     * @param id 认证服务类型主键
     * @return 结果
     */
    @Override
    public int deleteById(Long id) {
        int rows = sysAttestTypeMapper.deleteById(id);
        if (rows > 0) {
            loadingAttestTypeCache();
        }
        return rows;
    }

    @Override
    public List<SysAttestTypeTreeVO> buildTree(List<SysAttestType> list) {
        List<SysAttestType> treeData = buildTreeData(list);
        return treeData.stream().map(SysAttestTypeTreeVO::new).collect(Collectors.toList());
    }

    /**
     * 构建树结构数据
     *
     * @param list
     * @return
     */
    public List<SysAttestType> buildTreeData(List<SysAttestType> list) {
        List<SysAttestType> returnList = new ArrayList<SysAttestType>();
        List<Long> tempList = list.stream().map(SysAttestType::getId).collect(Collectors.toList());
        for (Iterator<SysAttestType> iterator = list.iterator(); iterator.hasNext(); ) {
            SysAttestType item = (SysAttestType) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(item.getParentId())) {
                recursionFn(list, item);
                returnList.add(item);
            }
        }
        if (returnList.isEmpty()) {
            returnList = list;
        }
        return returnList;
    }

    /**
     * 得到子节点列表
     */
    private List<SysAttestType> getChildList(List<SysAttestType> list, SysAttestType t) {
        List<SysAttestType> tlist = new ArrayList<SysAttestType>();
        Iterator<SysAttestType> it = list.iterator();
        while (it.hasNext()) {
            SysAttestType n = (SysAttestType) it.next();
            if (n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 递归列表
     *
     * @param list 分类表
     * @param t    子节点
     */
    private void recursionFn(List<SysAttestType> list, SysAttestType t) {
        // 得到子节点列表
        List<SysAttestType> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysAttestType tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysAttestType> list, SysAttestType t) {
        return getChildList(list, t).size() > 0;
    }
}
