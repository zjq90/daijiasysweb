<template>
	<view class="order-container">
		<!-- 顶部状态栏 -->
		<view class="status-header">
			<view class="header-left">
				<view class="status-dot" :class="{ online: isOnline }"></view>
				<text class="status-text">{{ isOnline ? '在线接单中' : '休息中' }}</text>
			</view>
			<view class="header-right" @click="goToSettings">
				<text class="settings-text">设置</text>
				<view class="arrow-right"></view>
			</view>
		</view>

		<!-- 今日接单统计 -->
		<view class="today-stats">
			<view class="card">
				<view class="stats-row">
					<view class="stat-item">
						<text class="stat-num">{{ todayOrders }}/{{ dailyLimit }}</text>
						<text class="stat-label">今日已接</text>
					</view>
					<view class="stat-divider"></view>
					<view class="stat-item">
						<text class="stat-num">{{ workHours }}h</text>
						<text class="stat-label">今日工作时长</text>
					</view>
					<view class="stat-divider"></view>
					<view class="stat-item">
						<text class="stat-num">¥{{ todayIncome }}</text>
						<text class="stat-label">今日收入</text>
					</view>
				</view>
				<view v-if="showRestReminder" class="rest-reminder">
					<text class="reminder-icon">⚠️</text>
					<text class="reminder-text">您已连续工作{{ consecutiveHours }}小时，请注意休息！</text>
				</view>
			</view>
		</view>

		<!-- 订单推送弹窗（派单/抢单） -->
		<view v-if="showOrderPopup" class="order-popup">
			<view class="popup-mask" @click="ignoreOrder"></view>
			<view class="popup-content">
				<view class="popup-header">
					<view class="order-type-badge" :class="currentOrder.type === 'dispatch' ? 'dispatch' : 'grab'">
						{{ currentOrder.type === 'dispatch' ? '派单' : '抢单' }}
					</view>
					<view class="countdown-timer">
						<text>{{ countdown }}</text>
						<text class="countdown-unit">s</text>
					</view>
				</view>

				<view class="order-info-section">
					<view class="location-row start">
						<view class="location-dot start"></view>
						<view class="location-content">
							<text class="location-label">起点</text>
							<text class="location-address">{{ currentOrder.startAddress }}</text>
						</view>
					</view>
					<view class="location-line"></view>
					<view class="location-row end">
						<view class="location-dot end"></view>
						<view class="location-content">
							<text class="location-label">终点</text>
							<text class="location-address">{{ currentOrder.endAddress }}</text>
						</view>
					</view>
				</view>

				<view class="order-detail-grid">
					<view class="detail-item">
						<text class="detail-label">预估费用</text>
						<text class="detail-value highlight">¥{{ currentOrder.estimatedCost }}</text>
					</view>
					<view class="detail-item">
						<text class="detail-label">行驶里程</text>
						<text class="detail-value">{{ currentOrder.distance }}km</text>
					</view>
					<view class="detail-item">
						<text class="detail-label">客户距离</text>
						<text class="detail-value">{{ currentOrder.customerDistance }}km</text>
					</view>
					<view class="detail-item">
						<text class="detail-label">信用等级</text>
						<view class="credit-stars">
							<text v-for="star in 5" :key="star" :class="{ active: star <= currentOrder.creditLevel }">★</text>
						</view>
					</view>
				</view>

				<view class="popup-actions">
					<button class="btn btn-outline ignore-btn" @click="ignoreOrder">忽略</button>
					<button class="btn btn-primary accept-btn" @click="acceptOrder">
						{{ currentOrder.type === 'dispatch' ? '立即接单' : '立即抢单' }}
					</button>
				</view>
			</view>
		</view>

		<!-- 行程中状态 -->
		<view v-else-if="inTrip" class="trip-status">
			<view class="card trip-card">
				<view class="trip-header">
					<text class="trip-status-text">行程进行中</text>
					<view class="trip-time">
						<text class="trip-time-label">已用时</text>
						<text class="trip-time-value">{{ tripDuration }}</text>
					</view>
				</view>

				<view class="trip-route">
					<view class="route-point start">
						<view class="point-dot start"></view>
						<view class="point-content">
							<text class="point-label">出发地</text>
							<text class="point-address">{{ currentTrip.startAddress }}</text>
						</view>
					</view>
					<view class="route-path">
						<view class="path-line"></view>
						<view class="path-distance">
							<text>{{ currentTrip.distance }}km</text>
						</view>
					</view>
					<view class="route-point end">
						<view class="point-dot end"></view>
						<view class="point-content">
							<text class="point-label">目的地</text>
							<text class="point-address">{{ currentTrip.endAddress }}</text>
						</view>
					</view>
				</view>

				<view class="trip-stats">
					<view class="trip-stat">
						<text class="trip-stat-label">预估费用</text>
						<text class="trip-stat-value">¥{{ currentTrip.estimatedCost }}</text>
					</view>
					<view class="trip-stat">
						<text class="trip-stat-label">剩余距离</text>
						<text class="trip-stat-value">{{ currentTrip.remainingDistance }}km</text>
					</view>
					<view class="trip-stat">
						<text class="trip-stat-label">预计到达</text>
						<text class="trip-stat-value">{{ currentTrip.estimatedArrival }}</text>
					</view>
				</view>

				<view class="trip-actions">
					<button class="btn btn-outline refresh-btn" @click="refreshRoute">
						<text>🔄</text>
						<text>刷新路线</text>
					</button>
					<button class="btn btn-danger end-trip-btn" @click="showEndTripConfirm">
						结束行程
					</button>
				</view>
			</view>

			<!-- 导航地图区域（模拟） -->
			<view class="map-container">
				<view class="map-placeholder">
					<text class="map-icon">🗺️</text>
					<text class="map-text">导航地图区域</text>
				</view>
			</view>
		</view>

		<!-- 空闲状态 -->
		<view v-else class="idle-state">
			<!-- 地图区域（模拟） -->
			<view class="map-container">
				<view class="map-placeholder">
					<text class="map-icon">📍</text>
					<text class="map-text">{{ isOnline ? '正在监听订单...' : '已停止接单' }}</text>
				</view>
			</view>

			<!-- 在线/离线切换 -->
			<view class="online-toggle-section">
				<view class="card">
					<view class="toggle-row">
						<view class="toggle-info">
							<text class="toggle-title">{{ isOnline ? '正在接单' : '休息中' }}</text>
							<text class="toggle-desc">
								{{ isOnline ? '系统将为您推送附近订单' : '开启后可接收订单推送' }}
							</text>
						</view>
						<switch 
							:checked="isOnline" 
							@change="toggleOnlineStatus"
							color="#4CAF50"
							style="transform: scale(1.2);"
						/>
					</view>
				</view>
			</view>

			<!-- 待处理订单列表 -->
			<view class="pending-orders">
				<view class="card">
					<view class="section-header">
						<text class="section-title">待处理订单</text>
						<text class="more-link" @click="showAllOrders">全部订单 ></text>
					</view>

					<view v-if="pendingOrders.length > 0" class="pending-list">
						<view 
							class="pending-item" 
							v-for="(order, index) in pendingOrders" 
							:key="index"
							@click="viewOrderDetail(order)"
						>
							<view class="pending-order-info">
								<view class="order-locations">
									<view class="loc-row">
										<view class="loc-dot start"></view>
										<text class="loc-text">{{ order.startAddress }}</text>
									</view>
									<view class="loc-arrow">↓</view>
									<view class="loc-row">
										<view class="loc-dot end"></view>
										<text class="loc-text">{{ order.endAddress }}</text>
									</view>
								</view>
								<view class="order-meta">
									<text class="order-time">{{ order.createTime }}</text>
									<text class="order-price">¥{{ order.estimatedCost }}</text>
								</view>
							</view>
							<view class="order-status-badge" :class="'status-' + order.status">
								{{ order.statusText }}
							</view>
						</view>
					</view>

					<view v-else class="empty-state">
						<text class="empty-icon">📋</text>
						<text class="empty-text">暂无待处理订单</text>
					</view>
				</view>
			</view>

			<!-- 快捷操作 -->
			<view class="quick-actions">
				<view class="card">
					<view class="action-grid">
						<view class="action-item" @click="goToSettings">
							<view class="action-icon" style="background-color: #E8F5E9;">
								<text>⚙️</text>
							</view>
							<text class="action-label">接单设置</text>
						</view>
						<view class="action-item" @click="showOrderHistory">
							<view class="action-icon" style="background-color: #E3F2FD;">
								<text>📊</text>
							</view>
							<text class="action-label">订单历史</text>
						</view>
						<view class="action-item" @click="contactCustomer">
							<view class="action-icon" style="background-color: #FFF3E0;">
								<text>📞</text>
							</view>
							<text class="action-label">联系乘客</text>
						</view>
						<view class="action-item" @click="emergencyHelp">
							<view class="action-icon" style="background-color: #FFEBEE;">
								<text>🚨</text>
							</view>
							<text class="action-label">紧急求助</text>
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
			isOnline: true,
			dailyLimit: 15,
			todayOrders: 8,
			workHours: 6.5,
			consecutiveHours: 4,
			todayIncome: 586,
			showRestReminder: true,
			
			// 订单推送弹窗
			showOrderPopup: false,
			currentOrder: null,
			countdown: 30,
			countdownTimer: null,
			
			// 行程中状态
			inTrip: false,
			currentTrip: null,
			tripDuration: '00:15:30',
			tripTimer: null,
			
			// 待处理订单
			pendingOrders: [
				{
					id: 1,
					startAddress: '朝阳区建国路88号SOHO现代城',
					endAddress: '海淀区中关村大街1号',
					estimatedCost: 85,
					distance: 18.5,
					createTime: '10分钟前',
					status: 'accepted',
					statusText: '已接单'
				},
				{
					id: 2,
					startAddress: '东城区王府井大街138号',
					endAddress: '西城区西单北大街120号',
					estimatedCost: 45,
					distance: 8.2,
					createTime: '25分钟前',
					status: 'completed',
					statusText: '已完成'
				}
			]
		}
	},
	onLoad() {
		this.checkLoginStatus()
	},
	onShow() {
		if (this.isOnline && !this.inTrip) {
			this.startListenOrders()
		}
	},
	onUnload() {
		this.clearTimers()
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
		clearTimers() {
			if (this.countdownTimer) {
				clearInterval(this.countdownTimer)
				this.countdownTimer = null
			}
			if (this.tripTimer) {
				clearInterval(this.tripTimer)
				this.tripTimer = null
			}
		},
		toggleOnlineStatus(e) {
			this.isOnline = e.detail.value
			if (this.isOnline) {
				uni.showToast({
					title: '已上线，开始接单',
					icon: 'success'
				})
				this.startListenOrders()
			} else {
				uni.showToast({
					title: '已下线，停止接单',
					icon: 'none'
				})
			}
		},
		startListenOrders() {
			// 模拟30秒后推送订单
			setTimeout(() => {
				if (this.isOnline && !this.inTrip && !this.showOrderPopup) {
					this.pushNewOrder()
				}
			}, 5000)
		},
		pushNewOrder() {
			const orderTypes = ['dispatch', 'grab']
			const type = orderTypes[Math.floor(Math.random() * orderTypes.length)]
			
			this.currentOrder = {
				id: Date.now(),
				type: type,
				startAddress: '朝阳区建国路88号SOHO现代城A座',
				endAddress: '海淀区中关村大街1号科技大厦',
				estimatedCost: (60 + Math.random() * 50).toFixed(0),
				distance: (10 + Math.random() * 15).toFixed(1),
				customerDistance: (1 + Math.random() * 2).toFixed(1),
				creditLevel: Math.floor(Math.random() * 3) + 3
			}
			
			this.showOrderPopup = true
			this.countdown = 30
			
			// 开始倒计时
			this.countdownTimer = setInterval(() => {
				this.countdown--
				if (this.countdown <= 0) {
					this.ignoreOrder()
				}
			}, 1000)
		},
		ignoreOrder() {
			this.clearTimers()
			this.showOrderPopup = false
			uni.showToast({
				title: '已忽略订单',
				icon: 'none'
			})
			
			// 继续监听订单
			if (this.isOnline) {
				this.startListenOrders()
			}
		},
		acceptOrder() {
			if (this.todayOrders >= this.dailyLimit) {
				uni.showModal({
					title: '提示',
					content: '您今日已达接单上限，是否继续接单？',
					confirmText: '继续',
					success: (res) => {
						if (res.confirm) {
							this.doAcceptOrder()
						}
					}
				})
				return
			}
			this.doAcceptOrder()
		},
		async doAcceptOrder() {
			this.clearTimers()
			
			uni.showLoading({
				title: '接单中...'
			})
			
			await new Promise(resolve => setTimeout(resolve, 1500))
			
			uni.hideLoading()
			
			// 模拟接单成功
			this.showOrderPopup = false
			this.todayOrders++
			
			// 进入行程中状态
			this.inTrip = true
			this.currentTrip = {
				...this.currentOrder,
				remainingDistance: this.currentOrder.distance,
				estimatedArrival: '预计25分钟后到达'
			}
			
			// 开始行程计时
			let seconds = 0
			this.tripTimer = setInterval(() => {
				seconds++
				const h = Math.floor(seconds / 3600).toString().padStart(2, '0')
				const m = Math.floor((seconds % 3600) / 60).toString().padStart(2, '0')
				const s = (seconds % 60).toString().padStart(2, '0')
				this.tripDuration = `${h}:${m}:${s}`
			}, 1000)
			
			uni.showToast({
				title: '接单成功，已通知用户',
				icon: 'success',
				duration: 2000
			})
		},
		refreshRoute() {
			uni.showLoading({
				title: '刷新路线中...'
			})
			
			setTimeout(() => {
				uni.hideLoading()
				uni.showToast({
					title: '路线已更新',
					icon: 'success'
				})
			}, 1500)
		},
		showEndTripConfirm() {
			uni.showModal({
				title: '确认结束行程',
				content: '请确认乘客已安全到达目的地',
				confirmText: '确认结束',
				success: (res) => {
					if (res.confirm) {
						this.endTrip()
					}
				}
			})
		},
		endTrip() {
			this.clearTimers()
			this.inTrip = false
			this.currentTrip = null
			
			uni.showToast({
				title: '行程已结束',
				icon: 'success'
			})
			
			// 继续监听订单
			if (this.isOnline) {
				this.startListenOrders()
			}
		},
		goToSettings() {
			uni.navigateTo({
				url: '/pages/settings/settings'
			})
		},
		showAllOrders() {
			uni.showToast({
				title: '查看全部订单',
				icon: 'none'
			})
		},
		viewOrderDetail(order) {
			uni.navigateTo({
				url: `/pages/order-detail/order-detail?id=${order.id}`
			})
		},
		showOrderHistory() {
			uni.showToast({
				title: '订单历史',
				icon: 'none'
			})
		},
		contactCustomer() {
			uni.showModal({
				title: '联系乘客',
				content: '乘客电话：138****8888',
				confirmText: '拨打电话',
				cancelText: '取消',
				success: (res) => {
					if (res.confirm) {
						uni.makePhoneCall({
							phoneNumber: '13888888888'
						})
					}
				}
			})
		},
		emergencyHelp() {
			uni.navigateTo({
				url: '/pages/emergency/emergency'
			})
		}
	}
}
</script>

