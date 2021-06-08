package com.twc.guanlang.service;


import com.twc.guanlang.entity.ChatImage;
import com.twc.guanlang.mapper.entity.ChatImageMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UploadService {


    @Resource
    private ChatImageMapper chatImageMapper;


    @Transactional
    public void save(ChatImage chatImage) {

        chatImageMapper.insert(chatImage);
    }

}
