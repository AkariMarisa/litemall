<template>
	<view>
		<cu-custom bgColor="bg-gradual-blue" :isBack="false"><block slot="content">核销端-登录</block></cu-custom>
		
		<view class="cu-card case">
			<view class="cu-item">
				<form>
					<view class="cu-form-group">
						<view class="title">用户名</view>
						<input v-model="username" type="text" placeholder="请输入用户名" name="input"></input>
					</view>
					<view class="cu-form-group">
						<view class="title">密&nbsp;&nbsp;&nbsp;&nbsp;码</view>
						<input v-model="password" type="password" placeholder="请输入密码" name="input"></input>
					</view>
				</form>
				<view class="padding flex flex-direction">
					<button class="cu-btn bg-green lg" @tap="login">登录</button>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { Login } from '@/api/config.js'
	export default {
		data() {
			return {
				username: '',
				password: ''
			}
		},
		created() {
			const token = this.$store.getters.getToken
			if (token) {
				uni.redirectTo({
					url: '/pages/index/index'
				})
			}
		},
		methods: {
			login() {
				if (!this.username) {
					uni.showToast({
						title: '用户名不能为空',
						icon: 'none'
					})
					return false;
				}
				
				if (!this.password) {
					uni.showToast({
						title: '密码不能为空',
						icon: 'none'
					})
					return false;
				}
				
				uni.request({
					url: Login,
					method: 'POST',
					data: {
						username: this.username,
						password: this.password
					},
					success: function(res) {
						if (res.data.errno !== 0) {
							uni.showModal({
								title: '错误',
								content: res.data.errmsg,
								showCancel: false
							})
						} else {
							const token = res.data.data.token
							this.$store.commit('setToken', token)
							uni.redirectTo({
								url: '/pages/index/index'
							})
						}
					}.bind(this)
				})
			}
		}
	}
</script>

<style>

</style>