<style scoped>
.order-container {
	min-height: 100vh;
	background-color: #F5F5F5;
	padding-bottom: 40rpx;
}

/* 顶部状态栏 */
.status-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 30rpx;
	background-color: #FFFFFF;
}

.header-left {
	display: flex;
	align-items: center;
}

.status-dot {
	width: 16rpx;
	height: 16rpx;
	border-radius: 50%;
	background-color: #999999;
	margin-right: 12rpx;
}

.status-dot.online {
	background-color: #4CAF50;
	box-shadow: 0 0 0 4rpx rgba(76, 175, 80, 0.3);
}

.status-text {
	font-size: 30rpx;
	font-weight: 500;
	color: #333333;
}

.header-right {
	display: flex;
	align-items: center;
}

.settings-text {
	font-size: 26rpx;
	color: #4CAF50;
	margin-right: 8rpx;
}

/* 今日统计 */
.today-stats {
	padding: 0 30rpx;
	margin-top: 20rpx;
}

.stats-row {
	display: flex;
	align-items: center;
	justify-content: space-between;
}

.stat-item {
	flex: 1;
	display: flex;
	flex-direction: column;
	align-items: center;
}

.stat-num {
	font-size: 36rpx;
	font-weight: 600;
	color: #4CAF50;
	margin-bottom: 8rpx;
}

