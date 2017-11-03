/*==============================================================*/
/* DBMS name:      PostgreSQL 9.3.6                             */
/* Created on:     9/25 ����һ 14:41:58                            */
/*==============================================================*/


drop index INDEX_ROOT_PARAMS_MASK;

drop table ROOT_PARAMS;

drop table WX_SERVER;

drop table wx_user;

drop table xjl_dw_album;

drop table xjl_dw_album_image;

drop table xjl_dw_album_template;

drop table xjl_dw_article;

drop table xjl_dw_class;

drop table xjl_dw_class_teacher;

drop table xjl_dw_exam;

drop table xjl_dw_exam_grade;

drop table xjl_dw_exam_subject;

drop table xjl_dw_file;

drop table xjl_dw_group_buy;

drop table xjl_dw_group_buy_item;

drop table xjl_dw_group_buy_order;

drop table xjl_dw_homework;

drop table xjl_dw_homework_model;

drop table xjl_dw_notice;

drop table xjl_dw_role;

drop index INDEX_PORTAL_VNO_ID_STATE;

drop table xjl_dw_school;

drop table xjl_dw_student;

drop table xjl_dw_subject;

drop table xjl_dw_teacher;

drop table xjl_dw_user;

drop table xjl_dw_wx_class;

drop table xjl_dw_wx_role;

drop table xjl_dw_wx_student;

/*==============================================================*/
/* Table: ROOT_PARAMS                                           */
/*==============================================================*/
create table ROOT_PARAMS (
   PARAM                NUMERIC              null,
   COMMENTS             VARCHAR(200)         null,
   CURRENT_VALUE        VARCHAR(300)         null,
   MASK                 VARCHAR(20)          null,
   STATE                VARCHAR(3)           null,
   CREATE_TIME          TIMESTAMP            null,
   UPDATE_TIME          TIMESTAMP            null,
   VERSION              NUMERIC              null
);

comment on table ROOT_PARAMS is
'ϵͳ������';

comment on column ROOT_PARAMS.PARAM is
'���';

comment on column ROOT_PARAMS.COMMENTS is
'��������';

comment on column ROOT_PARAMS.CURRENT_VALUE is
'����ֵ';

comment on column ROOT_PARAMS.MASK is
'������ʶ��';

comment on column ROOT_PARAMS.STATE is
'״̬';

comment on column ROOT_PARAMS.CREATE_TIME is
'���ʱ��';

comment on column ROOT_PARAMS.UPDATE_TIME is
'����ʱ��';

comment on column ROOT_PARAMS.VERSION is
'���´���';

INSERT INTO ROOT_PARAMS(PARAM,COMMENTS,CURRENT_VALUE,MASK)
VALUES(20,'��վ��ַ','http://wechat.airclub.xin','WEB_ROOT');
INSERT INTO ROOT_PARAMS(PARAM,COMMENTS,CURRENT_VALUE,MASK)
VALUES(23,'����֧��','΢���Ŷ�','TECHNICAL_SUPPORT');
INSERT INTO ROOT_PARAMS(PARAM,COMMENTS,CURRENT_VALUE,MASK)
VALUES(24,'�ͷ�����֧��QQ�ͷ���340416553','340416553','ONLINE_QQ');
INSERT INTO ROOT_PARAMS(PARAM,COMMENTS,CURRENT_VALUE,MASK)
VALUES(25,'�ͷ��绰','176-0155-1522','ONLINE_TEL');
INSERT INTO ROOT_PARAMS(PARAM,COMMENTS,CURRENT_VALUE,MASK)
VALUES(26,'�����ţ�ʾ������ICP��05059565��-3','��ICP��05059565��-3','ICP');
COMMIT;

/*==============================================================*/
/* Index: INDEX_ROOT_PARAMS_MASK                                */
/*==============================================================*/
create  index INDEX_ROOT_PARAMS_MASK on ROOT_PARAMS (
MASK
);

/*==============================================================*/
/* Table: WX_SERVER                                             */
/*==============================================================*/
create table WX_SERVER (
   WX_SERVER_ID         NUMERIC              not null,
   SCHOOL_ID            NUMERIC              null,
   WX_CODE              VARCHAR(15)          not null,
   WX_NAME              VARCHAR(100)         not null,
   WX_QR_CODE           VARCHAR(100)         null,
   APP_ID               VARCHAR(48)          null,
   APP_SECRET           VARCHAR(80)          null,
   ACCESS_TOKEN         VARCHAR(30)          null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_WX_SERVER primary key (WX_SERVER_ID)
);

comment on table WX_SERVER is
'��ҵ���ں�';

comment on column WX_SERVER.SCHOOL_ID is
'��ҵ��ʶ';

comment on column WX_SERVER.WX_CODE is
'ԭʼID';

comment on column WX_SERVER.WX_NAME is
'΢�Ź��ں�����';

comment on column WX_SERVER.WX_QR_CODE is
'���ںŶ�ά��';

comment on column WX_SERVER.APP_ID is
'Ӧ��ID';

comment on column WX_SERVER.APP_SECRET is
'Ӧ����Կ';

comment on column WX_SERVER.ACCESS_TOKEN is
'Access_Token';

comment on column WX_SERVER.STATUS is
'״̬
0AA����Ч
0XX����Ч';

comment on column WX_SERVER.CREATE_TIME is
'���ʱ��';

