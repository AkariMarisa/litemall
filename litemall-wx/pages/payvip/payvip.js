var util = require('../../utils/util.js');
var api = require('../../config/api.js');

var app = getApp();

Page({
  data: {
    checkedGoodsList: [],
    checkedAddress: {},
    availableCouponLength: 0, // 可用的优惠券数量
    goodsTotalPrice: 0.00, //商品总价
    freightPrice: 0.00, //快递费
    couponPrice: 0.00, //优惠券的价格
    grouponPrice: 0.00, //团购优惠价格
    orderTotalPrice: 0.00, //订单总价
    actualPrice: 0.00, //实际需要支付的总价
    cartId: 0,
    addressId: 0,
    couponId: 0,
    userCouponId: 0,
    message: '',
    grouponLinkId: 0, //参与的团购，如果是发起则为0
    grouponRulesId: 0 //团购规则ID
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

  getVIP1() {
    util.request(api.CartFastAdd, {
        goodsId: 1181006,
        number: 1,
        productId: 251
      }, "POST")
        .then(function (res) {
          if (res.errno == 0) {

            // 如果storage中设置了cartId，则是立即购买，否则是购物车购买
            // try {
            //   wx.setStorageSync('cartId', res.data);
            //   // wx.setStorageSync('grouponRulesId', checkedGroupon.id);
            //   // wx.setStorageSync('grouponLinkId', that.data.grouponLink.id);
            //   wx.navigateTo({
            //     url: '/pages/checkout/checkout'
            //   })
            // } catch (e) {}
            util.request(api.OrderSubmit, {
              cartId: res.data,//this.data.cartId,
              addressId: 0,
              couponId: 0,
              userCouponId: 0,
              message: 0,
              grouponRulesId:0,
              grouponLinkId: 0,
              isVirtualGoods:true
            }, 'POST').then(res => {
              if (res.errno === 0) {

                // 下单成功，重置couponId
                // try {
                //   wx.setStorageSync('couponId', 0);
                // } catch (error) {}
                const orderId = res.data.orderId;
                util.request(api.OrderPrepay, {
                  orderId: orderId
                }, 'POST').then(function (res) {
                  if (res.errno === 0) {
                    const payParam = res.data;
                    console.log("支付过程开始");
                    wx.requestPayment({
                      'timeStamp': payParam.timeStamp,
                      'nonceStr': payParam.nonceStr,
                      'package': payParam.packageValue,
                      'signType': payParam.signType,
                      'paySign': payParam.paySign,
                      'success': function (res) {
                        console.log("支付过程成功");
                        util.request(api.AgentConfirm, {
                          orderId: orderId
                        }, 'PUT').then(function (re) {
                          if (re.errno == 0) {
                            wx.showToast({
                            });
                          }
                        });
                        wx.redirectTo({
                          url: '/pages/ucenter/index'
                        });
                      },
                      'fail': function (res) {
                        console.log("支付过程失败");
                        // wx.redirectTo({
                        //   url: '/pages/payResult/payResult?status=0&orderId=' + orderId
                        // });
                        util.request(api.OrderCancel, {
                          orderId: orderId
                        }, 'POST').then(function (res) {
                          if (res.errno === 0) {
                            // wx.showToast({
                            //   title: '取消订单成功'
                            // });
                            // util.redirect('/pages/ucenter/order/order');
                          } else {
                            util.showErrorToast(res.errmsg);
                          }
                        });
                      },
                      'complete': function (res) {
                        console.log("支付过程结束")
                      }
                    });
                  } else {
                    util.request(api.OrderCancel, {
                      orderId: orderId
                    }, 'POST').then(function (res) {
                      if (res.errno === 0) {
                        // wx.showToast({
                        //   title: '取消订单成功'
                        // });
                        // util.redirect('/pages/ucenter/order/order');
                      } else {
                        util.showErrorToast(res.errmsg);
                      }
                    });
                    // wx.redirectTo({
                    //   url: '/pages/payResult/payResult?status=0&orderId=' + orderId
                    // });
                    wx.showToast({
                      title: '付款失败',
                      icon: 'none',
                    });
                  }
                });

              } else {
                util.request(api.OrderCancel, {
                  orderId: orderId
                }, 'POST').then(function (res) {
                  if (res.errno === 0) {
                    // wx.showToast({
                    //   title: '取消订单成功'
                    // });
                    util.redirect('/pages/ucenter/order/order');
                  } else {
                    util.showErrorToast(res.errmsg);
                  }
                });
                wx.redirectTo({
                  url: '/pages/payResult/payResult?status=0&orderId=' + orderId
                });
              }
            });
          } else {
            wx.showToast({
              image: '/static/images/icon_error.png',
              title: res.errmsg,
              mask: true
            });
          }
        });
  },
  getVIP2() {
    util.request(api.CartFastAdd, {
      goodsId: 1181007,
      number: 1,
      productId: 252
    }, "POST")
      .then(function (res) {
        if (res.errno == 0) {

          // 如果storage中设置了cartId，则是立即购买，否则是购物车购买
          // try {
          //   wx.setStorageSync('cartId', res.data);
          //   // wx.setStorageSync('grouponRulesId', checkedGroupon.id);
          //   // wx.setStorageSync('grouponLinkId', that.data.grouponLink.id);
          //   wx.navigateTo({
          //     url: '/pages/checkout/checkout'
          //   })
          // } catch (e) {}
          util.request(api.OrderSubmit, {
            cartId: res.data,//this.data.cartId,
            addressId: 0,
            couponId: 0,
            userCouponId: 0,
            message: 0,
            grouponRulesId: 0,
            grouponLinkId: 0,
            isVirtualGoods: true
          }, 'POST').then(res => {
            if (res.errno === 0) {

              // 下单成功，重置couponId
              // try {
              //   wx.setStorageSync('couponId', 0);
              // } catch (error) {}
              const orderId = res.data.orderId;
              util.request(api.OrderPrepay, {
                orderId: orderId
              }, 'POST').then(function (res) {
                if (res.errno === 0) {
                  const payParam = res.data;
                  console.log("支付过程开始");
                  wx.requestPayment({
                    'timeStamp': payParam.timeStamp,
                    'nonceStr': payParam.nonceStr,
                    'package': payParam.packageValue,
                    'signType': payParam.signType,
                    'paySign': payParam.paySign,
                    'success': function (res) {
                      console.log("支付过程成功");
                      util.request(api.AgentConfirm, {
                        orderId: orderId
                      }, 'PUT').then(function (re) {
                        if (re.errno == 0) {
                          wx.showToast({
                          });
                        }
                      });
                      wx.redirectTo({
                        url: '/pages/payResult/payResult?status=1&orderId=' + orderId
                      });
                    },
                    'fail': function (res) {
                      console.log("支付过程失败");
                      // wx.redirectTo({
                      //   url: '/pages/payResult/payResult?status=0&orderId=' + orderId
                      // });
                      util.request(api.OrderCancel, {
                        orderId: orderId
                      }, 'POST').then(function (res) {
                        if (res.errno === 0) {
                          // wx.showToast({
                          //   title: '取消订单成功'
                          // });
                          util.redirect('/pages/ucenter/order/order');
                        } else {
                          util.showErrorToast(res.errmsg);
                        }
                      });
                    },
                    'complete': function (res) {
                      console.log("支付过程结束")
                    }
                  });
                } else {
                  util.request(api.OrderCancel, {
                    orderId: orderId
                  }, 'POST').then(function (res) {
                    if (res.errno === 0) {
                      // wx.showToast({
                      //   title: '取消订单成功'
                      // });
                      // util.redirect('/pages/ucenter/order/order');
                    } else {
                      util.showErrorToast(res.errmsg);
                    }
                  });
                  // wx.redirectTo({
                  //   url: '/pages/payResult/payResult?status=0&orderId=' + orderId
                  // });
                  wx.showToast({
                    title: '付款失败',
                    icon: 'none',
                  });
                }
              });

            } else {
              util.request(api.OrderCancel, {
                orderId: orderId
              }, 'POST').then(function (res) {
                if (res.errno === 0) {
                  // wx.showToast({
                  //   title: '取消订单成功'
                  // });
                  util.redirect('/pages/ucenter/order/order');
                } else {
                  util.showErrorToast(res.errmsg);
                }
              });
              wx.redirectTo({
                url: '/pages/payResult/payResult?status=0&orderId=' + orderId
              });
            }
          });
        } else {
          wx.showToast({
            image: '/static/images/icon_error.png',
            title: res.errmsg,
            mask: true
          });
        }
      });
  },
})