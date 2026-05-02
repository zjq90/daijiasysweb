<template>
	<view class="index-container">
		<!-- 顶部状态栏 -->
		<view class="status-bar">
			<view class="location-info">
				<view class="location-icon">
					<text>📍</text>
				</view>
				<view class="location-text">
					<text class="location-label">当前位置</text>
					<text class="location-address">{{ currentLocation }}</text>
				</view>
			</view>
			<view class="status-badge">
				<text class="badge-text" :class="{ online: isOnline }">
					{{ isOnline ? '在线' : '休息' }}
				</text>
				<switch 
					:checked="isOnline" 
					@change="toggleOnlineStatus"
					color="#4CAF50"
				/>
			</view>
		</view>

		<!-- 轮播图 - 营销活动 -->
		<view class="banner-section">
			<swiper 
				class="banner-swiper" 
				indicator-dots 
				autoplay 
				circular
				indicator-color="rgba(255,255,255,0.5)"
				indicator-active-color="#4CAF50"
			>
				<swiper-item v-for="(banner, index) in banners" :key="index">
					<view class="banner-item" @click="clickBanner(banner)">
						<image class="banner-image" :src="banner.image" mode="aspectFill"></image>
						<view class="banner-overlay">
							<text class="banner-title">{{ banner.title }}</text>
							<text class="banner-subtitle">{{ banner.subtitle }}</text>
						</view>
					</view>
				</swiper-item>
			</swiper>
		</view>

		<!-- 今日统计 -->
		<view class="stats-section">
			<view class="card">
				<view class="section-title">今日数据</view>
				<view class="stats-grid">
					<view class="stat-card" @click="showStatDetail('orders')">
						<text class="stat-value">{{ todayStats.orders }}</text>
						<text class="stat-label">已接订单</text>
					</view>
					<view class="stat-card" @click="showStatDetail('income')">
						<text class="stat-value">¥{{ todayStats.income }}</text>
						<text class="stat-label">今日收入</text>
					</view>
					<view class="stat-card" @click="showStatDetail('distance')">
						<text class="stat-value">{{ todayStats.distance }}km</text>
						<text class="stat-label">行驶里程</text>
					</view>
					<view class="stat-card" @click="showStatDetail('time')">
						<text class="stat-value">{{ todayStats.time }}h</text>
						<text class="stat-label">工作时长</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 激励奖励政策 -->
		<view class="incentive-section">
			<view class="card">
				<view class="section-header">
					<text class="section-title">激励奖励</text>
					<text class="more-link" @click="showMoreIncentives">查看全部 ></text>
				</view>
				
				<!-- 当前进行中的活动 -->
				<view class="incentive-list">
					<view 
						class="incentive-item" 
						v-for="(item, index) in incentives" 
						:key="index"
						@click="showIncentiveDetail(item)"
					>
						<view class="incentive-icon" :style="{ backgroundColor: item.color }">
							<text>{{ item.icon }}</text>
						</view>
						<view class="incentive-content">
							<view class="incentive-title-row">
								<text class="incentive-title">{{ item.title }}</text>
								<text class="badge" :class="'badge-' + item.badgeType">{{ item.badge }}</text>
							</view>
							<text class="incentive-desc">{{ item.description }}</text>
							<view class="incentive-progress" v-if="item.progress">
								<view class="progress-bar">
									<view class="progress-fill" :style="{ width: item.progress + '%' }"></view>
								</view>
								<text class="progress-text">{{ item.progressText }}</text>
							</view>
						</view>
						<view class="arrow-right"></view>
					</view>
				</view>
			</view>
		</view>

		<!-- 公告通知 -->
		<view class="notice-section">
			<view class="card">
				<view class="section-header">
					<text class="section-title">公告通知</text>
				</view>
				<view class="notice-list">
					<view 
						class="notice-item" 
						v-for="(notice, index) in notices" 
						:key="index"
						@click="showNoticeDetail(notice)"
					>
						<view class="notice-dot" :class="{ urgent: notice.urgent }"></view>
						<view class="notice-content">
							<text class="notice-title">{{ notice.title }}</text>
							<text class="notice-time">{{ notice.time }}</text>
						</view>
						<view class="arrow-right"></view>
					</view>
				</view>
			</view>
		</view>

		<!-- 快捷入口 -->
		<view class="quick-actions">
			<view class="card">
				<view class="section-title">快捷入口</view>
				<view class="action-grid">
					<view class="action-item" @click="goToPage('order')">
						<view class="action-icon" style="background-color: #E8F5E9;">
							<text>🚗</text>
						</view>
						<text class="action-label">接单服务</text>
					</view>
					<view class="action-item" @click="goToPage('settings')">
						<view class="action-icon" style="background-color: #E3F2FD;">
							<text>⚙️</text>
						</view>
						<text class="action-label">接单设置</text>
					</view>
					<view class="action-item" @click="goToPage('profile')">
						<view class="action-icon" style="background-color: #FFF3E0;">
							<text>👤</text>
						</view>
						<text class="action-label">个人中心</text>
					</view>
					<view class="action-item" @click="contactService">
						<view class="action-icon" style="background-color: #FCE4EC;">
							<text>💬</text>
						</view>
						<text class="action-label">联系客服</text>
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
			isOnline: true,
			currentLocation: '北京市朝阳区XX路XX号',
			todayStats: {
				orders: 8,
				income: 586,
				distance: 68.5,
				time: 6.5
			},
			banners: [
				{
					id: 1,
					title: '新司机冲单奖',
					subtitle: '完成10单额外奖励200元',
					image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=ride%20sharing%20driver%20bonus%20promotion%20banner%20green%20theme&image_size=landscape_16_9',
					link: '/pages/activity/activity?id=1'
				},
				{
					id: 2,
					title: '夜间订单翻倍',
					subtitle: '22:00-次日6:00订单佣金翻倍',
					image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=night%20time%20taxi%20service%20promotion%20banner%20dark%20theme&image_size=landscape_16_9',
					link: '/pages/activity/activity?id=2'
				},
				{
					id: 3,
					title: '安全驾驶计划',
					subtitle: '连续30天零违章奖励500元',
					image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=safety%20driving%20campaign%20promotion%20banner%20green%20safety%20theme&image_size=landscape_16_9',
					link: '/pages/activity/activity?id=3'
				}
			],
			incentives: [
				{
					id: 1,
					icon: '🏆',
					title: '冲单奖励',
					description: '每日完成15单，额外奖励150元',
					badge: '进行中',
					badgeType: 'success',
					color: '#4CAF50',
					progress: 60,
					progressText: '已完成9/15单'
				},
				{
					id: 2,
					icon: '⭐',
					title: '五星好评奖',
					description: '本周获得10个五星好评，奖励80元',
					badge: '进行中',
					badgeType: 'warning',
					color: '#FF9800',
					progress: 70,
					progressText: '已获得7/10个好评'
				},
				{
					id: 3,
					icon: '🎯',
					title: '区域冲单',
					description: '在CBD区域完成5单，奖励50元',
					badge: '即将开始',
					badgeType: 'info',
					color: '#2196F3',
					progress: 0,
					progressText: ''
				}
			],
			notices: [
				{
					id: 1,
					title: '关于加强代驾服务规范的通知',
					time: '2026-05-02 10:30',
					urgent: true
				},
				{
					id: 2,
					title: '五一假期订单高峰期提醒',
					time: '2026-05-01 09:00',
					urgent: true
				},
				{
					id: 3,
					title: '系统功能更新公告',
					time: '2026-04-30 16:00',
					urgent: false
				}
			]
		}
	},
	onLoad() {
		this.checkLoginStatus()
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
		refreshData() {
			// 模拟刷新数据
		},
		toggleOnlineStatus(e) {
			this.isOnline = e.detail.value
			if (this.isOnline) {
				uni.showToast({
					title: '已上线，开始接单',
					icon: 'success'
				})
			} else {
				uni.showToast({
					title: '已下线，停止接单',
					icon: 'none'
				})
			}
		},
		clickBanner(banner) {
			uni.showToast({
				title: '查看活动详情',
				icon: 'none'
			})
		},
		showStatDetail(type) {
			const titles = {
				orders: '已接订单',
				income: '今日收入',
				distance: '行驶里程',
				time: '工作时长'
			}
			uni.showToast({
				title: '查看' + titles[type] + '详情',
				icon: 'none'
			})
		},
		showMoreIncentives() {
			uni.showToast({
				title: '查看全部激励活动',
				icon: 'none'
			})
		},
		showIncentiveDetail(item) {
			uni.showModal({
				title: item.title,
				content: item.description + '\n\n' + (item.progressText || '活动即将开始'),
				showCancel: false,
				confirmText: '我知道了'
			})
		},
		showNoticeDetail(notice) {
			uni.showModal({
				title: notice.title,
				content: '这是公告的详细内容。在实际应用中，这里会展示完整的公告内容。',
				showCancel: false,
				confirmText: '我知道了'
			})
		},
		goToPage(page) {
			const pageMap = {
				order: '/pages/order/order',
				settings: '/pages/settings/settings',
				profile: '/pages/profile/profile'
			}
			if (page === 'order' || page === 'profile') {
				uni.switchTab({
					url: pageMap[page]
				})
			} else {
				uni.navigateTo({
					url: pageMap[page]
				})
			}
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
		}
	}
}
</script>