/*==============================================================*/
/* Table: wx_user                                               */
/*==============================================================*/
create table wx_user (
   WX_OPEN_ID           VARCHAR(28)          not null,
   OPEN_ID_CHANNCEL     VARCHAR(20)          null,
   SCHOOL_ID            NUMERIC              null,
   TEACHER_ID           NUMERIC              null,
   NICK_NAME            VARCHAR(50)          not null,
   HEAD_IMG_URL         VARCHAR(255)         null,
   SEX                  VARCHAR(1)           null,
   LANGUAGE             VARCHAR(50)          null,
   COUNTRY              VARCHAR(60)          null,
   PROVINCE             VARCHAR(60)          null,
   CITY                 VARCHAR(60)          null,
   WX_PHONE             VARCHAR(20)          null,
   IS_CONCERNED         VARCHAR(1)           null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            null,
   constraint PK_WX_USER primary key (WX_OPEN_ID)
);

comment on table wx_user is
'΢���˻���';

comment on column wx_user.OPEN_ID_CHANNCEL is
'��ȡopenid��;�� webgrant����ҳ��Ȩ  subscribe����ע���ں�';

comment on column wx_user.WX_PHONE is
'΢�ŵ绰';

comment on column wx_user.IS_CONCERNED is
'Y:��,N:��';

comment on column wx_user.STATUS is
'״̬';

comment on column wx_user.CREATE_TIME is
'���ʱ��';

/*==============================================================*/
/* Table: xjl_dw_album                                          */
/*==============================================================*/
create table xjl_dw_album (
   ALBUM_ID             NUMERIC              not null,
   ALBUM_TEMPLATE_ID    NUMERIC              not null,
   CLASS_ID             NUMERIC              null,
   ALBUM_TITLE          VARCHAR(100)         not null,
   WX_OPEN_ID           VARCHAR(28)          not null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_XJL_DW_ALBUM primary key (ALBUM_ID)
);

comment on table xjl_dw_album is
'����';

comment on column xjl_dw_album.ALBUM_ID is
'���ID';

comment on column xjl_dw_album.ALBUM_TEMPLATE_ID is
'ģ��id';

comment on column xjl_dw_album.CLASS_ID is
'�༶��ʶ';

comment on column xjl_dw_album.ALBUM_TITLE is
'������';

comment on column xjl_dw_album.WX_OPEN_ID is
'��ᴴ����';

/*==============================================================*/
/* Table: xjl_dw_album_image                                    */
/*==============================================================*/
create table xjl_dw_album_image (
   ALBUM_IMAGE_ID       NUMERIC              not null,
   FILE_ID              NUMERIC              not null,
   ALBUM_ID             NUMERIC              not null,
   IMAGE_TITLE          VARCHAR(100)         not null,
   IMAGE_ORDER          NUMERIC              not null,
   WX_OPEN_ID           VARCHAR(28)          not null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_XJL_DW_ALBUM_IMAGE primary key (ALBUM_IMAGE_ID)
);

comment on table xjl_dw_album_image is
'���ͼƬ��';

comment on column xjl_dw_album_image.ALBUM_IMAGE_ID is
'���ͼƬID';

comment on column xjl_dw_album_image.FILE_ID is
'�ļ�ID';

comment on column xjl_dw_album_image.ALBUM_ID is
'���ID';

comment on column xjl_dw_album_image.IMAGE_TITLE is
'ͼƬ����';

comment on column xjl_dw_album_image.IMAGE_ORDER is
'ͼƬ����';

comment on column xjl_dw_album_image.WX_OPEN_ID is
'ͼƬ������';

/*==============================================================*/
/* Table: xjl_dw_album_template                                 */
/*==============================================================*/
create table xjl_dw_album_template (
   ALBUM_TEMPLATE_ID    NUMERIC              not null,
   SCHOOL_ID            NUMERIC              null,
   ALBUM_TEMPLATE_TITLE VARCHAR(100)         not null,
   ALBUM_TEMPLATE_IMG   VARCHAR(100)         null,
   ALBUM_TEMPLATE_STYLE VARCHAR(50)          null,
   ALBUM_TEMPLATE_IMG_NUM NUMERIC              null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_XJL_DW_ALBUM_TEMPLATE primary key (ALBUM_TEMPLATE_ID)
);

comment on table xjl_dw_album_template is
'���ģ���';

comment on column xjl_dw_album_template.ALBUM_TEMPLATE_ID is
'ģ��id';

comment on column xjl_dw_album_template.SCHOOL_ID is
'��ҵ��ʶ';

comment on column xjl_dw_album_template.ALBUM_TEMPLATE_TITLE is
'ģ�����';

comment on column xjl_dw_album_template.ALBUM_TEMPLATE_IMG is
'ģ��ͼƬ';

comment on column xjl_dw_album_template.ALBUM_TEMPLATE_STYLE is
'ģ����ʽ';

comment on column xjl_dw_album_template.ALBUM_TEMPLATE_IMG_NUM is
'ģ�����ͼƬ����';

comment on column xjl_dw_album_template.CREATE_TIME is
'���ʱ��';

/*==============================================================*/
/* Table: xjl_dw_article                                        */
/*==============================================================*/
create table xjl_dw_article (
   ARTICLE_ID           NUMERIC              not null,
   WX_OPEN_ID           VARCHAR(28)          not null,
   ARTICLE_TITLE        VARCHAR(100)         not null,
   ARTICLE_CONTENT      TEXT                 not null,
   ARTICLE_AUTHOR       VARCHAR(50)          not null,
   ARTICLE_PUBLISH_DATE TIMESTAMP            not null,
   ARTICLE_STATE        VARCHAR(1)           not null,
   CLASS_ID             NUMERIC              null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_XJL_DW_ARTICLE primary key (ARTICLE_ID)
);

comment on table xjl_dw_article is
'����';

comment on column xjl_dw_article.ARTICLE_ID is
'����id';

comment on column xjl_dw_article.WX_OPEN_ID is
'���·�����';

comment on column xjl_dw_article.ARTICLE_TITLE is
'���±���';

comment on column xjl_dw_article.ARTICLE_CONTENT is
'��������';

