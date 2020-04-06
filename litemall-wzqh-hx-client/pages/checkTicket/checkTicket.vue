<template>
	<view>
		<cu-custom bgColor="bg-gradual-blue" :isBack="true">
			<block slot="backText">返回</block>
			<block slot="content">核销端-核销</block>
		</cu-custom>

		<view class="padding flex flex-direction bg-white">
			<view class="cu-form-group">
				<view class="title">门票编号</view>
				<input v-model="ticketNum" disabled="true" />
			</view>
		</view>
		
		<view class="padding flex flex-direction">
			<button class="cu-btn bg-blue lg" @tap="scan">扫门票二维码</button>
		</view>

		<view class="padding flex flex-direction">
			<button class="cu-btn bg-blue lg" :disabled="!ticketNum" @tap="check">核销门票</button>
		</view>
	</view>
</template>

<script>
	import {
		UseTicket
	} from '@/api/config.js'
	export default {
		data() {
			return {
				ticketNum: ''
			}
		},
		methods: {
			scan() {
				uni.scanCode({
					onlyFromCamera: true,
					success: function(res) {
						console.log(res)
						this.ticketNum = res.result
					}.bind(this)
				})
			},
			check() {
				uni.showLoading({
					title: '正在处理，请稍等',
					mask: true
				})
				const blocks = this.ticketNum.split('-')
				const ticketId = blocks[0]
				const userId = blocks[1]
				const orderId = blocks[2]
				uni.request({
					url: UseTicket,
					method: 'POST',
					data: {
						id: ticketId,
						userId: userId,
						orderId: orderId
					},
					success: function(res) {
						if (res.data.errno !== 0) {
							setTimeout(() => {
								uni.showModal({
									title: '错误',
									content: res.data.errmsg,
									showCancel: false
								})
							}, 200)
						} else {
							this.ticketNum = ''
							setTimeout(() => {
								uni.showToast({
									title: '核销成功',
									icon: 'none'
								})
							}, 200)
						}
					}.bind(this),
					complete: function() {
						setTimeout(() => {
							uni.hideLoading()
						}, 100)
					}
				})
			}
		}
	}
</script>

<style>

</style>
