package com.jdd.partition.mapper;

import com.jdd.partition.entity.PartitionRedPacketUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 助力红包用户基本信息表 Mapper 接口
 * </p>
 *
 * @author pengbaoning
 * @since 2019-12-12
 */
public interface PartitionRedPacketUserMapper extends BaseMapper<PartitionRedPacketUser> {

    List<PartitionRedPacketUser> randomTen();

}
