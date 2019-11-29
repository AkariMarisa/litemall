var util = require('../../utils/util.js');
var api = require('../../config/api.js');

var app = getApp();

Page({
  data: {
    //vipList: [],
    count: 0,
    scrollTop: 0,
    showPage: false
  },


  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // this.getvipList();
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },
  // getvipList: function () {

  //   let that = this;
  //   that.setData({
  //     scrollTop: 0,
  //     showPage: false,
  //     vipList: []
  //   });
  //   // 页面渲染完成
  //   // wx.showToast({
  //   //   title: '加载中...',
  //   //   icon: 'loading',
  //   //   duration: 1000
  //   // });
  // },
  getVIP1() {
    if (!app.globalData.hasLogin) {
      wx.navigateTo({
        url: "/pages/auth/login/login"
      });
    }
  },
  getVIP2() {
    if (!app.globalData.hasLogin) {
      wx.navigateTo({
        url: "/pages/auth/login/login"
      });
    }
  },
})