comment on column xjl_dw_article.ARTICLE_AUTHOR is
'�������ߴ���΢���ǳƣ����޸�';

comment on column xjl_dw_article.ARTICLE_PUBLISH_DATE is
'ϵͳʱ��
ֻ�Ǽ�¼˭���������
��Ϣ������ʾ��֪ͨ��';

comment on column xjl_dw_article.ARTICLE_STATE is
'0�Ǵ�������1������ͨ
�����ѷ�����2��������ͨ�����ݲ�������';

comment on column xjl_dw_article.CLASS_ID is
'�༶��ʶ';

comment on column xjl_dw_article.STATUS is
'״̬';

comment on column xjl_dw_article.CREATE_TIME is
'���ʱ��';

/*==============================================================*/
/* Table: xjl_dw_class                                          */
/*==============================================================*/
create table xjl_dw_class (
   CLASS_ID             NUMERIC              not null,
   SCHOOL_ID            NUMERIC              null,
   CLASS_NAME           VARCHAR(50)          not null,
   CLASS_CODE           VARCHAR(50)          null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_XJL_DW_CLASS primary key (CLASS_ID)
);

comment on table xjl_dw_class is
'�༶��';

comment on column xjl_dw_class.CLASS_ID is
'�༶��ʶ';

comment on column xjl_dw_class.SCHOOL_ID is
'��ҵ��ʶ';

comment on column xjl_dw_class.CLASS_NAME is
'�༶����';

comment on column xjl_dw_class.CLASS_CODE is
'�༶����';

comment on column xjl_dw_class.STATUS is
'״̬';

comment on column xjl_dw_class.CREATE_TIME is
'���ʱ��';

/*==============================================================*/
/* Table: xjl_dw_class_teacher                                  */
/*==============================================================*/
create table xjl_dw_class_teacher (
   CLASS_TEACHER_ID     NUMERIC              not null,
   TEACHER_ID           NUMERIC              not null,
   CLASS_ID             NUMERIC              not null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_XJL_DW_CLASS_TEACHER primary key (CLASS_TEACHER_ID)
);

comment on table xjl_dw_class_teacher is
'�༶��ʦ��ϵ��';

comment on column xjl_dw_class_teacher.CLASS_ID is
'�༶��ʶ';

comment on column xjl_dw_class_teacher.STATUS is
'״̬';

comment on column xjl_dw_class_teacher.CREATE_TIME is
'���ʱ��';

/*==============================================================*/
/* Table: xjl_dw_exam                                           */
/*==============================================================*/
create table xjl_dw_exam (
   EXAM_ID              NUMERIC              not null,
   EXAM_TITLE           VARCHAR(100)         not null,
   EXAM_DATE            TIMESTAMP            not null,
   CLASS_ID             NUMERIC              null,
   WX_OPEN_ID           VARCHAR(28)          null,
   UP_CHANNCEL          VARCHAR(20)          null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_XJL_DW_EXAM primary key (EXAM_ID)
);

comment on table xjl_dw_exam is
'���Ա�';

comment on column xjl_dw_exam.EXAM_ID is
'����ID';

comment on column xjl_dw_exam.EXAM_TITLE is
'���Ա���';

comment on column xjl_dw_exam.EXAM_DATE is
'����ʱ��';

comment on column xjl_dw_exam.CLASS_ID is
'�༶��ʶ';

comment on column xjl_dw_exam.WX_OPEN_ID is
'������';

comment on column xjl_dw_exam.UP_CHANNCEL is
'admin��pc��̨����
wechat��΢���Ϸ���';

/*==============================================================*/
/* Table: xjl_dw_exam_grade                                     */
/*==============================================================*/
create table xjl_dw_exam_grade (
   EXAM_GRADE_ID        NUMERIC              not null,
   EXAM_ID              NUMERIC              not null,
   SUBJECT_ID           NUMERIC              not null,
   STUDENT_ID           NUMERIC              not null,
   EXAM_GRADE           NUMERIC(10,2)        not null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_XJL_DW_EXAM_GRADE primary key (EXAM_GRADE_ID)
);

comment on table xjl_dw_exam_grade is
'���Կ�Ŀ�ɼ���';

comment on column xjl_dw_exam_grade.EXAM_GRADE_ID is
'���Գɼ�ID';

comment on column xjl_dw_exam_grade.EXAM_ID is
'����ID';

comment on column xjl_dw_exam_grade.SUBJECT_ID is
'��Ŀid';

comment on column xjl_dw_exam_grade.STUDENT_ID is
'ѧ����ʶ';

comment on column xjl_dw_exam_grade.EXAM_GRADE is
'���Գɼ�';

/*==============================================================*/
/* Table: xjl_dw_exam_subject                                   */
/*==============================================================*/
create table xjl_dw_exam_subject (
   EXAM_SUBJECT_ID      NUMERIC              not null,
   EXAM_ID              NUMERIC              not null,
   SUBJECT_ID           NUMERIC              not null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_XJL_DW_EXAM_SUBJECT primary key (EXAM_SUBJECT_ID)
);

comment on table xjl_dw_exam_subject is
'���Կ�Ŀ��';

comment on column xjl_dw_exam_subject.EXAM_SUBJECT_ID is
'���Կ�ĿID';

comment on column xjl_dw_exam_subject.EXAM_ID is
'����ID';

comment on column xjl_dw_exam_subject.SUBJECT_ID is
'��Ŀid';

/*==============================================================*/
/* Table: xjl_dw_file                                           */
/*==============================================================*/
create table xjl_dw_file (
   FILE_ID              NUMERIC              not null,
   FILE_NAME            VARCHAR(50)          null,
   FILE_URL             VARCHAR(100)         not null,
   WX_OPEN_ID           VARCHAR(28)          not null,
   FILE_TYPE            VARCHAR(10)          not null,
   SAVE_FOLDER          VARCHAR(50)          null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   DELETE_TIME          TIMESTAMP            null,
   UP_CHANNEL           VARCHAR(10)          null,
   constraint PK_XJL_DW_FILE primary key (FILE_ID)
);

