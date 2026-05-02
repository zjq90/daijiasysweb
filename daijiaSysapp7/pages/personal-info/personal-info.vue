<template>
	<view class="personal-info-container">
		<!-- 头像区域 -->
		<view class="avatar-section">
			<view class="avatar-wrapper" @click="chooseAvatar">
				<image class="avatar" :src="userInfo.avatar" mode="aspectFill"></image>
				<view class="avatar-edit">
					<text>更换头像</text>
				</view>
			</view>
		</view>

		<!-- 基本信息 -->
		<view class="info-section">
			<view class="card">
				<view class="section-title">基本信息</view>
				
				<view class="info-item">
					<text class="info-label">姓名</text>
					<view class="info-value-row">
						<text class="info-value">{{ userInfo.name }}</text>
						<view class="arrow-right"></view>
					</view>
				</view>

				<view class="info-item">
					<text class="info-label">手机号</text>
					<view class="info-value-row">
						<text class="info-value">{{ userInfo.phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2') }}</text>
						<text class="change-link" @click="changePhone">更换</text>
					</view>
				</view>

				<view class="info-item">
					<text class="info-label">性别</text>
					<picker 
						:value="genderIndex" 
						:range="genderOptions" 
						@change="onGenderChange"
					>
						<view class="info-value-row">
							<text class="info-value">{{ genderOptions[genderIndex] }}</text>
							<view class="arrow-right"></view>
						</view>
					</picker>
				</view>

				<view class="info-item">
					<text class="info-label">年龄</text>
					<view class="info-value-row">
						<text class="info-value">{{ userInfo.age }}岁</text>
						<view class="arrow-right"></view>
					</view>
				</view>

				<view class="info-item">
					<text class="info-label">服务区域</text>
					<view class="info-value-row" @click="chooseServiceArea">
						<text class="info-value">{{ userInfo.serviceArea }}</text>
						<view class="arrow-right"></view>
					</view>
				</view>
			</view>
		</view>

		<!-- 认证信息 -->
		<view class="auth-section">
			<view class="card">
				<view class="section-title">认证信息</view>
				
				<view class="auth-item" @click="viewIdentityAuth">
					<view class="auth-left">
						<view class="auth-icon" style="background-color: #E8F5E9;">
							<text>🆔</text>
						</view>
						<view class="auth-content">
							<text class="auth-title">实名认证</text>
							<text class="auth-desc">已完成实名认证</text>
						</view>
					</view>
					<view class="auth-right">
						<view class="badge badge-success">已认证</view>
						<view class="arrow-right"></view>
					</view>
				</view>

				<view class="auth-item" @click="viewLicenseAuth">
					<view class="auth-left">
						<view class="auth-icon" style="background-color: #E3F2FD;">
							<text>🚗</text>
						</view>
						<view class="auth-content">
							<text class="auth-title">驾照认证</text>
							<text class="auth-desc">C1驾照，有效期至2030年12月</text>
						</view>
					</view>
					<view class="auth-right">
						<view class="badge badge-success">已认证</view>
						<view class="arrow-right"></view>
					</view>
				</view>

				<view class="auth-item" @click="viewVehicleAuth">
					<view class="auth-left">
						<view class="auth-icon" style="background-color: #FFF3E0;">
							<text>🚙</text>
						</view>
						<view class="auth-content">
							<text class="auth-title">车辆认证</text>
							<text class="auth-desc">大众帕萨特 京A·12345</text>
						</view>
					</view>
					<view class="auth-right">
						<view class="badge badge-success">已认证</view>
						<view class="arrow-right"></view>
					</view>
				</view>
			</view>
		</view>

		<!-- 信用等级 -->
		<view class="credit-section">
			<view class="card">
				<view class="section-title">信用等级</view>
				
				<view class="credit-display">
					<view class="credit-score">
						<text class="score-value">{{ userInfo.creditScore }}</text>
						<text class="score-unit">分</text>
					</view>
					<view class="credit-level">
						<view class="credit-stars">
							<text v-for="star in 5" :key="star" :class="{ active: star <= userInfo.creditLevel }">★</text>
						</view>
						<text class="level-text">优秀</text>
					</view>
				</view>

				<view class="credit-progress">
					<view class="progress-header">
						<text class="progress-label">信用评分</text>
						<text class="progress-range">30-100分</text>
					</view>
					<view class="progress-bar">
						<view class="progress-fill" :style="{ width: (userInfo.creditScore - 30) / 70 * 100 + '%' }"></view>
					</view>
					<view class="progress-marks">
						<text>较差</text>
						<text>一般</text>
						<text>良好</text>
						<text>优秀</text>
					</view>
				</view>

				<view class="credit-rules" @click="viewCreditRules">
					<text class="rules-text">查看信用评分规则</text>
					<view class="arrow-right"></view>
				</view>
			</view>
		</view>

		<!-- 服务评价 -->
		<view class="review-section">
			<view class="card">
				<view class="section-header">
					<text class="section-title">服务评价</text>
					<text class="more-link" @click="viewAllReviews">全部评价 ></text>
				</view>

				<view class="review-stats">
					<view class="stat-item">
						<text class="stat-value">{{ reviewStats.goodRate }}%</text>
						<text class="stat-label">好评率</text>
					</view>
					<view class="stat-item">
						<text class="stat-value">{{ reviewStats.totalReviews }}</text>
						<text class="stat-label">总评价数</text>
					</view>
					<view class="stat-item">
						<text class="stat-value">{{ reviewStats.avgScore }}</text>
						<text class="stat-label">平均评分</text>
					</view>
				</view>

				<view class="review-list">
					<view class="review-item" v-for="(review, index) in recentReviews" :key="index">
						<view class="review-header">
							<view class="review-user">
								<view class="user-avatar">
									<text>{{ review.userName.charAt(0) }}</text>
								</view>
								<view class="user-info">
									<text class="user-name">{{ review.userName }}</text>
									<view class="review-stars">
										<text v-for="star in 5" :key="star" :class="{ active: star <= review.score }">★</text>
									</view>
								</view>
							</view>
							<text class="review-time">{{ review.time }}</text>
						</view>
						<text class="review-content">{{ review.content }}</text>
						<view class="review-tags" v-if="review.tags && review.tags.length">
							<text class="tag" v-for="(tag, tagIndex) in review.tags" :key="tagIndex">{{ tag }}</text>
						</view>
					</view>
				</view>
			</view>
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
				age: 38,
				serviceArea: '北京市朝阳区、海淀区、东城区'
			},
			genderIndex: 0,
			genderOptions: ['男', '女', '保密'],
			reviewStats: {
				goodRate: 98.5,
				totalReviews: 1256,
				avgScore: 4.9
			},
			recentReviews: [
				{
					id: 1,
					userName: '李先生',
					score: 5,
					time: '2026-04-30',
					content: '师傅服务态度很好，驾驶技术娴熟，准时到达，非常满意！',
					tags: ['态度好', '技术好', '准时']
				},
				{
					id: 2,
					userName: '王女士',
					score: 5,
					time: '2026-04-28',
					content: '师傅很细心，帮忙搬行李，开车很稳，下次还会叫他。',
					tags: ['热心', '开车稳']
				},
				{
					id: 3,
					userName: '赵先生',
					score: 4,
					time: '2026-04-25',
					content: '整体服务不错，就是路线选择可以再优化一下。',
					tags: ['服务好']
				}
			]
		}
	},
	onLoad() {
		this.loadUserInfo()
	},
	methods: {
		loadUserInfo() {
			const savedInfo = uni.getStorageSync('driver_info')
			if (savedInfo) {
				this.userInfo = { ...this.userInfo, ...savedInfo }
			}
		},
		chooseAvatar() {
			uni.showActionSheet({
				itemList: ['拍照', '从相册选择'],
				success: (res) => {
					const sourceType = res.tapIndex === 0 ? ['camera'] : ['album']
					uni.chooseImage({
						count: 1,
						sourceType: sourceType,
						success: (res2) => {
							this.userInfo.avatar = res2.tempFilePaths[0]
							uni.showToast({
								title: '头像已更新',
								icon: 'success'
							})
						}
					})
				}
			})
		},
		changePhone() {
			uni.showModal({
				title: '更换手机号',
				content: '请联系客服：400-123-4567 进行手机号更换',
				confirmText: '拨打热线',
				success: (res) => {
					if (res.confirm) {
						uni.makePhoneCall({
							phoneNumber: '4001234567'
						})
					}
				}
			})
		},
		onGenderChange(e) {
			this.genderIndex = e.detail.value
			uni.showToast({
				title: '已更新',
				icon: 'success'
			})
		},
		chooseServiceArea() {
			uni.showToast({
				title: '选择服务区域',
				icon: 'none'
			})
		},
		viewIdentityAuth() {
			uni.showModal({
				title: '实名认证信息',
				content: '姓名：张师傅\n身份证号：110**********1234\n有效期：长期有效',
				showCancel: false,
				confirmText: '我知道了'
			})
		},
		viewLicenseAuth() {
			uni.showModal({
				title: '驾照认证信息',
				content: '驾照类型：C1\n驾照编号：110101********1234\n有效期至：2030年12月31日',
				showCancel: false,
				confirmText: '我知道了'
			})
		},
		viewVehicleAuth() {
			uni.showModal({
				title: '车辆认证信息',
				content: '品牌型号：大众帕萨特\n车牌号：京A·12345\n车辆颜色：黑色\n注册日期：2020年05月',
				showCancel: false,
				confirmText: '我知道了'
			})
		},
		viewCreditRules() {
			uni.showModal({
				title: '信用评分规则',
				content: '信用评分基础分为30分，满分为100分。\n\n加分项：\n1. 完成订单 +1分/单\n2. 获得五星好评 +5分/次\n3. 连续30天无投诉 +20分\n\n减分项：\n1. 拒绝订单 -2分/次\n2. 被投诉 -10分/次\n3. 迟到 -5分/次',
				showCancel: false,
				confirmText: '我知道了'
			})
		},
		viewAllReviews() {
			uni.showToast({
				title: '查看全部评价',
				icon: 'none'
			})
		}
	}
}
</script>

