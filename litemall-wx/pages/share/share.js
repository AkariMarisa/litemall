var util = require('../../utils/util.js');
var api = require('../../config/api.js');
Page({

  data: {
    aturl: 'https://api.weixin.qq.com/cgi-bin/token',
    rurl: 'https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=',
    accesstoken: '',
    expiresin: '',
    userid: 0,
    buff: '/static/images/rw.png'
  },

  onLoad: function() {
    this.setData({
      userid: wx.getStorageSync('userId'),
    });
    let that = this;
    //获取token
    util.request(that.data.aturl, {
      grant_type: 'client_credential',
      appid: 'wx8f702282f2e1df6b',
      secret: 'a1355f11a818f47fcdfaefc73945d801',
    }, 'GET').then(function(res) {


      //获取二维码
      util.request(that.data.rurl + res.access_token, {
        scene: '' + that.data.userid,
      }, 'POST' , 'other', 'arraybuffer').then(function(res) {
        that.setData({
          buff: "data:image/png;base64," + wx.arrayBufferToBase64(res)
        })
      })
    })
  }
})