comment on table xjl_dw_file is
'�ļ���';

comment on column xjl_dw_file.FILE_ID is
'�ļ�ID';

comment on column xjl_dw_file.FILE_NAME is
'�ļ�����';

comment on column xjl_dw_file.FILE_URL is
'�ļ���ַ';

comment on column xjl_dw_file.WX_OPEN_ID is
'�ϴ���';

comment on column xjl_dw_file.FILE_TYPE is
'�ļ�����1ͼƬ��2��Ƶ��3pdf��
4word
5����';

comment on column xjl_dw_file.SAVE_FOLDER is
'��ŵ��ļ���';

comment on column xjl_dw_file.STATUS is
'״̬';

comment on column xjl_dw_file.CREATE_TIME is
'���ʱ��';

comment on column xjl_dw_file.DELETE_TIME is
'ɾ��ʱ��';

comment on column xjl_dw_file.UP_CHANNEL is
'�ϴ�;��';

/*==============================================================*/
/* Table: xjl_dw_group_buy                                      */
/*==============================================================*/
create table xjl_dw_group_buy (
   GROUP_BUY_ID         NUMERIC              not null,
   GROUP_BUY_TITLE      VARCHAR(100)         not null,
   WX_OPEN_ID           VARCHAR(28)          not null,
   CLASS_ID             NUMERIC              null,
   GROUP_BUY_BEGIN_TIME TIMESTAMP            null,
   GROUP_BUY_END_TIME   TIMESTAMP            null,
   GROUP_BUY_STATE      VARCHAR(1)           null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_XJL_DW_GROUP_BUY primary key (GROUP_BUY_ID)
);

comment on table xjl_dw_group_buy is
'�Ź���';

comment on column xjl_dw_group_buy.GROUP_BUY_ID is
'�Ź�id';

comment on column xjl_dw_group_buy.GROUP_BUY_TITLE is
'�Ź�����';

comment on column xjl_dw_group_buy.WX_OPEN_ID is
'������';

comment on column xjl_dw_group_buy.CLASS_ID is
'�༶��ʶ';

comment on column xjl_dw_group_buy.GROUP_BUY_BEGIN_TIME is
'��ʼʱ��';

comment on column xjl_dw_group_buy.GROUP_BUY_END_TIME is
'��ֹʱ��';

comment on column xjl_dw_group_buy.GROUP_BUY_STATE is
'1����2�ر�';

comment on column xjl_dw_group_buy.CREATE_TIME is
'���ʱ��';

/*==============================================================*/
/* Table: xjl_dw_group_buy_item                                 */
/*==============================================================*/
create table xjl_dw_group_buy_item (
   GROUP_ITEM_ID        NUMERIC              not null,
   GROUP_BUY_ID         NUMERIC              not null,
   GROUP_ITEM_TITLE     VARCHAR(100)         not null,
   GROUP_ITEM_PRICE     NUMERIC(10,2)        not null,
   GROUP_ITEM_CONTENT   VARCHAR(200)         null,
   GROUP_ITEM_IMAGE     VARCHAR(100)         null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_XJL_DW_GROUP_BUY_ITEM primary key (GROUP_ITEM_ID)
);

comment on table xjl_dw_group_buy_item is
'�Ź���';

comment on column xjl_dw_group_buy_item.GROUP_ITEM_ID is
'�Ź���Ŀid';

comment on column xjl_dw_group_buy_item.GROUP_BUY_ID is
'�Ź�id';

comment on column xjl_dw_group_buy_item.GROUP_ITEM_TITLE is
'�Ź���Ŀ����';

comment on column xjl_dw_group_buy_item.GROUP_ITEM_PRICE is
'�Ź���Ŀ�۸�';

comment on column xjl_dw_group_buy_item.GROUP_ITEM_CONTENT is
'�Ź���Ŀ����';

comment on column xjl_dw_group_buy_item.GROUP_ITEM_IMAGE is
'����¼�룬ͼƬurl��ַ��
��ͼƬ����';

comment on column xjl_dw_group_buy_item.CREATE_TIME is
'���ʱ��';

/*==============================================================*/
/* Table: xjl_dw_group_buy_order                                */
/*==============================================================*/
create table xjl_dw_group_buy_order (
   GROUP_ORDER_ID       NUMERIC              not null,
   GROUP_BUY_ID         NUMERIC              not null,
   BUY_ITEM_ID          NUMERIC              not null,
   WX_OPEN_ID           VARCHAR(28)          not null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_XJL_DW_GROUP_BUY_ORDER primary key (GROUP_ORDER_ID)
);

comment on table xjl_dw_group_buy_order is
'������';

comment on column xjl_dw_group_buy_order.GROUP_ORDER_ID is
'����id';

comment on column xjl_dw_group_buy_order.GROUP_BUY_ID is
'�Ź�id';

comment on column xjl_dw_group_buy_order.BUY_ITEM_ID is
'�Ź���Ŀid';

comment on column xjl_dw_group_buy_order.WX_OPEN_ID is
'�Ź���id';

comment on column xjl_dw_group_buy_order.CREATE_TIME is
'���ʱ��';

/*==============================================================*/
/* Table: xjl_dw_homework                                       */
/*==============================================================*/
create table xjl_dw_homework (
   HOMEWORK_ID          NUMERIC              not null,
   CLASS_ID             NUMERIC              null,
   HOMEWORK_TITLE       VARCHAR(100)         not null,
   HOMEWORK_CONTENT     VARCHAR(100)         null,
   WX_OPEN_ID           VARCHAR(28)          null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_XJL_DW_HOMEWORK primary key (HOMEWORK_ID)
);

