package com.jdd.partition.service;

import com.jdd.partition.entity.PartitionRedPacketUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 助力红包用户基本信息表 服务类
 * </p>
 *
 * @author pengbaoning
 * @since 2019-12-12
 */
public interface PartitionRedPacketUserService extends IService<PartitionRedPacketUser> {

    public List<PartitionRedPacketUser> randomTen();

}
