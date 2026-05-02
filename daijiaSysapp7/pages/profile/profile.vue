<template>
	<view class="profile-container">
		<!-- 顶部用户信息卡片 -->
		<view class="user-header">
			<view class="header-bg"></view>
			<view class="user-info" @click="goToPersonalInfo">
				<view class="avatar-wrapper">
					<image class="avatar" :src="userInfo.avatar" mode="aspectFill"></image>
					<view class="avatar-edit">
						<text>✏️</text>
					</view>
				</view>
				<view class="user-detail">
					<view class="user-name-row">
						<text class="user-name">{{ userInfo.name }}</text>
						<view class="verify-badges">
							<view v-if="userInfo.identityVerified" class="verify-badge identity">
								<text>已实名</text>
							</view>
							<view v-if="userInfo.licenseVerified" class="verify-badge license">
								<text>驾照已认证</text>
							</view>
						</view>
					</view>
					<view class="user-phone">
						<text>{{ userInfo.phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2') }}</text>
					</view>
					<view class="credit-level">
						<text class="credit-label">信用等级：</text>
						<view class="credit-stars">
							<text v-for="star in 5" :key="star" :class="{ active: star <= userInfo.creditLevel }">★</text>
						</view>
						<text class="credit-score">{{ userInfo.creditScore }}分</text>
					</view>
				</view>
				<view class="arrow-right"></view>
			</view>
		</view>

		<!-- 统计数据 -->
		<view class="stats-section">
			<view class="card">
				<view class="stats-grid">
					<view class="stat-card" @click="showStatDetail('totalOrders')">
						<text class="stat-value">{{ userStats.totalOrders }}</text>
						<text class="stat-label">累计订单</text>
					</view>
					<view class="stat-card" @click="showStatDetail('totalIncome')">
						<text class="stat-value">¥{{ userStats.totalIncome }}</text>
						<text class="stat-label">累计收入</text>
					</view>
					<view class="stat-card" @click="showStatDetail('totalDistance')">
						<text class="stat-value">{{ userStats.totalDistance }}km</text>
						<text class="stat-label">累计里程</text>
					</view>
					<view class="stat-card" @click="showStatDetail('serviceDays')">
						<text class="stat-value">{{ userStats.serviceDays }}</text>
						<text class="stat-label">服务天数</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 功能列表 -->
		<view class="function-section">
			<view class="card">
				<view class="section-title">服务管理</view>
				
				<view class="list-item" @click="goToReview">
					<view class="list-item-left">
						<view class="item-icon" style="background-color: #FFF3E0;">
							<text>⭐</text>
						</view>
						<text class="list-item-text">投诉评价</text>
					</view>
					<view class="list-item-right">
						<text class="item-badge" v-if="pendingReviews > 0">{{ pendingReviews }}待评价</text>
						<view class="arrow-right"></view>
					</view>
				</view>

				<view class="list-item" @click="goToComplaint">
					<view class="list-item-left">
						<view class="item-icon" style="background-color: #FFEBEE;">
							<text>📝</text>
						</view>
						<text class="list-item-text">投诉单</text>
					</view>
					<view class="list-item-right">
						<text class="item-badge" v-if="pendingComplaints > 0">{{ pendingComplaints }}处理中</text>
						<view class="arrow-right"></view>
					</view>
				</view>

				<view class="list-item" @click="goToEmergency">
					<view class="list-item-left">
						<view class="item-icon" style="background-color: #FCE4EC;">
							<text>🚨</text>
						</view>
						<text class="list-item-text">紧急求助</text>
					</view>
					<view class="list-item-right">
						<view class="arrow-right"></view>
					</view>
				</view>
			</view>
		</view>

		<view class="function-section">
			<view class="card">
				<view class="section-title">设置</view>
				
				<view class="list-item" @click="goToSettings">
					<view class="list-item-left">
						<view class="item-icon" style="background-color: #E3F2FD;">
							<text>⚙️</text>
						</view>
						<text class="list-item-text">接单设置</text>
					</view>
					<view class="list-item-right">
						<view class="arrow-right"></view>
					</view>
				</view>

				<view class="list-item" @click="contactService">
					<view class="list-item-left">
						<view class="item-icon" style="background-color: #E8F5E9;">
							<text>💬</text>
						</view>
						<text class="list-item-text">联系客服</text>
					</view>
					<view class="list-item-right">
						<view class="arrow-right"></view>
					</view>
				</view>

				<view class="list-item" @click="showAbout">
					<view class="list-item-left">
						<view class="item-icon" style="background-color: #F3E5F5;">
							<text>ℹ️</text>
						</view>
						<text class="list-item-text">关于我们</text>
					</view>
					<view class="list-item-right">
						<text class="version-text">v1.0.0</text>
						<view class="arrow-right"></view>
					</view>
				</view>
			</view>
		</view>

		<!-- 退出登录 -->
		<view class="logout-section">
			<button class="logout-btn" @click="showLogoutConfirm">退出登录</button>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			userInfo: {
				id: 1,
				name: '张师傅',
				phone: '13888888888',
				avatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=professional%20driver%20avatar%20portrait%20friendly%20middle-aged%20man&image_size=square_hd',
				creditLevel: 5,
				creditScore: 98,
				identityVerified: true,
				licenseVerified: true
			},
			userStats: {
				totalOrders: 1256,
				totalIncome: 89520,
				totalDistance: 5680,
				serviceDays: 365
			},
			pendingReviews: 3,
			pendingComplaints: 1
		}
	},
	onLoad() {
		this.checkLoginStatus()
		this.loadUserInfo()
	},
	onShow() {
		this.refreshData()
	},
	methods: {
		checkLoginStatus() {
			const token = uni.getStorageSync('driver_token')
			if (!token) {
				uni.redirectTo({
					url: '/pages/login/login'
				})
			}
		},
		loadUserInfo() {
			const savedInfo = uni.getStorageSync('driver_info')
			if (savedInfo) {
				this.userInfo = { ...this.userInfo, ...savedInfo }
			}
		},
		refreshData() {
			// 模拟刷新数据
		},
		goToPersonalInfo() {
			uni.navigateTo({
				url: '/pages/personal-info/personal-info'
			})
		},
		goToReview() {
			uni.navigateTo({
				url: '/pages/review/review'
			})
		},
		goToComplaint() {
			uni.navigateTo({
				url: '/pages/complaint/complaint'
			})
		},
		goToEmergency() {
			uni.navigateTo({
				url: '/pages/emergency/emergency'
			})
		},
		goToSettings() {
			uni.navigateTo({
				url: '/pages/settings/settings'
			})
		},
		showStatDetail(type) {
			const titles = {
				totalOrders: '累计订单',
				totalIncome: '累计收入',
				totalDistance: '累计里程',
				serviceDays: '服务天数'
			}
			uni.showToast({
				title: '查看' + titles[type] + '详情',
				icon: 'none'
			})
		},
		contactService() {
			uni.showModal({
				title: '联系客服',
				content: '客服热线：400-123-4567\n工作时间：全天24小时',
				confirmText: '拨打热线',
				cancelText: '取消',
				success: (res) => {
					if (res.confirm) {
						uni.makePhoneCall({
							phoneNumber: '4001234567'
						})
					}
				}
			})
		},
		showAbout() {
			uni.showModal({
				title: '关于我们',
				content: '代驾司机端 v1.0.0\n\n专业代驾服务平台，为您提供安全、便捷的代驾服务。\n\n客服热线：400-123-4567',
				showCancel: false,
				confirmText: '我知道了'
			})
		},
		showLogoutConfirm() {
			uni.showModal({
				title: '提示',
				content: '确定要退出登录吗？',
				confirmText: '确定',
				success: (res) => {
					if (res.confirm) {
						this.logout()
					}
				}
			})
		},
		logout() {
			uni.removeStorageSync('driver_token')
			uni.removeStorageSync('driver_info')
			
			uni.showToast({
				title: '已退出登录',
				icon: 'success'
			})
			
			setTimeout(() => {
				uni.redirectTo({
					url: '/pages/login/login'
				})
			}, 1000)
		}
	}
}
</script>

