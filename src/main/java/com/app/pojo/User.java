package com.app.pojo;

public class User {
    private Long id;

    private String openid;

    private String sn;

    private String name;

    private String realname;

    private String mobile;

    private Integer directteamnum;

    private Integer teamnum;

    private String wechat;

    private String nickname;

    private String headimgurl;

    private Boolean sotpRecruit;

    private String email;

    private String idcard;

    private Byte isCheck;

    private Byte ischeckcard;

    private Integer time;

    private String password;

    private String paypassword;

    private String idcardphoto1;

    private String idcardphoto2;

    private String lifephoto;

    private Byte wechatuserinfo;

    private String devicetype;

    private Byte isXy;

    private Integer xyTime;

    private Byte isUsePassword;

    private Byte isOversease;

    public User(Long id, String openid, String sn, String name, String realname, String mobile, Integer directteamnum, Integer teamnum, String wechat, String nickname, String headimgurl, Boolean sotpRecruit, String email, String idcard, Byte isCheck, Byte ischeckcard, Integer time, String password, String paypassword, String idcardphoto1, String idcardphoto2, String lifephoto, Byte wechatuserinfo, String devicetype, Byte isXy, Integer xyTime, Byte isUsePassword, Byte isOversease) {
        this.id = id;
        this.openid = openid;
        this.sn = sn;
        this.name = name;
        this.realname = realname;
        this.mobile = mobile;
        this.directteamnum = directteamnum;
        this.teamnum = teamnum;
        this.wechat = wechat;
        this.nickname = nickname;
        this.headimgurl = headimgurl;
        this.sotpRecruit = sotpRecruit;
        this.email = email;
        this.idcard = idcard;
        this.isCheck = isCheck;
        this.ischeckcard = ischeckcard;
        this.time = time;
        this.password = password;
        this.paypassword = paypassword;
        this.idcardphoto1 = idcardphoto1;
        this.idcardphoto2 = idcardphoto2;
        this.lifephoto = lifephoto;
        this.wechatuserinfo = wechatuserinfo;
        this.devicetype = devicetype;
        this.isXy = isXy;
        this.xyTime = xyTime;
        this.isUsePassword = isUsePassword;
        this.isOversease = isOversease;
    }

    public User() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn == null ? null : sn.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname == null ? null : realname.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Integer getDirectteamnum() {
        return directteamnum;
    }

    public void setDirectteamnum(Integer directteamnum) {
        this.directteamnum = directteamnum;
    }

    public Integer getTeamnum() {
        return teamnum;
    }

    public void setTeamnum(Integer teamnum) {
        this.teamnum = teamnum;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat == null ? null : wechat.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl == null ? null : headimgurl.trim();
    }

    public Boolean getSotpRecruit() {
        return sotpRecruit;
    }

    public void setSotpRecruit(Boolean sotpRecruit) {
        this.sotpRecruit = sotpRecruit;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard == null ? null : idcard.trim();
    }

    public Byte getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Byte isCheck) {
        this.isCheck = isCheck;
    }

    public Byte getIscheckcard() {
        return ischeckcard;
    }

    public void setIscheckcard(Byte ischeckcard) {
        this.ischeckcard = ischeckcard;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPaypassword() {
        return paypassword;
    }

    public void setPaypassword(String paypassword) {
        this.paypassword = paypassword == null ? null : paypassword.trim();
    }

    public String getIdcardphoto1() {
        return idcardphoto1;
    }

    public void setIdcardphoto1(String idcardphoto1) {
        this.idcardphoto1 = idcardphoto1 == null ? null : idcardphoto1.trim();
    }

    public String getIdcardphoto2() {
        return idcardphoto2;
    }

    public void setIdcardphoto2(String idcardphoto2) {
        this.idcardphoto2 = idcardphoto2 == null ? null : idcardphoto2.trim();
    }

    public String getLifephoto() {
        return lifephoto;
    }

    public void setLifephoto(String lifephoto) {
        this.lifephoto = lifephoto == null ? null : lifephoto.trim();
    }

    public Byte getWechatuserinfo() {
        return wechatuserinfo;
    }

    public void setWechatuserinfo(Byte wechatuserinfo) {
        this.wechatuserinfo = wechatuserinfo;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype == null ? null : devicetype.trim();
    }

    public Byte getIsXy() {
        return isXy;
    }

    public void setIsXy(Byte isXy) {
        this.isXy = isXy;
    }

    public Integer getXyTime() {
        return xyTime;
    }

    public void setXyTime(Integer xyTime) {
        this.xyTime = xyTime;
    }

    public Byte getIsUsePassword() {
        return isUsePassword;
    }

    public void setIsUsePassword(Byte isUsePassword) {
        this.isUsePassword = isUsePassword;
    }

    public Byte getIsOversease() {
        return isOversease;
    }

    public void setIsOversease(Byte isOversease) {
        this.isOversease = isOversease;
    }
}