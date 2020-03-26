// pages/ucenter/ticketDetail/ticketDetail.js
// var QRCode = require('../../../utils/qrCode.js')
import QRCode from '../../../utils/qrCode.js'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    id: 0
  },

  patchNumber(num, n) {
    return (Array(n).join(0) + num).slice(-n);
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      id: this.patchNumber(options.id, 12)
    });

    console.log(QRCode)
    var qrcode = new QRCode('canvas', {
      text: this.patchNumber(options.id, 12),
      width: 300,
      height: 300,
      colorDark: '#000000',
      colorLight: '#ffffff',
      correctLevel: 2
    });
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

  }
})