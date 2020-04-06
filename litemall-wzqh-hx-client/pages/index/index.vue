<template>
	<view>
		<cu-custom bgColor="bg-gradual-blue" :isBack="false"><block slot="content">核销端-首页</block></cu-custom>
		
		<view class="nav-list margin-top-xl">
			<navigator hover-class="none" url="/pages/checkTicket/checkTicket" class="bg-cyan nav-li" navigateTo>
				<view class="nav-title">核销</view>
				<view class="nav-name">HeXiao</view>
				<text class="cuIcon-ticket"></text>
			</navigator>
			<navigator hover-class="none" url="/pages/refundTicket/refundTicket" class="bg-red nav-li" navigateTo>
				<view class="nav-title">退票</view>
				<view class="nav-name">TuiPiao</view>
				<text class="cuIcon-refund"></text>
			</navigator>
		</view>
		
		<view class="padding margin-top-xl"></view>
		<view class="padding margin-top-xl"></view>
		<view class="padding margin-top-xl"></view>
		
		<view class="padding margin-top-xl flex flex-direction">
			<button class="cu-btn bg-red lg" @tap="logout">注销</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {

			}
		},
		created() {
			const token = this.$store.getters.getToken
			if (!token) {
				uni.redirectTo({
					url: '/pages/login/login'
				})
			}
		},
		methods: {
			logout() {
				this.$store.commit('setToken', '')
				uni.redirectTo({
					url: '/pages/login/login'
				})
			}
		}
	}
</script>

<style>

</style>
