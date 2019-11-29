//index.js
var wxbarcode = require('../../utils/index.js');

Page({

  data: {
    code: 'ddddddddddddddddddddddddddddd' 
  },

  onLoad: function () {
    let that =this
    wxbarcode.qrcode('qrcode', that.data.code,700,700);
  }
})