<style scoped>
.index-container {
	min-height: 100vh;
	padding-bottom: 40rpx;
}

/* 顶部状态栏 */
.status-bar {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 30rpx;
	background-color: #FFFFFF;
}

.location-info {
	display: flex;
	align-items: center;
}

.location-icon {
	width: 64rpx;
	height: 64rpx;
	border-radius: 50%;
	background-color: #E8F5E9;
	display: flex;
	align-items: center;
	justify-content: center;
	margin-right: 16rpx;
}

.location-text {
	display: flex;
	flex-direction: column;
}

.location-label {
	font-size: 22rpx;
	color: #999999;
}

.location-address {
	font-size: 26rpx;
	color: #333333;
	max-width: 300rpx;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

.status-badge {
	display: flex;
	align-items: center;
}

.badge-text {
	font-size: 24rpx;
	margin-right: 16rpx;
	padding: 8rpx 20rpx;
	border-radius: 20rpx;
	background-color: #F5F5F5;
	color: #999999;
}

.badge-text.online {
	background-color: #E8F5E9;
	color: #4CAF50;
}

/* 轮播图 */
.banner-section {
	padding: 20rpx 30rpx;
}

.banner-swiper {
	height: 300rpx;
	border-radius: 16rpx;
	overflow: hidden;
}

.banner-item {
	position: relative;
	width: 100%;
	height: 100%;
}

.banner-image {
	width: 100%;
	height: 100%;
}

.banner-overlay {
	position: absolute;
	bottom: 0;
	left: 0;
	right: 0;
	padding: 24rpx;
	background: linear-gradient(transparent, rgba(0, 0, 0, 0.6));
}

.banner-title {
	font-size: 32rpx;
	font-weight: 600;
	color: #FFFFFF;
	display: block;
	margin-bottom: 8rpx;
}

.banner-subtitle {
	font-size: 24rpx;
	color: rgba(255, 255, 255, 0.9);
}

/* 统计区域 */
.stats-section {
	padding: 0 30rpx;
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

/* 激励奖励 */
.incentive-section,
.notice-section,
.quick-actions {
	padding: 0 30rpx;
}

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

.incentive-list {
	display: flex;
	flex-direction: column;
}

.incentive-item {
	display: flex;
	align-items: center;
	padding: 20rpx 0;
	border-bottom: 1rpx solid #F0F0F0;
}

.incentive-item:last-child {
	border-bottom: none;
}

.incentive-icon {
	width: 80rpx;
	height: 80rpx;
	border-radius: 16rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	margin-right: 20rpx;
	font-size: 36rpx;
}

.incentive-content {
	flex: 1;
	display: flex;
	flex-direction: column;
}

.incentive-title-row {
	display: flex;
	align-items: center;
	margin-bottom: 8rpx;
}

.incentive-title {
	font-size: 28rpx;
	font-weight: 500;
	color: #333333;
	margin-right: 12rpx;
}

.incentive-desc {
	font-size: 24rpx;
	color: #666666;
	margin-bottom: 12rpx;
}

.incentive-progress {
	display: flex;
	align-items: center;
}

.progress-bar {
	flex: 1;
	height: 8rpx;
	background-color: #F0F0F0;
	border-radius: 4rpx;
	overflow: hidden;
	margin-right: 16rpx;
}

.progress-fill {
	height: 100%;
	background: linear-gradient(90deg, #4CAF50, #8BC34A);
	border-radius: 4rpx;
	transition: width 0.3s;
}

.progress-text {
	font-size: 22rpx;
	color: #4CAF50;
	white-space: nowrap;
}

/* 公告列表 */
.notice-list {
	display: flex;
	flex-direction: column;
}

.notice-item {
	display: flex;
	align-items: center;
	padding: 20rpx 0;
	border-bottom: 1rpx solid #F0F0F0;
}

.notice-item:last-child {
	border-bottom: none;
}

.notice-dot {
	width: 12rpx;
	height: 12rpx;
	border-radius: 50%;
	background-color: #999999;
	margin-right: 16rpx;
}

.notice-dot.urgent {
	background-color: #F44336;
}

.notice-content {
	flex: 1;
	display: flex;
	flex-direction: column;
}

.notice-title {
	font-size: 28rpx;
	color: #333333;
	margin-bottom: 8rpx;
}

.notice-time {
	font-size: 22rpx;
	color: #999999;
}

/* 快捷入口 */
.action-grid {
	display: flex;
	flex-wrap: wrap;
	margin: 0 -15rpx;
}

.action-item {
	width: 25%;
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 20rpx 15rpx;
}

.action-icon {
	width: 96rpx;
	height: 96rpx;
	border-radius: 20rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	margin-bottom: 16rpx;
	font-size: 44rpx;
}

.action-label {
	font-size: 24rpx;
	color: #666666;
}
</style>