.stat-label {
	font-size: 24rpx;
	color: #999999;
}

.stat-divider {
	width: 1rpx;
	height: 60rpx;
	background-color: #E0E0E0;
}

.rest-reminder {
	display: flex;
	align-items: center;
	margin-top: 20rpx;
	padding: 20rpx;
	background-color: #FFF3E0;
	border-radius: 12rpx;
}

.reminder-icon {
	font-size: 32rpx;
	margin-right: 12rpx;
}

.reminder-text {
	font-size: 24rpx;
	color: #E65100;
	flex: 1;
}

/* 订单推送弹窗 */
.order-popup {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	z-index: 999;
}

.popup-mask {
	position: absolute;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background-color: rgba(0, 0, 0, 0.6);
}

.popup-content {
	position: absolute;
	left: 40rpx;
	right: 40rpx;
	top: 50%;
	transform: translateY(-50%);
	background-color: #FFFFFF;
	border-radius: 24rpx;
	overflow: hidden;
	animation: popupIn 0.3s ease-out;
}

@keyframes popupIn {
	from {
		opacity: 0;
		transform: translateY(-40%) scale(0.9);
	}
	to {
		opacity: 1;
		transform: translateY(-50%) scale(1);
	}
}

.popup-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 30rpx;
	background: linear-gradient(135deg, #4CAF50, #8BC34A);
}

