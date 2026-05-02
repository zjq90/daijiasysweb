<template>
	<view class="complaint-container">
		<!-- 切换标签 -->
		<view class="tab-section">
			<view class="card tab-card">
				<view 
					class="tab-item" 
					:class="{ active: activeTab === 'pending' }"
					@click="switchTab('pending')"
				>
					<text>处理中</text>
					<text class="tab-count" v-if="pendingComplaints.length > 0">{{ pendingComplaints.length }}</text>
				</view>
				<view 
					class="tab-item" 
					:class="{ active: activeTab === 'completed' }"
					@click="switchTab('completed')"
				>
					<text>已处理</text>
				</view>
				<view 
					class="tab-item" 
					:class="{ active: activeTab === 'create' }"
					@click="switchTab('create')"
				>
					<text>发起投诉</text>
				</view>
			</view>
		</view>

		<!-- 处理中列表 -->
		<view v-if="activeTab === 'pending'" class="list-section">
			<view v-if="pendingComplaints.length > 0" class="card">
				<view class="complaint-list">
					<view 
						class="complaint-item" 
						v-for="(item, index) in pendingComplaints" 
						:key="index"
						@click="viewComplaintDetail(item)"
					>
						<view class="complaint-header">
							<view class="complaint-type">{{ item.type }}</view>
							<view class="complaint-status pending">处理中</view>
						</view>
						
						<view class="order-info">
							<view class="order-locations">
								<view class="loc-row">
									<view class="loc-dot start"></view>
									<text class="loc-text">{{ item.startAddress }}</text>
								</view>
								<view class="loc-arrow">↓</view>
								<view class="loc-row">
									<view class="loc-dot end"></view>
									<text class="loc-text">{{ item.endAddress }}</text>
								</view>
							</view>
						</view>

						<view class="complaint-content">
							<text class="content-label">投诉内容：</text>
							<text class="content-text">{{ item.content }}</text>
						</view>

						<view class="complaint-meta">
							<text class="create-time">提交时间：{{ item.createTime }}</text>
							<text class="order-no">订单号：{{ item.orderNo }}</text>
						</view>

						<view class="complaint-actions">
							<button class="btn btn-small btn-outline" @click.stop="contactService">
								联系客服
							</button>
							<button class="btn btn-small btn-primary" @click.stop="cancelComplaint(item)">
								撤销投诉
							</button>
						</view>
					</view>
				</view>
			</view>

			<view v-else class="card">
				<view class="empty-state">
					<text class="empty-icon">📋</text>
					<text class="empty-text">暂无处理中的投诉</text>
				</view>
			</view>
		</view>

		<!-- 已处理列表 -->
		<view v-if="activeTab === 'completed'" class="list-section">
			<view v-if="completedComplaints.length > 0" class="card">
				<view class="complaint-list">
					<view 
						class="complaint-item" 
						v-for="(item, index) in completedComplaints" 
						:key="index"
						@click="viewComplaintDetail(item)"
					>
						<view class="complaint-header">
							<view class="complaint-type">{{ item.type }}</view>
							<view 
								class="complaint-status" 
								:class="item.result === 'sustained' ? 'sustained' : 'dismissed'"
							>
								{{ item.result === 'sustained' ? '投诉成立' : '投诉不成立' }}
							</view>
						</view>
						
						<view class="order-info">
							<view class="order-locations">
								<view class="loc-row">
									<view class="loc-dot start"></view>
									<text class="loc-text">{{ item.startAddress }}</text>
								</view>
								<view class="loc-arrow">↓</view>
								<view class="loc-row">
									<view class="loc-dot end"></view>
									<text class="loc-text">{{ item.endAddress }}</text>
								</view>
							</view>
						</view>

						<view class="complaint-content">
							<text class="content-label">投诉内容：</text>
							<text class="content-text">{{ item.content }}</text>
						</view>

						<view class="handle-result">
							<view class="result-header">
								<text class="result-label">处理结果</text>
								<text class="handle-time">{{ item.handleTime }}</text>
							</view>
							<text class="result-text">{{ item.resultText }}</text>
						</view>

						<view class="complaint-meta">
							<text class="create-time">提交时间：{{ item.createTime }}</text>
							<text class="order-no">订单号：{{ item.orderNo }}</text>
						</view>
					</view>
				</view>
			</view>

			<view v-else class="card">
				<view class="empty-state">
					<text class="empty-icon">📋</text>
					<text class="empty-text">暂无已处理的投诉</text>
				</view>
			</view>
		</view>

		<!-- 发起投诉表单 -->
		<view v-if="activeTab === 'create'" class="form-section">
			<view class="card">
				<view class="form-item">
					<text class="form-label">选择订单</text>
					<picker 
						:value="selectedOrderIndex" 
						:range="orderOptions"
						range-key="displayText"
						@change="onOrderChange"
					>
						<view class="picker-value">
							<text v-if="selectedOrder">{{ selectedOrder.displayText }}</text>
							<text v-else class="placeholder">请选择要投诉的订单</text>
						</view>
					</picker>
				</view>

				<view class="form-item">
					<text class="form-label">投诉类型</text>
					<view class="type-options">
						<view 
							class="type-option" 
							v-for="(type, index) in complaintTypes" 
							:key="index"
							:class="{ active: selectedType === type.value }"
							@click="selectType(type.value)"
						>
							<text>{{ type.label }}</text>
						</view>
					</view>
				</view>

				<view class="form-item">
					<text class="form-label">投诉内容</text>
					<textarea 
						class="complaint-textarea"
						placeholder="请详细描述您要投诉的内容，以便我们更好地为您处理..."
						v-model="complaintContent"
						:maxlength="500"
					></textarea>
					<text class="char-count">{{ complaintContent.length }}/500</text>
				</view>

				<view class="form-item">
					<text class="form-label">上传凭证</text>
					<view class="upload-section">
						<view 
							class="upload-item" 
							v-for="(img, index) in uploadImages" 
							:key="index"
						>
							<image class="upload-image" :src="img" mode="aspectFill"></image>
							<view class="delete-btn" @click="deleteImage(index)">
								<text>×</text>
							</view>
						</view>
						<view 
							class="upload-add" 
							v-if="uploadImages.length < 9"
							@click="chooseImage"
						>
							<text class="add-icon">+</text>
							<text class="add-text">添加图片</text>
						</view>
					</view>
					<text class="upload-tip">最多可上传9张图片，支持jpg、png格式</text>
				</view>

				<view class="form-item">
					<text class="form-label">联系电话</text>
					<input 
						class="input-field"
						type="number"
						placeholder="请输入您的联系电话"
						v-model="contactPhone"
						maxlength="11"
					/>
				</view>
			</view>

			<view class="submit-section">
				<button 
					class="btn btn-primary submit-btn"
					:class="{ disabled: !canSubmit }"
					:disabled="!canSubmit"
					@click="submitComplaint"
				>
					提交投诉
				</button>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			activeTab: 'pending',
			pendingComplaints: [
				{
					id: 1,
					orderNo: 'OD202605020003',
					type: '服务态度',
					startAddress: '朝阳区三里屯',
					endAddress: '海淀区中关村',
					content: '乘客在行程中态度恶劣，多次辱骂司机，并拒绝支付额外费用。',
					createTime: '2026-05-02 15:30',
					status: 'pending'
				}
			],
			completedComplaints: [
				{
					id: 2,
					orderNo: 'OD202604300005',
					type: '费用纠纷',
					startAddress: '东城区王府井',
					endAddress: '西城区西单',
					content: '乘客更改了目的地，但拒绝支付额外的路程费用。',
					createTime: '2026-04-30 22:00',
					handleTime: '2026-05-01 10:30',
					result: 'sustained',
					resultText: '经核实，乘客确实更改了目的地且未支付额外费用。已联系乘客补交费用，并对该乘客进行信用扣分处理。'
				},
				{
					id: 3,
					orderNo: 'OD202604280002',
					type: '安全问题',
					startAddress: '丰台区丽泽商务区',
					endAddress: '通州区梨园',
					content: '乘客疑似醉酒，在车上呕吐，造成车辆污染，要求赔偿洗车费。',
					createTime: '2026-04-28 23:15',
					handleTime: '2026-04-29 14:00',
					result: 'sustained',
					resultText: '经核实，乘客醉酒呕吐情况属实。已从乘客账户扣除50元洗车费用至您的账户。'
				}
			],
			orderOptions: [
				{ id: 1, displayText: 'OD202605020001 - 朝阳区→海淀区', startAddress: '朝阳区建国路', endAddress: '海淀区中关村' },
				{ id: 2, displayText: 'OD202605020002 - 东城区→西城区', startAddress: '东城区王府井', endAddress: '西城区西单' },
				{ id: 3, displayText: 'OD202605010003 - 丰台区→通州区', startAddress: '丰台区丽泽', endAddress: '通州区梨园' }
			],
			selectedOrderIndex: -1,
			selectedOrder: null,
			complaintTypes: [
				{ label: '服务态度', value: 'attitude' },
				{ label: '费用纠纷', value: 'fee' },
				{ label: '安全问题', value: 'safety' },
				{ label: '车辆损坏', value: 'damage' },
				{ label: '其他问题', value: 'other' }
			],
			selectedType: '',
			complaintContent: '',
			uploadImages: [],
			contactPhone: ''
		}
	},
	computed: {
		canSubmit() {
			return this.selectedOrder && this.selectedType && this.complaintContent.trim()
		}
	},
	methods: {
		switchTab(tab) {
			this.activeTab = tab
		},
		onOrderChange(e) {
			this.selectedOrderIndex = e.detail.value
			this.selectedOrder = this.orderOptions[e.detail.value]
		},
		selectType(type) {
			this.selectedType = type
		},
		chooseImage() {
			uni.chooseImage({
				count: 9 - this.uploadImages.length,
				sourceType: ['album', 'camera'],
				success: (res) => {
					this.uploadImages = this.uploadImages.concat(res.tempFilePaths)
				}
			})
		},
		deleteImage(index) {
			this.uploadImages.splice(index, 1)
		},
		viewComplaintDetail(item) {
			uni.showToast({
				title: '查看投诉详情',
				icon: 'none'
			})
		},
		contactService() {
			uni.showModal({
				title: '联系客服',
				content: '客服热线：400-123-4567\n工作时间：全天24小时',
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
		cancelComplaint(item) {
			uni.showModal({
				title: '确认撤销',
				content: '确定要撤销这个投诉吗？撤销后将无法恢复。',
				success: (res) => {
					if (res.confirm) {
						const index = this.pendingComplaints.findIndex(c => c.id === item.id)
						if (index > -1) {
							this.pendingComplaints.splice(index, 1)
							uni.showToast({
								title: '已撤销投诉',
								icon: 'success'
							})
						}
					}
				}
			})
		},
		async submitComplaint() {
			if (!this.canSubmit) {
				uni.showToast({
					title: '请填写完整信息',
					icon: 'none'
				})
				return
			}

			uni.showLoading({
				title: '提交中...'
			})

			await new Promise(resolve => setTimeout(resolve, 1500))

			uni.hideLoading()

			// 添加到处理中列表
			var self = this
			var typeLabel = '其他问题'
			for (var i = 0; i < this.complaintTypes.length; i++) {
				if (this.complaintTypes[i].value === this.selectedType) {
					typeLabel = this.complaintTypes[i].label
					break
				}
			}
			
			var newComplaint = {
				id: Date.now(),
				orderNo: this.selectedOrder.displayText.split(' - ')[0],
				type: typeLabel,
				startAddress: this.selectedOrder.startAddress,
				endAddress: this.selectedOrder.endAddress,
				content: this.complaintContent,
				createTime: this.formatTime(new Date()),
				status: 'pending'
			}

			this.pendingComplaints.unshift(newComplaint)

			// 重置表单
			this.selectedOrderIndex = -1
			this.selectedOrder = null
			this.selectedType = ''
			this.complaintContent = ''
			this.uploadImages = []
			this.contactPhone = ''

			uni.showToast({
				title: '投诉已提交',
				icon: 'success'
			})

			// 切换到处理中标签
			setTimeout(() => {
				this.activeTab = 'pending'
			}, 1500)
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
.complaint-container {
	min-height: 100vh;
	background-color: #F5F5F5;
	padding-bottom: 60rpx;
}

/* 标签切换 */
.tab-section {
	padding: 20rpx 30rpx;
}

.tab-card {
	display: flex;
	padding: 8rpx;
	background-color: #F5F5F5;
	border-radius: 12rpx;
}

.tab-item {
	flex: 1;
	height: 72rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 28rpx;
	color: #666666;
	border-radius: 10rpx;
	position: relative;
}

.tab-item.active {
	background-color: #FFFFFF;
	color: #4CAF50;
	font-weight: 500;
	box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.tab-count {
	position: absolute;
	top: 8rpx;
	right: 20rpx;
	background-color: #F44336;
	color: #FFFFFF;
	font-size: 18rpx;
	padding: 2rpx 8rpx;
	border-radius: 10rpx;
	min-width: 28rpx;
	text-align: center;
}

/* 列表区域 */
.list-section,
.form-section {
	padding: 0 30rpx;
}

.complaint-list {
	display: flex;
	flex-direction: column;
}

.complaint-item {
	padding: 24rpx 0;
	border-bottom: 1rpx solid #F0F0F0;
}

.complaint-item:last-child {
	border-bottom: none;
}

.complaint-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 16rpx;
}

.complaint-type {
	font-size: 26rpx;
	color: #4CAF50;
	background-color: #E8F5E9;
	padding: 6rpx 16rpx;
	border-radius: 20rpx;
}

.complaint-status {
	font-size: 24rpx;
	padding: 6rpx 16rpx;
	border-radius: 20rpx;
}

.complaint-status.pending {
	background-color: #FFF3E0;
	color: #FF9800;
}

.complaint-status.sustained {
	background-color: #E8F5E9;
	color: #4CAF50;
}

.complaint-status.dismissed {
	background-color: #F5F5F5;
	color: #999999;
}

/* 订单信息 */
.order-info {
	margin-bottom: 16rpx;
}

.order-locations {
	background-color: #F8F9FA;
	border-radius: 12rpx;
	padding: 16rpx 20rpx;
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
	font-size: 18rpx;
	color: #999999;
	padding-left: 24rpx;
}

/* 投诉内容 */
.complaint-content {
	margin-bottom: 16rpx;
}

.content-label {
	font-size: 24rpx;
	color: #999999;
}

.content-text {
	font-size: 26rpx;
	color: #333333;
	line-height: 1.6;
}

/* 处理结果 */
.handle-result {
	background-color: #F8F9FA;
	border-radius: 12rpx;
	padding: 16rpx 20rpx;
	margin-bottom: 16rpx;
}

.result-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 12rpx;
}

.result-label {
	font-size: 26rpx;
	color: #333333;
	font-weight: 500;
}

.handle-time {
	font-size: 22rpx;
	color: #999999;
}

.result-text {
	font-size: 24rpx;
	color: #666666;
	line-height: 1.6;
}

/* 投诉元信息 */
.complaint-meta {
	display: flex;
	flex-wrap: wrap;
	gap: 16rpx;
	margin-bottom: 16rpx;
}

.create-time,
.order-no {
	font-size: 22rpx;
	color: #999999;
}

/* 投诉操作按钮 */
.complaint-actions {
	display: flex;
	gap: 16rpx;
	justify-content: flex-end;
}

/* 表单样式 */
.form-item {
	padding: 24rpx 0;
	border-bottom: 1rpx solid #F0F0F0;
}

.form-item:last-child {
	border-bottom: none;
}

.form-label {
	display: block;
	font-size: 28rpx;
	color: #333333;
	margin-bottom: 16rpx;
	font-weight: 500;
}

.picker-value {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 20rpx;
	background-color: #F8F9FA;
	border-radius: 12rpx;
}

.picker-value .placeholder {
	color: #999999;
}

.type-options {
	display: flex;
	flex-wrap: wrap;
	gap: 16rpx;
}

.type-option {
	padding: 12rpx 28rpx;
	border-radius: 24rpx;
	font-size: 26rpx;
	color: #666666;
	background-color: #F5F5F5;
	border: 1rpx solid #E0E0E0;
}

.type-option.active {
	background-color: #E8F5E9;
	color: #4CAF50;
	border-color: #4CAF50;
}

.complaint-textarea {
	width: 100%;
	height: 200rpx;
	background-color: #F8F9FA;
	border-radius: 12rpx;
	padding: 20rpx;
	font-size: 26rpx;
	color: #333333;
	box-sizing: border-box;
}

.char-count {
	text-align: right;
	font-size: 22rpx;
	color: #999999;
	margin-top: 8rpx;
}

/* 上传图片 */
.upload-section {
	display: flex;
	flex-wrap: wrap;
	gap: 16rpx;
}

.upload-item {
	position: relative;
	width: 160rpx;
	height: 160rpx;
}

.upload-image {
	width: 100%;
	height: 100%;
	border-radius: 12rpx;
}

.delete-btn {
	position: absolute;
	top: -12rpx;
	right: -12rpx;
	width: 40rpx;
	height: 40rpx;
	background-color: #F44336;
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
}

.delete-btn text {
	color: #FFFFFF;
	font-size: 28rpx;
	line-height: 1;
}

.upload-add {
	width: 160rpx;
	height: 160rpx;
	border: 2rpx dashed #E0E0E0;
	border-radius: 12rpx;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
}

.add-icon {
	font-size: 48rpx;
	color: #999999;
	line-height: 1;
}

.add-text {
	font-size: 22rpx;
	color: #999999;
	margin-top: 8rpx;
}

.upload-tip {
	font-size: 22rpx;
	color: #999999;
	margin-top: 12rpx;
}

/* 提交按钮 */
.submit-section {
	padding: 40rpx 30rpx;
}

.submit-btn {
	margin: 0;
}

.submit-btn.disabled {
	opacity: 0.5;
}
</style>
