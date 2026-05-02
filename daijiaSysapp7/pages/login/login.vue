<template>
	<view class="login-container">
		<!-- 顶部Logo区域 -->
		<view class="login-header">
			<view class="logo-icon">
				<text class="logo-text">代驾</text>
			</view>
			<view class="app-title">代驾司机端</view>
			<view class="app-subtitle">安全便捷，专业代驾</view>
		</view>

		<!-- 登录表单 -->
		<view class="login-form">
			<!-- 登录方式切换 -->
			<view class="login-tabs">
				<view 
					class="login-tab" 
					:class="{ active: loginType === 'code' }"
					@click="switchLoginType('code')"
				>
					验证码登录
				</view>
				<view 
					class="login-tab" 
					:class="{ active: loginType === 'password' }"
					@click="switchLoginType('password')"
				>
					密码登录
				</view>
			</view>

			<!-- 手机号输入 -->
			<view class="input-group">
				<view class="input-label">手机号</view>
				<view class="phone-input-container">
					<input 
						class="input-field phone-input"
						type="number"
						placeholder="请输入手机号"
						maxlength="11"
						v-model="phone"
					/>
					<view 
						v-if="loginType === 'code'"
						class="code-btn" 
						:class="{ disabled: countdown > 0 }"
						@click="getCode"
					>
						{{ countdown > 0 ? `${countdown}s后重发` : '获取验证码' }}
					</view>
				</view>
			</view>

			<!-- 验证码输入 -->
			<view v-if="loginType === 'code'" class="input-group">
				<view class="input-label">验证码</view>
				<input 
					class="input-field"
					type="number"
					placeholder="请输入验证码"
					maxlength="6"
					v-model="code"
				/>
			</view>

			<!-- 密码输入 -->
			<view v-if="loginType === 'password'" class="input-group">
				<view class="input-label">密码</view>
				<view class="password-input-container">
					<input 
						class="input-field password-input"
						:type="showPassword ? 'text' : 'password'"
						placeholder="请输入密码"
						v-model="password"
					/>
					<view class="password-toggle" @click="togglePassword">
						<text>{{ showPassword ? '隐藏' : '显示' }}</text>
					</view>
				</view>
			</view>

			<!-- 登录按钮 -->
			<button 
				class="btn btn-primary login-btn"
				:class="{ disabled: !canLogin }"
				:disabled="!canLogin"
				@click="handleLogin"
			>
				登录
			</button>

			<!-- 忘记密码 -->
			<view v-if="loginType === 'password'" class="forgot-password">
				<text @click="forgotPassword">忘记密码？</text>
			</view>
		</view>

		<!-- 底部协议 -->
		<view class="agreement">
			<checkbox 
				class="agreement-checkbox" 
				:checked="agreed" 
				@click="agreed = !agreed"
				color="#4CAF50"
			/>
			<text class="agreement-text">
				我已阅读并同意
				<text class="agreement-link" @click="showAgreement('user')">《用户协议》</text>
				和
				<text class="agreement-link" @click="showAgreement('privacy')">《隐私政策》</text>
			</text>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			loginType: 'code',
			phone: '',
			code: '',
			password: '',
			showPassword: false,
			countdown: 0,
			agreed: false
		}
	},
	computed: {
		canLogin() {
			if (!this.agreed) return false
			if (this.phone.length !== 11) return false
			if (this.loginType === 'code') {
				return this.code.length === 6
			} else {
				return this.password.length >= 6
			}
		}
	},
	onLoad() {
		// 检查是否已登录
		const token = uni.getStorageSync('driver_token')
		if (token) {
			uni.switchTab({
				url: '/pages/index/index'
			})
		}
	},
	methods: {
		switchLoginType(type) {
			this.loginType = type
			this.code = ''
			this.password = ''
		},
		async getCode() {
			if (this.countdown > 0) return
			if (this.phone.length !== 11) {
				uni.showToast({
					title: '请输入正确的手机号',
					icon: 'none'
				})
				return
			}
			
			// 模拟发送验证码
			uni.showLoading({
				title: '发送中...'
			})
			
			await new Promise(resolve => setTimeout(resolve, 1000))
			
			uni.hideLoading()
			uni.showToast({
				title: '验证码已发送',
				icon: 'success'
			})
			
			// 开启倒计时
			this.countdown = 60
			const timer = setInterval(() => {
				this.countdown--
				if (this.countdown <= 0) {
					clearInterval(timer)
				}
			}, 1000)
		},
		togglePassword() {
			this.showPassword = !this.showPassword
		},
		async handleLogin() {
			if (!this.canLogin) return
			
			uni.showLoading({
				title: '登录中...'
			})
			
			await new Promise(resolve => setTimeout(resolve, 1500))
			
			// 模拟登录成功
			const mockUser = {
				id: 1,
				phone: this.phone,
				name: '张师傅',
				avatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=professional%20driver%20avatar%20portrait%20friendly%20middle-aged%20man&image_size=square_hd',
				creditLevel: 5,
				licenseVerified: true,
				identityVerified: true
			}
			
			// 存储登录状态
			uni.setStorageSync('driver_token', 'mock_token_' + Date.now())
			uni.setStorageSync('driver_info', mockUser)
			
			uni.hideLoading()
			uni.showToast({
				title: '登录成功',
				icon: 'success'
			})
			
			setTimeout(() => {
				uni.switchTab({
					url: '/pages/index/index'
				})
			}, 1000)
		},
		forgotPassword() {
			uni.showToast({
				title: '请联系客服重置密码',
				icon: 'none'
			})
		},
		showAgreement(type) {
			const titles = {
				user: '用户协议',
				privacy: '隐私政策'
			}
			uni.showModal({
				title: titles[type],
				content: '这是' + titles[type] + '的内容。在实际应用中，这里应该展示完整的协议内容。',
				showCancel: false,
				confirmText: '我知道了'
			})
		}
	}
}
</script>

