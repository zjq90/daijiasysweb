<template>
	<view class="review-container">
		<!-- 待评价订单列表 -->
		<view class="pending-section" v-if="pendingReviews.length > 0">
			<view class="card">
				<view class="section-header">
					<text class="section-title">待评价订单</text>
					<text class="pending-count">{{ pendingReviews.length }}单待评价</text>
				</view>

				<view class="review-list">
					<view 
						class="review-item" 
						v-for="(review, index) in pendingReviews" 
						:key="index"
					>
						<view class="order-info">
							<view class="order-locations">
								<view class="loc-row">
									<view class="loc-dot start"></view>
									<text class="loc-text">{{ review.startAddress }}</text>
								</view>
								<view class="loc-arrow">↓</view>
								<view class="loc-row">
									<view class="loc-dot end"></view>
									<text class="loc-text">{{ review.endAddress }}</text>
								</view>
							</view>
							<view class="order-meta">
								<text class="order-time">{{ review.createTime }}</text>
								<text class="order-price">¥{{ review.price }}</text>
							</view>
						</view>

						<view class="review-form" v-if="review.showForm">
							<view class="form-section">
								<text class="form-label">服务评分</text>
								<view class="star-rating" @click.stop>
									<text 
										v-for="star in 5" 
										:key="star"
										:class="{ active: star <= review.rating }"
										@click="setRating(review, star)"
									>★</text>
								</view>
							</view>

							<view class="form-section">
								<text class="form-label">客户评价标签</text>
								<view class="tag-options">
									<view 
										class="tag-option" 
										:class="{ active: review.tags.includes('drunk') }"
										@click="toggleTag(review, 'drunk')"
									>
										<text>醉酒</text>
									</view>
									<view 
										class="tag-option" 
										:class="{ active: review.tags.includes('changedDest') }"
										@click="toggleTag(review, 'changedDest')"
									>
										<text>更改目的地</text>
									</view>
									<view 
										class="tag-option" 
										:class="{ active: review.tags.includes('polite') }"
										@click="toggleTag(review, 'polite')"
									>
										<text>礼貌</text>
									</view>
									<view 
										class="tag-option" 
										:class="{ active: review.tags.includes('generous') }"
										@click="toggleTag(review, 'generous')"
									>
										<text>大方</text>
									</view>
								</view>
							</view>

							<view class="form-section">
								<text class="form-label">评价内容</text>
								<textarea 
									class="review-textarea"
									placeholder="请输入您对乘客的评价..."
									v-model="review.comment"
									:maxlength="200"
								></textarea>
								<text class="char-count">{{ review.comment.length }}/200</text>
							</view>

							<view class="form-actions">
								<button class="btn btn-outline skip-btn" @click="skipReview(review)">
									稍后评价
								</button>
								<button class="btn btn-primary submit-btn" @click="submitReview(review)">
									提交评价
								</button>
							</view>
						</view>

						<view class="review-actions" v-else>
							<button class="btn btn-primary evaluate-btn" @click="showReviewForm(review)">
								去评价
							</button>
						</view>
					</view>
				</view>
			</view>
		</view>

		<!-- 已评价历史 -->
		<view class="history-section">
			<view class="card">
				<view class="section-header">
					<text class="section-title">评价历史</text>
					<text class="more-link" @click="showAllHistory">全部 ></text>
				</view>

				<view v-if="reviewHistory.length > 0" class="history-list">
					<view 
						class="history-item" 
						v-for="(item, index) in reviewHistory" 
						:key="index"
					>
						<view class="history-header">
							<view class="customer-info">
								<view class="customer-avatar">
									<text>{{ item.customerName.charAt(0) }}</text>
								</view>
								<view class="customer-detail">
									<text class="customer-name">{{ item.customerName }}</text>
									<view class="rating-stars">
										<text 
											v-for="star in 5" 
											:key="star"
											:class="{ active: star <= item.rating }"
										>★</text>
									</view>
								</view>
							</view>
							<text class="review-time">{{ item.reviewTime }}</text>
						</view>

						<view class="history-tags" v-if="item.tags && item.tags.length">
							<text 
								class="history-tag" 
								v-for="(tag, tagIndex) in item.tags" 
								:key="tagIndex"
								:class="tag.class"
							>{{ tag.label }}</text>
						</view>

						<view class="history-comment" v-if="item.comment">
							<text>{{ item.comment }}</text>
						</view>

						<view class="history-route">
							<view class="route-row">
								<view class="route-dot start"></view>
								<text class="route-text">{{ item.startAddress }}</text>
							</view>
							<view class="route-arrow">↓</view>
							<view class="route-row">
								<view class="route-dot end"></view>
								<text class="route-text">{{ item.endAddress }}</text>
							</view>
						</view>

						<view class="history-price-row">
							<text class="order-price">¥{{ item.price }}</text>
							<text class="order-distance">{{ item.distance }}km</text>
						</view>
					</view>
				</view>

				<view v-else class="empty-state">
					<text class="empty-icon">📝</text>
					<text class="empty-text">暂无评价历史</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			pendingReviews: [
				{
					id: 1,
					orderId: 'OD202605020001',
					startAddress: '朝阳区建国路88号SOHO现代城',
					endAddress: '海淀区中关村大街1号',
					price: 85,
					distance: 18.5,
					customerName: '李先生',
					createTime: '2小时前',
					showForm: false,
					rating: 0,
					tags: [],
					comment: ''
				},
				{
					id: 2,
					orderId: 'OD202605020002',
					startAddress: '东城区王府井大街138号',
					endAddress: '西城区西单北大街120号',
					price: 45,
					distance: 8.2,
					customerName: '王女士',
					createTime: '5小时前',
					showForm: true,
					rating: 5,
					tags: ['polite'],
					comment: '乘客很有礼貌，全程交流愉快'
				}
			],
			reviewHistory: [
				{
					id: 1,
					orderId: 'OD202605010001',
					startAddress: '朝阳区三里屯太古里',
					endAddress: '通州区新华大街',
					price: 120,
					distance: 25.6,
					customerName: '张先生',
					reviewTime: '2026-05-01 23:30',
					rating: 4,
					tags: [
						{ label: '醉酒', class: 'danger' },
						{ label: '大方', class: 'success' }
					],
					comment: '乘客喝了点酒，但还算清醒，最后还多给了小费。'
				},
				{
					id: 2,
					orderId: 'OD202604300001',
					startAddress: '海淀区五道口',
					endAddress: '朝阳区望京SOHO',
					price: 68,
					distance: 15.3,
					customerName: '刘小姐',
					reviewTime: '2026-04-30 21:15',
					rating: 5,
					tags: [
						{ label: '更改目的地', class: 'warning' },
						{ label: '礼貌', class: 'success' }
					],
					comment: '中途改了一次目的地，乘客态度很好，主动说要加钱。'
				},
				{
					id: 3,
					orderId: 'OD202604290001',
					startAddress: '西城区金融街',
					endAddress: '东城区东直门',
					price: 52,
					distance: 10.8,
					customerName: '赵先生',
					reviewTime: '2026-04-29 19:45',
					rating: 5,
					tags: [
						{ label: '礼貌', class: 'success' },
						{ label: '大方', class: 'success' }
					],
					comment: '非常好的乘客，全程愉快。'
				}
			]
		}
	},
	methods: {
		showReviewForm(review) {
			review.showForm = true
		},
		setRating(review, star) {
			review.rating = star
		},
		toggleTag(review, tag) {
			const index = review.tags.indexOf(tag)
			if (index > -1) {
				review.tags.splice(index, 1)
			} else {
				review.tags.push(tag)
			}
		},
		async submitReview(review) {
			if (review.rating === 0) {
				uni.showToast({
					title: '请先选择评分',
					icon: 'none'
				})
				return
			}

			uni.showLoading({
				title: '提交中...'
			})

			await new Promise(resolve => setTimeout(resolve, 1000))

			uni.hideLoading()

			// 从待评价列表移除
			const index = this.pendingReviews.findIndex(r => r.id === review.id)
			if (index > -1) {
				this.pendingReviews.splice(index, 1)
			}

			// 添加到历史记录
			const tagMap = {
				drunk: { label: '醉酒', class: 'danger' },
				changedDest: { label: '更改目的地', class: 'warning' },
				polite: { label: '礼貌', class: 'success' },
				generous: { label: '大方', class: 'success' }
			}

			const historyItem = {
				id: Date.now(),
				orderId: review.orderId,
				startAddress: review.startAddress,
				endAddress: review.endAddress,
				price: review.price,
				distance: review.distance,
				customerName: review.customerName,
				reviewTime: '刚刚',
				rating: review.rating,
				tags: review.tags.map(tag => tagMap[tag] || { label: tag, class: 'info' }),
				comment: review.comment
			}

			this.reviewHistory.unshift(historyItem)

			uni.showToast({
				title: '评价成功',
				icon: 'success'
			})
		},
		skipReview(review) {
			review.showForm = false
			uni.showToast({
				title: '已保存为草稿',
				icon: 'none'
			})
		},
		showAllHistory() {
			uni.showToast({
				title: '查看全部评价历史',
				icon: 'none'
			})
		}
	}
}
</script>