comment on table xjl_dw_homework is
'��ҵ��';

comment on column xjl_dw_homework.HOMEWORK_ID is
'��ҵid';

comment on column xjl_dw_homework.CLASS_ID is
'�༶��ʶ';

comment on column xjl_dw_homework.HOMEWORK_TITLE is
'��ҵ����';

comment on column xjl_dw_homework.HOMEWORK_CONTENT is
'��ҵ����';

comment on column xjl_dw_homework.WX_OPEN_ID is
'������';

comment on column xjl_dw_homework.STATUS is
'״̬';

comment on column xjl_dw_homework.CREATE_TIME is
'���ʱ��';

/*==============================================================*/
/* Table: xjl_dw_homework_model                                 */
/*==============================================================*/
create table xjl_dw_homework_model (
   MODEL_ID             NUMERIC              not null,
   MODEL_TITLE          VARCHAR(100)         not null,
   MODEL_CONTENT        VARCHAR(200)         null,
   HOMEWORK_ID          NUMERIC              not null,
   STUDENT_ID           NUMERIC              not null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_XJL_DW_HOMEWORK_MODEL primary key (MODEL_ID)
);

comment on table xjl_dw_homework_model is
'��ҵ���';

comment on column xjl_dw_homework_model.MODEL_ID is
'���id';

comment on column xjl_dw_homework_model.MODEL_TITLE is
'���ͼƬ';

comment on column xjl_dw_homework_model.MODEL_CONTENT is
'����';

comment on column xjl_dw_homework_model.HOMEWORK_ID is
'��ҵ��ʶ';

comment on column xjl_dw_homework_model.STUDENT_ID is
'ѧ����ʶ';

/*==============================================================*/
/* Table: xjl_dw_notice                                         */
/*==============================================================*/
create table xjl_dw_notice (
   NOTICE_ID            NUMERIC              not null,
   NOTICE_TITLE         VARCHAR(100)         not null,
   NOTICE_CONTENT       TEXT                 not null,
   NOTICE_DATE          TIMESTAMP            not null,
   WX_OPEN_ID           VARCHAR(28)          not null,
   CLASS_ID             NUMERIC              null,
   USER_ID              NUMERIC              null,
   UP_CHANNEL           VARCHAR(10)          null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_XJL_DW_NOTICE primary key (NOTICE_ID)
);

comment on table xjl_dw_notice is
'֪ͨ��';

comment on column xjl_dw_notice.NOTICE_ID is
'֪ͨid';

comment on column xjl_dw_notice.NOTICE_TITLE is
'֪ͨ����';

comment on column xjl_dw_notice.NOTICE_CONTENT is
'֪ͨ����';

comment on column xjl_dw_notice.NOTICE_DATE is
'֪ͨʱ��';

comment on column xjl_dw_notice.WX_OPEN_ID is
'������
΢�ŵ�openId,�Զ����ɣ�
ֻ�Ǽ�¼˭���������
��Ϣ������ʾ��֪ͨ��';

comment on column xjl_dw_notice.CLASS_ID is
'�༶��ʶ';

comment on column xjl_dw_notice.USER_ID is
'�û���ʶ';

comment on column xjl_dw_notice.UP_CHANNEL is
'����;������̨��admin  ΢�ţ�wechat';

comment on column xjl_dw_notice.STATUS is
'״̬';

comment on column xjl_dw_notice.CREATE_TIME is
'���ʱ��';

/*==============================================================*/
/* Table: xjl_dw_role                                           */
/*==============================================================*/
create table xjl_dw_role (
   ROLE_ID              NUMERIC              not null,
   ROLE_NAME            VARCHAR(50)          not null,
   ROLE_CODE            VARCHAR(50)          null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_XJL_DW_ROLE primary key (ROLE_ID)
);

comment on table xjl_dw_role is
'��ɫ��';

comment on column xjl_dw_role.ROLE_ID is
'��ɫ��ʶ';

comment on column xjl_dw_role.ROLE_NAME is
'��ɫ����';

comment on column xjl_dw_role.ROLE_CODE is
'��ɫ����';

comment on column xjl_dw_role.STATUS is
'״̬';

comment on column xjl_dw_role.CREATE_TIME is
'���ʱ��';

INSERT INTO xjl_dw_role (role_id, role_name, role_code, status, create_time) 
VALUES (1, '�༶����Ա', 'class_admin', '0AA', now());
INSERT INTO xjl_dw_role (role_id, role_name, role_code, status, create_time) 
VALUES (2, '��ʦ', 'teacher', '0AA', now());
INSERT INTO xjl_dw_role (role_id, role_name, role_code, status, create_time) 
VALUES (3, 'ѧ��', 'student', '0AA', now());

/*==============================================================*/
/* Table: xjl_dw_school                                         */
/*==============================================================*/
create table xjl_dw_school (
   SCHOOL_ID            NUMERIC              not null,
   SCHOOL_NAME          VARCHAR(100)         not null,
   SCHOOL_CODE          VARCHAR(50)          null,
   SCHOOL_LOGO          VARCHAR(200)         null,
   SCHOOL_TEL           VARCHAR(20)          null,
   SCHOOL_AREA          VARCHAR(100)         null,
   SCHOOL_ADDRESS       VARCHAR(100)         null,
   EFF_DATE             TIMESTAMP            not null,
   EXP_DATE             TIMESTAMP            not null,
   SCHOOL_DOMAIN        VARCHAR(50)          null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_XJL_DW_SCHOOL primary key (SCHOOL_ID)
);

comment on table xjl_dw_school is
'ѧУ��';

