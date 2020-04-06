import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
const store = new Vuex.Store({
	state: {
		userInfo: {
			id: 0,
			username: ''
		},
		token: ''
	},
	getters: {
		getToken: state => state.token
	},
	mutations: {
		setUserInfo(state, {
			user_id,
			username
		}) {
			state.userInfo = {
				id: user_id,
				username
			}
		},
		setToken(state, token) {
			state.token = token
		}
	},
	actions: {
		logout({
			commit
		}) {
			commit('setToken', '')
		}
	}
})
export default store