<style scoped>
.review-container {
	min-height: 100vh;
	background-color: #F5F5F5;
	padding-bottom: 40rpx;
}

.pending-section,
.history-section {
	padding: 20rpx 30rpx 0;
}

.section-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 24rpx;
}

.pending-count {
	font-size: 24rpx;
	color: #F44336;
	background-color: #FFEBEE;
	padding: 4rpx 16rpx;
	border-radius: 20rpx;
}

.more-link {
	font-size: 24rpx;
	color: #4CAF50;
}

/* 评价列表 */
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

.order-info {
	margin-bottom: 20rpx;
}

.order-locations {
	margin-bottom: 16rpx;
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

/* 评价表单 */
.review-form {
	padding: 24rpx;
	background-color: #F8F9FA;
	border-radius: 16rpx;
	margin-bottom: 20rpx;
}

.form-section {
	margin-bottom: 24rpx;
}

.form-section:last-child {
	margin-bottom: 0;
}

.form-label {
	display: block;
	font-size: 26rpx;
	color: #333333;
	margin-bottom: 16rpx;
	font-weight: 500;
}

.star-rating {
	display: flex;
	gap: 8rpx;
}

.star-rating text {
	font-size: 48rpx;
	color: #E0E0E0;
}

.star-rating text.active {
	color: #FFD700;
}

.tag-options {
	display: flex;
	flex-wrap: wrap;
	gap: 16rpx;
}

.tag-option {
	padding: 12rpx 24rpx;
	border-radius: 24rpx;
	font-size: 24rpx;
	color: #666666;
	background-color: #FFFFFF;
	border: 1rpx solid #E0E0E0;
}

.tag-option.active {
	background-color: #E8F5E9;
	color: #4CAF50;
	border-color: #4CAF50;
}

.review-textarea {
	width: 100%;
	height: 180rpx;
	background-color: #FFFFFF;
	border-radius: 12rpx;
	padding: 20rpx;
	font-size: 26rpx;
	color: #333333;
	box-sizing: border-box;
	border: 1rpx solid #E0E0E0;
}

.char-count {
	text-align: right;
	font-size: 22rpx;
	color: #999999;
	margin-top: 8rpx;
}

.form-actions {
	display: flex;
	gap: 20rpx;
	margin-top: 24rpx;
}

.skip-btn,
.submit-btn {
	flex: 1;
	margin: 0;
}

.review-actions {
	display: flex;
	justify-content: flex-end;
}

.evaluate-btn {
	width: 200rpx;
	margin: 0;
}

/* 历史记录 */
.history-list {
	display: flex;
	flex-direction: column;
}

.history-item {
	padding: 24rpx 0;
	border-bottom: 1rpx solid #F0F0F0;
}

.history-item:last-child {
	border-bottom: none;
}

.history-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 16rpx;
}