<style scoped>
.profile-container {
	min-height: 100vh;
	background-color: #F5F5F5;
	padding-bottom: 60rpx;
}

/* 顶部用户信息 */
.user-header {
	position: relative;
	padding: 40rpx 30rpx 60rpx;
}

.header-bg {
	position: absolute;
	top: 0;
	left: 0;
	right: 0;
	height: 300rpx;
	background: linear-gradient(135deg, #4CAF50, #8BC34A);
	border-bottom-left-radius: 40rpx;
	border-bottom-right-radius: 40rpx;
}

.user-info {
	position: relative;
	display: flex;
	align-items: center;
	background-color: #FFFFFF;
	border-radius: 20rpx;
	padding: 30rpx;
	box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
}

.avatar-wrapper {
	position: relative;
	margin-right: 24rpx;
}

.avatar {
	width: 140rpx;
	height: 140rpx;
	border-radius: 50%;
	border: 4rpx solid #FFFFFF;
	box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
}

.avatar-edit {
	position: absolute;
	bottom: 0;
	right: 0;
	width: 44rpx;
	height: 44rpx;
	background-color: #4CAF50;
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 20rpx;
	border: 3rpx solid #FFFFFF;
}

.user-detail {
	flex: 1;
	display: flex;
	flex-direction: column;
}

.user-name-row {
	display: flex;
	align-items: center;
	flex-wrap: wrap;
	margin-bottom: 12rpx;
}

.user-name {
	font-size: 36rpx;
	font-weight: 600;
	color: #333333;
	margin-right: 16rpx;
}

.verify-badges {
	display: flex;
	gap: 12rpx;
	margin-top: 8rpx;
}

.verify-badge {
	padding: 4rpx 16rpx;
	border-radius: 20rpx;
	font-size: 20rpx;
}

.verify-badge.identity {
	background-color: #E8F5E9;
	color: #4CAF50;
}

.verify-badge.license {
	background-color: #E3F2FD;
	color: #2196F3;
}

.user-phone {
	font-size: 26rpx;
	color: #666666;
	margin-bottom: 12rpx;
}

.credit-level {
	display: flex;
	align-items: center;
}

.credit-label {
	font-size: 24rpx;
	color: #999999;
}

.credit-stars {
	font-size: 28rpx;
	margin-right: 8rpx;
}

.credit-stars text {
	color: #E0E0E0;
}

.credit-stars text.active {
	color: #FFD700;
}

.credit-score {
	font-size: 24rpx;
	color: #4CAF50;
	font-weight: 500;
}

/* 统计数据 */
.stats-section {
	padding: 0 30rpx;
	margin-top: -20rpx;
	position: relative;
}

.stats-grid {
	display: flex;
	flex-wrap: wrap;
	margin: 0 -10rpx;
}

.stats-grid .stat-card {
	width: 50%;
	padding: 20rpx 10rpx;
}

/* 功能列表 */
.function-section {
	padding: 0 30rpx;
	margin-top: 20rpx;
}

.item-icon {
	width: 72rpx;
	height: 72rpx;
	border-radius: 16rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	margin-right: 20rpx;
	font-size: 36rpx;
}

.item-badge {
	font-size: 22rpx;
	color: #F44336;
	background-color: #FFEBEE;
	padding: 4rpx 16rpx;
	border-radius: 20rpx;
	margin-right: 12rpx;
}

.version-text {
	font-size: 24rpx;
	color: #999999;
	margin-right: 12rpx;
}

/* 退出登录 */
.logout-section {
	padding: 60rpx 60rpx 0;
}

.logout-btn {
	height: 88rpx;
	background-color: #FFFFFF;
	color: #F44336;
	border-radius: 44rpx;
	font-size: 30rpx;
	font-weight: 500;
	border: 2rpx solid #FFCDD2;
}

.logout-btn:active {
	background-color: #FFEBEE;
}
</style>