<style scoped>
.login-container {
	min-height: 100vh;
	background: linear-gradient(180deg, #E8F5E9 0%, #FFFFFF 100%);
	padding: 0 60rpx;
	display: flex;
	flex-direction: column;
}

.login-header {
	display: flex;
	flex-direction: column;
	align-items: center;
	padding-top: 120rpx;
	padding-bottom: 80rpx;
}

.logo-icon {
	width: 160rpx;
	height: 160rpx;
	border-radius: 50%;
	background: linear-gradient(135deg, #4CAF50, #8BC34A);
	display: flex;
	align-items: center;
	justify-content: center;
	margin-bottom: 32rpx;
	box-shadow: 0 12rpx 32rpx rgba(76, 175, 80, 0.3);
}

.logo-text {
	font-size: 48rpx;
	font-weight: 600;
	color: #FFFFFF;
}

.app-title {
	font-size: 40rpx;
	font-weight: 600;
	color: #333333;
	margin-bottom: 12rpx;
}

.app-subtitle {
	font-size: 26rpx;
	color: #999999;
}

.login-form {
	background-color: #FFFFFF;
	border-radius: 24rpx;
	padding: 40rpx;
	box-shadow: 0 8rpx 40rpx rgba(0, 0, 0, 0.05);
}

.login-tabs {
	display: flex;
	margin-bottom: 48rpx;
	background-color: #F5F5F5;
	border-radius: 12rpx;
	padding: 8rpx;
}

.login-tab {
	flex: 1;
	height: 72rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 28rpx;
	color: #666666;
	border-radius: 8rpx;
	transition: all 0.3s;
}

.login-tab.active {
	background-color: #FFFFFF;
	color: #4CAF50;
	font-weight: 500;
	box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.input-group {
	margin-bottom: 36rpx;
}

.input-label {
	font-size: 28rpx;
	color: #666666;
	margin-bottom: 16rpx;
}

.phone-input-container {
	display: flex;
	align-items: center;
}

.phone-input {
	flex: 1;
}

.code-btn {
	min-width: 200rpx;
	height: 88rpx;
	margin-left: 20rpx;
	background: linear-gradient(135deg, #4CAF50, #8BC34A);
	color: #FFFFFF;
	border-radius: 12rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 26rpx;
	font-weight: 500;
	transition: all 0.3s;
}

.code-btn.disabled {
	background: #E0E0E0;
	color: #999999;
}

.password-input-container {
	display: flex;
	align-items: center;
}

.password-input {
	flex: 1;
}

.password-toggle {
	padding: 0 24rpx;
	font-size: 26rpx;
	color: #4CAF50;
}

.login-btn {
	margin-top: 60rpx;
}

.login-btn.disabled {
	opacity: 0.5;
}

.forgot-password {
	text-align: right;
	margin-top: 24rpx;
}

.forgot-password text {
	font-size: 26rpx;
	color: #4CAF50;
}

.agreement {
	display: flex;
	align-items: flex-start;
	padding: 40rpx 0 60rpx;
}

.agreement-checkbox {
	margin-right: 12rpx;
	margin-top: 4rpx;
	transform: scale(0.8);
}

.agreement-text {
	font-size: 24rpx;
	color: #999999;
	line-height: 1.5;
}

.agreement-link {
	color: #4CAF50;
}
</style>
