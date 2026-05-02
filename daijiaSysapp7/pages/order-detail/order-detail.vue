<template>
	<view class="order-detail-container">
		<!-- 订单状态 -->
		<view class="status-section">
			<view class="status-icon">
				<text>{{ statusIcon }}</text>
			</view>
			<view class="status-info">
				<text class="status-title">{{ statusText }}</text>
				<text class="status-desc" v-if="statusDesc">{{ statusDesc }}</text>
			</view>
		</view>

		<!-- 行程信息 -->
		<view class="card">
			<view class="section-title">行程信息</view>
			
			<view class="route-section">
				<view class="route-point start">
					<view class="point-dot start"></view>
					<view class="point-info">
						<text class="point-label">起点</text>
						<text class="point-address">{{ orderDetail.startAddress }}</text>
					</view>
				</view>
				
				<view class="route-line"></view>
				
				<view class="route-point end">
					<view class="point-dot end"></view>
					<view class="point-info">
						<text class="point-label">终点</text>
						<text class="point-address">{{ orderDetail.endAddress }}</text>
					</view>
				</view>
			</view>

			<view class="route-stats">
				<view class="route-stat">
					<text class="stat-label">行驶里程</text>
					<text class="stat-value">{{ orderDetail.distance }}公里</text>
				</view>
				<view class="route-stat">
					<text class="stat-label">预估时长</text>
					<text class="stat-value">{{ orderDetail.estimatedTime }}分钟</text>
				</view>
				<view class="route-stat">
					<text class="stat-label">预估费用</text>
					<text class="stat-value price">¥{{ orderDetail.estimatedFee }}</text>
				</view>
			</view>
		</view>

		<!-- 乘客信息 -->
		<view class="card">
			<view class="section-title">乘客信息</view>
			
			<view class="passenger-info">
				<view class="passenger-avatar">
					<image :src="orderDetail.passengerAvatar" mode="aspectFill"></image>
				</view>
				<view class="passenger-detail">
					<view class="passenger-name-row">
						<text class="passenger-name">{{ orderDetail.passengerName }}</text>
						<view class="credit-stars">
							<text v-for="star in 5" :key="star" :class="{ active: star <= orderDetail.passengerCredit }">★</text>
						</view>
					</view>
					<text class="passenger-phone">{{ orderDetail.passengerPhone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2') }}</text>
					<view class="passenger-tags" v-if="orderDetail.tags && orderDetail.tags.length">
						<view v-for="(tag, index) in orderDetail.tags" :key="index" class="tag" :class="tag.type">
							<text>{{ tag.name }}</text>
						</view>
					</view>
				</view>
				<view class="contact-btn" @click="callPassenger">
					<text>📞</text>
					<text>联系</text>
				</view>
			</view>
		</view>

		<!-- 订单信息 -->
		<view class="card">
			<view class="section-title">订单信息</view>
			
			<view class="info-list">
				<view class="info-item">
					<text class="info-label">订单号</text>
					<text class="info-value">{{ orderDetail.orderNo }}</text>
				</view>
				<view class="info-item">
					<text class="info-label">订单类型</text>
					<text class="info-value">{{ orderDetail.orderType === 'dispatch' ? '系统派单' : '抢单' }}</text>
				</view>
				<view class="info-item">
					<text class="info-label">下单时间</text>
					<text class="info-value">{{ orderDetail.createTime }}</text>
				</view>
				<view class="info-item">
					<text class="info-label">接单时间</text>
					<text class="info-value">{{ orderDetail.acceptTime || '待接单' }}</text>
				</view>
				<view class="info-item" v-if="orderDetail.startTime">
					<text class="info-label">出发时间</text>
					<text class="info-value">{{ orderDetail.startTime }}</text>
				</view>
				<view class="info-item" v-if="orderDetail.endTime">
					<text class="info-label">到达时间</text>
					<text class="info-value">{{ orderDetail.endTime }}</text>
				</view>
			</view>
		</view>

		<!-- 费用明细 -->
		<view class="card" v-if="orderDetail.status === 'completed' || orderDetail.status === 'ongoing'">
			<view class="section-title" @click="toggleFeeDetail">
				<text>费用明细</text>
				<view class="arrow-toggle" :class="{ expanded: showFeeDetail }"></view>
			</view>
			
			<view v-show="showFeeDetail" class="fee-detail">
				<view class="fee-item">
					<text class="fee-label">起步价</text>
					<text class="fee-value">¥{{ orderDetail.feeDetail.startFee }}</text>
				</view>
				<view class="fee-item">
					<text class="fee-label">里程费（{{ orderDetail.feeDetail.distance }}公里）</text>
					<text class="fee-value">¥{{ orderDetail.feeDetail.distanceFee }}</text>
				</view>
				<view class="fee-item" v-if="orderDetail.feeDetail.waitFee > 0">
					<text class="fee-label">等待费（{{ orderDetail.feeDetail.waitMinutes }}分钟）</text>
					<text class="fee-value">¥{{ orderDetail.feeDetail.waitFee }}</text>
				</view>
				<view class="fee-item" v-if="orderDetail.feeDetail.nightFee > 0">
					<text class="fee-label">夜间服务费</text>
					<text class="fee-value">¥{{ orderDetail.feeDetail.nightFee }}</text>
				</view>
				
				<view class="divider"></view>
				
				<view class="fee-item total">
					<text class="fee-label">实付金额</text>
					<text class="fee-value">¥{{ orderDetail.actualFee }}</text>
				</view>
			</view>
		</view>

		<!-- 操作按钮 -->
		<view class="action-section" v-if="orderDetail.status === 'pending'">
			<button class="btn btn-outline action-btn" @click="ignoreOrder">忽略订单</button>
			<button class="btn btn-primary action-btn" @click="acceptOrder">立即接单</button>
		</view>

		<view class="action-section" v-if="orderDetail.status === 'accepted'">
			<button class="btn btn-outline action-btn" @click="refreshRoute">刷新路线</button>
			<button class="btn btn-primary action-btn" @click="startTrip">开始行程</button>
		</view>

		<view class="action-section" v-if="orderDetail.status === 'ongoing'">
			<view class="trip-timer">
				<text class="timer-label">行程用时</text>
				<text class="timer-value">{{ formatDuration(tripDuration) }}</text>
			</view>
			<button class="btn btn-outline action-btn" @click="refreshRoute">刷新路线</button>
			<button class="btn btn-danger action-btn" @click="endTrip">结束行程</button>
		</view>

		<view class="action-section" v-if="orderDetail.status === 'completed'">
			<button class="btn btn-primary action-btn" @click="goToReview">去评价</button>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			orderId: null,
			orderDetail: {
				orderNo: 'DJ202401150001',
				status: 'pending',
				orderType: 'dispatch',
				startAddress: '北京市朝阳区建国路88号SOHO现代城A座',
				endAddress: '北京市海淀区中关村大街1号',
				distance: 18.5,
				estimatedTime: 45,
				estimatedFee: 128,
				actualFee: 132,
				passengerName: '李先生',
				passengerPhone: '13912345678',
				passengerAvatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=professional%20business%20man%20avatar%20portrait%20friendly&image_size=square_hd',
				passengerCredit: 5,
				createTime: '2024-01-15 20:30:00',
				acceptTime: '',
				startTime: '',
				endTime: '',
				tags: [
					{ name: '老用户', type: 'info' },
					{ name: '高信用', type: 'success' }
				],
				feeDetail: {
					startFee: 38,
					distance: 18.5,
					distanceFee: 74,
					waitMinutes: 8,
					waitFee: 12,
					nightFee: 8,
					total: 132
				}
			},
			showFeeDetail: true,
			tripDuration: 0,
			tripTimer: null
		}
	},
	computed: {
		statusIcon() {
			const icons = {
				pending: '⏳',
				accepted: '✅',
				ongoing: '🚗',
				completed: '🎉',
				cancelled: '❌'
			}
			return icons[this.orderDetail.status] || '📋'
		},
		statusText() {
			const texts = {
				pending: '待接单',
				accepted: '已接单',
				ongoing: '行程中',
				completed: '已完成',
				cancelled: '已取消'
			}
			return texts[this.orderDetail.status] || '未知状态'
		},
		statusDesc() {
			const descs = {
				pending: '请在30秒内确认接单',
				accepted: '请尽快前往接驾地点',
				ongoing: '正在为乘客提供代驾服务',
				completed: '订单已完成，感谢您的服务'
			}
			return descs[this.orderDetail.status] || ''
		}
	},
	onLoad(options) {
		if (options.id) {
			this.orderId = options.id
		}
		if (options.status) {
			this.orderDetail.status = options.status
		}
		this.loadOrderDetail()
	},
	onUnload() {
		this.stopTripTimer()
	},
	methods: {
		loadOrderDetail() {
			uni.showLoading({
				title: '加载中...'
			})
			
			setTimeout(() => {
				uni.hideLoading()
				
				if (this.orderDetail.status === 'accepted') {
					this.orderDetail.acceptTime = '2024-01-15 20:31:00'
				} else if (this.orderDetail.status === 'ongoing') {
					this.orderDetail.acceptTime = '2024-01-15 20:31:00'
					this.orderDetail.startTime = '2024-01-15 20:40:00'
					this.startTripTimer()
				} else if (this.orderDetail.status === 'completed') {
					this.orderDetail.acceptTime = '2024-01-15 20:31:00'
					this.orderDetail.startTime = '2024-01-15 20:40:00'
					this.orderDetail.endTime = '2024-01-15 21:25:00'
				}
			}, 500)
		},
		toggleFeeDetail() {
			this.showFeeDetail = !this.showFeeDetail
		},
		callPassenger() {
			uni.makePhoneCall({
				phoneNumber: this.orderDetail.passengerPhone,
				fail: () => {
					uni.showToast({
						title: '拨打电话失败',
						icon: 'none'
					})
				}
			})
		},
		ignoreOrder() {
			uni.showModal({
				title: '提示',
				content: '确定要忽略此订单吗？',
				success: (res) => {
					if (res.confirm) {
						uni.showToast({
							title: '已忽略订单',
							icon: 'success'
						})
						setTimeout(() => {
							uni.navigateBack()
						}, 1000)
					}
				}
			})
		},
		acceptOrder() {
			uni.showLoading({
				title: '接单中...'
			})
			
			setTimeout(() => {
				uni.hideLoading()
				this.orderDetail.status = 'accepted'
				this.orderDetail.acceptTime = this.getCurrentTime()
				
				uni.showToast({
					title: '接单成功',
					icon: 'success'
				})
			}, 1000)
		},
		refreshRoute() {
			uni.showLoading({
				title: '刷新路线...'
			})
			
			setTimeout(() => {
				uni.hideLoading()
				uni.showToast({
					title: '路线已更新',
					icon: 'success'
				})
			}, 1500)
		},
		startTrip() {
			uni.showModal({
				title: '确认开始行程',
				content: '请确认乘客已上车，是否开始行程？',
				success: (res) => {
					if (res.confirm) {
						this.orderDetail.status = 'ongoing'
						this.orderDetail.startTime = this.getCurrentTime()
						this.startTripTimer()
						
						uni.showToast({
							title: '行程已开始',
							icon: 'success'
						})
					}
				}
			})
		},
		endTrip() {
			uni.showModal({
				title: '确认结束行程',
				content: '请确认已到达目的地，是否结束行程？',
				confirmText: '确认到达',
				success: (res) => {
					if (res.confirm) {
						this.stopTripTimer()
						this.orderDetail.status = 'completed'
						this.orderDetail.endTime = this.getCurrentTime()
						
						uni.showToast({
							title: '行程已结束',
							icon: 'success'
						})
					}
				}
			})
		},
		startTripTimer() {
			this.tripTimer = setInterval(() => {
				this.tripDuration++
			}, 1000)
		},
		stopTripTimer() {
			if (this.tripTimer) {
				clearInterval(this.tripTimer)
				this.tripTimer = null
			}
		},
		formatDuration(seconds) {
			const hours = Math.floor(seconds / 3600)
			const minutes = Math.floor((seconds % 3600) / 60)
			const secs = seconds % 60
			
			if (hours > 0) {
				return `${hours}时${minutes}分${secs}秒`
			} else {
				return `${minutes}分${secs}秒`
			}
		},
		getCurrentTime() {
			const now = new Date()
			const year = now.getFullYear()
			const month = String(now.getMonth() + 1).padStart(2, '0')
			const day = String(now.getDate()).padStart(2, '0')
			const hours = String(now.getHours()).padStart(2, '0')
			const minutes = String(now.getMinutes()).padStart(2, '0')
			const seconds = String(now.getSeconds()).padStart(2, '0')
			return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
		},
		goToReview() {
			uni.navigateTo({
				url: '/pages/review/review?orderId=' + this.orderId
			})
		}
	}
}
</script>

