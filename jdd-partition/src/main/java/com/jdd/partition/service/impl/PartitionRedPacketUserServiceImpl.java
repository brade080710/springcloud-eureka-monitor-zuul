package com.jdd.partition.service.impl;

import com.jdd.partition.entity.PartitionRedPacketUser;
import com.jdd.partition.mapper.PartitionRedPacketUserMapper;
import com.jdd.partition.service.PartitionRedPacketUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 助力红包用户基本信息表 服务实现类
 * </p>
 *
 * @author pengbaoning
 * @since 2019-12-12
 */
@Service
public class PartitionRedPacketUserServiceImpl extends ServiceImpl<PartitionRedPacketUserMapper, PartitionRedPacketUser> implements PartitionRedPacketUserService {

    @Override
    public List<PartitionRedPacketUser> randomTen() {
        return baseMapper.randomTen();
    }
}