comment on column xjl_dw_school.SCHOOL_ID is
'��ҵ��ʶ';

comment on column xjl_dw_school.SCHOOL_NAME is
'��ҵ����';

comment on column xjl_dw_school.SCHOOL_CODE is
'��ҵ����';

comment on column xjl_dw_school.SCHOOL_LOGO is
'��ҵLogo';

comment on column xjl_dw_school.SCHOOL_TEL is
'��ҵ�绰';

comment on column xjl_dw_school.SCHOOL_AREA is
'��ҵ��������';

comment on column xjl_dw_school.SCHOOL_ADDRESS is
'��ҵ��ַ';

comment on column xjl_dw_school.EFF_DATE is
'��Чʱ��';

comment on column xjl_dw_school.EXP_DATE is
'ʧЧʱ��';

comment on column xjl_dw_school.SCHOOL_DOMAIN is
'������ʾ����wechat.airclub.xin';

comment on column xjl_dw_school.STATUS is
'״̬
0AA����Ч��0XX��ʧЧ';

comment on column xjl_dw_school.CREATE_TIME is
'����ʱ��';

/*==============================================================*/
/* Index: INDEX_PORTAL_VNO_ID_STATE                             */
/*==============================================================*/
create  index INDEX_PORTAL_VNO_ID_STATE on xjl_dw_school (
SCHOOL_ID,
STATUS
);

/*==============================================================*/
/* Table: xjl_dw_student                                        */
/*==============================================================*/
create table xjl_dw_student (
   STUDENT_ID           NUMERIC              not null,
   CLASS_ID             NUMERIC              not null,
   STUDENT_NAME         VARCHAR(50)          not null,
   STUDENT_NO           VARCHAR(50)          not null,
   STUDENT_SEX          VARCHAR(1)           not null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   UPDATE_TIME          TIMESTAMP            null,
   VERSION              NUMERIC              not null,
   constraint PK_XJL_DW_STUDENT primary key (STUDENT_ID)
);

comment on table xjl_dw_student is
'ѧ����';

comment on column xjl_dw_student.STUDENT_ID is
'ѧ����ʶ';

comment on column xjl_dw_student.CLASS_ID is
'�༶��ʶ';

comment on column xjl_dw_student.STUDENT_NAME is
'ѧ������';

comment on column xjl_dw_student.STUDENT_NO is
'ѧ��ѧ��';

comment on column xjl_dw_student.STUDENT_SEX is
'�У�M
Ů��F';

comment on column xjl_dw_student.STATUS is
'״̬';

comment on column xjl_dw_student.CREATE_TIME is
'���ʱ��';

comment on column xjl_dw_student.UPDATE_TIME is
'����ʱ��';

comment on column xjl_dw_student.VERSION is
'���´���';

/*==============================================================*/
/* Table: xjl_dw_subject                                        */
/*==============================================================*/
create table xjl_dw_subject (
   SUBJECT_ID           NUMERIC              not null,
   SUBJECT_TITLE        VARCHAR(50)          not null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_XJL_DW_SUBJECT primary key (SUBJECT_ID)
);

comment on table xjl_dw_subject is
'��Ŀ��';

comment on column xjl_dw_subject.SUBJECT_ID is
'��Ŀid';

comment on column xjl_dw_subject.SUBJECT_TITLE is
'��Ŀ����';

/*==============================================================*/
/* Table: xjl_dw_teacher                                        */
/*==============================================================*/
create table xjl_dw_teacher (
   TEACHER_ID           NUMERIC              not null,
   TEACHER_NAME         VARCHAR(50)          not null,
   TEACHER_CODE         VARCHAR(50)          not null,
   TEACHER_SEX          VARCHAR(1)           not null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_XJL_DW_TEACHER primary key (TEACHER_ID)
);

comment on table xjl_dw_teacher is
'��ʦ��';

comment on column xjl_dw_teacher.TEACHER_SEX is
'�У�M
Ů��F';

comment on column xjl_dw_teacher.STATUS is
'״̬';

comment on column xjl_dw_teacher.CREATE_TIME is
'���ʱ��';

/*==============================================================*/
/* Table: xjl_dw_user                                           */
/*==============================================================*/
create table xjl_dw_user (
   USER_ID              NUMERIC              not null,
   USER_NAME            VARCHAR(50)          not null,
   USER_CODE            VARCHAR(50)          not null,
   USER_PWD             VARCHAR(50)          not null,
   USER_ROLE            VARCHAR(1)           null,
   SCHOOL_ID            NUMERIC              null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_XJL_DW_USER primary key (USER_ID)
);

comment on table xjl_dw_user is
'��̨�û���';

comment on column xjl_dw_user.USER_ID is
'�û���ʶ';

comment on column xjl_dw_user.USER_NAME is
'�û�����';

comment on column xjl_dw_user.USER_CODE is
'��¼�˺�';

comment on column xjl_dw_user.USER_PWD is
'��¼����';

comment on column xjl_dw_user.USER_ROLE is
'��ɫ��0δ֪��1�ҳ���2����Ա��3��ʦ��S:��������Ա';

comment on column xjl_dw_user.SCHOOL_ID is
'ѧУ��ʶ';

comment on column xjl_dw_user.STATUS is
'״̬';

comment on column xjl_dw_user.CREATE_TIME is
'���ʱ��';

/*==============================================================*/
/* Table: xjl_dw_wx_class                                       */
/*==============================================================*/
create table xjl_dw_wx_class (
   CLASS_WX_ID          NUMERIC              not null,
   CLASS_ID             NUMERIC              null,
   WX_OPEN_ID           VARCHAR(28)          null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_XJL_DW_WX_CLASS primary key (CLASS_WX_ID)
);

