package com.twc.guanlang.entity;


import lombok.Data;

import javax.persistence.*;


/**
 * 聊天图片
 */


@Entity
@Table(name = "t_chat_image")
@Data
public class ChatImage {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    @Column(name="from_user")
    private String fromUser;


    @Column(name="to_user")
    private String toUser;

    /**
     * 图片名称
     */
    private String name;

    /**
     * 是否已读
     */
    @Column(name="has_read")
    private Integer hasRead;
}