<style scoped>
.personal-info-container {
	min-height: 100vh;
	background-color: #F5F5F5;
	padding-bottom: 40rpx;
}

/* 头像区域 */
.avatar-section {
	display: flex;
	justify-content: center;
	padding: 60rpx 0;
	background: linear-gradient(180deg, #E8F5E9 0%, #FFFFFF 100%);
}

.avatar-wrapper {
	position: relative;
}

.avatar {
	width: 200rpx;
	height: 200rpx;
	border-radius: 50%;
	border: 6rpx solid #FFFFFF;
	box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.1);
}

.avatar-edit {
	position: absolute;
	bottom: 0;
	left: 50%;
	transform: translateX(-50%);
	background: linear-gradient(135deg, #4CAF50, #8BC34A);
	color: #FFFFFF;
	font-size: 22rpx;
	padding: 8rpx 24rpx;
	border-radius: 20rpx;
	box-shadow: 0 4rpx 12rpx rgba(76, 175, 80, 0.4);
}

/* 信息区域 */
.info-section,
.auth-section,
.credit-section,
.review-section {
	padding: 0 30rpx;
	margin-top: 20rpx;
}

.info-item {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 28rpx 0;
	border-bottom: 1rpx solid #F0F0F0;
}

.info-item:last-child {
	border-bottom: none;
}

.info-label {
	font-size: 28rpx;
	color: #666666;
}

.info-value-row {
	display: flex;
	align-items: center;
}

.info-value {
	font-size: 28rpx;
	color: #333333;
	margin-right: 12rpx;
}

.change-link {
	font-size: 24rpx;
	color: #4CAF50;
}

/* 认证信息 */
.auth-item {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 24rpx 0;
	border-bottom: 1rpx solid #F0F0F0;
}

.auth-item:last-child {
	border-bottom: none;
}

.auth-left {
	display: flex;
	align-items: center;
	flex: 1;
}

.auth-icon {
	width: 72rpx;
	height: 72rpx;
	border-radius: 16rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	margin-right: 20rpx;
	font-size: 36rpx;
}

.auth-content {
	display: flex;
	flex-direction: column;
}

.auth-title {
	font-size: 28rpx;
	color: #333333;
	margin-bottom: 6rpx;
}

.auth-desc {
	font-size: 24rpx;
	color: #999999;
}

.auth-right {
	display: flex;
	align-items: center;
}

/* 信用等级 */
.credit-display {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 30rpx;
	background: linear-gradient(135deg, #E8F5E9, #C8E6C9);
	border-radius: 16rpx;
	margin-bottom: 30rpx;
}

.credit-score {
	display: flex;
	align-items: baseline;
}

.score-value {
	font-size: 72rpx;
	font-weight: 600;
	color: #4CAF50;
}

.score-unit {
	font-size: 28rpx;
	color: #4CAF50;
	margin-left: 8rpx;
}

.credit-level {
	display: flex;
	flex-direction: column;
	align-items: flex-end;
}

.credit-stars {
	font-size: 36rpx;
	margin-bottom: 8rpx;
}

.credit-stars text {
	color: #E0E0E0;
}

.credit-stars text.active {
	color: #FFD700;
}

.level-text {
	font-size: 24rpx;
	color: #4CAF50;
	font-weight: 500;
}

.credit-progress {
	margin-bottom: 24rpx;
}

.progress-header {
	display: flex;
	justify-content: space-between;
	margin-bottom: 12rpx;
}

.progress-label {
	font-size: 26rpx;
	color: #666666;
}

.progress-range {
	font-size: 24rpx;
	color: #999999;
}

.progress-bar {
	height: 16rpx;
	background-color: #F0F0F0;
	border-radius: 8rpx;
	overflow: hidden;
	margin-bottom: 12rpx;
}

.progress-fill {
	height: 100%;
	background: linear-gradient(90deg, #4CAF50, #8BC34A);
	border-radius: 8rpx;
	transition: width 0.3s;
}

.progress-marks {
	display: flex;
	justify-content: space-between;
	font-size: 20rpx;
	color: #999999;
}

.credit-rules {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding-top: 24rpx;
	border-top: 1rpx solid #F0F0F0;
}

.rules-text {
	font-size: 26rpx;
	color: #4CAF50;
}

/* 服务评价 */
.section-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 24rpx;
}

.more-link {
	font-size: 24rpx;
	color: #4CAF50;
}

.review-stats {
	display: flex;
	justify-content: space-around;
	padding: 24rpx 0;
	background-color: #F8F9FA;
	border-radius: 16rpx;
	margin-bottom: 24rpx;
}

.stat-item {
	display: flex;
	flex-direction: column;
	align-items: center;
}

.stat-value {
	font-size: 36rpx;
	font-weight: 600;
	color: #4CAF50;
	margin-bottom: 8rpx;
}

.stat-label {
	font-size: 24rpx;
	color: #999999;
}

.review-list {
	display: flex;
	flex-direction: column;
}

.review-item {
	padding: 24rpx 0;
	border-bottom: 1rpx solid #F0F0F0;
}

.review-item:last-child {
	border-bottom: none;
}

.review-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 16rpx;
}

.review-user {
	display: flex;
	align-items: center;
}

.user-avatar {
	width: 64rpx;
	height: 64rpx;
	border-radius: 50%;
	background-color: #E8F5E9;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 28rpx;
	color: #4CAF50;
	font-weight: 500;
	margin-right: 16rpx;
}

.user-info {
	display: flex;
	flex-direction: column;
}

.user-name {
	font-size: 28rpx;
	color: #333333;
	margin-bottom: 4rpx;
}

.review-stars {
	font-size: 24rpx;
}

.review-stars text {
	color: #E0E0E0;
}

.review-stars text.active {
	color: #FFD700;
}

.review-time {
	font-size: 22rpx;
	color: #999999;
}

.review-content {
	font-size: 26rpx;
	color: #666666;
	line-height: 1.6;
	margin-bottom: 16rpx;
}

.review-tags {
	display: flex;
	flex-wrap: wrap;
	gap: 12rpx;
}

.tag {
	font-size: 22rpx;
	color: #4CAF50;
	background-color: #E8F5E9;
	padding: 4rpx 16rpx;
	border-radius: 20rpx;
}
</style>