.customer-info {
	display: flex;
	align-items: center;
}

.customer-avatar {
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

.customer-detail {
	display: flex;
	flex-direction: column;
}

.customer-name {
	font-size: 28rpx;
	color: #333333;
	margin-bottom: 4rpx;
}

.rating-stars {
	font-size: 24rpx;
}

.rating-stars text {
	color: #E0E0E0;
}

.rating-stars text.active {
	color: #FFD700;
}

.review-time {
	font-size: 22rpx;
	color: #999999;
}

.history-tags {
	display: flex;
	flex-wrap: wrap;
	gap: 12rpx;
	margin-bottom: 12rpx;
}

.history-tag {
	font-size: 22rpx;
	padding: 4rpx 16rpx;
	border-radius: 20rpx;
}

.history-tag.success {
	background-color: #E8F5E9;
	color: #4CAF50;
}

.history-tag.warning {
	background-color: #FFF3E0;
	color: #FF9800;
}

.history-tag.danger {
	background-color: #FFEBEE;
	color: #F44336;
}

.history-tag.info {
	background-color: #E3F2FD;
	color: #2196F3;
}

.history-comment {
	font-size: 24rpx;
	color: #666666;
	line-height: 1.6;
	margin-bottom: 16rpx;
}

.history-route {
	margin-bottom: 12rpx;
}

.route-row {
	display: flex;
	align-items: center;
	margin-bottom: 8rpx;
}

.route-row:last-child {
	margin-bottom: 0;
}

.route-dot {
	width: 10rpx;
	height: 10rpx;
	border-radius: 50%;
	margin-right: 10rpx;
}

.route-dot.start {
	background-color: #4CAF50;
}

.route-dot.end {
	background-color: #F44336;
}

.route-text {
	font-size: 24rpx;
	color: #666666;
	flex: 1;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

.route-arrow {
	font-size: 18rpx;
	color: #999999;
	padding-left: 20rpx;
}

.history-price-row {
	display: flex;
	align-items: center;
	justify-content: space-between;
}

.order-distance {
	font-size: 22rpx;
	color: #999999;
}
</style>