comment on table xjl_dw_wx_class is
'΢�Ű༶��ϵ��';

comment on column xjl_dw_wx_class.CLASS_WX_ID is
'�༶΢�ű�ʶ';

comment on column xjl_dw_wx_class.CLASS_ID is
'�༶��ʶ';

comment on column xjl_dw_wx_class.WX_OPEN_ID is
'΢�ű�ʶ';

/*==============================================================*/
/* Table: xjl_dw_wx_role                                        */
/*==============================================================*/
create table xjl_dw_wx_role (
   ROLE_WX_ID           NUMERIC              not null,
   ROLE_ID              NUMERIC              null,
   WX_OPEN_ID           VARCHAR(28)          null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_XJL_DW_WX_ROLE primary key (ROLE_WX_ID)
);

comment on table xjl_dw_wx_role is
'΢�Ž�ɫ��ϵ��';

comment on column xjl_dw_wx_role.ROLE_WX_ID is
'��ɫ΢�ű�ʶ';

comment on column xjl_dw_wx_role.ROLE_ID is
'��ɫ��ʶ';

comment on column xjl_dw_wx_role.WX_OPEN_ID is
'΢�ű�ʶ';

/*==============================================================*/
/* Table: xjl_dw_wx_student                                     */
/*==============================================================*/
create table xjl_dw_wx_student (
   STUDENT_WX_ID        NUMERIC              not null,
   STUDENT_ID           NUMERIC              null,
   WX_OPEN_ID           VARCHAR(28)          null,
   STATUS               VARCHAR(3)           not null,
   CREATE_TIME          TIMESTAMP            not null,
   constraint PK_XJL_DW_WX_STUDENT primary key (STUDENT_WX_ID)
);

comment on table xjl_dw_wx_student is
'΢��ѧ����ϵ��';

comment on column xjl_dw_wx_student.STUDENT_WX_ID is
'ѧ��΢�ű�ʶ';

comment on column xjl_dw_wx_student.STUDENT_ID is
'ѧ����ʶ';

comment on column xjl_dw_wx_student.WX_OPEN_ID is
'΢�ű�ʶ';

alter table WX_SERVER
   add constraint FK_WX_SERVE_REFERENCE_XJL_DW_S foreign key (SCHOOL_ID)
      references xjl_dw_school (SCHOOL_ID)
      on delete restrict on update restrict;

alter table wx_user
   add constraint FK_WX_USER_REFERENCE_XJL_DW_T foreign key (TEACHER_ID)
      references xjl_dw_teacher (TEACHER_ID)
      on delete restrict on update restrict;

alter table wx_user
   add constraint FK_WX_USER_REFERENCE_XJL_DW_S foreign key (SCHOOL_ID)
      references xjl_dw_school (SCHOOL_ID)
      on delete restrict on update restrict;

alter table xjl_dw_album
   add constraint FK_XJL_DW_A_REFERENCE_XJL_DW_A foreign key (ALBUM_TEMPLATE_ID)
      references xjl_dw_album_template (ALBUM_TEMPLATE_ID)
      on delete restrict on update restrict;

alter table xjl_dw_album
   add constraint FK_XJL_DW_A_REFERENCE_XJL_DW_C foreign key (CLASS_ID)
      references xjl_dw_class (CLASS_ID)
      on delete restrict on update restrict;

alter table xjl_dw_album_image
   add constraint FK_XJL_DW_A_REFERENCE_XJL_DW_F foreign key (FILE_ID)
      references xjl_dw_file (FILE_ID)
      on delete restrict on update restrict;

alter table xjl_dw_album_image
   add constraint FK_XJL_DW_A_REFERENCE_XJL_DW_A foreign key (ALBUM_ID)
      references xjl_dw_album (ALBUM_ID)
      on delete restrict on update restrict;

alter table xjl_dw_album_template
   add constraint FK_XJL_DW_A_REFERENCE_XJL_DW_S foreign key (SCHOOL_ID)
      references xjl_dw_school (SCHOOL_ID)
      on delete restrict on update restrict;

alter table xjl_dw_article
   add constraint FK_XJL_DW_A_REFERENCE_WX_USER foreign key (WX_OPEN_ID)
      references wx_user (WX_OPEN_ID)
      on delete restrict on update restrict;

alter table xjl_dw_article
   add constraint FK_XJL_DW_A_REFERENCE_XJL_DW_C foreign key (CLASS_ID)
      references xjl_dw_class (CLASS_ID)
      on delete restrict on update restrict;

alter table xjl_dw_class
   add constraint FK_XJL_DW_C_REFERENCE_XJL_DW_S foreign key (SCHOOL_ID)
      references xjl_dw_school (SCHOOL_ID)
      on delete restrict on update restrict;

alter table xjl_dw_class_teacher
   add constraint FK_XJL_DW_C_REFERENCE_XJL_DW_T foreign key (TEACHER_ID)
      references xjl_dw_teacher (TEACHER_ID)
      on delete restrict on update restrict;

alter table xjl_dw_class_teacher
   add constraint FK_XJL_DW_C_REFERENCE_XJL_DW_C foreign key (CLASS_ID)
      references xjl_dw_class (CLASS_ID)
      on delete restrict on update restrict;

alter table xjl_dw_exam
   add constraint FK_XJL_DW_E_REFERENCE_XJL_DW_C foreign key (CLASS_ID)
      references xjl_dw_class (CLASS_ID)
      on delete restrict on update restrict;

alter table xjl_dw_exam_grade
   add constraint FK_XJL_DW_E_REFERENCE_XJL_DW_E foreign key (EXAM_ID)
      references xjl_dw_exam (EXAM_ID)
      on delete restrict on update restrict;

