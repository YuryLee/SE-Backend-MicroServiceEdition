create table comment
(
    cid         int auto_increment comment '评论主cid'
        primary key,
    vid         int           not null comment '评论的视频id',
    sid         varchar(10)   not null,
    parent_id   int           not null comment '被回复的评论id，只有root_id为0时才允许为0，表示根评论',
    to_user_sid varchar(10)   null comment '回复目标用户id',
    content     varchar(2000) not null comment '评论内容',
    create_time datetime      not null comment '创建时间'
)
    comment '评论表';

create table daily_play
(
    id              int auto_increment comment '唯一主键'
        primary key,
    sid             varchar(10)                        not null comment '用户sid',
    weekday         int                                not null comment '周几',
    fans_count      int      default 0                 not null,
    love_count      int      default 0                 not null,
    play_count      int      default 0                 not null comment '视频播放量',
    new_play_count  int      default 0                 not null comment '新一周的视频播放量',
    user_play_count int      default 0                 not null comment '今天用户的总播放量',
    store_date      datetime default CURRENT_TIMESTAMP not null comment '日期'
)
    comment '每日播放';

create table daily_watch
(
    id              int auto_increment comment '唯一主键'
        primary key,
    sid             varchar(10)                        not null comment '用户sid',
    weekday         int                                not null comment '周几',
    love_count      int      default 0                 not null,
    collect_count   int      default 0                 not null,
    play_count      int      default 0                 not null comment '视频播放量',
    new_play_count  int      default 0                 not null comment '新一周的视频播放量',
    user_play_count int      default 0                 not null comment '今天用户的总播放量',
    store_date      datetime default CURRENT_TIMESTAMP not null comment '日期'
)
    comment '每日观看';

create table danmu
(
    id           int auto_increment comment '弹幕ID'
        primary key,
    vid          int                          not null comment '视频ID',
    sid          varchar(10)                  not null comment '用户SID',
    barrage_type varchar(10)                  not null comment '弹幕类型',
    color        varchar(7) default '#FFFFFF' not null comment '弹幕颜色 6位十六进制标准格式',
    duration     int                          not null comment '弹幕持续时间',
    fontsize     tinyint    default 25        not null comment '字体大小',
    line_height  double                       not null comment '弹幕高度',
    prior        tinyint(1)                   not null comment '弹幕优先',
    content      varchar(100)                 not null comment '弹幕内容',
    time         double                       not null comment '弹幕所在视频的时间点',
    create_date  datetime                     not null comment '发送弹幕的日期时间',
    constraint id
        unique (id)
)
    comment '弹幕表';

create table follow
(
    id          int auto_increment comment '关注主id'
        primary key,
    sid_from    varchar(10) not null comment '谁关注',
    sid_to      varchar(10) not null comment '关注谁',
    create_time datetime    not null comment '创建时间'
)
    comment '用户关注表';

create table msg
(
    id      int auto_increment comment '唯一主键'
        primary key,
    sid     varchar(10)  not null comment '消息接收者',
    title   varchar(100) not null comment '标题',
    content varchar(500) not null comment '消息内容',
    id_send int          not null comment '发送的id',
    time    datetime     not null comment '消息发送时间',
    type    int          null comment '视频0 专栏1',
    constraint id
        unique (id)
)
    comment '消息提醒';

create table post
(
    pid         int auto_increment comment '唯一主键'
        primary key,
    type        varchar(20)  not null comment '动态类型，text or video',
    sid         varchar(10)  not null comment '动态发送者',
    vid         int          not null comment '视频vid',
    picture     varchar(500) not null comment '图片url字符串',
    content     varchar(500) not null comment '动态内容',
    create_time datetime     not null comment '动态发送时间',
    constraint id
        unique (pid)
)
    comment '动态';

create table report
(
    rid           int auto_increment comment '唯一主键'
        primary key,
    vid           int           not null comment '被举报视频',
    title         varchar(100)  not null comment '视频标题',
    url           varchar(100)  not null comment '视频链接',
    reason        varchar(100)  not null comment '举报原因',
    status        int           not null comment '处理状态',
    sid           varchar(10)   not null comment '举报人',
    name          varchar(20)   not null comment '举报人名字',
    report_date   datetime      not null comment '举报时间',
    auth_sid      varchar(10)   not null comment '作者sid',
    auth_name     varchar(10)   not null comment '作业name',
    appeal        int default 0 not null comment '申诉',
    appeal_reason varchar(500)  null comment '申诉力有'
)
    comment '举报列表';

