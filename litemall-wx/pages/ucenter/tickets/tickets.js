var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');

var app = getApp();

Page({
  data: {
    ticketList: [],
    page: 1,
    limit: 10,
    totalPages: 1
  },
  getTicketList() {
    wx.showLoading({
      title: '加载中...',
    });
    let that = this;
    util.request(api.TicketList, {
      page: that.data.page,
      limit: that.data.limit
    }).then(function (res) {
      if (res.errno === 0) {
        that.setData({
          ticketList: that.data.ticketList.concat(res.data.list),
          totalPages: res.data.pages
        });
      }
      wx.hideLoading();
    });
  },
  onLoad: function (options) {
    this.getTicketList()
  },
  onReachBottom() {
    if (this.data.totalPages > this.data.page) {
      this.setData({
        page: this.data.page + 1
      });
      this.getTicketList();
    } else {
      wx.showToast({
        title: '没有更多门票信息了',
        icon: 'none',
        duration: 2000
      });
      return false;
    }
  },
  onReady: function () {

  },
  onShow: function () {

  },
  onHide: function () {
    // 页面隐藏

  },
  onUnload: function () {
    // 页面关闭
  },
  ticketDetail(event) {
    console.log(event)
    const i = event.currentTarget.dataset.index;
    const ticket = this.data.ticketList[i];
    if (!ticket.isUsed) {
      wx.navigateTo({
        url: '/pages/ucenter/ticketDetail/ticketDetail?id=' + ticket.id + '&userId=' + ticket.userId + '&orderId=' + ticket.orderId 
      })
    }
  },
})