alter table xjl_dw_exam_grade
   add constraint FK_XJL_DW_E_REFERENCE_XJL_DW_S2 foreign key (SUBJECT_ID)
      references xjl_dw_subject (SUBJECT_ID)
      on delete restrict on update restrict;

alter table xjl_dw_exam_grade
   add constraint FK_XJL_DW_E_REFERENCE_XJL_DW_S foreign key (STUDENT_ID)
      references xjl_dw_student (STUDENT_ID)
      on delete restrict on update restrict;

alter table xjl_dw_exam_subject
   add constraint FK_XJL_DW_E_REFERENCE_XJL_DW_E foreign key (EXAM_ID)
      references xjl_dw_exam (EXAM_ID)
      on delete restrict on update restrict;

alter table xjl_dw_exam_subject
   add constraint FK_XJL_DW_E_REFERENCE_XJL_DW_S foreign key (SUBJECT_ID)
      references xjl_dw_subject (SUBJECT_ID)
      on delete restrict on update restrict;

alter table xjl_dw_group_buy
   add constraint FK_XJL_DW_G_REFERENCE_WX_USER foreign key (WX_OPEN_ID)
      references wx_user (WX_OPEN_ID)
      on delete restrict on update restrict;

alter table xjl_dw_group_buy
   add constraint FK_XJL_DW_G_REFERENCE_XJL_DW_C foreign key (CLASS_ID)
      references xjl_dw_class (CLASS_ID)
      on delete restrict on update restrict;

alter table xjl_dw_group_buy_item
   add constraint FK_XJL_DW_G_REFERENCE_XJL_DW_G foreign key (GROUP_BUY_ID)
      references xjl_dw_group_buy (GROUP_BUY_ID)
      on delete restrict on update restrict;

alter table xjl_dw_group_buy_order
   add constraint FK_XJL_DW_G_REFERENCE_XJL_DW_G2 foreign key (GROUP_BUY_ID)
      references xjl_dw_group_buy (GROUP_BUY_ID)
      on delete restrict on update restrict;

alter table xjl_dw_group_buy_order
   add constraint FK_XJL_DW_G_REFERENCE_XJL_DW_G foreign key (BUY_ITEM_ID)
      references xjl_dw_group_buy_item (GROUP_ITEM_ID)
      on delete restrict on update restrict;

alter table xjl_dw_group_buy_order
   add constraint FK_XJL_DW_G_REFERENCE_WX_USER foreign key (WX_OPEN_ID)
      references wx_user (WX_OPEN_ID)
      on delete restrict on update restrict;

alter table xjl_dw_homework
   add constraint FK_XJL_DW_H_REFERENCE_XJL_DW_C foreign key (CLASS_ID)
      references xjl_dw_class (CLASS_ID)
      on delete restrict on update restrict;

alter table xjl_dw_homework_model
   add constraint FK_XJL_DW_H_REFERENCE_XJL_DW_H foreign key (HOMEWORK_ID)
      references xjl_dw_homework (HOMEWORK_ID)
      on delete restrict on update restrict;

alter table xjl_dw_homework_model
   add constraint FK_XJL_DW_H_REFERENCE_XJL_DW_S foreign key (STUDENT_ID)
      references xjl_dw_student (STUDENT_ID)
      on delete restrict on update restrict;

alter table xjl_dw_notice
   add constraint FK_XJL_DW_N_REFERENCE_WX_USER foreign key (WX_OPEN_ID)
      references wx_user (WX_OPEN_ID)
      on delete restrict on update restrict;

alter table xjl_dw_notice
   add constraint FK_XJL_DW_N_REFERENCE_XJL_DW_U foreign key (USER_ID)
      references xjl_dw_user (USER_ID)
      on delete restrict on update restrict;

alter table xjl_dw_notice
   add constraint FK_XJL_DW_N_REFERENCE_XJL_DW_C foreign key (CLASS_ID)
      references xjl_dw_class (CLASS_ID)
      on delete restrict on update restrict;

alter table xjl_dw_student
   add constraint FK_XJL_DW_S_REFERENCE_XJL_DW_C foreign key (CLASS_ID)
      references xjl_dw_class (CLASS_ID)
      on delete restrict on update restrict;

alter table xjl_dw_user
   add constraint FK_XJL_DW_U_REFERENCE_XJL_DW_S foreign key (SCHOOL_ID)
      references xjl_dw_school (SCHOOL_ID)
      on delete restrict on update restrict;

alter table xjl_dw_wx_class
   add constraint FK_XJL_DW_W_REFERENCE_WX_USER foreign key (WX_OPEN_ID)
      references wx_user (WX_OPEN_ID)
      on delete restrict on update restrict;

alter table xjl_dw_wx_class
   add constraint FK_XJL_DW_W_REFERENCE_XJL_DW_C foreign key (CLASS_ID)
      references xjl_dw_class (CLASS_ID)
      on delete restrict on update restrict;

alter table xjl_dw_wx_role
   add constraint FK_XJL_DW_W_REFERENCE_WX_USER foreign key (WX_OPEN_ID)
      references wx_user (WX_OPEN_ID)
      on delete restrict on update restrict;

alter table xjl_dw_wx_role
   add constraint FK_XJL_DW_W_REFERENCE_XJL_DW_R foreign key (ROLE_ID)
      references xjl_dw_role (ROLE_ID)
      on delete restrict on update restrict;

alter table xjl_dw_wx_student
   add constraint FK_XJL_DW_W_REFERENCE_WX_USER foreign key (WX_OPEN_ID)
      references wx_user (WX_OPEN_ID)
      on delete restrict on update restrict;

alter table xjl_dw_wx_student
   add constraint FK_XJL_DW_W_REFERENCE_XJL_DW_S foreign key (STUDENT_ID)
      references xjl_dw_student (STUDENT_ID)
      on delete restrict on update restrict;

