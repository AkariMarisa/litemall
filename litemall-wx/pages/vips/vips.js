var util = require('../../utils/util.js');
var api = require('../../config/api.js');
Page({
  data: {
    agentlist: [],
    agentLevel: 0,
    agentLevels: '无',
    userInfo: {
      nickName: '用户名',
      avatarUrl: 'http://yanxuan.nosdn.127.net/8945ae63d940cc42406c3f67019c5cb6.png'
    },
    // aturl: 'https://api.weixin.qq.com/cgi-bin/token',
    // url: 'https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=',
    // accesstoken: '',
    // expiresin: '',
    // userid:0,
  },

  onReady: function() {

  },

  onShow: function() {
    //获取用户的登录信息
    this.setData({
      userInfo: wx.getStorageSync('userInfo'),
      agentLevel: wx.getStorageSync('agentLevel'),
    });
    let that = this;
    util.request(api.AgentList).then(function(res) {
      if (res.errno === 0) {
        that.setData({
          agentlist: res.data,
        });
      }
    });
  },
  onHide: function() {
    // 页面隐藏
  },
  onUnload: function() {
    // 页面关闭
  },

  goShare: function() {
    wx.navigateTo({
      url: "/pages/share/share"
    });
    // let that = this;

    // util.request(that.data.aturl, {
    //   grant_type: 'client_credential',
    //   appid: 'wx8f702282f2e1df6b',
    //   secret: 'a1355f11a818f47fcdfaefc73945d801',
    // }, 'GET').then(function(res) {
    //   util.request(that.data.url + res.access_token, {
    //     scene: ''+that.data.userid
    //   }, 'POST').then(function (res) {
    //     wx.showToast({});
    //   })
    //   that.setData({
    //     accesstoken: res.access_token,
    //     expiresin: res.expires_in,
    //   })
    //   wx.showToast({
    //     title: res.access_token,
    //   });
    // })
  },

})