.order-type-badge {
	padding: 8rpx 24rpx;
	border-radius: 20rpx;
	font-size: 24rpx;
	font-weight: 500;
}

.order-type-badge.dispatch {
	background-color: #FFFFFF;
	color: #4CAF50;
}

.order-type-badge.grab {
	background-color: #FFEB3B;
	color: #F57F17;
}

.countdown-timer {
	display: flex;
	align-items: baseline;
}

.countdown-timer text:first-child {
	font-size: 48rpx;
	font-weight: 600;
	color: #FFFFFF;
}

.countdown-unit {
	font-size: 24rpx;
	color: rgba(255, 255, 255, 0.8);
	margin-left: 4rpx;
}

.order-info-section {
	padding: 30rpx;
	border-bottom: 1rpx solid #F0F0F0;
}

.location-row {
	display: flex;
	align-items: flex-start;
}

.location-dot {
	width: 16rpx;
	height: 16rpx;
	border-radius: 50%;
	margin-right: 16rpx;
	margin-top: 8rpx;
	flex-shrink: 0;
}

.location-dot.start {
	background-color: #4CAF50;
}

.location-dot.end {
	background-color: #F44336;
}

.location-content {
	flex: 1;
	display: flex;
	flex-direction: column;
}

.location-label {
	font-size: 22rpx;
	color: #999999;
	margin-bottom: 4rpx;
}