<style scoped>
.order-detail-container {
	min-height: 100vh;
	background-color: #F5F5F5;
	padding-bottom: 140rpx;
}

/* 订单状态 */
.status-section {
	background: linear-gradient(135deg, #4CAF50, #8BC34A);
	padding: 40rpx 30rpx;
	display: flex;
	align-items: center;
}

.status-icon {
	width: 100rpx;
	height: 100rpx;
	background-color: rgba(255, 255, 255, 0.2);
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 48rpx;
	margin-right: 24rpx;
}

.status-info {
	flex: 1;
	display: flex;
	flex-direction: column;
}

.status-title {
	font-size: 36rpx;
	font-weight: 600;
	color: #FFFFFF;
	margin-bottom: 8rpx;
}

.status-desc {
	font-size: 24rpx;
	color: rgba(255, 255, 255, 0.8);
}

/* 行程信息 */
.route-section {
	display: flex;
	flex-direction: column;
	margin-bottom: 24rpx;
}

.route-point {
	display: flex;
	align-items: flex-start;
	position: relative;
	padding-left: 40rpx;
}

.point-dot {
	position: absolute;
	left: 0;
	top: 8rpx;
	width: 20rpx;
	height: 20rpx;
	border-radius: 50%;
}

.point-dot.start {
	background-color: #4CAF50;
}

.point-dot.end {
	background-color: #F44336;
}

.point-info {
	flex: 1;
	padding-bottom: 24rpx;
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

.route-line {
	position: absolute;
	left: 9rpx;
	top: 40rpx;
	width: 2rpx;
	height: 60rpx;
	background-color: #E0E0E0;
}

.route-stats {
	display: flex;
	justify-content: space-around;
	padding-top: 24rpx;
	border-top: 1rpx solid #E0E0E0;
}

.route-stat {
	display: flex;
	flex-direction: column;
	align-items: center;
}

.route-stat .stat-label {
	font-size: 22rpx;
	color: #999999;
	margin-bottom: 8rpx;
}

.route-stat .stat-value {
	font-size: 32rpx;
	font-weight: 600;
	color: #333333;
}

.route-stat .stat-value.price {
	color: #F44336;
}

/* 乘客信息 */
.passenger-info {
	display: flex;
	align-items: center;
}

.passenger-avatar {
	width: 100rpx;
	height: 100rpx;
	border-radius: 50%;
	overflow: hidden;
	margin-right: 20rpx;
}

.passenger-avatar image {
	width: 100%;
	height: 100%;
}

.passenger-detail {
	flex: 1;
	display: flex;
	flex-direction: column;
}

.passenger-name-row {
	display: flex;
	align-items: center;
	margin-bottom: 8rpx;
}

.passenger-name {
	font-size: 32rpx;
	font-weight: 600;
	color: #333333;
	margin-right: 16rpx;
}

.credit-stars {
	font-size: 24rpx;
}

.credit-stars text {
	color: #E0E0E0;
}

.credit-stars text.active {
	color: #FFD700;
}

.passenger-phone {
	font-size: 26rpx;
	color: #666666;
	margin-bottom: 12rpx;
}

.passenger-tags {
	display: flex;
	gap: 12rpx;
	flex-wrap: wrap;
}

.tag {
	padding: 4rpx 16rpx;
	border-radius: 20rpx;
	font-size: 20rpx;
}

.tag.info {
	background-color: #E3F2FD;
	color: #2196F3;
}

.tag.success {
	background-color: #E8F5E9;
	color: #4CAF50;
}

.tag.warning {
	background-color: #FFF3E0;
	color: #FF9800;
}

.contact-btn {
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 16rpx 24rpx;
	background-color: #E8F5E9;
	border-radius: 12rpx;
}

.contact-btn text:first-child {
	font-size: 32rpx;
	margin-bottom: 4rpx;
}

.contact-btn text:last-child {
	font-size: 22rpx;
	color: #4CAF50;
}

/* 订单信息 */
.info-list {
	display: flex;
	flex-direction: column;
}

.info-item {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 16rpx 0;
	border-bottom: 1rpx solid #E0E0E0;
}

.info-item:last-child {
	border-bottom: none;
}

.info-label {
	font-size: 26rpx;
	color: #666666;
}

.info-value {
	font-size: 26rpx;
	color: #333333;
}

/* 费用明细 */
.section-title {
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.arrow-toggle {
	width: 20rpx;
	height: 20rpx;
	border-top: 3rpx solid #999999;
	border-right: 3rpx solid #999999;
	transform: rotate(135deg);
	transition: transform 0.3s ease;
}

.arrow-toggle.expanded {
	transform: rotate(-45deg);
}

.fee-detail {
	margin-top: 20rpx;
}

.fee-item {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 12rpx 0;
}

.fee-label {
	font-size: 26rpx;
	color: #666666;
}

.fee-value {
	font-size: 26rpx;
	color: #333333;
}

.fee-item.total {
	padding-top: 16rpx;
}

.fee-item.total .fee-label {
	font-size: 30rpx;
	font-weight: 600;
	color: #333333;
}

.fee-item.total .fee-value {
	font-size: 36rpx;
	font-weight: 600;
	color: #F44336;
}

/* 行程计时器 */
.trip-timer {
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 20rpx;
	background-color: #FFF3E0;
	border-radius: 12rpx;
	margin-bottom: 20rpx;
}

.timer-label {
	font-size: 24rpx;
	color: #FF9800;
	margin-bottom: 8rpx;
}

.timer-value {
	font-size: 40rpx;
	font-weight: 600;
	color: #FF9800;
	font-family: 'Courier New', monospace;
}

/* 操作按钮 */
.action-section {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	display: flex;
	gap: 20rpx;
	padding: 20rpx 30rpx;
	background-color: #FFFFFF;
	border-top: 1rpx solid #E0E0E0;
	padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
}

.action-btn {
	flex: 1;
}
</style>
