<template>
	<view class="settings-container">
		<!-- 接单状态 -->
		<view class="card">
			<view class="section-title">接单状态</view>
			<view class="list-item">
				<view class="list-item-left">
					<view class="item-icon" style="background-color: #E8F5E9;">
						<text>🚗</text>
					</view>
					<text class="list-item-text">开始接单</text>
				</view>
				<view class="list-item-right">
					<switch :checked="isOnline" @change="toggleOnline" color="#4CAF50" />
				</view>
			</view>
			<view class="status-desc">
				<text class="desc-text">{{ isOnline ? '您当前处于在线状态，可以接收订单推送' : '您当前处于休息状态，暂时无法接收订单' }}</text>
			</view>
		</view>

		<!-- 接单设置 -->
		<view class="card">
			<view class="section-title">接单设置</view>
			
			<view class="list-item">
				<view class="list-item-left">
					<view class="item-icon" style="background-color: #FFF3E0;">
						<text>📊</text>
					</view>
					<text class="list-item-text">每日接单上限</text>
				</view>
				<view class="list-item-right">
					<text class="value-text">{{ dailyOrderLimit }}单</text>
					<view class="arrow-right"></view>
				</view>
			</view>

			<view class="limit-setting">
				<view class="limit-row">
					<text class="limit-label">设置上限：</text>
					<view class="limit-buttons">
						<view class="limit-btn" :class="{ active: dailyOrderLimit === 10 }" @click="setLimit(10)">
							<text>10单</text>
						</view>
						<view class="limit-btn" :class="{ active: dailyOrderLimit === 15 }" @click="setLimit(15)">
							<text>15单</text>
						</view>
						<view class="limit-btn" :class="{ active: dailyOrderLimit === 20 }" @click="setLimit(20)">
							<text>20单</text>
						</view>
						<view class="limit-btn" :class="{ active: dailyOrderLimit === 0 }" @click="setLimit(0)">
							<text>不限</text>
						</view>
					</view>
				</view>
				<view class="limit-info">
					<text class="info-text">今日已接：{{ todayOrders }}单，剩余可接：{{ dailyOrderLimit === 0 ? '无限制' : (dailyOrderLimit - todayOrders) + '单' }}</text>
				</view>
			</view>

			<view class="divider"></view>

			<view class="list-item">
				<view class="list-item-left">
					<view class="item-icon" style="background-color: #E3F2FD;">
						<text>⏰</text>
					</view>
					<text class="list-item-text">休息提醒</text>
				</view>
				<view class="list-item-right">
					<switch :checked="restReminderEnabled" @change="toggleRestReminder" color="#4CAF50" />
				</view>
			</view>

			<view v-if="restReminderEnabled" class="reminder-setting">
				<view class="reminder-row">
					<text class="reminder-label">连续工作：</text>
					<picker mode="selector" :range="workHoursOptions" :value="workHourIndex" @change="onWorkHourChange">
						<view class="picker-value">
							<text>{{ workHoursOptions[workHourIndex] }}小时</text>
							<view class="arrow-right small"></view>
						</view>
					</picker>
					<text class="reminder-desc">后提醒休息</text>
				</view>
				<view class="reminder-row">
					<text class="reminder-label">每次休息：</text>
					<picker mode="selector" :range="restMinutesOptions" :value="restMinuteIndex" @change="onRestMinuteChange">
						<view class="picker-value">
							<text>{{ restMinutesOptions[restMinuteIndex] }}分钟</text>
							<view class="arrow-right small"></view>
						</view>
					</picker>
				</view>
				<view class="reminder-info">
					<text class="info-text">当前已连续工作：{{ currentWorkHours }}小时{{ currentWorkMinutes }}分钟</text>
					<text v-if="shouldRemindRest" class="warning-text">⚠️ 已达到休息提醒时间</text>
				</view>
			</view>

			<view class="divider"></view>

			<view class="list-item">
				<view class="list-item-left">
					<view class="item-icon" style="background-color: #F3E5F5;">
						<text>📍</text>
					</view>
					<text class="list-item-text">接单范围</text>
				</view>
				<view class="list-item-right">
					<text class="value-text">{{ orderRange }}公里</text>
					<view class="arrow-right"></view>
				</view>
			</view>

			<view class="range-setting">
				<slider :value="orderRange" :min="1" :max="20" :step="1" activeColor="#4CAF50" backgroundColor="#E0E0E0" @change="onRangeChange" />
				<view class="range-labels">
					<text class="range-label">1km</text>
					<text class="range-label">20km</text>
				</view>
			</view>
		</view>

		<!-- 通知设置 -->
		<view class="card">
			<view class="section-title">通知设置</view>
			
			<view class="list-item">
				<view class="list-item-left">
					<view class="item-icon" style="background-color: #E8F5E9;">
						<text>🔔</text>
					</view>
					<text class="list-item-text">订单推送通知</text>
				</view>
				<view class="list-item-right">
					<switch :checked="orderNotification" @change="toggleOrderNotification" color="#4CAF50" />
				</view>
			</view>

			<view class="divider"></view>

			<view class="list-item">
				<view class="list-item-left">
					<view class="item-icon" style="background-color: #FFF3E0;">
						<text>🎵</text>
					</view>
					<text class="list-item-text">订单提示音</text>
				</view>
				<view class="list-item-right">
					<switch :checked="orderSound" @change="toggleOrderSound" color="#4CAF50" />
				</view>
			</view>

			<view class="divider"></view>

			<view class="list-item">
				<view class="list-item-left">
					<view class="item-icon" style="background-color: #E3F2FD;">
						<text>📳</text>
					</view>
					<text class="list-item-text">订单震动提醒</text>
				</view>
				<view class="list-item-right">
					<switch :checked="orderVibrate" @change="toggleOrderVibrate" color="#4CAF50" />
				</view>
			</view>
		</view>

		<!-- 疲劳驾驶监测 -->
		<view class="card">
			<view class="section-title">疲劳驾驶监测</view>
			
			<view class="fatigue-monitor">
				<view class="monitor-item">
					<view class="monitor-icon">
						<text>⏱️</text>
					</view>
					<view class="monitor-info">
						<text class="monitor-label">今日工作时长</text>
						<text class="monitor-value">{{ formatDuration(todayWorkSeconds) }}</text>
					</view>
					<view class="monitor-status" :class="isFatigued ? 'danger' : 'safe'">
						<text>{{ isFatigued ? '疲劳' : '正常' }}</text>
					</view>
				</view>

				<view class="monitor-progress">
					<view class="progress-bar">
						<view class="progress-fill" :style="{ width: workProgress + '%', backgroundColor: isFatigued ? '#F44336' : '#4CAF50' }"></view>
					</view>
					<view class="progress-info">
						<text class="progress-text">每日建议工作不超过12小时</text>
						<text class="progress-percent">{{ workProgress }}%</text>
					</view>
				</view>
			</view>

			<view v-if="isFatigued" class="fatigue-warning">
				<view class="warning-icon">
					<text>⚠️</text>
				</view>
				<view class="warning-content">
					<text class="warning-title">疲劳驾驶提醒</text>
					<text class="warning-desc">您今日工作时间较长，请注意休息，保障行车安全。</text>
				</view>
			</view>
		</view>

		<!-- 保存按钮 -->
		<view class="save-section">
			<button class="btn btn-primary save-btn" @click="saveSettings">保存设置</button>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			isOnline: true,
			dailyOrderLimit: 15,
			todayOrders: 8,
			restReminderEnabled: true,
			workHoursOptions: [2, 3, 4, 5, 6],
			workHourIndex: 2,
			restMinutesOptions: [15, 20, 30, 45, 60],
			restMinuteIndex: 2,
			currentWorkHours: 3,
			currentWorkMinutes: 45,
			orderRange: 5,
			orderNotification: true,
			orderSound: true,
			orderVibrate: true,
			todayWorkSeconds: 13500,
			maxWorkSeconds: 43200,
			workTimer: null
		}
	},
	computed: {
		shouldRemindRest() {
			const totalMinutes = this.currentWorkHours * 60 + this.currentWorkMinutes
			const limitMinutes = this.workHoursOptions[this.workHourIndex] * 60
			return totalMinutes >= limitMinutes
		},
		isFatigued() {
			return this.todayWorkSeconds >= this.maxWorkSeconds
		},
		workProgress() {
			const progress = (this.todayWorkSeconds / this.maxWorkSeconds) * 100
			return Math.min(Math.round(progress), 100)
		}
	},
	onLoad() {
		this.loadSettings()
		this.startWorkTimer()
	},
	onUnload() {
		this.stopWorkTimer()
	},
	methods: {
		loadSettings() {
			const savedSettings = uni.getStorageSync('order_settings')
			if (savedSettings) {
				Object.assign(this, savedSettings)
			}
		},
		toggleOnline(e) {
			this.isOnline = e.detail.value
			if (this.isOnline) {
				uni.showToast({
					title: '已开始接单',
					icon: 'success'
				})
			} else {
				uni.showToast({
					title: '已停止接单',
					icon: 'none'
				})
			}
		},
		setLimit(limit) {
			this.dailyOrderLimit = limit
		},
		toggleRestReminder(e) {
			this.restReminderEnabled = e.detail.value
		},
		onWorkHourChange(e) {
			this.workHourIndex = e.detail.value
		},
		onRestMinuteChange(e) {
			this.restMinuteIndex = e.detail.value
		},
		onRangeChange(e) {
			this.orderRange = e.detail.value
		},
		toggleOrderNotification(e) {
			this.orderNotification = e.detail.value
		},
		toggleOrderSound(e) {
			this.orderSound = e.detail.value
		},
		toggleOrderVibrate(e) {
			this.orderVibrate = e.detail.value
		},
		startWorkTimer() {
			this.workTimer = setInterval(() => {
				this.todayWorkSeconds++
				
				const totalMinutes = Math.floor(this.todayWorkSeconds / 60)
				this.currentWorkHours = Math.floor(totalMinutes / 60)
				this.currentWorkMinutes = totalMinutes % 60
				
				if (this.restReminderEnabled && this.shouldRemindRest && this.todayWorkSeconds % 300 === 0) {
					this.showRestReminder()
				}
			}, 1000)
		},
		stopWorkTimer() {
			if (this.workTimer) {
				clearInterval(this.workTimer)
				this.workTimer = null
			}
		},
		showRestReminder() {
			uni.showModal({
				title: '休息提醒',
				content: `您已连续工作${this.currentWorkHours}小时${this.currentWorkMinutes}分钟，建议休息${this.restMinutesOptions[this.restMinuteIndex]}分钟后再继续工作。`,
				confirmText: '我知道了',
				showCancel: false
			})
		},
		formatDuration(seconds) {
			const hours = Math.floor(seconds / 3600)
			const minutes = Math.floor((seconds % 3600) / 60)
			return `${hours}小时${minutes}分钟`
		},
		saveSettings() {
			const settings = {
				isOnline: this.isOnline,
				dailyOrderLimit: this.dailyOrderLimit,
				restReminderEnabled: this.restReminderEnabled,
				workHourIndex: this.workHourIndex,
				restMinuteIndex: this.restMinuteIndex,
				orderRange: this.orderRange,
				orderNotification: this.orderNotification,
				orderSound: this.orderSound,
				orderVibrate: this.orderVibrate
			}
			
			uni.setStorageSync('order_settings', settings)
			
			uni.showToast({
				title: '设置已保存',
				icon: 'success'
			})
			
			setTimeout(() => {
				uni.navigateBack()
			}, 1000)
		}
	}
}
</script>