create table user
(
    sid                  varchar(10)                        not null,
    name                 varchar(20)                        not null,
    password             varchar(20)                        not null,
    uid                  int auto_increment
        primary key,
    avatar               varchar(500)                       null comment '用户头像url',
    background           varchar(500)                       null comment '主页背景图url',
    gender               tinyint  default 2                 not null comment '性别 0女 1男 2未知',
    signature            varchar(100)                       null comment '个性签名',
    exp                  int      default 0                 not null comment '经验值',
    coin                 double   default 0                 not null comment '硬币数',
    vip                  tinyint  default 0                 not null comment '会员类型 0普通用户 1月度大会员 2季度大会员 3年度大会员',
    state                tinyint  default 0                 not null comment '状态 0正常 1封禁 2注销',
    role                 tinyint  default 0                 not null comment '角色类型 0普通用户 1管理员 2超级管理员',
    create_date          datetime default CURRENT_TIMESTAMP null comment '创建时间',
    delete_date          datetime                           null comment '注销时间',
    fans_count           int      default 0                 not null,
    follows_count        int      default 0                 not null,
    video_count          int      default 0                 not null,
    love_count           int      default 0                 not null,
    play_count           int      default 0                 not null,
    collect_video_count  int      default 0                 not null comment '收藏视频数',
    collect_column_count int      default 0                 not null comment '收藏专栏数',
    constraint user_pk
        unique (sid)
);

create table user_video
(
    id           int auto_increment comment '唯一标识'
        primary key,
    sid          varchar(10)       not null comment '观看视频的用户SID',
    vid          int               not null comment '视频ID',
    play         int     default 0 not null comment '播放次数',
    love         tinyint default 0 not null comment '点赞 0没赞 1已点赞',
    unlove       tinyint default 0 not null comment '不喜欢 0没点 1已不喜欢',
    coin         tinyint default 0 not null comment '投币数 0-2 默认0',
    collect      tinyint default 0 not null comment '收藏 0没收藏 1已收藏',
    play_time    datetime          not null comment '最近播放时间',
    love_time    datetime          null comment '点赞时间',
    coin_time    datetime          null comment '投币时间',
    collect_time datetime          null comment '收藏时间',
    constraint id
        unique (id),
    constraint uid_vid__index
        unique (sid, vid)
)
    comment '用户视频关联表';

create table video
(
    vid         int auto_increment comment '视频ID'
        primary key,
    uid         int               not null comment '投稿用户ID',
    sid         varchar(10)       not null,
    title       varchar(80)       not null comment '标题',
    type        tinyint default 1 not null comment '类型 1自制 2转载',
    auth        tinyint default 0 not null comment '作者声明 0不声明 1未经允许禁止转载',
    duration    double  default 0 not null comment '播放总时长 单位秒',
    mc_id       varchar(20)       not null comment '主分区ID',
    tags        varchar(500)      null comment '标签 回车分隔',
    descr       varchar(2000)     null comment '简介',
    cover_url   varchar(500)      not null comment '封面url',
    video_url   varchar(500)      not null comment '视频url',
    status      tinyint default 0 not null comment '状态 0审核中 1已过审 2未通过 3已删除',
    complain    int     default 0 not null comment '投诉数',
    advice      varchar(500)      null comment '审核建议',
    upload_date datetime          not null comment '上传时间',
    delete_date datetime          null comment '删除时间',
    play        int     default 0 not null comment '播放量',
    danmu       int     default 0 not null comment '弹幕数',
    good        int     default 0 not null comment '点赞数',
    bad         int     default 0 not null comment '点踩数',
    coin        int     default 0 not null comment '投币数',
    collect     int     default 0 not null comment '收藏数',
    share       int     default 0 not null comment '分享数',
    comment     int     default 0 null comment '评论数量统计'
);

create table zhuanlan
(
    zid         int auto_increment comment '专栏ID'
        primary key,
    sid         varchar(10)             not null comment '所属用户SID',
    visible     tinyint      default 1  not null comment '对外开放 0隐藏 1公开',
    cover       varchar(255)            null comment '收藏夹封面',
    title       varchar(20)             not null comment '标题',
    description varchar(200) default '' null comment '简介',
    count       int          default 0  not null comment '收藏夹中视频数量',
    update_time datetime                not null comment '最近更改时间',
    status      int          default 0  not null comment '状态',
    constraint fid
        unique (zid)
)
    comment '专栏';

create table zhuanlan_report
(
    rid           int auto_increment comment '唯一主键'
        primary key,
    zid           int           not null comment '被举报专栏',
    title         varchar(100)  not null comment '视频标题',
    reason        varchar(100)  not null comment '举报原因',
    status        int           not null comment '处理状态',
    sid           varchar(10)   not null comment '举报人',
    name          varchar(20)   not null comment '举报人名字',
    report_date   datetime      not null comment '举报时间',
    auth_sid      varchar(10)   not null comment '作者sid',
    auth_name     varchar(10)   not null comment '作业name',
    appeal        int default 0 not null comment '申诉状态',
    appeal_reason varchar(500)  null comment '申诉理由'
)
    comment '专栏举报列表';

create table zhuanlan_video
(
    id   int auto_increment comment '唯一标识'
        primary key,
    vid  int      not null comment '视频ID',
    zid  int      not null comment '专栏ID',
    time datetime not null comment '进入时间',
    constraint id
        unique (id),
    constraint vid_fid__index
        unique (vid, zid)
)
    comment '视频专栏关系表';