.location-address {
	font-size: 28rpx;
	color: #333333;
	line-height: 1.5;
}

.location-line {
	width: 2rpx;
	height: 40rpx;
	background-color: #E0E0E0;
	margin-left: 7rpx;
}

.order-detail-grid {
	display: flex;
	flex-wrap: wrap;
	padding: 20rpx 30rpx;
}

.detail-item {
	width: 50%;
	padding: 16rpx 0;
}

.detail-label {
	font-size: 24rpx;
	color: #999999;
	display: block;
	margin-bottom: 8rpx;
}

.detail-value {
	font-size: 28rpx;
	color: #333333;
}

.detail-value.highlight {
	color: #4CAF50;
	font-weight: 600;
}

.credit-stars {
	font-size: 28rpx;
}

.credit-stars text {
	color: #E0E0E0;
}

.credit-stars text.active {
	color: #FFD700;
}

.popup-actions {
	display: flex;
	padding: 30rpx;
	gap: 20rpx;
}

.popup-actions .btn {
	flex: 1;
}

.ignore-btn,
.accept-btn {
	margin: 0;
}

/* 行程中状态 */
.trip-status {
	padding: 0 30rpx;
}

.trip-card {
	margin-top: 20rpx;
}

.trip-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding-bottom: 24rpx;
	border-bottom: 1rpx solid #F0F0F0;
	margin-bottom: 24rpx;
}

.trip-status-text {
	font-size: 32rpx;
	font-weight: 600;
	color: #4CAF50;
}

.trip-time {
	display: flex;
	flex-direction: column;
	align-items: flex-end;
}

.trip-time-label {
	font-size: 22rpx;
	color: #999999;
	margin-bottom: 4rpx;
}

.trip-time-value {
	font-size: 32rpx;
	font-weight: 600;
	color: #333333;
}

.trip-route {
	margin-bottom: 24rpx;
}

.route-point {
	display: flex;
	align-items: flex-start;
}

.point-dot {
	width: 20rpx;
	height: 20rpx;
	border-radius: 50%;
	margin-right: 20rpx;
	margin-top: 6rpx;
	flex-shrink: 0;
}

.point-dot.start {
	background-color: #4CAF50;
	box-shadow: 0 0 0 4rpx rgba(76, 175, 80, 0.3);
}

