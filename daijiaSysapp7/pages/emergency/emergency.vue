<template>
	<view class="emergency-container">
		<!-- 顶部提示 -->
		<view class="header-section">
			<view class="card warning-card">
				<view class="warning-icon">
					<text>⚠️</text>
				</view>
				<view class="warning-content">
					<text class="warning-title">紧急求助功能</text>
					<text class="warning-desc">如遇紧急情况，可一键求助，平台将实时获取您的位置、行程信息和录音</text>
				</view>
			</view>
		</view>

		<!-- 紧急求助按钮 -->
		<view class="action-section">
			<view class="card action-card">
				<view class="sos-button" @click="triggerSOS" :class="{ active: isSOSActive }">
					<view class="sos-icon">
						<text>{{ isSOSActive ? '✓' : 'SOS' }}</text>
					</view>
					<text class="sos-text">{{ isSOSActive ? '求助已发出' : '紧急求助' }}</text>
				</view>

				<view class="action-buttons">
					<view class="action-item" @click="callPolice">
						<view class="action-icon" style="background-color: #E3F2FD;">
							<text>🚔</text>
						</view>
						<text class="action-label">报警</text>
					</view>
					<view class="action-item" @click="callPlatform">
						<view class="action-icon" style="background-color: #E8F5E9;">
							<text>📞</text>
						</view>
						<text class="action-label">联系平台</text>
					</view>
					<view class="action-item" @click="callEmergency">
						<view class="action-icon" style="background-color: #FFEBEE;">
							<text>🚑</text>
						</view>
						<text class="action-label">急救</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 录音功能 -->
		<view class="recording-section">
			<view class="card">
				<view class="section-header">
					<text class="section-title">全程录音</text>
					<view class="recording-status" :class="{ recording: isRecording }">
						<view class="recording-dot"></view>
						<text>{{ isRecording ? '录音中' : '未录音' }}</text>
					</view>
				</view>

				<view class="recording-controls">
					<view class="record-button" @click="toggleRecording" :class="{ recording: isRecording }">
						<view class="record-icon">
							<text v-if="isRecording">⏹</text>
							<text v-else>⏺</text>
						</view>
						<text class="record-text">{{ isRecording ? '停止录音' : '开始录音' }}</text>
					</view>
					<view class="recording-info" v-if="isRecording">
						<text class="recording-duration">{{ recordingDuration }}</text>
						<view class="recording-wave">
							<view class="wave-bar" v-for="i in 5" :key="i" :style="{ animationDelay: (i * 0.1) + 's' }"></view>
						</view>
					</view>
				</view>

				<view class="recording-tip">
					<text class="tip-text">录音将自动加密上传至平台服务器，仅在紧急情况下可调取</text>
				</view>
			</view>
		</view>

		<!-- 当前位置和行程信息 -->
		<view class="location-section">
			<view class="card">
				<view class="section-header">
					<text class="section-title">位置与行程</text>
					<view class="refresh-btn" @click="refreshLocation">
						<text>🔄 刷新</text>
					</view>
				</view>

				<view class="location-info">
					<view class="info-row">
						<view class="info-icon">
							<text>📍</text>
						</view>
						<view class="info-content">
							<text class="info-label">当前位置</text>
							<text class="info-value">{{ currentLocation }}</text>
						</view>
					</view>

					<view class="info-row">
						<view class="info-icon">
							<text>🗺️</text>
						</view>
						<view class="info-content">
							<text class="info-label">经纬度</text>
							<text class="info-value">{{ coordinates }}</text>
						</view>
					</view>

					<view class="info-row">
						<view class="info-icon">
							<text>🕐</text>
						</view>
						<view class="info-content">
							<text class="info-label">更新时间</text>
							<text class="info-value">{{ locationUpdateTime }}</text>
						</view>
					</view>
				</view>

				<view class="trip-info" v-if="currentTrip">
					<view class="divider"></view>
					<view class="trip-header">
						<text class="trip-title">当前行程</text>
						<view class="trip-status active">进行中</view>
					</view>
					<view class="trip-route">
						<view class="route-point">
							<view class="route-dot start"></view>
							<text class="route-text">{{ currentTrip.startAddress }}</text>
						</view>
						<view class="route-line"></view>
						<view class="route-point">
							<view class="route-dot end"></view>
							<text class="route-text">{{ currentTrip.endAddress }}</text>
						</view>
					</view>
					<view class="trip-meta">
						<text class="trip-meta-item">订单号：{{ currentTrip.orderNo }}</text>
						<text class="trip-meta-item">乘客：{{ currentTrip.customerName }}</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 求助记录 -->
		<view class="history-section">
			<view class="card">
				<view class="section-header">
					<text class="section-title">求助记录</text>
					<text class="more-link" @click="showAllHistory">全部 ></text>
				</view>

				<view v-if="sosHistory.length > 0" class="history-list">
					<view 
						class="history-item" 
						v-for="(item, index) in sosHistory" 
						:key="index"
						@click="viewHistoryDetail(item)"
					>
						<view class="history-left">
							<view class="history-icon" :class="item.status === 'resolved' ? 'resolved' : 'processing'">
								<text>{{ item.status === 'resolved' ? '✓' : '🚨' }}</text>
							</view>
						</view>
						<view class="history-content">
							<view class="history-header-row">
								<text class="history-title">{{ item.title }}</text>
								<view class="history-status" :class="item.status === 'resolved' ? 'resolved' : 'processing'">
									{{ item.status === 'resolved' ? '已处理' : '处理中' }}
								</view>
							</view>
							<text class="history-location">{{ item.location }}</text>
							<view class="history-footer">
								<text class="history-time">{{ item.createTime }}</text>
								<view class="history-tags" v-if="item.tags && item.tags.length">
									<text class="history-tag" v-for="(tag, tagIndex) in item.tags" :key="tagIndex">{{ tag }}</text>
								</view>
							</view>
						</view>
					</view>
				</view>

				<view v-else class="empty-state">
					<text class="empty-icon">📋</text>
					<text class="empty-text">暂无求助记录</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			isSOSActive: false,
			isRecording: false,
			recordingDuration: '00:00:00',
			recordingTimer: null,
			recordingSeconds: 0,
			
			currentLocation: '北京市朝阳区建国路88号SOHO现代城',
			coordinates: '39.9042°N, 116.4074°E',
			locationUpdateTime: '',
			
			currentTrip: {
				orderNo: 'OD202605020005',
				startAddress: '朝阳区建国路88号SOHO现代城',
				endAddress: '海淀区中关村大街1号',
				customerName: '李先生'
			},
			
			sosHistory: [
				{
					id: 1,
					title: '乘客醉酒滋事求助',
					location: '北京市朝阳区三里屯',
					createTime: '2026-04-28 23:15',
					status: 'resolved',
					tags: ['有录音', '已报警']
				},
				{
					id: 2,
					title: '车辆故障求助',
					location: '北京市通州区梨园',
					createTime: '2026-04-15 19:30',
					status: 'resolved',
					tags: ['有录音']
				}
			]
		}
	},
	onLoad() {
		this.refreshLocation()
	},
	onUnload() {
		this.stopRecording()
	},
	methods: {
		triggerSOS() {
			if (this.isSOSActive) {
				uni.showModal({
					title: '提示',
					content: '求助已发出，是否要取消求助？',
					confirmText: '取消求助',
					success: (res) => {
						if (res.confirm) {
							this.isSOSActive = false
							uni.showToast({
								title: '已取消求助',
								icon: 'success'
							})
						}
					}
				})
				return
			}

			uni.showModal({
				title: '确认紧急求助',
				content: '点击确认后，平台将立即获取您的位置、行程信息并开始录音，同时通知紧急联系人。确定要发起求助吗？',
				confirmText: '确认求助',
				confirmColor: '#F44336',
				success: (res) => {
					if (res.confirm) {
						this.doSOS()
					}
				}
			})
		},
		async doSOS() {
			uni.showLoading({
				title: '正在发送求助...'
			})

			await new Promise(resolve => setTimeout(resolve, 1500))

			uni.hideLoading()

			this.isSOSActive = true
			
			// 自动开始录音
			if (!this.isRecording) {
				this.startRecording()
			}

			// 添加到求助记录
			const newRecord = {
				id: Date.now(),
				title: '紧急求助',
				location: this.currentLocation,
				createTime: this.formatTime(new Date()),
				status: 'processing',
				tags: ['有录音', '位置已获取']
			}

			this.sosHistory.unshift(newRecord)

			uni.showModal({
				title: '求助已发出',
				content: '平台已收到您的求助信息，正在联系相关人员处理。请保持电话畅通，注意自身安全。\n\n平台紧急联系电话：400-123-4567',
				showCancel: false,
				confirmText: '我知道了'
			})
		},
		callPolice() {
			uni.showModal({
				title: '拨打报警电话',
				content: '确认拨打 110 报警电话？',
				confirmText: '拨打',
				success: (res) => {
					if (res.confirm) {
						uni.makePhoneCall({
							phoneNumber: '110'
						})
					}
				}
			})
		},
		callPlatform() {
			uni.showModal({
				title: '联系平台',
				content: '平台紧急联系电话：400-123-4567\n工作时间：全天24小时',
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
		callEmergency() {
			uni.showModal({
				title: '拨打急救电话',
				content: '确认拨打 120 急救电话？',
				confirmText: '拨打',
				success: (res) => {
					if (res.confirm) {
						uni.makePhoneCall({
							phoneNumber: '120'
						})
					}
				}
			})
		},
		toggleRecording() {
			if (this.isRecording) {
				this.stopRecording()
			} else {
				this.startRecording()
			}
		},
		startRecording() {
			uni.showLoading({
				title: '启动录音...'
			})

			setTimeout(() => {
				uni.hideLoading()
				this.isRecording = true
				this.recordingSeconds = 0
				this.recordingDuration = '00:00:00'

				this.recordingTimer = setInterval(() => {
					this.recordingSeconds++
					const h = Math.floor(this.recordingSeconds / 3600).toString().padStart(2, '0')
					const m = Math.floor((this.recordingSeconds % 3600) / 60).toString().padStart(2, '0')
					const s = (this.recordingSeconds % 60).toString().padStart(2, '0')
					this.recordingDuration = `${h}:${m}:${s}`
				}, 1000)

				uni.showToast({
					title: '已开始录音',
					icon: 'success'
				})
			}, 500)
		},
		stopRecording() {
			if (this.recordingTimer) {
				clearInterval(this.recordingTimer)
				this.recordingTimer = null
			}
			this.isRecording = false
			uni.showToast({
				title: '已停止录音',
				icon: 'none'
			})
		},
		refreshLocation() {
			uni.showLoading({
				title: '获取位置中...'
			})

			setTimeout(() => {
				uni.hideLoading()
				this.locationUpdateTime = this.formatTime(new Date())
				uni.showToast({
					title: '位置已更新',
					icon: 'success'
				})
			}, 1000)
		},
		showAllHistory() {
			uni.showToast({
				title: '查看全部求助记录',
				icon: 'none'
			})
		},
		viewHistoryDetail(item) {
			uni.showModal({
				title: '求助详情',
				content: `标题：${item.title}\n位置：${item.location}\n时间：${item.createTime}\n状态：${item.status === 'resolved' ? '已处理' : '处理中'}\n\n${item.status === 'resolved' ? '平台已处理完成，如有疑问请联系客服。' : '平台正在处理中，请保持电话畅通。'}`,
				showCancel: false,
				confirmText: '我知道了'
			})
		},
		formatTime(date) {
			const year = date.getFullYear()
			const month = String(date.getMonth() + 1).padStart(2, '0')
			const day = String(date.getDate()).padStart(2, '0')
			const hour = String(date.getHours()).padStart(2, '0')
			const minute = String(date.getMinutes()).padStart(2, '0')
			return `${year}-${month}-${day} ${hour}:${minute}`
		}
	}
}
</script>

<style scoped>
.emergency-container {
	min-height: 100vh;
	background-color: #F5F5F5;
	padding-bottom: 40rpx;
}

/* 顶部提示 */
.header-section {
	padding: 20rpx 30rpx 0;
}

.warning-card {
	display: flex;
	align-items: flex-start;
	background-color: #FFF3E0;
	border-left: 8rpx solid #FF9800;
}

.warning-icon {
	font-size: 48rpx;
	margin-right: 20rpx;
}

.warning-content {
	flex: 1;
}

.warning-title {
	font-size: 28rpx;
	font-weight: 500;
	color: #E65100;
	display: block;
	margin-bottom: 8rpx;
}

.warning-desc {
	font-size: 24rpx;
	color: #FF9800;
	line-height: 1.5;
}

/* 紧急求助按钮 */
.action-section {
	padding: 20rpx 30rpx 0;
}

.action-card {
	display: flex;
	flex-direction: column;
	align-items: center;
}

.sos-button {
	width: 240rpx;
	height: 240rpx;
	border-radius: 50%;
	background: linear-gradient(135deg, #F44336, #FF5252);
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	box-shadow: 0 12rpx 40rpx rgba(244, 67, 54, 0.4);
	margin-bottom: 40rpx;
	transition: all 0.3s;
}

.sos-button.active {
	background: linear-gradient(135deg, #4CAF50, #8BC34A);
	box-shadow: 0 12rpx 40rpx rgba(76, 175, 80, 0.4);
}

.sos-icon {
	font-size: 72rpx;
	color: #FFFFFF;
	font-weight: 600;
	margin-bottom: 8rpx;
}

.sos-text {
	font-size: 28rpx;
	color: #FFFFFF;
	font-weight: 500;
}

.action-buttons {
	display: flex;
	justify-content: space-around;
	width: 100%;
}

.action-item {
	display: flex;
	flex-direction: column;
	align-items: center;
}

.action-icon {
	width: 88rpx;
	height: 88rpx;
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 40rpx;
	margin-bottom: 12rpx;
}

.action-label {
	font-size: 24rpx;
	color: #666666;
}

/* 录音功能 */
.recording-section,
.location-section,
.history-section {
	padding: 20rpx 30rpx 0;
}

.section-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 20rpx;
}

.recording-status {
	display: flex;
	align-items: center;
	font-size: 24rpx;
	color: #999999;
}

.recording-status.recording {
	color: #F44336;
}

.recording-dot {
	width: 16rpx;
	height: 16rpx;
	border-radius: 50%;
	background-color: #999999;
	margin-right: 8rpx;
}

.recording-status.recording .recording-dot {
	background-color: #F44336;
	animation: pulse 1s infinite;
}

@keyframes pulse {
	0%, 100% { opacity: 1; }
	50% { opacity: 0.5; }
}

.recording-controls {
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 20rpx 0;
}

.record-button {
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 24rpx 48rpx;
	border-radius: 44rpx;
	background-color: #F5F5F5;
	transition: all 0.3s;
}

.record-button.recording {
	background-color: #FFEBEE;
}

.record-icon {
	font-size: 48rpx;
	margin-bottom: 8rpx;
}

.record-text {
	font-size: 26rpx;
	color: #666666;
}

.recording-info {
	display: flex;
	align-items: center;
	margin-top: 20rpx;
}

.recording-duration {
	font-size: 36rpx;
	font-weight: 600;
	color: #F44336;
	margin-right: 20rpx;
}

.recording-wave {
	display: flex;
	align-items: center;
	gap: 4rpx;
	height: 40rpx;
}

.wave-bar {
	width: 6rpx;
	height: 20rpx;
	background-color: #F44336;
	border-radius: 3rpx;
	animation: wave 0.5s ease-in-out infinite;
}

@keyframes wave {
	0%, 100% { height: 10rpx; }
	50% { height: 30rpx; }
}

.recording-tip {
	padding: 16rpx 20rpx;
	background-color: #F8F9FA;
	border-radius: 12rpx;
	margin-top: 20rpx;
}

.tip-text {
	font-size: 22rpx;
	color: #999999;
	line-height: 1.5;
}

.refresh-btn {
	font-size: 24rpx;
	color: #4CAF50;
}

/* 位置信息 */
.location-info {
	display: flex;
	flex-direction: column;
}

.info-row {
	display: flex;
	align-items: flex-start;
	padding: 16rpx 0;
	border-bottom: 1rpx solid #F0F0F0;
}

.info-row:last-child {
	border-bottom: none;
}

.info-icon {
	font-size: 32rpx;
	margin-right: 16rpx;
}

.info-content {
	flex: 1;
	display: flex;
	flex-direction: column;
}

.info-label {
	font-size: 24rpx;
	color: #999999;
	margin-bottom: 4rpx;
}

.info-value {
	font-size: 26rpx;
	color: #333333;
	line-height: 1.5;
}

.divider {
	height: 1rpx;
	background-color: #F0F0F0;
	margin: 20rpx 0;
}

.trip-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 20rpx;
}

.trip-title {
	font-size: 28rpx;
	font-weight: 500;
	color: #333333;
}

.trip-status {
	font-size: 22rpx;
	padding: 4rpx 16rpx;
	border-radius: 20rpx;
}

.trip-status.active {
	background-color: #E8F5E9;
	color: #4CAF50;
}

.trip-route {
	margin-bottom: 16rpx;
}

.route-point {
	display: flex;
	align-items: center;
	margin-bottom: 8rpx;
}

.route-point:last-child {
	margin-bottom: 0;
}

.route-dot {
	width: 16rpx;
	height: 16rpx;
	border-radius: 50%;
	margin-right: 16rpx;
}

.route-dot.start {
	background-color: #4CAF50;
}

.route-dot.end {
	background-color: #F44336;
}

.route-text {
	font-size: 26rpx;
	color: #333333;
	flex: 1;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

.route-line {
	width: 2rpx;
	height: 24rpx;
	background: repeating-linear-gradient(
		180deg,
		#E0E0E0,
		#E0E0E0 8rpx,
		transparent 8rpx,
		transparent 16rpx
	);
	margin-left: 7rpx;
}

.trip-meta {
	display: flex;
	flex-wrap: wrap;
	gap: 16rpx;
}

.trip-meta-item {
	font-size: 22rpx;
	color: #999999;
}

/* 求助记录 */
.history-list {
	display: flex;
	flex-direction: column;
}

.history-item {
	display: flex;
	align-items: flex-start;
	padding: 20rpx 0;
	border-bottom: 1rpx solid #F0F0F0;
}

.history-item:last-child {
	border-bottom: none;
}

.history-left {
	margin-right: 16rpx;
}

.history-icon {
	width: 64rpx;
	height: 64rpx;
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 28rpx;
}

.history-icon.resolved {
	background-color: #E8F5E9;
	color: #4CAF50;
}

.history-icon.processing {
	background-color: #FFEBEE;
	color: #F44336;
}

.history-content {
	flex: 1;
}

.history-header-row {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 8rpx;
}

.history-title {
	font-size: 28rpx;
	color: #333333;
	font-weight: 500;
}

.history-status {
	font-size: 22rpx;
	padding: 4rpx 12rpx;
	border-radius: 20rpx;
}

.history-status.resolved {
	background-color: #E8F5E9;
	color: #4CAF50;
}

.history-status.processing {
	background-color: #FFF3E0;
	color: #FF9800;
}

.history-location {
	font-size: 24rpx;
	color: #666666;
	margin-bottom: 12rpx;
}

.history-footer {
	display: flex;
	align-items: center;
	justify-content: space-between;
}

.history-time {
	font-size: 22rpx;
	color: #999999;
}

.history-tags {
	display: flex;
	gap: 8rpx;
}

.history-tag {
	font-size: 20rpx;
	color: #4CAF50;
	background-color: #E8F5E9;
	padding: 2rpx 10rpx;
	border-radius: 12rpx;
}

.more-link {
	font-size: 24rpx;
	color: #4CAF50;
}
</style>
