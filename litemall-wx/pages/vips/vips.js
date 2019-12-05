var util = require('../../utils/util.js');
var api = require('../../config/api.js');
Page({
  data: {
    agentLevel: 0,
    agentLevels: '无',
    userInfo: {
      nickName: '用户名',
      avatarUrl: 'http://yanxuan.nosdn.127.net/8945ae63d940cc42406c3f67019c5cb6.png'
    },
  },
  onLoad: function(options) {
    // 页面初始化 options为页面跳转所带来的参数
  },
  onReady: function() {
  },
  onShow: function() {

    //获取用户的登录信息
    let userInfo = wx.getStorageSync('userInfo');
    this.setData({
      userInfo: userInfo,
    });
    this.setData({
      agentLevel: 1 //wx.getStorageSync('agentLevel')
    });
    
  },
  onHide: function() {
    // 页面隐藏

  },
  onUnload: function() {
    // 页面关闭
  },
})