.point-dot.end {
	background-color: #F44336;
	box-shadow: 0 0 0 4rpx rgba(244, 67, 54, 0.3);
}

.point-content {
	flex: 1;
}

.point-label {
	font-size: 22rpx;
	color: #999999;
	display: block;
	margin-bottom: 4rpx;
}

.point-address {
	font-size: 28rpx;
	color: #333333;
	line-height: 1.5;
}

.route-path {
	display: flex;
	align-items: center;
	padding-left: 9rpx;
	margin: 8rpx 0;
}

.path-line {
	flex: 1;
	height: 2rpx;
	background: repeating-linear-gradient(
		90deg,
		#E0E0E0,
		#E0E0E0 10rpx,
		transparent 10rpx,
		transparent 20rpx
	);
}

.path-distance {
	font-size: 22rpx;
	color: #999999;
	margin-left: 16rpx;
}

.trip-stats {
	display: flex;
	justify-content: space-around;
	padding: 24rpx 0;
	background-color: #F8F9FA;
	border-radius: 16rpx;
	margin-bottom: 24rpx;
}

.trip-stat {
	display: flex;
	flex-direction: column;
	align-items: center;
}

.trip-stat-label {
	font-size: 22rpx;
	color: #999999;
	margin-bottom: 8rpx;
}

.trip-stat-value {
	font-size: 28rpx;
	font-weight: 500;
	color: #333333;
}

.trip-actions {
	display: flex;
	gap: 20rpx;
}

.refresh-btn,
.end-trip-btn {
	flex: 1;
	margin: 0;
}

.refresh-btn text:first-child {
	margin-right: 8rpx;
}

/* 地图容器 */
.map-container {
	height: 400rpx;
	margin: 20rpx 0;
	background-color: #FFFFFF;
	border-radius: 16rpx;
	overflow: hidden;
}

.map-placeholder {
	width: 100%;
	height: 100%;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	background: linear-gradient(180deg, #E8F5E9 0%, #FFFFFF 100%);
}

.map-icon {
	font-size: 80rpx;
	margin-bottom: 16rpx;
}

.map-text {
	font-size: 26rpx;
	color: #999999;
}

/* 空闲状态 */
.idle-state {
	padding: 0 30rpx;
}

.online-toggle-section {
	margin-top: 20rpx;
}

.toggle-row {
	display: flex;
	align-items: center;
	justify-content: space-between;
}

.toggle-info {
	flex: 1;
}

.toggle-title {
	font-size: 30rpx;
	font-weight: 500;
	color: #333333;
	display: block;
	margin-bottom: 8rpx;
}

.toggle-desc {
	font-size: 24rpx;
	color: #999999;
}

/* 待处理订单 */
.pending-orders {
	margin-top: 20rpx;
}

.pending-list {
	display: flex;
	flex-direction: column;
}

.pending-item {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 20rpx 0;
	border-bottom: 1rpx solid #F0F0F0;
}

.pending-item:last-child {
	border-bottom: none;
}

.pending-order-info {
	flex: 1;
}

.order-locations {
	margin-bottom: 12rpx;
}

.loc-row {
	display: flex;
	align-items: center;
	margin-bottom: 8rpx;
}

.loc-row:last-child {
	margin-bottom: 0;
}

.loc-dot {
	width: 12rpx;
	height: 12rpx;
	border-radius: 50%;
	margin-right: 12rpx;
}

.loc-dot.start {
	background-color: #4CAF50;
}

.loc-dot.end {
	background-color: #F44336;
}

.loc-text {
	font-size: 26rpx;
	color: #333333;
	flex: 1;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

.loc-arrow {
	font-size: 20rpx;
	color: #999999;
	padding-left: 24rpx;
}

.order-meta {
	display: flex;
	align-items: center;
	justify-content: space-between;
}

.order-time {
	font-size: 22rpx;
	color: #999999;
}

.order-price {
	font-size: 28rpx;
	font-weight: 600;
	color: #4CAF50;
}

.order-status-badge {
	padding: 8rpx 20rpx;
	border-radius: 20rpx;
	font-size: 22rpx;
	margin-left: 20rpx;
}

.order-status-badge.status-accepted {
	background-color: #E8F5E9;
	color: #4CAF50;
}

.order-status-badge.status-completed {
	background-color: #E3F2FD;
	color: #2196F3;
}

/* 快捷操作 */
.quick-actions {
	margin-top: 20rpx;
}

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