<style scoped>
.settings-container {
	min-height: 100vh;
	background-color: #F5F5F5;
	padding-bottom: 120rpx;
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

.value-text {
	font-size: 28rpx;
	color: #666666;
	margin-right: 12rpx;
}

.status-desc {
	padding-top: 20rpx;
}

.desc-text {
	font-size: 24rpx;
	color: #999999;
}

/* 接单上限设置 */
.limit-setting {
	padding: 20rpx 0;
	background-color: #F5F5F5;
	border-radius: 12rpx;
	margin-top: 16rpx;
	padding: 20rpx;
}

.limit-row {
	display: flex;
	align-items: center;
	flex-wrap: wrap;
}

.limit-label {
	font-size: 26rpx;
	color: #666666;
	margin-right: 16rpx;
}

.limit-buttons {
	display: flex;
	flex-wrap: wrap;
	gap: 16rpx;
	flex: 1;
}

.limit-btn {
	padding: 12rpx 24rpx;
	border-radius: 20rpx;
	background-color: #FFFFFF;
	border: 2rpx solid #E0E0E0;
}

.limit-btn text {
	font-size: 24rpx;
	color: #666666;
}

.limit-btn.active {
	background-color: #E8F5E9;
	border-color: #4CAF50;
}

.limit-btn.active text {
	color: #4CAF50;
}

.limit-info {
	margin-top: 16rpx;
	padding-top: 16rpx;
	border-top: 1rpx solid #E0E0E0;
}

.info-text {
	font-size: 24rpx;
	color: #999999;
}

/* 休息提醒设置 */
.reminder-setting {
	padding: 20rpx;
	background-color: #F5F5F5;
	border-radius: 12rpx;
	margin-top: 16rpx;
}

.reminder-row {
	display: flex;
	align-items: center;
	margin-bottom: 16rpx;
}

.reminder-row:last-child {
	margin-bottom: 0;
}

.reminder-label {
	font-size: 26rpx;
	color: #666666;
	margin-right: 16rpx;
	width: 140rpx;
}

.picker-value {
	display: flex;
	align-items: center;
	background-color: #FFFFFF;
	padding: 12rpx 24rpx;
	border-radius: 8rpx;
}

.picker-value text {
	font-size: 26rpx;
	color: #333333;
}

.arrow-right.small {
	width: 16rpx;
	height: 16rpx;
	border-width: 2rpx;
}

.reminder-desc {
	font-size: 26rpx;
	color: #666666;
	margin-left: 16rpx;
}

.reminder-info {
	margin-top: 16rpx;
	padding-top: 16rpx;
	border-top: 1rpx solid #E0E0E0;
}

.warning-text {
	font-size: 24rpx;
	color: #F44336;
	margin-left: 16rpx;
}

/* 接单范围设置 */
.range-setting {
	padding: 20rpx 0;
	margin-top: 16rpx;
}

.range-labels {
	display: flex;
	justify-content: space-between;
	margin-top: 8rpx;
}

.range-label {
	font-size: 22rpx;
	color: #999999;
}

/* 疲劳驾驶监测 */
.fatigue-monitor {
	background-color: #F5F5F5;
	border-radius: 12rpx;
	padding: 20rpx;
}

.monitor-item {
	display: flex;
	align-items: center;
	margin-bottom: 20rpx;
}

.monitor-icon {
	width: 72rpx;
	height: 72rpx;
	background-color: #FFF3E0;
	border-radius: 16rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	margin-right: 20rpx;
	font-size: 36rpx;
}

.monitor-info {
	flex: 1;
	display: flex;
	flex-direction: column;
}

.monitor-label {
	font-size: 24rpx;
	color: #999999;
	margin-bottom: 4rpx;
}

.monitor-value {
	font-size: 32rpx;
	font-weight: 600;
	color: #333333;
}

.monitor-status {
	padding: 8rpx 20rpx;
	border-radius: 20rpx;
	background-color: #E8F5E9;
}

.monitor-status text {
	font-size: 24rpx;
	color: #4CAF50;
}

.monitor-status.danger {
	background-color: #FFEBEE;
}

.monitor-status.danger text {
	color: #F44336;
}

.monitor-progress {
	margin-top: 16rpx;
}

.progress-bar {
	height: 12rpx;
	background-color: #E0E0E0;
	border-radius: 6rpx;
	overflow: hidden;
}

.progress-fill {
	height: 100%;
	border-radius: 6rpx;
	transition: width 0.3s ease;
}

.progress-info {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-top: 12rpx;
}

.progress-text {
	font-size: 22rpx;
	color: #999999;
}

.progress-percent {
	font-size: 24rpx;
	font-weight: 600;
	color: #4CAF50;
}

/* 疲劳警告 */
.fatigue-warning {
	display: flex;
	align-items: flex-start;
	margin-top: 20rpx;
	padding: 20rpx;
	background-color: #FFF3E0;
	border-radius: 12rpx;
}

.warning-icon {
	font-size: 36rpx;
	margin-right: 16rpx;
}

.warning-content {
	flex: 1;
}

.warning-title {
	font-size: 28rpx;
	font-weight: 600;
	color: #FF9800;
	display: block;
	margin-bottom: 8rpx;
}

.warning-desc {
	font-size: 24rpx;
	color: #FF9800;
	line-height: 1.6;
}

/* 保存按钮 */
.save-section {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	padding: 20rpx 30rpx;
	background-color: #FFFFFF;
	border-top: 1rpx solid #E0E0E0;
	padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
}

.save-btn {
	width: 100%;
}
</style>
